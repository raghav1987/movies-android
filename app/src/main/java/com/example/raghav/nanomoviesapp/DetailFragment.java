package com.example.raghav.nanomoviesapp;


import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.text.TextUtils;
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
                HashMap<String, String> responseTrailers = new HashMap<String, String>();
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
                        responseTrailers.put(currentObject.getString("key"),
                                currentObject.getString("name"));
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

    //    TODO: definitely need to ask if its better practice to create a listview/recycleview here
//    TODO: or not
    private void updateRemainingUI() {
        HashMap<String, String> trailers = mCurrentMovie.getTrailers();
        ArrayList<String> reviews = mCurrentMovie.getReviews();
        int buttonDimension = (int) getResources().getDimension(R.dimen.trailer_size);
        int imageDimension = (int) getResources().getDimension(R.dimen.play_image_size);

        TextView trailerHeaderTextView = new TextView(getActivity());
        LinearLayout.LayoutParams trailerHeaderlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        trailerHeaderTextView.setText("Trailers");
        trailerHeaderTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        trailerHeaderTextView.setLayoutParams(trailerHeaderlp);
        mCurrentLayout.addView(trailerHeaderTextView);

        for (final Map.Entry movie : trailers.entrySet()) {
            LinearLayout trailerLayout = new LinearLayout(getActivity());
            trailerLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            buttonDimension));
            trailerLayout.setOrientation(LinearLayout.HORIZONTAL);
            trailerLayout.setWeightSum(3f);
            mCurrentLayout.addView(trailerLayout);

            ImageButton btn = new ImageButton(getActivity());
            LinearLayout.LayoutParams lpImageButton = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            btn.setLayoutParams(lpImageButton);
            Picasso.with(getActivity())
                    .load(R.drawable.play_button)
                    .resize(imageDimension, imageDimension)
                    .centerInside()
                    .into(btn);
            trailerLayout.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Uri.parse("vnd.youtube:" + movie.getKey()));
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + movie.getKey()));
                    startActivity(intent);
                }
            });

            TextView tv = new TextView(getActivity());
            LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f);
            tv.setText((String) movie.getValue());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setLayoutParams(lpTextView);

            trailerLayout.addView(tv);
        }

        View divider = new View(getActivity());
        LinearLayout.LayoutParams dividerLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.divider_height));
        divider.setLayoutParams(dividerLp);
        divider.setBackgroundColor(getResources().getColor(R.color.divider));
        mCurrentLayout.addView(divider);

//        TODO: java question - I'm accessing the textview from the inner class - is it better practice to declare the first instance of tv as final and access it within the onClickListener class or is it a better idea to redeclare tv by casting the view in the onClick method?

        for (String review : reviews) {
            TextView tv = new TextView(getActivity());
            tv.setText(review);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            tv.setMaxLines(3);
            tv.setEllipsize(TextUtils.TruncateAt.END);

            mCurrentLayout.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    Layout layout = tv.getLayout();
                    int lines = layout.getLineCount();
                    if (lines > 2) {
                        int ellipsisCount = layout.getEllipsisCount(2);
                        if (ellipsisCount > 0) {
                            tv.setEllipsize(null);
                            tv.setMaxLines(Integer.MAX_VALUE);
                        } else {
                            tv.setMaxLines(3);
                            tv.setEllipsize(TextUtils.TruncateAt.END);
                        }
                    }
                }
            });
        }

    }
}
