package com.example.banach.gottawatchit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    public List<Movie> items = new ArrayList<Movie>();
    private MovieClickListener callback;

    public MoviesAdapter(List<Movie> movies) {
        items = movies;
    }

    public void setItems(List<Movie> data) {
        if (data != null) {
            items.clear();
            items.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void onClickListener(MovieClickListener callback) {
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private final Context context;

        public ViewHolder(View view) {

            super(view);
            context = view.getContext();
            poster = (ImageView) view.findViewById(R.id.poster_image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(callback != null) {
                        callback.onMovieClicked(items.get(pos));
                    }
                }
            });
        }

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(holder.context).load(items.get(position).getmPoster()).into(holder.poster);

    }


    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}




