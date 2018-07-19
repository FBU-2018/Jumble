package com.example.lkimberly.userstories.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.adapters.SwipeCardAdapter;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.SwipeCard;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    ArrayList<SwipeCard> al;
    SwipeCardAdapter swipeCardAdapter;
//    private ArrayAdapter<String> arrayAdapter;
    int i;

    SwipeFlingAdapterView flingContainer;

    ParseUser currentUser;
//    SwipeCardAdapter swipeCardAdapter;
    ArrayList<Job> jobs;
//    RecyclerView rvPosts;
//    private SwipeRefreshLayout swipeContainer;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_feed, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        jobs = new ArrayList<>();
//        swipeCardAdapter = new SwipeCardAdapter(jobs);

        currentUser = ParseUser.getCurrentUser();

        final Job.Query postsQuery = new Job.Query();
        postsQuery.getTop().withUser();

//        al = new ArrayList<>();
//        al.add("Job One");
//        al.add("Job Two");
//        al.add("Job Three");
//        al.add("Job Four");
//        al.add("Job Five");
//        al.add("Job Six");
//        al.add("Job Seven");
//        al.add("Job Eight");

//        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item, R.id.helloText, al );

        al = new ArrayList<SwipeCard>();
//        al.add(new SwipeCard("card1text1", "card1text2"));
//        al.add(new SwipeCard("card2text1", "card2text2"));
//        al.add(new SwipeCard("card3text1", "card3text2"));
//        al.add(new SwipeCard("card4text1", "card4text2"));
//        al.add(new SwipeCard("card5text1", "card5text2"));

        loadTopPosts();

        swipeCardAdapter = new SwipeCardAdapter(getContext(), getLayoutInflater(), al);

//        flingContainer.setAdapter(swipeCardAdapter);

        flingContainer = getActivity().findViewById(R.id.frame);

        flingContainer.setAdapter(swipeCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                SwipeCard temp = al.remove(0);
                swipeCardAdapter.notifyDataSetChanged();
                al.add(temp);
                swipeCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(getContext(), "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(getContext(), "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
////                 Ask for more data here
//                al.add("XML ".concat(String.valueOf(i)));
//                swipeCardAdapter.notifyDataSetChanged();
//                Log.d("LIST", "notified");
//                i++;
//                makeToast(getContext(), "No more!");
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
//                View view = flingContainer.getSelectedView();
                View view = flingContainer.getRootView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(getContext(), "Clicked!");
            }
        });

//        @OnClick(R.id.right)
//        public void right() {
//            /**
//             * Trigger the right event manually.
//             */
//            flingContainer.getTopCardListener().selectRight();
//        }
//
//        @OnClick(R.id.left)
//        public void left() {
//            flingContainer.getTopCardListener().selectLeft();
//        }

//        rvPosts = getActivity().findViewById(R.id.rvPost);
//        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvPosts.setAdapter(postAdapter);
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    private void loadTopPosts() {
        final Job.Query postsQuery = new Job.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Job>() {
            @Override
            public void done(List<Job> objects, ParseException e) {
                if (e == null) {
                    // posts.addAll(objects);
                    // postAdapter.notifyDataSetChanged();

                    for (int i = 0; i < objects.size(); ++i) {
                        /*
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                        */

                        Job job = objects.get(objects.size() - i - 1);
//                        jobs.add(job);
//                        swipeCardAdapter.notifyItemInserted(jobs.size() - 1);
                        try {
                            al.add(new SwipeCard(job.getTitle().toString(), job.getDescription().toString(), job.getImage().getUrl()));
                        } catch (NullPointerException e2) {
                            al.add(new SwipeCard("EMPTY", "EMPTY", "EMPTY"));
                        }
                        swipeCardAdapter.notifyDataSetChanged();
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}