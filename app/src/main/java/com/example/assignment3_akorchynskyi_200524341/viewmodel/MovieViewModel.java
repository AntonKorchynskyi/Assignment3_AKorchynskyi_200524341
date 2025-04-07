package com.example.assignment3_akorchynskyi_200524341.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3_akorchynskyi_200524341.utils.ApiClient;
import com.example.assignment3_akorchynskyi_200524341.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {

    private final MutableLiveData<List<Movie>> movieData = new MutableLiveData<List<Movie>>();

    List<Movie> movieList = new ArrayList<>();

    String searchQuery;

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public LiveData<List<Movie>> getMovieData() {
        return movieData;
    }

    // makes API call and records received data into LiveData<Movie> list
    public void Refresh() {

        String urlString = "https://www.omdbapi.com/?apikey=6f31ad0d&type=movie&s=" + searchQuery;

        ApiClient.get(urlString, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("jsonError", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String responseData = response.body().string();

                JSONObject json = null;
                JSONArray jsonArray = null;

                try {
                    json = new JSONObject(responseData);
                    jsonArray = json.getJSONArray("Search");

                    // clears list each time to not duplicate items from the new API calls
                    movieList.clear();

                    // loops through the array of received movies
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String movieName = jsonArray.getJSONObject(i).getString("Title");
                        String movieYear = jsonArray.getJSONObject(i).getString("Year");
                        String img = jsonArray.getJSONObject(i).getString("Poster");
                        String imdbID = jsonArray.getJSONObject(i).getString("imdbID");

                        Movie movieModel = new Movie();

                        movieModel.setMovieName(movieName);
                        movieModel.setMovieYear(movieYear);
                        movieModel.setImg(img);
                        movieModel.setImdbID(imdbID);

                        movieList.add(movieModel);
                    }

                    // adds movie list directly to the LiveData
                    movieData.postValue(movieList);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
