package com.example.android.giphyapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.android.giphyapi.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawableBuilder;



public class GiphyFragment extends Fragment {
    private static final String ARG_URL = "url";

    private ImageView gif;
    private String url;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giphy_view, container, false);
        gif = (ImageView) view.findViewById(R.id.image1);
        CardView giphyCard = (CardView) view.findViewById(R.id.card_view);
//        Todo: getColor is deprecated, find another way to do this
        giphyCard.setCardBackgroundColor(getResources().getColor(R.color.cardViewBackground));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayGif();
    }

//    todo: loads of unused code
    private void displayGif() {
        final SimpleTarget<byte[]> something = new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
              final pl.droidsonroids.gif.GifDrawable gifDrawable;
                try {
                    gifDrawable = new GifDrawableBuilder().from(resource).build();
                    gif.setImageDrawable(gifDrawable);
                } catch (final IOException e) {
                }
            }
        };
        Glide.with(this)
                .load(url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(gif);
    }

    public static GiphyFragment newInstance(String url) {
        GiphyFragment fragment = new GiphyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

}
