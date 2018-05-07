package com.computecrib.android.popularmovies;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_small) ImageView mMovieSmallImageView;
    @BindView(R.id.tv_movie_name) TextView mTitleTextView;
    @BindView(R.id.tv_movie_description) TextView mDescriptionTextView;
    @BindView(R.id.tv_movie_rating) TextView mRatingTextView;
    @BindView(R.id.tv_movie_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.ib_fav_button) ImageButton mFavButton;
    private Boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        isFavorite = false;

        Intent theSourceIntent = getIntent();
        mTitleTextView.setText(theSourceIntent.getStringExtra(getString(R.string.title_label)));
        mDescriptionTextView.setText(theSourceIntent.getStringExtra(getString(R.string.description_label)));
        mRatingTextView.setText(theSourceIntent.getStringExtra(getString(R.string.rating_label)));
        mReleaseDateTextView.setText(theSourceIntent.getStringExtra(getString(R.string.release_date_label)));
        Picasso.with(this)
                .load(getApplicationContext().getString(R.string.base_image_url) + theSourceIntent.getStringExtra(getString(R.string.poster_path_label)))
                .into(mMovieSmallImageView);

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorite){
                    mFavButton.setBackground(getResources().getDrawable(R.drawable.fav_button_off));
                    isFavorite = false;
                }else{
                    mFavButton.setBackground(getResources().getDrawable(R.drawable.fav_button_on));
                    isFavorite = true;
                }
            }
        });

    }


}
