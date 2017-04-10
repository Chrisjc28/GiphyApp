package com.example.android.giphyapi.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.giphyapi.fragments.GiphyFragment;

import java.util.ArrayList;

/**
 * Created by ccu17 on 05/04/2017.
 */

//todo: delete this, use ViewPagerAdapter
public class TrendingViewPagerAdapter extends FragmentStatePagerAdapter {
    //todo: privatr
    ArrayList<String> gifs;

    public TrendingViewPagerAdapter( FragmentManager fm, ArrayList<String> gifs ) {
        super(fm);
        this.gifs = gifs;
    }

    public ArrayList<String> getGifs() {
        return gifs;
    }

    @Override
    public Fragment getItem( int position ) {
        return GiphyFragment.newInstance(gifs.get(position));
    }

    @Override
    public int getCount() {
        return gifs.size();
    }

    @Override
    public int getItemPosition( Object object ) {
        return POSITION_NONE;

    }
}

