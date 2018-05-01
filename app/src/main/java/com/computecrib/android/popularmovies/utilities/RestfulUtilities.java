package com.computecrib.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class RestfulUtilities {

    private final static String BASE_REST_URL = "https://api.themoviedb.org/3/movie";
    private final static String PARAM_API_KEY = "api_key";
    final static String apiKey = "";

    public static URL buildUrlWithKey(String pathParam) {
        Uri builtUri = Uri.parse(BASE_REST_URL).buildUpon()
                .appendPath(pathParam)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    //Inspired by Lesson 5 from Github Repo Search Exercise - T02.05-Exercise-CreateAsyncTask
    //Date: 1 May 2018
    //Link to Repo: https://github.com/udacity/ud851-Exercises/blob/student/Lesson02-GitHub-Repo-Search/T02.05-Exercise-CreateAsyncTask/app/src/main/java/com/example/android/datafrominternet/utilities/NetworkUtils.java
    public static String getMovieResponse(URL url) throws IOException {
        HttpURLConnection RESTConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = RESTConnection.getInputStream();

            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            if (scan.hasNext()) {
                return scan.next();
            } else {
                return null;
            }
        } finally {
            RESTConnection.disconnect();
        }
    }
}