package com.example.android.giphyapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.giphyapi.R;
import com.example.android.giphyapi.activity.ShareableFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecentFragment extends ShareableFragment {

    @BindView(R.id.recent_searches)
    CardView cardView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static RecentFragment newInstance() {
        RecentFragment fragment = new RecentFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        View v = inflater.inflate(R.layout.fragment_recent, container, false);
        ButterKnife.bind(this, v);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        return v;
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }

}
