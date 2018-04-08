package com.example.elio.movies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.util.List;

//Creating for adding movies from the internet

public class InternetActivity extends AppCompatActivity {

    public ListView listViewMovies;
    private Dialog insertOrUpdateDialog;
    private AlertDialog deleteDialog;
    public List<MovieSample> tempMovies;
    public MoviesReaderController moviesReaderController;
    public Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_select);
        mContext = App.getContext();

        listViewMovies = (ListView) findViewById(R.id.movieList);
        moviesReaderController = new MoviesReaderController(this);

        listViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                send(i);
            }
        });
    }

    public void send(int i) {
        String tempName = listViewMovies.getAdapter().getItem(i).toString();
        String baseImageUrl = "http://image.tmdb.org/t/p/w185";
        tempMovies = moviesReaderController.giveMovies();
        for (int j = 0; j < tempMovies.size(); j++) {
            if (tempName.equals(tempMovies.get(j).getName())) {
                if (i == j) {
                    String description = tempMovies.get(j).getDescription();
                    String imageUrl = baseImageUrl + tempMovies.get(j).getImageUrl();

                    Intent editActivity = new Intent(this, AddMovie.class);
                    editActivity.putExtra("name", tempName);
                    editActivity.putExtra("des", description);
                    editActivity.putExtra("url", imageUrl);
                    editActivity.putExtra("id", 1);
                    editActivity.putExtra("state", "edit");

                    if (mContext instanceof Activity) {
                        ((Activity) mContext).startActivityForResult(editActivity, 1);
                        finish();
                    }
                }
            }
        }
    }

    public void showMovies(View v){
        EditText nameText = (EditText) findViewById(R.id.search);
        String name = nameText.getText().toString();
        moviesReaderController.readAllMovies(name);
    }

    public void cancel(View v){
        finish();
    }
}
