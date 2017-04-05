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
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.android.giphyapi.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawableBuilder;

public class GiphyTrendingFragement extends Fragment {

    private static final String ARG_URL = "url";

    private CardView giphyCard;
    private ImageView gif1;
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
        View view = inflater.inflate(R.layout.fragment_giphy_trending_fragement, container, false);
        gif1 = (ImageView) view.findViewById(R.id.trending_image1);
        giphyCard = (CardView) view.findViewById(R.id.trending_card_view);
        giphyCard.setCardBackgroundColor(getResources().getColor(R.color.cardViewBackground));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayGif();
    }

    private void displayGif() {
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gif1);
        final SimpleTarget<byte[]> something = new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                final pl.droidsonroids.gif.GifDrawable gifDrawable;
                try {
                    gifDrawable = new GifDrawableBuilder().from(resource).build();
                    gif1.setImageDrawable(gifDrawable);
                } catch (final IOException e) {
                }
            }
        };
        Glide.with(this)
                .load(url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(gif1);
    }

    public static GiphyTrendingFragement newInstance( String url) {
        GiphyTrendingFragement fragment = new GiphyTrendingFragement();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }
}
