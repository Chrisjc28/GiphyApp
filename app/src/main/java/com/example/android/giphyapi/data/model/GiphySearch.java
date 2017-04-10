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
 * Created by ccu17 on 28/03/2017.
 */

public class GiphySearch implements GiphyDAO {

    private static final String BASE_URL = "http://api.giphy.com/v1/gifs/{uri}";
    private static final String API_KEY = "dc6zaTOxFJmzC";
    private static final String JSON_OBJ_IMAGES = "images";
    private static final String JSON_OBJ_FIXED_HEIGHT = "fixed_height";

    @Override
    public void getGif(String searchString, final GiphyCallback cb) {
        //todo: what happens if the API gives a different, unexpected response, try changing api key
        ANRequest request = AndroidNetworking.get(BASE_URL)
                .addPathParameter("uri", "search")
                .addQueryParameter("q", searchString)
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
    public void getTrendingGif( GiphyCallback cb ) {

    }

    public void jsonParsing(JSONObject response ,final GiphyCallback cb) {
        try {
            JSONArray data = response.getJSONArray("data");
            ArrayList<String> gifs = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject images = data.getJSONObject(i).getJSONObject(JSON_OBJ_IMAGES);
                JSONObject fixedHeight = images.getJSONObject(JSON_OBJ_FIXED_HEIGHT);
                String resultUrl = fixedHeight.getString("url");
                gifs.add(resultUrl);
            }
            cb.success(gifs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
