package com.lecomte.jessy.popularmovies;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.lecomte.jessy.mythemoviedblib.data.TrailerInfo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jessy on 2016-01-26.
 */
public class TrailerListViewAdapter implements ListAdapter {

    // Data to display in the listView
    private ArrayList<TrailerInfo> mData;

    public TrailerListViewAdapter(TrailerInfo[] trailerArray) {
        mData = new ArrayList<TrailerInfo>(Arrays.asList(trailerArray));
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(mData.get(position).getId());
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create layout on first use
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trailer_list_item, null);
        }

        // Bind data to layout
        TextView trailerName = (TextView)convertView.findViewById(R.id.trailer_name_TextView);
        trailerName.setText(mData.get(position).getName());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mData.isEmpty();
    }
}
