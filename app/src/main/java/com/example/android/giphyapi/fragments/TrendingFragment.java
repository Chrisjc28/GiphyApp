package com.example.android.giphyapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.giphyapi.R;
import com.example.android.giphyapi.activity.ShareableFragment;
import com.example.android.giphyapi.adapters.ViewPagerAdapter;
import com.example.android.giphyapi.data.model.GiphyCallback;
import com.example.android.giphyapi.data.model.GiphyTrending;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrendingFragment extends ShareableFragment {

    private static final int OFF_SCREEN_PAGE_LIMIT = 5;

    private GiphyTrending giphyTrendingDAO = new GiphyTrending();

    @BindView(R.id.pager)
    ViewPager viewPager;


    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        collectTrendingGifs();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        View v = inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTrendingViewPager();
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach(context);
    }


    public void initTrendingViewPager() {
        ViewPagerAdapter trendingViewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), new ArrayList<String>());
        viewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
        viewPager.setAdapter(trendingViewPagerAdapter);
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.minus_clip_bounds));
        trendingViewPagerAdapter.notifyDataSetChanged();
    }

    public void updateViewPager(ArrayList<String> gifs) {
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), gifs);
        viewPager.setAdapter(viewPagerAdapter);
    }


    private void collectTrendingGifs() {
        giphyTrendingDAO.getTrendingGif(new GiphyCallback() {
            @Override
            public void success( ArrayList<String> gifs ) {
                updateViewPager(gifs);
            }
            @Override
            public void failure( String failed ) {
                Log.i("CHRIS", "Sorry there was an error displaying the gifs");
            }
        });
    }

    public static TrendingFragment newInstance() {
        TrendingFragment fragment = new TrendingFragment();
        return fragment;
    }



}
