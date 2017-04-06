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
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.giphyapi.R;
import com.example.android.giphyapi.adapters.TrendingViewPagerAdapter;
import com.example.android.giphyapi.adapters.ViewPagerAdapter;
import com.example.android.giphyapi.data.model.GiphyCallback;
import com.example.android.giphyapi.data.model.GiphySearch;
import com.example.android.giphyapi.data.model.GiphyTrending;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_KEY = "search_key";



    private EditText search;
    private SharedPreferences prefs;
    private ViewPager viewPager;

    //Todo:re-order all of the class level variables

    private BottomNavigationView bottomNavigationView;
    private GiphySearch refreshDAO = new GiphySearch();
    private GiphyTrending trendingDAO = new GiphyTrending();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.search);
        viewPager = (ViewPager) findViewById(R.id.pager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        Toolbar giphyOptionsToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        giphyOptionsToolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(giphyOptionsToolbar);

        //todo: move these init methods to be all together and rename
        initViewPager();
        searchGifs();
        bottomNavControls();
        prefs = getPreferences(Context.MODE_APPEND);

    }

    /*Todo: refactor normal and trending view pager to use the same adapter/implementation with a different dataset
    * Todo: also, make the adapter a field.*/
    /*Todo: Change offscreenpagelimit to a constant
    * Todo: CHange the -margin into a dimen in dimens.xml like: getResources().getDimensionPixelSize(R.dimen.pager_negative_margin)*/
//
    public void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new ArrayList<String>());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20*5, getResources().getDisplayMetrics());
        viewPager.setPageMargin(-margin);
//        viewPagerAdapter.notifyDataSetChanged();
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
                    //todo: create getGif(int position) (singular) method in adapter and use that instead
                    shareGifLink(adapter.getGifs().get(viewPager.getCurrentItem()));
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
        //Todo: RefreshDAO and TrendingDAO can just be GiphyDAO and have two methods
        //Potnetially change method names as well to be more appropriate
        refreshDAO.getGif(searchString, new GiphyCallback() {
            @Override
            public void success( ArrayList<String> gifs ) {
                updateViewPager(gifs);
            }
            @Override
            public void failure( String failed ) {
//                todo: Do something, log at least, toast, snack bar etc.?
                Log.i("CHRIS", "Sorry there was an error displaying the gifs");
            }
        });
    }

    private void collectTrendingGifs() {
        trendingDAO.getGif(new GiphyCallback() {
            @Override
            public void success( ArrayList<String> gifs ) {
                updateViewPager(gifs);
            }
            @Override
            public void failure( String failed ) {
                //todo: same as above

            }
        });
    }

//    todo: rename this method to something more appropriate
    private void searchGifs() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null) {
                    collectGifs(search.getText().toString());
                    saveSearchPref(search.getText().toString());
                    return true;
                } else {
                    Log.i("Chirs", "onEditorAction: There was an error setting the listener ");
                    return false;
                }
//                todo: should we return false all the time?
            }
        });
//        search.setText(prefs.getString(PREF_KEY, ""));
        //Now crashed the app with the changes made
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
        // todo: should this be called "chooser"? and put in strings.xml
        Intent chooser = Intent.createChooser(intent, getString(R.string.Chooser));
        if (intent.resolveActivity(getPackageManager()) != null)
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
                        item.setChecked(true);
                        break;
                    case R.id.action_recent:
                        break;
                    case R.id.action_about:
                        startAboutActivity();
                        break;
                }
                //todo: should this return false all the time
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


