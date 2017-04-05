package com.example.android.giphyapi.data.model;

import com.androidnetworking.AndroidNetworking;
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

public class GiphyTrending implements TrendingDAO {

    private static final String API_KEY = "dc6zaTOxFJmzC";

    @Override
    public void getGif(String searchString, final GiphyCallback cb ) {
        String query = new StringBuilder()
                .append("http://api.giphy.com/v1/gifs/trending?")
                .append("api_key=")
                .append(API_KEY).toString();

        AndroidNetworking.get(query)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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

                    @Override
                    public void onError(ANError anError) {
                        cb.failure("Failed");
                    }
                });

    }
}
