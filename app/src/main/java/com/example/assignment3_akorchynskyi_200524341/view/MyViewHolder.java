package com.example.assignment3_akorchynskyi_200524341.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3_akorchynskyi_200524341.R;

public class MyViewHolder extends RecyclerView.ViewHolder {


    ImageView imageView;
    TextView movieName;
    TextView movieYear;

    MovieClickListener clickListener;

    public MyViewHolder(@NonNull View itemView, MovieClickListener clickListener) {
        super(itemView);

        // binds items in ViewHolder to layout elements
        imageView = itemView.findViewById(R.id.imageview);
        movieName = itemView.findViewById(R.id.title_txt);
        movieYear = itemView.findViewById(R.id.year_text);

        this.clickListener = clickListener;

        // records and sends what kind of movie was clicked and sends it to parent component
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "onViewHolder Click");
                clickListener.onClick(view, getAdapterPosition());
            }
        });
    }
}
