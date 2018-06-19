package com.computecrib.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class RestfulUtilities {



    public static URL buildUrlWithKey(String baseRestURL, String pathParam, String paramAPIKey, String apiKey) {
        Uri builtUri = Uri.parse(baseRestURL).buildUpon()
                .appendPath(pathParam)
                .appendQueryParameter(paramAPIKey, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailerUrlWithKey(String baseRestURL, String pathParam1, String pathParam2, String paramAPIKey, String apiKey) {
        Uri builtUri = Uri.parse(baseRestURL).buildUpon()
                .appendPath(pathParam1)
                .appendPath(pathParam2)
                .appendQueryParameter(paramAPIKey, apiKey)
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
        Log.e("ERROR:  ", url.toString()+"\n\n\n\n\n");
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

    //Inspired by StackOverflow thread:
    //https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //Date: 1 May 2018
    public static boolean isConnected(ConnectivityManager cm) {
        NetworkInfo networkStatus = null;
        if (cm != null) {
            networkStatus = cm.getActiveNetworkInfo();
        }
        return networkStatus != null && networkStatus.isConnected();
    }
}