package com.computecrib.android.popularmovies;

/**
 * Created by bhavy on 4/28/2018.
 */

public class Movie {
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private int movieId;

    public Movie(String title, String posterPath, String overview, String releaseDate, int movieId) {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getMovieId() {
        return movieId;
    }


}
