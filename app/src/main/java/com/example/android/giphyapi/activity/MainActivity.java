package com.example.android.giphyapi.activity;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.giphyapi.R;
import com.example.android.giphyapi.adapters.ViewPagerAdapter;
import com.example.android.giphyapi.data.model.GiphyCallback;
import com.example.android.giphyapi.data.model.GiphySearch;
import com.example.android.giphyapi.data.model.GiphyTrending;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_KEY = "search_key";
    private static final int OFF_SCREEN_PAGE_LIMIT = 5;

    private GiphySearch giphyDAO = new GiphySearch();
    private GiphyTrending giphyTrendingDAO = new GiphyTrending();

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.my_toolbar)
    Toolbar giphyOptionsToolbar;

    private SharedPreferences prefs;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        giphyOptionsToolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(giphyOptionsToolbar);

        initViewPager();
        initBottomNavigation();
        initSearchValueListener();
        prefs = getPreferences(Context.MODE_APPEND);
    }

    public void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new ArrayList<String>());
        viewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.minus_clip_bounds));
    }

    public void initTrendingViewPager() {
        ViewPagerAdapter trendingViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new ArrayList<String>());
        viewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
        viewPager.setAdapter(trendingViewPagerAdapter);
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.minus_clip_bounds));
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
                    shareGifLink(adapter.getCurrentGif(viewPager.getCurrentItem()));
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
//        search.setText(prefs.getString(PREF_KEY, ""));
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
        Intent chooser = Intent.createChooser(intent, getString(R.string.Chooser));
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(chooser);
    }

    private void initBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
                switch (item.getItemId()) {
                    case R.id.action_trending:
                        collectTrendingGifs();
                        initTrendingViewPager();
                        item.setChecked(true);
                        break;
                    case R.id.action_recent:
                        break;
                    case R.id.action_about:
                        startAboutActivity();
                        break;
                }
                return true;
            }
        });
    }

    private void startAboutActivity() {
        Intent About = new Intent(this, AboutActivity.class);
        startActivity(About);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


