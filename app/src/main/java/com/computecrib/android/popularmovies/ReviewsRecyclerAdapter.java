package com.computecrib.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.Holder> {

    private List<Review> reviews;
    private Context context;

    public ReviewsRecyclerAdapter(Context cont, List<Review> reviewList){
        this.reviews = reviewList;
        this.context = cont;
    }

    public void setReviews(List<Review> newReviews){
        this.reviews = newReviews;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Review review = reviews.get(position);
        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView mAuthorTextView;
        private TextView mContentTextView;

        private Holder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

    }
}
