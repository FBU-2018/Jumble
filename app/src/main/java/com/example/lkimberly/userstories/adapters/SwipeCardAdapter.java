package com.example.lkimberly.userstories.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.SwipeCard;

import java.util.List;

public class SwipeCardAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<SwipeCard> mJobs;

    Job job;

    public SwipeCardAdapter(Context context, LayoutInflater layoutInflater, List<SwipeCard> swipeCardList) {
        this.context = context;
        this.inflater = layoutInflater;
        this.mJobs = swipeCardList;
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        inflater = LayoutInflater.from(context);
//
//        View postView = inflater.inflate(R.layout.item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(postView);
//        return viewHolder;
//    }

//    public void onBindViewHolder(ViewHolder holder, int position) {
//        job = mJobs.get(position);
//
//        holder.tvUsername.setText(job.getUser().getUsername());
//        holder.tvBody.setText(job.getDescription());
//        holder.tvTime.setText(job.getCreatedAt().toString());
//        holder.tvTime.setText(getRelativeTimeAgo(job.getCreatedAt().toString()));
//        holder.tvTitle.setText(job.getTitle());
//        holder.tvDescription.setText(job.getDescription());
//
//        try {
//            Glide.with(context).load(job.getImage().getUrl()).into(holder.ivJobImage);
//        } catch (NullPointerException e) {
//            Log.d("Post Adapter", "No Profile Pic");
//        }
//    }

    @Override
    public int getCount() {
        return mJobs.size();
    }

    @Override
    public Object getItem(int i) {
        return mJobs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.helloText);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.helloText2);
            viewHolder.ivJob = convertView.findViewById(R.id.imageView2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SwipeCard sw = mJobs.get(position);

        viewHolder.textView1.setText(sw.getText1());
        viewHolder.textView2.setText(sw.getText2());

        try {
            Glide.with(context).load(sw.getImageUrl()).into(viewHolder.ivJob);
        } catch (NullPointerException e) {
            Log.d("Card Adapter", "No Pic");
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView textView1, textView2;
        public ImageView ivJob;
    }

//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
////        public ImageView ivPostImage;
////        public TextView tvUsername;
////        public TextView tvBody;
////        public TextView tvTime;
////        public ImageView ivProfileImage;
////        public ImageButton heartBtn;
//        public ImageView ivJobImage;
//        public TextView tvTitle;
//        public TextView tvDescription;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            // findViewByID lookup
////            ivPostImage = itemView.findViewById(R.id.post_image_iv);
////            tvUsername = itemView.findViewById(R.id.post_username_et);
////            tvBody = itemView.findViewById(R.id.post_body_et);
////            tvTime = itemView.findViewById(R.id.post_time);
////            ivProfileImage = itemView.findViewById(R.id.imageView3);
////            heartBtn = itemView.findViewById(R.id.heart_btn);
//            tvTitle = itemView.findViewById(R.id.helloText);
//            tvDescription = itemView.findViewById(R.id.helloText2);
//            ivJobImage = itemView.findViewById(R.id.imageView2);
//
//            itemView.setOnClickListener(this);
//
////            ivPostImage.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    /*
////                    final Intent intent = new Intent(context, DetailActivity.class);
////                    context.startActivity(intent);
////                    */
////                    // gets item position
////                    int position = getAdapterPosition();
////                    // make sure the position is valid, i.e. actually exists in the view
////                    if (position != RecyclerView.NO_POSITION) {
////                        // get the movie at the position, this won't work if the class is static
////                        Job job = mJobs.get(position);
////                        // create intent for the new activity
////                        Intent intent = new Intent(context, DetailActivity.class);
////                        // serialize the movie using parceler, use its short name as a key
////                        intent.putExtra(Job.class.getSimpleName(), job.getObjectId());
////                        // show the activity
////                        context.startActivity(intent);
////                    }
////                }
////            });
//        }
//
//        @Override
//        public void onClick(View view) {
////            // gets item position
////            int position = getAdapterPosition();
////            // make sure the position is valid, i.e. actually exists in the view
////            if (position != RecyclerView.NO_POSITION) {
////                // get the movie at the position, this won't work if the class is static
////                Job job = mJobs.get(position);
////                // create intent for the new activity
////                Intent intent = new Intent(context, DetailActivity.class);
////                // serialize the movie using parceler, use its short name as a key
////                intent.putExtra(Job.class.getSimpleName(), job);
////                // show the activity
////                context.startActivity(intent);
////            }
//        }
//    }

//    // Clean all elements of the recycler
//    public void clear() {
//        mJobs.clear();
//        notifyDataSetChanged();
//    }
//
//    // Add a list of items -- change to type used
//    public void addAll(List<Job> list) {
//        mJobs.addAll(list);
//        notifyDataSetChanged();
//    }

//    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
//    public String getRelativeTimeAgo(String rawJsonDate) {
//        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
//        sf.setLenient(true);
//
//        String relativeDate = "";
//        try {
//            long dateMillis = sf.parse(rawJsonDate).getTime();
//            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        return relativeDate;
//    }
}