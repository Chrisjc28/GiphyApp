package com.example.android.giphyapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.giphyapi.adapters.TrendingViewPagerAdapter;
import com.example.android.giphyapi.adapters.ViewPagerAdapter;
import com.example.android.giphyapi.data.model.GiphyCallback;
import com.example.android.giphyapi.data.model.GiphySearch;
import com.example.android.giphyapi.data.model.GiphyTrending;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText search;
    SharedPreferences prefs;
    ViewPager viewPager;

    private static final String PREF_KEY = "search_key";

    private BottomNavigationView bottomNavigationView;
    private GiphySearch RefreshDAO = new GiphySearch();
    private GiphyTrending TrendingDAO = new GiphyTrending();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = MainActivity.this.getPreferences(Context.MODE_APPEND);

        Toolbar giphy_options_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        giphy_options_toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(giphy_options_toolbar);
        getSupportActionBar();

        search = (EditText) findViewById(R.id.search);
        viewPager = (ViewPager) findViewById(R.id.pager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        initViewPager();
        searchGifs();
        bottomNavControls();
    }

    public void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new ArrayList<String>());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20*5, getResources().getDisplayMetrics());
        viewPager.setPageMargin(-margin);
        viewPagerAdapter.notifyDataSetChanged();
    }

    public void initTrendingViewPager() {
        TrendingViewPagerAdapter trendingViewPagerAdapter = new TrendingViewPagerAdapter(getSupportFragmentManager(), new ArrayList<String>());
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(trendingViewPagerAdapter);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20*5, getResources().getDisplayMetrics());
        viewPager.setPageMargin(-margin);
        trendingViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_share).getIcon().setTint(getColor(R.color.white));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                    ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
                if (adapter.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Please search for a gif", Toast.LENGTH_LONG).show();
                } else {
                    shareGifLink(adapter.getGifs().get(viewPager.getCurrentItem()).toString());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateViewPager(ArrayList<String> gifs) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), gifs);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();
    }

    private void collectGifs(String searchString) {
        RefreshDAO.getGif(searchString, new GiphyCallback() {
            @Override
            public void success( ArrayList<String> gifs ) {
                updateViewPager(gifs);
            }
            @Override
            public void failure( String failed ) {
            }
        });
    }

    private void collectTrendingGifs() {
        TrendingDAO.getGif(new GiphyCallback() {
            @Override
            public void success( ArrayList<String> gifs ) {
                updateViewPager(gifs);
            }
            @Override
            public void failure( String failed ) {

            }
        });
    }

    private void searchGifs() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null) {
                    collectGifs(search.getText().toString());
                    saveSearchPref(search.getText().toString());
                }
                return false;
            }
        });
        search.setText(prefs.getString(PREF_KEY, ""));
    }

    private void saveSearchPref(String searchPref) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY, searchPref);
        editor.apply();
    }

    private void shareGifLink(String currentGif) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(MainActivity.this)
                .setType("text/plain")
                .setSubject("GIF")
                .setText(currentGif);
        Intent intent = builder.getIntent();
        intent.setAction(Intent.ACTION_SEND);
        Intent chooser = Intent.createChooser(intent, "Chooser");
        if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null)
            startActivity(chooser);
    }

    private void bottomNavControls() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
                switch (item.getItemId()) {
                    case R.id.action_trending:
                        collectTrendingGifs();
                        initTrendingViewPager();
                        break;
                    case R.id.action_recent:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


