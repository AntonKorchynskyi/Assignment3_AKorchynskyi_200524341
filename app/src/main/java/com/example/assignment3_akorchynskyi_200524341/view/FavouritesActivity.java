package com.example.assignment3_akorchynskyi_200524341.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3_akorchynskyi_200524341.R;
import com.example.assignment3_akorchynskyi_200524341.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class FavouritesActivity extends AppCompatActivity implements MovieClickListener {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<Movie> favouriteMovies;
    private List<String> favouriteDocIds;
    private Button searchScreenBtn, favouritesScreenBtn;
    private TextView textView;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String uid;


    //adds Views, and click listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);
        searchScreenBtn = findViewById(R.id.searchScreenBtn);
        favouritesScreenBtn = findViewById(R.id.favouritesScreenBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favouriteMovies = new ArrayList<>();
        favouriteDocIds = new ArrayList<>();

        myAdapter = new MyAdapter(this, favouriteMovies);
        myAdapter.setClickListener(this);
        recyclerView.setAdapter(myAdapter);

        fetchFavouritesFromFirestore();

        searchScreenBtn.setOnClickListener(v -> {
            finish();
        });

        favouritesScreenBtn.setOnClickListener(v -> {
            fetchFavouritesFromFirestore();
            Toast.makeText(this, "Refreshed Favourites", Toast.LENGTH_SHORT).show();
        });
    }


    //fetches Movies from FireDB
    private void fetchFavouritesFromFirestore() {
        CollectionReference moviesRef = db.collection("users")
                .document(uid)
                .collection("movies");

        moviesRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    favouriteMovies.clear();
                    favouriteDocIds.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Movie movie = doc.toObject(Movie.class);
                        favouriteMovies.add(movie);
                        favouriteDocIds.add(doc.getId());
                    }
                    myAdapter.updateMovies(favouriteMovies);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FavouritesActivity.this,
                            "Error fetching favourites: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    //passes data of specific movie on movie click, to Details page
    @Override
    public void onClick(View v, int pos) {
        if (pos < 0 || pos >= favouriteMovies.size()) return;
        Movie clickedMovie = favouriteMovies.get(pos);
        String docId = favouriteDocIds.get(pos);

        Intent intent = new Intent(FavouritesActivity.this, FavouritesDetailActivity.class);
        intent.putExtra("movieData", clickedMovie);
        intent.putExtra("docId", docId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchFavouritesFromFirestore();
    }
}
