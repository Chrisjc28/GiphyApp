package com.example.android.giphyapi.data.model;

/**
 * Created by ccu17 on 28/03/2017.
 */

public interface RefreshDAO {

    void getGif(String searchString, GiphyCallback cb);

}
