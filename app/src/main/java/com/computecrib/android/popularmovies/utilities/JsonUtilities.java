package com.computecrib.android.popularmovies.utilities;

import android.util.Log;

import com.computecrib.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtilities {

    public static ArrayList<Movie> getMoviesFromJSON(String json){
        String title;
        String posterPath;
        String overview;
        String releaseDate;
        String movieId;
        String rating;
        ArrayList<Movie> movies;

        try {
            JSONObject responseObject = new JSONObject(json);
            JSONArray resultsJSONArray = responseObject.getJSONArray("results");
            movies = new ArrayList<>();

            for(int i = 0; i<resultsJSONArray.length(); i++){
                JSONObject movieObject = resultsJSONArray.getJSONObject(i);
                title = movieObject.getString("title");
                rating = movieObject.getString("vote_average");
                posterPath = movieObject.getString("poster_path");
                overview = movieObject.getString("overview");
                releaseDate = movieObject.getString("release_date");
                movieId = movieObject.getString("id");
                Movie movie = new Movie(
                        title,
                        posterPath,
                        overview,
                        releaseDate,
                        movieId,
                        rating
                );
                movies.add(movie);
            }


            return movies;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getTrailersFromJSON(String json){
        ArrayList<String> trailers;
        try {
            JSONObject responseObject = new JSONObject(json);
            JSONArray resultsJSONArray = responseObject.getJSONArray("results");
            trailers = new ArrayList<>();

            for(int i = 0; i<resultsJSONArray.length(); i++){
                JSONObject trailerObject = resultsJSONArray.getJSONObject(i);
                String trailer = trailerObject.getString("key");
                trailers.add(trailer);
            }

            return trailers;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getReviewsFromJSON(String json){
        ArrayList<String> reviews;
        try {
            JSONObject responseObject = new JSONObject(json);
        }catch(Exception e){
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
