package com.example.lkimberly.userstories.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.ProfileDetailsActivity;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.User;
import com.parse.FindCallback;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MatchCarouselAdapter extends RecyclerView.Adapter<MatchCarouselAdapter.ViewHolder> {


    private final Activity activity;
    private List<ParseUser> mUsers = new ArrayList<>();
    private static String jobObjectId;
    private static Job mJob;

    private int mRowIndex = -1;




    // pass in the Tweets array into the constructor
    public MatchCarouselAdapter(Activity activity) {
        this.activity = activity;

    }

    Context context;




    // for each row, inflate
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View matchView = inflater.inflate(R.layout.match_carousel_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(activity, matchView);


        return viewHolder;

    }
    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // get the data according to the position
        final ParseUser user = mUsers.get(position);

        // populate the view according to this data
        holder.tvName.setText(user.get("name").toString());

        holder.tvInstitution.setText(user.get("institution").toString());
//        holder.tvDate.setText(getRelativeTimeAgo(post.createdAt()));
        holder.user = user;

        int round_radius = context.getResources().getInteger(R.integer.radius);
        int round_margin = context.getResources().getInteger(R.integer.margin);

        final RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(round_radius, round_margin);

        final RequestOptions requestOptions = RequestOptions.bitmapTransform(
                roundedCornersTransformation
        );


        // get the correct place holder and image view for the current orientation
        int placeholderId = R.drawable.ic_instagram_profile;
        ImageView imageView = holder.ivProfileImage;


        try {
            if (user.fetchIfNeeded().getParseFile("profilePicture")!= null) {
                Glide.with(holder.itemView.getContext())
                        .load(user.fetchIfNeeded().getParseFile("profilePicture").getUrl())
                        .apply(
                                RequestOptions.placeholderOf(placeholderId)
                                        .error(placeholderId)
                                        .fitCenter()
                        )
                        .apply(requestOptions)
                        .into(imageView);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(user.fetchIfNeeded().getParseFile("profilePicture"))
                        .apply(
                                RequestOptions.placeholderOf(placeholderId)
                                        .error(placeholderId)
                                        .fitCenter()
                        )
                        .apply(requestOptions)
                        .into(imageView);
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        //            implements View.OnClickListener, View.OnLongClickListener{
        public final Activity activity;
        public ImageView ivProfileImage;
        public TextView tvName;
        public TextView tvInstitution;
        public TextView tvPreviousJobs;
        public TextView tvRating;
        public TextView tvPhoneNumber;
        public TextView tvLinksToSocialMedia;
        public ParseUser user;
        private final int REQUEST_CODE = 21;


        public ViewHolder (Activity activity, View itemView) {
            super(itemView);
            this.activity = activity;

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.iv_profilePicDetailsPage);
            tvName = (TextView) itemView.findViewById(R.id.tv_potentialMatchNameDetailsPage);
            tvInstitution = (TextView) itemView.findViewById(R.id.tv_institutionValue);
            tvRating = (TextView) itemView.findViewById(R.id.tv_ratingValue);


            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent i = new Intent(itemView.getContext(), ProfileDetailsActivity.class);

            i.putExtra("User", Parcels.wrap(user));
            i.putExtra("job", Parcels.wrap(mJob));
            Log.d("Whats going on", mJob.getObjectId());
            activity.startActivityForResult(i, REQUEST_CODE);

        }

//        @Override
//        public boolean onLongClick(View v) {
//            // handle click here
//            Intent i = new Intent(itemView.getContext(), ReplyActivity.class);
//            i.putExtra("UserName", tvUsername.getText().toString());
//            i.putExtra("Tweet", Parcels.wrap(tweet));
//            activity.startActivityForResult(i, REQUEST_CODE);
//            return true;
//        }


    }




    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        mUsers.addAll(list);
        notifyDataSetChanged();
    }


    public void setData(List<ParseUser> data) {
        if (mUsers != data) {
            mUsers = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    public void setJob(final String job) {
        jobObjectId = job;

        Job.Query getJob = new Job.Query();
        getJob.
                getTop().
                whereEqualTo("objectId", job).
                findInBackground(new FindCallback<Job>() {
                    @Override
                    public void done(List<Job> objects, com.parse.ParseException e) {
                        mJob = objects.get(0);
                    }
                });
    }


}
