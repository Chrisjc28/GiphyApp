package com.example.android.giphyapi.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.giphyapi.fragments.GiphyFragment;

import java.util.ArrayList;

/**
 * Created by ccu17 on 04/04/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> gifs;

    public ViewPagerAdapter( FragmentManager fm, ArrayList<String> gifs) {
        super(fm);
        this.gifs = gifs;
    }

    @Override
    public Fragment getItem( int position) {
        return GiphyFragment.newInstance(gifs.get(position));

    }

    public ArrayList<String> getGifs() {
        return gifs;
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


