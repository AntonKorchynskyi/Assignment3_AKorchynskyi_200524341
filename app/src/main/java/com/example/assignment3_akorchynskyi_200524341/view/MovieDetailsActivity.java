package com.example.assignment3_akorchynskyi_200524341.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.assignment3_akorchynskyi_200524341.databinding.MovieDetailsBinding;
import com.example.assignment3_akorchynskyi_200524341.viewmodel.MovieDetailsViewModel;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieDetailsBinding binding;
    private MovieDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use the correct binding for details layout (different from the main activity)
        binding = MovieDetailsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // get the movie id from the intent
        String movieId = getIntent().getStringExtra("imdbId");

        // initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        // observe LiveData to update UI automatically
        viewModel.getMovieDetails().observe(this, movie -> {
            binding.title.setText(movie.getMovieName());
            binding.year.setText(movie.getMovieYear());
            binding.genres.setText(movie.getGenres());
            binding.rating.setText(movie.getRating());
            binding.description.setText(movie.getDescription());
            Glide.with(this).load(movie.getImg()).into(binding.imageView);
        });

        // trigger api call
        viewModel.Refresh(movieId);

        binding.backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // stop the execution of current activity
                finish();
            }

        });
    }
}
