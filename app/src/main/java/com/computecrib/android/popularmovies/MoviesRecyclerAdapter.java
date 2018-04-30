package com.computecrib.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.Holder> {

    private List<Movie> movies;
    private Context context;

    public MoviesRecyclerAdapter(Context cont,List<Movie> movieList){
        this.movies = movieList;
        this.context = cont;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Movie movie = movies.get(position);
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w342" + movie.getPosterPath())
                .placeholder(R.drawable.movies)
                .into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        private ImageView movieImageView;

        private Holder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Item Was Clicked!", Toast.LENGTH_SHORT).show();
        }
    }
}
