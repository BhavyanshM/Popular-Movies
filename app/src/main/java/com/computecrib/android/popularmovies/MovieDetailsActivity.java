package com.computecrib.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.computecrib.android.popularmovies.utilities.JsonUtilities;
import com.computecrib.android.popularmovies.utilities.RestfulUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_small) ImageView mMovieSmallImageView;
    @BindView(R.id.tv_movie_name) TextView mTitleTextView;
    @BindView(R.id.tv_movie_description) TextView mDescriptionTextView;
    @BindView(R.id.tv_movie_rating) TextView mRatingTextView;
    @BindView(R.id.tv_movie_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.ib_fav_button) ImageButton mFavButton;
    @BindView(R.id.rv_trailers) RecyclerView mTrailersRecyclerView;

    @BindString(R.string.base_rest_url) String BASE_REST_URL;
    @BindString(R.string.param_api_key) String PARAM_API_KEY;
    @BindString(R.string.api_key) String API_KEY;
    @BindString(R.string.value_trailers) String paramValueTrailers;
    @BindString(R.string.value_reviews) String paramValueReviews;

    private Boolean isFavorite;

    private String titleText;
    private String descriptionText;
    private String ratingText;
    private String releaseDateText;
    private String posterPathText;
    private String movieId;

    private List<String> trailers;
    private List<String> reviews;
    private TrailersRecyclerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Intent theSourceIntent = getIntent();

        trailers = new ArrayList<>();
        reviews = new ArrayList<>();
        movieId = theSourceIntent.getStringExtra(getString(R.string.id_label));
        Log.e("WHERE",(movieId + "/" + paramValueTrailers));
        URL theTrailerURL = buildTheCustomURL(movieId, paramValueTrailers);
        URL theReviewURL = buildTheCustomURL(movieId, paramValueReviews);
        if(RestfulUtilities.isConnected((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
            new GetTrailerTask().execute(theTrailerURL,
                    theReviewURL);
        }else{
            Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
        }

        isFavorite = false;


        mTitleTextView.setText(theSourceIntent.getStringExtra(getString(R.string.title_label)));
        mDescriptionTextView.setText(theSourceIntent.getStringExtra(getString(R.string.description_label)));
        mRatingTextView.setText(theSourceIntent.getStringExtra(getString(R.string.rating_label)));
        mReleaseDateTextView.setText(theSourceIntent.getStringExtra(getString(R.string.release_date_label)));
        Picasso.with(this)
                .load(getApplicationContext().getString(R.string.base_image_url) + theSourceIntent.getStringExtra(getString(R.string.poster_path_label)))
                .into(mMovieSmallImageView);

        titleText = theSourceIntent.getStringExtra(getString(R.string.title_label));
        descriptionText = theSourceIntent.getStringExtra(getString(R.string.description_label));
        ratingText = theSourceIntent.getStringExtra(getString(R.string.rating_label));
        releaseDateText = theSourceIntent.getStringExtra(getString(R.string.release_date_label));
        posterPathText = theSourceIntent.getStringExtra(getString(R.string.poster_path_label));

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorite){
                    mFavButton.setBackground(getResources().getDrawable(R.drawable.fav_button_off));
                    isFavorite = false;
                }else{
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, titleText);
                    cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, descriptionText);
                    cv.put(MovieContract.MovieEntry.COLUMN_RATING, ratingText);
                    cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDateText);
                    cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, posterPathText);
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

                    if(uri!=null){
                        Toast.makeText(MovieDetailsActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                    
                    mFavButton.setBackground(getResources().getDrawable(R.drawable.fav_button_on));
                    isFavorite = true;
                }
            }
        });

        trailerAdapter = new TrailersRecyclerAdapter(this, trailers);
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailersRecyclerView.setAdapter(trailerAdapter);
    }

    public URL buildTheCustomURL(String pathParam1, String pathParam2){
        return RestfulUtilities.buildTrailerUrlWithKey(
                BASE_REST_URL,
                pathParam1,
                pathParam2,
                PARAM_API_KEY,
                API_KEY
        );
    }



    @SuppressLint("StaticFieldLeak")
    public class GetTrailerTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... urls) {
            URL fullTrailerURL = urls[0];
            URL fullReviewURL = urls[1];
            String theTrailerResults= null;
            String theReviewResults = null;
            try {
                theTrailerResults = RestfulUtilities.getMovieResponse(fullTrailerURL);
                theReviewResults = RestfulUtilities.getMovieResponse(fullReviewURL);
            }catch (Exception e){
                e.printStackTrace();
            }
            return new String[]{theTrailerResults, theReviewResults};
        }

        @Override
        protected void onPostExecute(String[] responses) {
            String trailerString = responses[0];
            if(trailerString!=null && !trailerString.equals("")){
                trailers = JsonUtilities.getTrailersFromJSON(trailerString);
                trailerAdapter.setTrailers(trailers);
                trailerAdapter.notifyDataSetChanged();
            }
            String reviewString = responses[1];
            if(reviewString!=null && !reviewString.equals("")){
                reviews = JsonUtilities.getReviewsFromJSON(reviewString);
//                reviewAdapter.setReview(reviews);
//                reviewAdapter.notifyDataSetChanged();
            }
        }
    }


}
