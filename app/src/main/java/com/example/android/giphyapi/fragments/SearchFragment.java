package com.example.android.giphyapi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.giphyapi.R;
import com.example.android.giphyapi.activity.ShareableFragment;
import com.example.android.giphyapi.adapters.ViewPagerAdapter;
import com.example.android.giphyapi.data.model.GiphyCallback;
import com.example.android.giphyapi.data.model.GiphySearch;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ccu17 on 10/04/2017.
 */

public class SearchFragment extends ShareableFragment {

    private static final int OFF_SCREEN_PAGE_LIMIT = 5;
    private static final String PREF_KEY = "search_key";

    private GiphySearch giphyDAO = new GiphySearch();

    @BindView(R.id.search)
    EditText search;

    private SharedPreferences prefs;

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getViewPagerAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
        initSearchValueListener();
    }

    public ViewPagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), new ArrayList<String>());
        viewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.minus_clip_bounds));
        viewPagerAdapter.notifyDataSetChanged();
    }

    public void updateViewPager(ArrayList<String> gifs) {
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), gifs);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void displayingSearchedGifs( String searchString) {
        giphyDAO.getGif(searchString, new GiphyCallback() {
            @Override
            public void success( ArrayList<String> gifs ) {
                Log.i("CHRIS", "success: " + gifs);
                updateViewPager(gifs);
            }
            @Override
            public void failure( String failed ) {
                Log.i("CHRIS", "Sorry there was an error displaying the gifs");
            }
        });
    }

    private void saveSearchPref(String searchPref) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY, searchPref);
        editor.apply();
    }

    private void initSearchValueListener() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null) {
                    displayingSearchedGifs(search.getText().toString());
                    saveSearchPref(search.getText().toString());
                    return true;
                } else {
                    Log.i("Chris", "onEditorAction: There was an error setting the listener ");
                    return false;
                }
            }
        });
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        search.setText(prefs.getString(PREF_KEY, ""));
    }

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }
}
