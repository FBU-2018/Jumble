package com.example.lkimberly.userstories.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.JobDetailsActivity;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.MatchDataModel;
import com.example.lkimberly.userstories.models.Matches;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MatchPageAdapter extends RecyclerView.Adapter {
    private static final int TYPE_NO_MATCHES = 2;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_NO_MATCHES_HEADER = 3;


    private final Activity activity;


    private List<List<ParseUser>> mAllMatches;

    RecyclerView.RecycledViewPool viewPool;

    private static RecyclerView horizontalList;

    private List<String> mJobs;

    boolean putHeader = true;

    private final int REQUEST_CODE = 20;

    //match data (job followed by list of users who matched with that job
    List<MatchDataModel> mMatchesModelList;

    // pass in the Matches array into the constructor
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
        } else if (viewType == TYPE_NO_MATCHES_HEADER){
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View matchView = inflater.inflate(R.layout.no_matches_header, parent, false);
            return new VHNoMatchesHeader(matchView);
        } else {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View matchView = inflater.inflate(R.layout.job_no_match, parent, false);


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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder incomingViewHolder, final int position) {

        final RecyclerView.ViewHolder viewHolder = incomingViewHolder;

        Log.d("Position", Integer.toString(position));
        Log.d("Data", mMatchesModelList.get(position).toString());
        if (mMatchesModelList.get(position).getItemTypee() == 0) {
            // get the data according to the position
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.setJob(mMatchesModelList.get(position).jobObjectId);
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
                                VHheader.job = objects.get(0);
                                VHheader.txtTitle.setText(objects.get(0).getTitle());


                                int round_radius = context.getResources().getInteger(R.integer.radius);
                                int round_margin = context.getResources().getInteger(R.integer.margin);

                                final RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(round_radius, round_margin);

                                final RequestOptions requestOptions = RequestOptions.bitmapTransform(
                                        roundedCornersTransformation
                                );


                                // get the correct place holder and image view for the current orientation
                                int placeholderId = R.drawable.ic_instagram_profile;
                                ImageView imageView = VHheader.jobPic;


                                try {
                                    if (objects.get(0).fetchIfNeeded().getParseFile("image")!= null) {
                                        Glide.with(VHheader.itemView.getContext())
                                                .load(objects.get(0).fetchIfNeeded().getParseFile("image").getUrl())
                                                .apply(
                                                        RequestOptions.placeholderOf(placeholderId)
                                                                .error(placeholderId)
                                                                .fitCenter()
                                                )
                                                .apply(requestOptions)
                                                .into(imageView);
                                    } else {
                                        Glide.with(VHheader.itemView.getContext())
                                                .load(objects.get(0).fetchIfNeeded().getParseFile("image"))
                                                .apply(
                                                        RequestOptions.placeholderOf(placeholderId)
                                                                .error(placeholderId)
                                                                .fitCenter()
                                                )
                                                .into(imageView);
                                    }
                                } catch (com.parse.ParseException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                    });
        } else if (mMatchesModelList.get(position).getItemTypee() == 3){ // Dealing with the header saying the following jobs have no matches
            // do nothing
        } else {
            final VHNoMatches VHNoMatches = (VHNoMatches) viewHolder;
            Job jobForNoMatchJobView = mMatchesModelList.get(position).getJob();
            VHNoMatches.job = jobForNoMatchJobView;
            VHNoMatches.txtNoMatches.setText(jobForNoMatchJobView.getTitle());


            int round_radius = context.getResources().getInteger(R.integer.radius);
            int round_margin = context.getResources().getInteger(R.integer.margin);

            final RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(round_radius, round_margin);

            final RequestOptions requestOptions = RequestOptions.bitmapTransform(
                    roundedCornersTransformation
            );


            // get the correct place holder and image view for the current orientation
            int placeholderId = R.drawable.ic_instagram_profile;
            ImageView imageView = VHNoMatches.jobPic;


            try {
                if (jobForNoMatchJobView.fetchIfNeeded().getParseFile("image")!= null) {
                    Glide.with(VHNoMatches.itemView.getContext())
                            .load(jobForNoMatchJobView.fetchIfNeeded().getParseFile("image").getUrl())
                            .apply(
                                    RequestOptions.placeholderOf(placeholderId)
                                            .error(placeholderId)
                                            .fitCenter()
                            )
//                                                .apply(requestOptions)
                            .into(imageView);
                } else {
                    Glide.with(VHNoMatches.itemView.getContext())
                            .load(jobForNoMatchJobView.fetchIfNeeded().getParseFile("image"))
                            .apply(
                                    RequestOptions.placeholderOf(placeholderId)
                                            .error(placeholderId)
                                            .fitCenter()
                            )
                            .into(imageView);
                }
            } catch (com.parse.ParseException e2) {
                e2.printStackTrace();
            }
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

        public void setJob(String job){
            horizontalAdapter.setJob(job);
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


    class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtTitle;
        ImageView jobPic;
        Job job;
        Matches match;
        public VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.txtHeader);
            this.jobPic = (ImageView) itemView.findViewById(R.id.iv_headerJobPic);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), JobDetailsActivity.class);
            i.putExtra("job", Parcels.wrap(job));
            activity.startActivityForResult(i, REQUEST_CODE);

        }
    }

    class VHNoMatches extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtNoMatches;
        ImageView jobPic;
        Job job;
        public VHNoMatches(View itemView) {
            super(itemView);
            this.txtNoMatches = (TextView)itemView.findViewById(R.id.tv_jobNoMatchesTitle);
            this.jobPic = (ImageView) itemView.findViewById(R.id.iv_jobNoMatchPic);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), JobDetailsActivity.class);
            i.putExtra("job", Parcels.wrap(job));
            activity.startActivityForResult(i, REQUEST_CODE);

        }
    }


    class VHNoMatchesHeader extends RecyclerView.ViewHolder {
        TextView header;
        public VHNoMatchesHeader(View itemView) {
             super(itemView);
             this.header = (TextView) itemView.findViewById(R.id.tv_noMatches);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMatchesModelList.get(position).getItemTypee() == 1)
        {
            return TYPE_HEADER;
        } else if (mMatchesModelList.get(position).getItemTypee() == 2) {
            return TYPE_NO_MATCHES;
        } else if (mMatchesModelList.get(position).getItemTypee() == 3) {
            return TYPE_NO_MATCHES_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }



}
