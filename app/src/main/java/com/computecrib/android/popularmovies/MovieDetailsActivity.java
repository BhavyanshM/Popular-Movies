package com.computecrib.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_small) ImageView mMovieSmallImageView;
    @BindView(R.id.tv_movie_name) TextView mTitleTextView;
    @BindView(R.id.tv_movie_description) TextView mDescriptionTextView;
    @BindView(R.id.tv_movie_rating) TextView mRatingTextView;
    @BindView(R.id.tv_movie_release_date) TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent theSourceIntent = getIntent();
        mTitleTextView.setText(theSourceIntent.getStringExtra("TITLE"));
        mDescriptionTextView.setText(theSourceIntent.getStringExtra("DESCRIPTION"));
        mRatingTextView.setText(theSourceIntent.getStringExtra("RATING"));
        mReleaseDateTextView.setText(theSourceIntent.getStringExtra("RELEASE_DATE"));
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w342" + theSourceIntent.getStringExtra("POSTER_PATH"))
                .into(mMovieSmallImageView);
    }
}
