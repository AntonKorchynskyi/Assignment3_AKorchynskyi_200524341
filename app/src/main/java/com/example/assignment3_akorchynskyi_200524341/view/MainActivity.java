package com.example.assignment3_akorchynskyi_200524341.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment3_akorchynskyi_200524341.databinding.ActivityMainBinding;
import com.example.assignment3_akorchynskyi_200524341.viewmodel.MovieViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieClickListener {

    ActivityMainBinding binding;

    MyAdapter myAdapter;

    MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // creates a new list in recycler view and adds an adapter with empty list to it
        // also attaches click event to the adapter so that when item inside the list is clicked
        // it would register the event
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(getApplicationContext(), new ArrayList<>());
        binding.recyclerView.setAdapter(myAdapter);
        myAdapter.setClickListener(this);


        // observes changes in viewModel LiveData list
        viewModel.getMovieData().observe(this, movieData -> {
            Log.i("tag", "Update View");
            myAdapter.updateMovies(movieData);
        });

        // outputs a list of movies based on user query
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.editTextText.getText().toString().isEmpty()) {
                    Log.d("TAG", "Search is empty");
                }
                else {
                    viewModel.setSearchQuery(binding.editTextText.getText().toString());
                    viewModel.Refresh();
                }

            }
        });

    }

    // moves user to movie details page once movie ViewHolder is clicked
    @Override
    public void onClick(View v, int pos) {

        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra("imdbId", myAdapter.movies.get(pos).getImdbID());
        startActivity(intent);

    }


}