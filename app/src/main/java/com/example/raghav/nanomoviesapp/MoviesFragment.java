package com.example.raghav.nanomoviesapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private ArrayList<MovieData> mCurrentMovieList = new ArrayList<>();
    private ImageAdapter mImageAdapter;
    private String sortBy;


    public final static String ADAPTER_TAG = "ADAPTER_TAG";
    public final static String SORT_TAG = "SORT_BY";

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.gridview_movies);

        String previousStateSortBy;
        String preferenceSortBy;

        Boolean fetchNewData = false;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferenceSortBy = prefs.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));

        if (savedInstanceState != null) {
            mCurrentMovieList = savedInstanceState.getParcelableArrayList(ADAPTER_TAG);
            previousStateSortBy = savedInstanceState.getString(SORT_TAG);

            if (previousStateSortBy != preferenceSortBy) {
                fetchNewData = true;
            }

        } else {
            fetchNewData = true;
        }

        sortBy = preferenceSortBy;

        if (fetchNewData) {
            fetchMovieList(sortBy);
        }

        mImageAdapter = new ImageAdapter(getActivity(), mCurrentMovieList);
        moviesGridView.setAdapter(mImageAdapter);

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieData clickedMovie = (MovieData)mImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("CURRENT_MOVIE",clickedMovie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void fetchMovieList(String sortParameter) {
        RequestParams params = new RequestParams();
        params.put("api_key", Constants.API_KEY);
        params.put("sort_by", sortParameter);
        MoviesdbRestClient.get("", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("Success Response", Integer.toString(statusCode));
                Log.v("Success Response", response.toString());


                try {
                    JSONArray resultArray = response.getJSONArray("results");
                    mCurrentMovieList.clear();
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject currentObject = resultArray.getJSONObject(i);
                        if (currentObject.isNull("poster_path")) {
//                            do nothing
                        } else {
                            MovieData newMovie = new MovieData(currentObject.getDouble("vote_average"), currentObject.getInt("id"), currentObject.getString("title"), currentObject.getString("overview"), currentObject.getString("original_title"), currentObject.getString("poster_path"), currentObject.getDouble("popularity"),currentObject.getString("release_date"));
                            mCurrentMovieList.add(newMovie);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mImageAdapter.notifyDataSetChanged();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ADAPTER_TAG, mCurrentMovieList);
        outState.putString(SORT_TAG,sortBy);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preferenceSortBy = prefs.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));
        if (preferenceSortBy!=sortBy) {
            sortBy = preferenceSortBy;
            fetchMovieList(sortBy);
        }
    }



}
