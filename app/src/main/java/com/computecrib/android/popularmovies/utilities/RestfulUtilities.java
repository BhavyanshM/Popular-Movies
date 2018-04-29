package com.computecrib.android.popularmovies.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class RestfulUtilities {

    final static String BASE_REST_URL = "https://api.themoviedb.org/3/movie";
    final static String PARAM_API_KEY = "api_key";
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