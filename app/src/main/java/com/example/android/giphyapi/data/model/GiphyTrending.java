package com.example.android.giphyapi.data.model;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ccu17 on 05/04/2017.
 */

//Remove this class, but keep functionality, join it with other search class
public class GiphyTrending implements GiphyDAO {

    private static final String BASE_URL = "http://api.giphy.com/v1/gifs/{uri}";
    private static final String API_KEY = "dc6zaTOxFJmzC";

    @Override
    public void getTrendingGif( final GiphyCallback cb ) {
        ANRequest request = AndroidNetworking.get(BASE_URL)
                .addPathParameter("uri", "trending")
                .addQueryParameter("q", "")
                .addQueryParameter("api_key", API_KEY)
                .setPriority(Priority.LOW)
                .build();

                request.getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                       jsonParsing(response, cb);
                    }
                    @Override
                    public void onError(ANError anError) {
                        cb.failure("Failed");
                    }
                });
    }

    @Override
    public void getGif( String searchString, GiphyCallback cb ) {

    }

    public void jsonParsing(JSONObject response ,final GiphyCallback cb) {
        try {
            JSONArray data = response.getJSONArray("data");
            ArrayList<String> trendingGifs = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject images = data.getJSONObject(i).getJSONObject("images");
                JSONObject fixedHeight = images.getJSONObject("fixed_height");
                String resultUrl = fixedHeight.getString("url");
                trendingGifs.add(resultUrl);
            }
            cb.success(trendingGifs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
