package com.example.lkimberly.userstories.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.MatchDataModel;
import com.parse.FindCallback;
import com.parse.ParseException;

import com.parse.ParseUser;

import java.util.List;

public class MatchPageAdapter extends RecyclerView.Adapter {
    private static final int TYPE_NO_MATCHES = 2;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 0;


    private final Activity activity;


    private List<List<ParseUser>> mAllMatches;

    RecyclerView.RecycledViewPool viewPool;

    private static RecyclerView horizontalList;

    private List<String> mJobs;

    boolean putHeader = true;

    //match data (job followed by list of users who matched with that job
    List<MatchDataModel> mMatchesModelList;

    // pass in the Tweets array into the constructor
    public MatchPageAdapter(Activity activity, List<MatchDataModel> matchesModelList) {
        this.activity = activity;
//        mAllMatches = allMatches;
        viewPool = new RecyclerView.RecycledViewPool();
//        mJobs = jobs;
        mMatchesModelList = matchesModelList;

    }

    Context context;



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("What it view", Integer.toString(viewType));
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View matchView = inflater.inflate(R.layout.match_carousel, parent, false);

            int height = parent.getMeasuredHeight() / 4;
            matchView.setMinimumHeight(height);


            ViewHolder viewHolder = new ViewHolder(activity, matchView);

            horizontalList.setRecycledViewPool(viewPool);
            return viewHolder;

        } else if (viewType == TYPE_HEADER){
            //inflate your layout and pass it to view holder

            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View matchView = inflater.inflate(R.layout.job_header, parent, false);


            return new VHHeader(matchView);
        } else {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View matchView = inflater.inflate(R.layout.no_matches, parent, false);


            return new VHNoMatches(matchView);
        }

//        context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(context);
//
//        View matchView = inflater.inflate(R.layout.match_carousel, parent, false);
//
//        int height = parent.getMeasuredHeight() / 4;
//        matchView.setMinimumHeight(height);
//
//
//        ViewHolder viewHolder = new ViewHolder(activity, matchView);
//
//        horizontalList.setRecycledViewPool(viewPool);
//        return viewHolder;

    }

    // bind the values based on the position of the element


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder incomingViewHolder, int position) {

        final RecyclerView.ViewHolder viewHolder = incomingViewHolder;

        Log.d("Position", Integer.toString(position));
        Log.d("Data", mMatchesModelList.get(position).toString());
        if (mMatchesModelList.get(position).getItemTypee() == 0) {
            // get the data according to the position
            ViewHolder holder = (ViewHolder) viewHolder;

            holder.horizontalAdapter.setData(mMatchesModelList.get(position).getAllMatches()); // List of Users
            holder.horizontalAdapter.setRowIndex(position);
        } else if (mMatchesModelList.get(position).getItemTypee() == 1){ // Dealing with a header

            Job.Query jobQuery = new Job.Query();
            jobQuery.getTop()
                    .whereEqualTo("objectId", mMatchesModelList.get(position).getJobId())
                    .findInBackground(new FindCallback<Job>() {
                        @Override
                        public void done(List<Job> objects, ParseException e) {
                            if (e == null) {
                                final VHHeader VHheader = (VHHeader) viewHolder;
                                VHheader.txtTitle.setText(objects.get(0).getTitle());
                            }
                        }
                    });

        } else {

        }
    }


    @Override
    public int getItemCount() {
        return mMatchesModelList.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //            implements View.OnClickListener{
        //            implements View.OnClickListener, View.OnLongClickListener{
        public final Activity activity;

        private final int REQUEST_CODE = 21;

        private MatchCarouselAdapter horizontalAdapter;

        public ViewHolder (Activity activity, View itemView) {
            super(itemView);
            this.activity = activity;

            // perform findViewById lookups

            horizontalList = (RecyclerView) itemView.findViewById(R.id.rv_innerRecyclerView);
            horizontalList.setLayoutManager(new LinearLayoutManager(activity.getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new MatchCarouselAdapter(activity);
            horizontalList.setAdapter(horizontalAdapter);


        }

    }

    // Clean all elements of the recycler
    public void clear() {
        mMatchesModelList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<MatchDataModel> list) {
        mMatchesModelList.addAll(list);
        notifyDataSetChanged();
    }


    class VHHeader extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView jobPic;
        public VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.txtHeader);
            this.jobPic = (ImageView) itemView.findViewById(R.id.iv_headerJobPic);

        }
    }

    class VHNoMatches extends RecyclerView.ViewHolder {
        TextView txtNoMatches;
        public VHNoMatches(View itemView) {
            super(itemView);
            this.txtNoMatches = (TextView)itemView.findViewById(R.id.tv_noMatches);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMatchesModelList.get(position).getItemTypee() == 1)
        {
            return TYPE_HEADER;
        } else if (mMatchesModelList.get(position).getItemTypee() == 2) {
            return TYPE_NO_MATCHES;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }



}
