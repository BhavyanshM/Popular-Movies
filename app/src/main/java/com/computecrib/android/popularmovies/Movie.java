package com.computecrib.android.popularmovies;


public class Movie {
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String rating;
    private String movieId;

    public Movie(String title, String posterPath, String overview, String releaseDate, String movieId, String voteAverage) {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = voteAverage;
        this.movieId = movieId;
    }

    public String getRating(){
        return rating;
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

    public String getMovieId() {
        return movieId;
    }


}
