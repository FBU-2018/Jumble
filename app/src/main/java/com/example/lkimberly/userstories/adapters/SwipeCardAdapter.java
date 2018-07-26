package com.example.lkimberly.userstories.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.HomeActivity;
import com.example.lkimberly.userstories.activities.JobDetailsActivity;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.SwipeCard;

import org.parceler.Parcels;

import java.util.List;

public class SwipeCardAdapter extends BaseAdapter {

  Context context;
  LayoutInflater inflater;
  List<SwipeCard> mJobs;
  public final int REQUEST_CODE = 23;


  public SwipeCardAdapter(
      Context context, LayoutInflater layoutInflater, List<SwipeCard> swipeCardList) {
    this.context = context;
    this.inflater = layoutInflater;
    this.mJobs = swipeCardList;
  }

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

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.item, parent, false);

      viewHolder = new ViewHolder();
      viewHolder.textView1 = (TextView) convertView.findViewById(R.id.helloText);
      viewHolder.textView2 = (TextView) convertView.findViewById(R.id.helloText2);
      viewHolder.ivJob = convertView.findViewById(R.id.ivJobPhoto);

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

    viewHolder.job = mJobs.get(position).getJob();

    return convertView;
  }

  private static class ViewHolder{
    public TextView textView1, textView2;
    public ImageView ivJob;
    public Job job;

  }

  public void goToDetailsPage(Job job){
    Intent i = new Intent(context, JobDetailsActivity.class);
    i.putExtra("job", Parcels.wrap(job));
    i.putExtra("viewForPotentialHire", true);
    ((HomeActivity) context).startActivityForResult(i, REQUEST_CODE);
  }
}