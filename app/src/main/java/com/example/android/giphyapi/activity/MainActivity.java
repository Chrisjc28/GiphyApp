package com.example.android.giphyapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.giphyapi.R;
import com.example.android.giphyapi.fragments.AboutFragment;
import com.example.android.giphyapi.fragments.RecentFragment;
import com.example.android.giphyapi.fragments.SearchFragment;
import com.example.android.giphyapi.fragments.TrendingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private SearchFragment searchFragment = new SearchFragment();
    private AboutFragment aboutFragment = new AboutFragment();
    private TrendingFragment trendingFragment = new TrendingFragment();
    private RecentFragment recentFragment = new RecentFragment();

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.my_toolbar)
    Toolbar giphyOptionsToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private Menu menuOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        giphyOptionsToolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(giphyOptionsToolbar);

        AboutFragment.newInstance();
        SearchFragment.newInstance();
        TrendingFragment.newInstance();
        RecentFragment.newInstance();
        initBottomNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuOptions = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_share).getIcon().setTint(getColor(R.color.white));
        return true;
    }

    //TODO: only show the share icon when it applies (aka to the screen your on)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                //TODO: this will now be contextual and wont apply to all screens (fragments). It will share onl giphys when on screens Search and Trending. Use bottomToolbar.getPosition() to tell which screen you are on (and possibly make an enum)
//                ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
//                if (adapter.getCount() == 0) {
//                    Toast.makeText(MainActivity.this, "Please search for a gif", Toast.LENGTH_LONG).show();
//                } else {
//                    shareGifLink(adapter.getCurrentGif(viewPager.getCurrentItem()));
//                }
//                break;
        }
        return super.onOptionsItemSelected(item);
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
            int position = 0;
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commit();
                        position = 0;
                        break;
                    case R.id.action_trending:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, trendingFragment).commit();
                        position = 1;
                        break;
                    case R.id.action_recent:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recentFragment).commit();
                        position = 2;
                        break;
                    case R.id.action_about:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, aboutFragment).commit();
                        position = 3;
                        break;
                }
                MenuItem shareMenuItem = menuOptions.findItem(R.id.action_share);
                if (position == 2 || position == 3 ) {
                    shareMenuItem.setVisible(false);
                } else {
                    shareMenuItem.setVisible(true);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}


