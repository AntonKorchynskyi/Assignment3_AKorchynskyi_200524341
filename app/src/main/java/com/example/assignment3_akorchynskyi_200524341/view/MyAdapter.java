package com.example.assignment3_akorchynskyi_200524341.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment3_akorchynskyi_200524341.R;
import com.example.assignment3_akorchynskyi_200524341.models.Movie;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Movie> movies;

    Context context;

    public MovieClickListener clickListener;

    public MyAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }


    // updates movie list
    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    public void setClickListener(MovieClickListener myListener) {
        this.clickListener = myListener;
    }

    // creates viewHolder items
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView, this.clickListener);
        return myViewHolder;
    }

    // connects values to the UI in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie item = movies.get(position);

        holder.movieName.setText(item.getMovieName());
        holder.movieYear.setText(item.getMovieYear());

        // using Glide library to output image from the URL
        Glide.with(context).load(item.getImg()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
