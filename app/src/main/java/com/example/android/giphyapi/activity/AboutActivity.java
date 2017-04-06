package com.example.android.giphyapi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.giphyapi.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar giphy_options_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        giphy_options_toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(giphy_options_toolbar);

    }
}
