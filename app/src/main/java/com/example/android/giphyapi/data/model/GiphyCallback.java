package com.example.android.giphyapi.data.model;

import java.util.ArrayList;

/**
 * Created by ccu17 on 29/03/2017.
 */

public interface GiphyCallback {

    void success(ArrayList<String> gifs);

    void failure(String failed);
}
