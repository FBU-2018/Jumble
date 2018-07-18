package com.example.lkimberly.userstories;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.List;

public class MatchPageAdapter extends RecyclerView.Adapter<MatchPageAdapter.ViewHolder> {


    private final Activity activity;


    private List<List<ParseUser>> mAllMatches;

    RecyclerView.RecycledViewPool viewPool;

    private static RecyclerView horizontalList;

    // pass in the Tweets array into the constructor
    public MatchPageAdapter(Activity activity, List<List<ParseUser>> allMatches) {
        this.activity = activity;
        mAllMatches = allMatches;
        viewPool = new RecyclerView.RecycledViewPool();

    }

    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View matchView = inflater.inflate(R.layout.match_carousel, parent, false);

        int height = parent.getMeasuredHeight() / 4;
        matchView.setMinimumHeight(height);


        ViewHolder viewHolder = new ViewHolder(activity, matchView);

        horizontalList.setRecycledViewPool(viewPool);


        return viewHolder;

    }
    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // get the data according to the position

        holder.horizontalAdapter.setData(mAllMatches.get(position)); // List of Users
        holder.horizontalAdapter.setRowIndex(position);


    }

    @Override
    public int getItemCount() {
        return mAllMatches.size();
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
        mAllMatches.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<List<ParseUser>> list) {
        mAllMatches.addAll(list);
        notifyDataSetChanged();
    }




}
