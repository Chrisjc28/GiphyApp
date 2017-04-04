package com.example.android.giphyapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.giphyapi.data.model.GiphyCallback;
import com.example.android.giphyapi.data.model.GiphySearch;
import com.example.android.giphyapi.fragments.GiphyFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private static final String PREF_KEY = "search_key";

    private EditText search;

    private GiphySearch RefreshDAO = new GiphySearch();

    SharedPreferences prefs;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = MainActivity.this.getPreferences(Context.MODE_APPEND);

        Toolbar giphy_options_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        giphy_options_toolbar.setBackgroundColor(getColor(R.color.toolbarBackground));
        giphy_options_toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(giphy_options_toolbar);
        getSupportActionBar();

        search = (EditText) findViewById(R.id.search);
        viewPager = (ViewPager) findViewById(R.id.pager);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(MainActivity.this, "About clicked",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onRefresh() {
//        swipeRefreshLayout.setRefreshing(false);
//    }


    private void collectGifs(String searchString) {
        RefreshDAO.getGif(searchString ,new GiphyCallback() {
            @Override
            public void success(ArrayList<String> gifs) {
                viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(),gifs);
                viewPager.setAdapter(viewPagerAdapter);
                viewPagerAdapter.notifyDataSetChanged();
            }
            @Override
            public void failure(String failed) {
            }
        });
    }

    private void saveSearchPref(String searchPref) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY, searchPref);
        editor.apply();
    }

    public class viewPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> gifs;

        public viewPagerAdapter(FragmentManager fm, ArrayList<String> gifs) {
            super(fm);
            this.gifs = gifs;
        }

        @Override
        public Fragment getItem(int position) {
           return GiphyFragment.newInstance(gifs.get(position));
        }

        @Override
        public int getItemPosition( Object object ) {
            return POSITION_NONE;

        }

        @Override
        public int getCount() {
            return gifs.size();
        }
    }

}


