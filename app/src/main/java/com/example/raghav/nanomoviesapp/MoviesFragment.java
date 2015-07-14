package com.example.raghav.nanomoviesapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        fetchMovieList();
        return rootView;
    }

    private void fetchMovieList() {
        RequestParams params = new RequestParams();
        params.put("api_key","0afa7d3d93c6b0544e3f2a01fc31c0dd");
        params.put("sort_by","popularity.desc");
        MoviesdbRestClient.get("",params,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("Success Response", Integer.toString(statusCode));
                Log.v("Success Response",response.toString());
            }

            @Override
            public void onFailure(int statusCode,
                                  Header[] headers,
                                  String responseString,
                                  Throwable throwable) {
                Log.d("Failed Response",Integer.toString(statusCode));
            }
        });
    }
}