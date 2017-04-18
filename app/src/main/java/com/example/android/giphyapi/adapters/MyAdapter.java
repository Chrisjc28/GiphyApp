package com.example.android.giphyapi.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.giphyapi.R;

/**
 * Created by ccu17 on 18/04/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private String[] dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textView;
        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.recents_card_view);
            textView = (TextView) v.findViewById(R.id.gif_url);
        }
    }

    public MyAdapter (String[] MyDataSet) {
        dataSet = MyDataSet;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recents_recycler_card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        holder.textView.setText(dataSet[position]);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;

    }

}
