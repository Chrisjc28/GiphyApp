package com.example.android.giphyapi.data.model;

/**
 * Created by ccu17 on 05/04/2017.
 */

public interface TrendingDAO {

    void getGif(String searchString, GiphyCallback cb);

}
