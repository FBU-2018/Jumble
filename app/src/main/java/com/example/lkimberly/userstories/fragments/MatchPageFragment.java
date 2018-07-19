package com.example.lkimberly.userstories.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.RecyclerViewItemDecorator;
import com.example.lkimberly.userstories.adapters.MatchPageAdapter;
import com.example.lkimberly.userstories.models.Matches;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.ConstraintSet.VERTICAL;
import static com.parse.Parse.getApplicationContext;

public class MatchPageFragment extends Fragment {


    // list of matches
    final List<List<ParseUser>> allMatches = new ArrayList<>();

    // match dictionary
    final Map<String, List<ParseUser>> matchDict= new HashMap<>();
    // the recycler view
    RecyclerView rvMatches;

    // the adapter wired to the recycler view
    MatchPageAdapter adapter;

    public MatchPageFragment() {
    }

    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMatches = view.findViewById(R.id.rv_outerRecyclerView);
        rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MatchPageAdapter(getActivity(), allMatches);
        rvMatches.setAdapter(adapter);

        int spaceInPixels = 100;
        DividerItemDecoration divider = new DividerItemDecoration(getApplicationContext(), VERTICAL);
//        divider.setDrawable(getContext().getResources().getDrawable(R.drawable.ic_linear_separator_1));
        rvMatches.addItemDecoration(divider);
        rvMatches.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));

        loadMatches();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadMatches();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }


    public void refresh() {
        loadMatches();
    }

    public void loadMatches() {
        ParseObject currentUserPointer = ParseObject.createWithoutData("User",ParseUser.getCurrentUser().getObjectId());

        final Matches.Query matchQuery = new Matches.Query();
        Log.d("Id", ParseUser.getCurrentUser().toString());
        matchQuery.getTop()
                .withJob()
                .withJobPoster()
                .withJobSubscriber()
                .whereEqualTo("jobPoster", currentUserPointer)
                .findInBackground(new FindCallback<Matches>() {
                    @Override
                    public void done(List<Matches> objects, ParseException e) {
                        if (e == null) {
                            allMatches.clear();

                            for (int i = 0; i < objects.size(); i++) {
                                Log.d(Integer.toString(i), objects.get(i).getJobPoster().toString());
                                Matches singleMatch = (Matches) objects.get(i);
                                if (matchDict.containsKey(singleMatch.getJob().getObjectId())){
                                    List<ParseUser> listOfMatchesForGivenJob = matchDict.get(singleMatch.getJob().getObjectId());
                                    ParseUser jobSubscriber = singleMatch.getJobSubscriber();
                                    listOfMatchesForGivenJob.add(jobSubscriber);
                                } else {
                                    List<ParseUser> startMatchList = new ArrayList<>();
                                    startMatchList.add(singleMatch.getJobSubscriber());
                                    matchDict.put( singleMatch.getJob().getObjectId(), startMatchList);
                                }
                            }

                            for (String jobOfCurrentUser: matchDict.keySet()) {
                                allMatches.add(matchDict.get(jobOfCurrentUser));
                            }
                            adapter.notifyDataSetChanged();
                            Log.d("Matches", allMatches.toString());
                        } else {
                            e.printStackTrace();
                        }
                        swipeContainer.setRefreshing(false);
                    }
                });
    }

}
