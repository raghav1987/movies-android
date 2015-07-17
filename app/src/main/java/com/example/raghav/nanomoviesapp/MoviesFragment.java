package com.example.raghav.nanomoviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends android.support.v4.app.Fragment {

    private ArrayList<MovieData> mCurrentMovieList = new ArrayList<>();
    private ImageAdapter myImageAdapter;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        myImageAdapter = new ImageAdapter(getActivity(),mCurrentMovieList);
        moviesGridView.setAdapter(myImageAdapter);

        return rootView;
    }

    private void fetchMovieList(String sortParameter) {
        RequestParams params = new RequestParams();
        params.put("api_key", "0afa7d3d93c6b0544e3f2a01fc31c0dd");
        params.put("sort_by", sortParameter);
        Log.d("SORT",sortParameter);
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
                            MovieData newMovie = new MovieData(currentObject.getInt("vote_average"), currentObject.getInt("id"), currentObject.getString("title"), currentObject.getString("overview"), currentObject.getString("original_title"), currentObject.getString("poster_path"), currentObject.getDouble("popularity"));
                            mCurrentMovieList.add(newMovie);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myImageAdapter.notifyDataSetChanged();
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString(getString(R.string.pref_sorting_key),getString(R.string.pref_sorting_default));

        fetchMovieList(sortBy);
        super.onResume();
    }


}
