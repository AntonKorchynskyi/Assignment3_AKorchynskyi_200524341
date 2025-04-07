package com.example.assignment3_akorchynskyi_200524341.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.assignment3_akorchynskyi_200524341.R;
import com.example.assignment3_akorchynskyi_200524341.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FavouritesDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title, rating, year, genres;
    private EditText description;
    private Button backButton, updateButton, deleteButton;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String uid;
    private String docId;

    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_movies_details);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(FavouritesDetailActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        rating = findViewById(R.id.rating);
        year = findViewById(R.id.year);
        genres = findViewById(R.id.genres);
        description = findViewById(R.id.description);
        backButton = findViewById(R.id.backButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        if (getIntent().hasExtra("movieData") && getIntent().hasExtra("docId")) {
            currentMovie = (Movie) getIntent().getSerializableExtra("movieData");
            docId = getIntent().getStringExtra("docId");
            populateUI(currentMovie);
        } else {
            Toast.makeText(this, "Missing movie data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        backButton.setOnClickListener(v -> finish());
        updateButton.setOnClickListener(v -> updateMovie());
        deleteButton.setOnClickListener(v -> deleteMovie());
    }
    //populates UI
    private void populateUI(Movie movie) {
        if (movie == null) return;

        title.setText(movie.getMovieName());
        rating.setText(movie.getRating());
        year.setText(movie.getMovieYear());
        genres.setText(movie.getGenres());
        description.setText(movie.getDescription());

        if (movie.getImg() != null && !movie.getImg().isEmpty()) {
            Glide.with(this).load(movie.getImg()).into(imageView);
        }
    }

    //updates Movie
    private void updateMovie() {
        if (currentMovie == null || docId == null) return;

        String newDescription = description.getText().toString().trim();
        if (newDescription.isEmpty()) {
            Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .document(uid)
                .collection("movies")
                .document(docId)
                .update("description", newDescription)
                .addOnSuccessListener(aVoid -> Toast.makeText(FavouritesDetailActivity.this, "Movie updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(FavouritesDetailActivity.this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // deletes Movie
    private void deleteMovie() {
        if (currentMovie == null || docId == null) return;

        db.collection("users")
                .document(uid)
                .collection("movies")
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FavouritesDetailActivity.this, "Movie deleted from favourites", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(FavouritesDetailActivity.this, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
