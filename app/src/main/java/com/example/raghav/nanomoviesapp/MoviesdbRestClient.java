package com.example.raghav.nanomoviesapp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by raghav on 7/14/15.
 */
public class MoviesdbRestClient {
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String DATA_BASE_URL = "http://api.themoviedb.org/3/discover/movie";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return DATA_BASE_URL + relativeUrl;
    }
}