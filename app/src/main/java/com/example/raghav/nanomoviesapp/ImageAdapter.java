package com.example.raghav.nanomoviesapp;

import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by raghav on 7/14/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MovieData> mAdapterData;

    public ImageAdapter(Context c, ArrayList<MovieData> moviesList) {
        this.mContext = c;
        this.mAdapterData = moviesList;
    }

    public ArrayList<MovieData> getAdapterData() {
        return mAdapterData;
    }

    public void setAdapterData(ArrayList<MovieData> mAdapterData) {
        this.mAdapterData = mAdapterData;
    }

    @Override
    public int getCount() {
        return mAdapterData.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mAdapterData.get(position).getFullImageUrl())
                .resize(500,500)
                .centerInside()
                .into(imageView);

        return imageView;
    }
}
