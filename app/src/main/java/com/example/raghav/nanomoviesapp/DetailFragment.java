package com.example.raghav.nanomoviesapp;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    MovieData mCurrentMovie;
    ArrayList<String> mTrailerButtons = new ArrayList<>();
    ArrayList<TextView> mReviewTextViews = new ArrayList<>();

    LinearLayout mCurrentLayout;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mCurrentLayout = (LinearLayout) rootView.findViewById(R.id.main_layout);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("CURRENT_MOVIE")) {
            mCurrentMovie = intent.getParcelableExtra("CURRENT_MOVIE");
            ((TextView) rootView.findViewById(R.id.movie_title_bar))
                    .setText(mCurrentMovie.getTitle());

            ImageView moviePoster = (ImageView) rootView.findViewById(R.id.detail_poster_image);
            moviePoster.setScaleType(ImageView.ScaleType.FIT_XY);

            Picasso.with(getActivity())
                    .load(mCurrentMovie.getFullImageUrl())
                    .into(moviePoster);

            TextView ratingText = (TextView) rootView.findViewById(R.id.rating_text);
            ratingText.setText("Rating: " + mCurrentMovie.getVoteAverage());

            TextView releaseDateText = (TextView) rootView.findViewById(R.id.release_date_text);
            releaseDateText.setText("Release Date: " + mCurrentMovie.getReleaseDate());

            TextView synopsisText = (TextView) rootView.findViewById(R.id.synopsis_text);
            synopsisText.setText(mCurrentMovie.getOverview());

            fetchMovieDetails();

        }
        return rootView;
    }

    private void fetchMovieDetails() {
        RequestParams params = new RequestParams();
        params.put("api_key", Constants.API_KEY);
        params.put("append_to_response", "reviews,videos");

        MoviesdbRestClient.get("/" + String.valueOf(mCurrentMovie.getId()), true, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("Success Response", Integer.toString(statusCode));
                Log.v("Success Response", response.toString());
                HashMap<String,String> responseTrailers = new HashMap<String, String>();
                try {
                    JSONArray reviewArray = response.getJSONObject("reviews").getJSONArray("results");
                    for (int i = 0; i < reviewArray.length(); i++) {
                        JSONObject currentObject = reviewArray.getJSONObject(i);
                        mCurrentMovie.getReviews().add(currentObject.getString("content"));
//                        TODO: check to see if maybe reviews are missing, how to deal with that?
                    }
                    JSONArray videoArray = response.getJSONObject("videos").getJSONArray("results");
                    for (int i = 0; i < videoArray.length(); i++) {
                        JSONObject currentObject = videoArray.getJSONObject(i);
                        Log.v("KEY", currentObject.getString("key"));
                        responseTrailers.put(currentObject.getString("key"),currentObject.getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                TODO: ask if its better practice to save an instance variable as mCurrentMovie
//                TODO:      or is it better to pass it as a variable
                mCurrentMovie.setTrailers(responseTrailers);
                updateRemainingUI();

            }

            @Override
            public void onFailure(int statusCode,
                                  Header[] headers,
                                  Throwable throwable,
                                  JSONObject responseJSON) {
                Log.d("Failed Response", Integer.toString(statusCode));
            }
        });

    }

    private void updateRemainingUI() {
        HashMap<String,String> trailers = mCurrentMovie.getTrailers();
        ArrayList<String> reviews = mCurrentMovie.getReviews();
        int buttonDimension = (int) getResources().getDimension(R.dimen.trailer_size);
        int imageDimension = (int) getResources().getDimension(R.dimen.play_image_size);

        for(Map.Entry m:trailers.entrySet()){
            ImageButton btn = new ImageButton(getActivity());
            LinearLayout trailerLayout = new LinearLayout(getActivity());
            trailerLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            buttonDimension));
            trailerLayout.setOrientation(LinearLayout.HORIZONTAL);
            trailerLayout.setWeightSum(3f);
            mCurrentLayout.addView(trailerLayout);

            LinearLayout.LayoutParams lpImageButton = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            btn.setLayoutParams(lpImageButton);
            Picasso.with(getActivity())
                    .load(R.drawable.play_button)
                    .resize(imageDimension, imageDimension)
                    .centerInside()
                    .into(btn);
            trailerLayout.addView(btn);

            TextView tv = new TextView(getActivity());
            LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f);
            tv.setText((String)m.getValue());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setLayoutParams(lpTextView);

            trailerLayout.addView(tv);

        }
    }
}
