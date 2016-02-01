package com.lecomte.jessy.popularmovies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecomte.jessy.popularmovies.RecyclerView.RV_MultiViewTypeAdapter;
import com.lecomte.jessy.popularmovies.RecyclerView.RecyclerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jessy on 2016-01-28.
 */
public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        assert recyclerView != null;

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<RecyclerViewData> recyclerViewItems = new ArrayList<RecyclerViewData>();
        for (int i=0; i<100; i++) {
            recyclerViewItems.add(new RecyclerViewData("Item " + i));
        }

        RV_MultiViewTypeAdapter recyclerViewAdapter =
                new RV_MultiViewTypeAdapter(recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }
}
