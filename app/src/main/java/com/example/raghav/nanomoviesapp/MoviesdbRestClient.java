package com.example.raghav.nanomoviesapp;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by raghav on 7/14/15.
 */
public class MoviesdbRestClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    private static final String DETAIL_URL = "http://api.themoviedb.org/3/movie";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, boolean detail, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.v("REQUEST", "making request to " + url);
        client.get(getAbsoluteUrl(url, detail), params, responseHandler);
    }

    public static void post(String url, boolean detail, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url, detail), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl, boolean detail) {
        if (detail) {
            return DETAIL_URL + relativeUrl;
        }
        else {
            return BASE_URL + relativeUrl;
        }
    }
}