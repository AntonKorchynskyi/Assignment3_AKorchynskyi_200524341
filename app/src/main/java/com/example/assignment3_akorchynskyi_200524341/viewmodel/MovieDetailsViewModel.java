package com.example.assignment3_akorchynskyi_200524341.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3_akorchynskyi_200524341.utils.ApiClient;
import com.example.assignment3_akorchynskyi_200524341.models.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieDetailsViewModel extends ViewModel {
    private final MutableLiveData<Movie> movieDetails = new MutableLiveData<>();

    public LiveData<Movie> getMovieDetails() {
        return movieDetails;
    }

    // makes API call and records received data into LiveData<Movie> list
    public void Refresh(String imdbId) {

        String url = "https://www.omdbapi.com/?apikey=6f31ad0d&type=movie&i=" + imdbId;

        ApiClient.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Movie details error:", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String responseData = response.body().string();

                try {

                    JSONObject json = new JSONObject(responseData);

                    // creates movie object and assigns received values to it
                    Movie movie = new Movie();
                    movie.setMovieName(json.getString("Title"));
                    movie.setMovieYear(json.getString("Year"));
                    movie.setImg(json.getString("Poster"));
                    movie.setGenres(json.optString("Genre"));
                    movie.setRating(json.optString("imdbRating"));
                    movie.setDescription(json.optString("Plot"));

                    // adds object to LiveData list
                    movieDetails.postValue(movie);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
