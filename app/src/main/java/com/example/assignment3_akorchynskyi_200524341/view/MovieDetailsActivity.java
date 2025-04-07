package com.example.assignment3_akorchynskyi_200524341.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.assignment3_akorchynskyi_200524341.databinding.MovieDetailsBinding;
import com.example.assignment3_akorchynskyi_200524341.models.Movie;
import com.example.assignment3_akorchynskyi_200524341.viewmodel.MovieDetailsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieDetailsBinding binding;
    private MovieDetailsViewModel viewModel;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MovieDetailsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(MovieDetailsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = auth.getCurrentUser().getUid();
        String movieId = getIntent().getStringExtra("imdbId");

        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        viewModel.getMovieDetails().observe(this, movie -> {
            binding.title.setText(movie.getMovieName());
            binding.year.setText(movie.getMovieYear());
            binding.genres.setText(movie.getGenres());
            binding.rating.setText(movie.getRating());
            binding.description.setText(movie.getDescription());
            Glide.with(this).load(movie.getImg()).into(binding.imageView);
        });

        viewModel.Refresh(movieId);

        binding.AddFavouritesButton.setOnClickListener(v -> {
            Movie movie = viewModel.getMovieDetails().getValue();
            if (movie == null) {
                Toast.makeText(MovieDetailsActivity.this, "Movie details not loaded", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("users")
                    .document(uid)
                    .collection("movies")
                    .whereEqualTo("movieName", movie.getMovieName())
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (querySnapshot.isEmpty()) {
                            // No duplicate found, so add the movie with an auto-generated document ID
                            db.collection("users")
                                    .document(uid)
                                    .collection("movies")
                                    .add(movie)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(MovieDetailsActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MovieDetailsActivity.this, "Failed to add to favourites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(MovieDetailsActivity.this, "Movie already in favourites", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MovieDetailsActivity.this, "Error checking favourites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }

        });
    }


}
