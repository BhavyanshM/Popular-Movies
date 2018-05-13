package com.computecrib.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.Holder> {

    private List<String> trailers;
    private Context context;

    public TrailersRecyclerAdapter(Context cont, List<String> trailerList){
        this.trailers = trailerList;
        this.context = cont;
    }

    public void setTrailers(List<String> newTrailers){
        this.trailers = newTrailers;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String trailer = trailers.get(position);
        Picasso.with(context)
                .load(context.getString(R.string.base_trailer_image_url) + trailer + context.getString(R.string.trailer_image_file_name))
                .placeholder(R.drawable.movies)
                .error(R.drawable.movies)
                .into(holder.trailerImageView);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        private ImageView trailerImageView;

        private Holder(View itemView) {
            super(itemView);
            trailerImageView = (ImageView) itemView.findViewById(R.id.iv_trailer_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context viewContext = view.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    viewContext.getString(R.string.base_trailer_youtube_url) +
                            trailers.get(getAdapterPosition())));
            viewContext.startActivity(intent);
        }
    }
}
