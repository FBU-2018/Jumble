package com.example.lkimberly.userstories.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.adapters.MatchPageAdapter;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.MatchDataModel;
import com.example.lkimberly.userstories.models.Matches;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchPageFragment extends Fragment {


    // match dictionary
    final Map<String, List<ParseUser>> matchDict= new HashMap<>();
    // the recycler view
    RecyclerView rvMatches;

    //match data (job followed by list of users who matched with that job
    List<MatchDataModel> matchesModelList = new ArrayList<>();

    // the adapter wired to the recycler view
    MatchPageAdapter adapter;

    public MatchPageFragment() {

    }

    private SwipeRefreshLayout swipeContainer;

    TextView tv_no_matches;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_no_matches = view.findViewById(R.id.tv_no_matches);
        rvMatches = view.findViewById(R.id.rv_outerRecyclerView);
        rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MatchPageAdapter(getActivity(), matchesModelList);
        rvMatches.setAdapter(adapter);

        int spaceInPixels = 0;
//        DividerItemDecoration divider = new DividerItemDecoration(getApplicationContext(), VERTICAL);
//        divider.setDrawable(getContext().getResources().getDrawable(R.drawable.ic_linear_separator_1));
//        rvMatches.addItemDecoration(divider);
//        rvMatches.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));

        loadMatches(false);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadMatches(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    double parseDouble(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }

    public void refresh() {
        loadMatches(true);
        adapter.notifyDataSetChanged();
        rvMatches.scrollToPosition(0);
    }

    public void loadMatches(final boolean scrollToTop) {
        ParseObject currentUserPointer = ParseObject.createWithoutData("User",ParseUser.getCurrentUser().getObjectId());

        // Query matches current user has
        final Matches.Query matchQuery = new Matches.Query();
        Log.d("Id", ParseUser.getCurrentUser().toString());
        matchQuery.getTop()
                .withJob()
                .withJobPoster()
                .withJobSubscriber()
                .whereEqualTo("jobPoster", ParseUser.getCurrentUser())
                .findInBackground(new FindCallback<Matches>() {
                    @Override
                    public void done(List<Matches> objects, ParseException e) {
                        if (e == null) {
                            matchDict.clear();
                            matchesModelList.clear();
                            Log.d("Objects", objects.toString());


                            if (objects.size() == 0) {
                                tv_no_matches.setVisibility(View.VISIBLE);
                            }


                            for (int i = 0; i < objects.size(); i++) {

                                Log.d("what is this", "this is a " + objects.get(i));
                                Matches singleMatch = (Matches) objects.get(i);
                                if (singleMatch.getJob() != null) {
                                    try {
                                        if (matchDict.containsKey(singleMatch.getJob().getObjectId())) {
                                            List<ParseUser> listOfMatchesForGivenJob = matchDict.get(singleMatch.getJob().getObjectId());
                                            ParseUser jobSubscriber = singleMatch.getJobSubscriber();
                                            listOfMatchesForGivenJob.add(jobSubscriber);
                                        } else {
                                            List<ParseUser> startMatchList = new ArrayList<>();
                                            startMatchList.add(singleMatch.getJobSubscriber());
                                            matchDict.put(singleMatch.getJob().getObjectId(), startMatchList);
                                        }
                                    } catch (NullPointerException renderMatchError) {
                                        Log.d("Match/Job Error", "Data causing error: Match - " + singleMatch.getObjectId() + ", Job - " + singleMatch.getJob());
                                        renderMatchError.printStackTrace();
                                        throw new NullPointerException("Match points to non-existant job");
                                    }
                                }
                            }

                            for (String jobOfCurrentUser: matchDict.keySet()) {
                                matchesModelList.add(new MatchDataModel(1, jobOfCurrentUser));

                                List<ParseUser> usersList = matchDict.get(jobOfCurrentUser);

                                Collections.sort(usersList, new Comparator<ParseUser>(){
                                    public int compare(ParseUser p1, ParseUser p2){
                                        return Double.compare(parseDouble((String) p1.get("rating")),(parseDouble((String) p2.get("rating"))));
                                    }
                                });

                                Collections.reverse(usersList);
                                for (ParseUser u : usersList) {
                                    //Log.d("User list", (String) u.get("rating"));
                                }
                                matchesModelList.add(new MatchDataModel(0, usersList,jobOfCurrentUser));
                            }

                            // Query other jobs user may have
                            final Job.Query jobQuery = new Job.Query();
                            jobQuery.getTop()
                                    .whereEqualTo("user", ParseUser.getCurrentUser())
                                    .findInBackground(new FindCallback<Job>() {
                                        @Override
                                        public void done(List<Job> objects, ParseException e) {
                                            if (e == null) {
                                                if (objects.size() > 0) {
                                                    matchesModelList.add(new MatchDataModel(3));
                                                }

                                                for (int i = 0; i < objects.size(); i++) {
                                                    Log.d("Query JOB", objects.get(i).getObjectId());

                                                    if (!matchDict.keySet().contains(objects.get(i).getObjectId())) {
                                                        matchesModelList.add(new MatchDataModel(2, objects.get(i)));
                                                    }
                                                }

                                                Log.d("Size",matchesModelList.toString());
                                                adapter.notifyDataSetChanged();
                                                if (scrollToTop) {
                                                    rvMatches.scrollToPosition(0);
                                                }
                                            }
                                        }
                                    });

                            tv_no_matches.setVisibility(View.INVISIBLE);
                        } else {
                            e.printStackTrace();
                        }
                        swipeContainer.setRefreshing(false);
                    }
                });
    }
}