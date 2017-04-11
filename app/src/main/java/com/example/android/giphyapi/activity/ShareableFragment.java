package com.example.android.giphyapi.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.android.giphyapi.R;
import com.example.android.giphyapi.adapters.ViewPagerAdapter;

import butterknife.BindView;

/**
 * Created by ccu17 on 11/04/2017.
 */

public class ShareableFragment extends Fragment {

    @BindView(R.id.pager)
    protected ViewPager viewPager;

    protected ViewPagerAdapter viewPagerAdapter;

    protected void shareGif() {
        if (viewPagerAdapter.getCount() == 0) {
            Toast.makeText(getActivity(), "Please search for a gif", Toast.LENGTH_LONG).show();
        } else {
            shareLink(viewPagerAdapter.getCurrentGif(viewPager.getCurrentItem()));
        }
    }

    protected void shareLink(String link) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setSubject("GIF")
                .setText(link);

        Intent intent = builder.getIntent();
        intent.setAction(Intent.ACTION_SEND);
        Intent chooser = Intent.createChooser(intent, getString(R.string.Chooser));

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }

    }

}
