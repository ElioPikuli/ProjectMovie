package com.example.elio.movies;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MoviesReaderController extends MovieController {

    public Activity context;

    public List<MovieSample> giveMovies(){
        return allMoviesData;
    }

    public MoviesReaderController(Activity activity) {
        super(activity);
        context=activity;
    }

    public void readAllMovies(String name) {
        HttpRequest httpRequest = new HttpRequest(this);
        httpRequest.execute("https://api.themoviedb.org/3/search/movie?api_key=6c454ebc91617fc2aee3e8ea1eb1b7be" +
                "&query=" + name + "&page=1");
    }

    public void onSuccess(String downloadedText) {
        try {
            List<MovieSample> tempList = new ArrayList<>();
            JSONObject jsonArray = new JSONObject(downloadedText);
            JSONArray resultArray = jsonArray.getJSONArray("results");
            Movies = new ArrayList<>();
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject jsonObject = resultArray.getJSONObject(i);
                String name = jsonObject.getString("title");
                String desc = jsonObject.getString("overview");
                String image = jsonObject.getString("poster_path");
                MovieSample movie = new MovieSample(1,name,desc,image);
                tempList.add(i,movie);
                Movies.add(name);
            }
            allMoviesData = tempList;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, Movies);
            listViewMovies.setAdapter(adapter);
        }
        catch (JSONException ex) {
            Toast.makeText(activity, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        progressDialog.dismiss();
    }
}
