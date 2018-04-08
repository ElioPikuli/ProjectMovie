package com.example.elio.movies;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DownloadImageTask.CallBack {

    private String movieName = "";
    private String movieDesc = "";
    private String movieUrl = "";
    private int counter = 1;
    private float sizeW = 0;
    private float sizeH = 0;
    private MenuItem addMovieManually;
    private MenuItem addMovieByInternet;
    private MenuItem deleteAll;
    private MenuItem exit;
    private final int requestCode = 1;
    private LinearLayout mainLayout;
    private ArrayList<MovieSample> movies;
    private LinearLayout.LayoutParams imageViewDetails;
    private LinearLayout.LayoutParams TextViewDetails;
    private MovieSample movieSample;
    private ImageView iv;
    private String state = "add";
    DownloadImageTask downloadImageTask;
    private int resultCode;
    DataBaseHandler db;
    int i=0;
    String tempTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.setContext(this);
        db = new DataBaseHandler(this);
        movies =db.getAllMovieList();
        i=0;

        //creating a Layout
        mainLayout = findViewById(R.id.mainLayout);
        Resources r = getResources();
        sizeW = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                r.getDisplayMetrics());
        sizeH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300,
                r.getDisplayMetrics());
        TextViewDetails =new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imageViewDetails = new LinearLayout.LayoutParams(
                Math.round(sizeW),
                Math.round(sizeH));
        while(i<movies.size()){
            setDisplayMovie();
            i++;
        }
    }

    public void setDisplayMovie(){
        TextView movieSampleName=new TextView(this);
        movieSampleName.setText(movies.get(i).getName());
        movieSampleName.setTextColor(getResources().getColor(R.color.black));
        movieSampleName.setTextSize(getResources().getDimensionPixelSize(R.dimen.textSize));
        InputStream imageStream = this.getResources().openRawResource(R.raw.image);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        iv = new ImageView(this);

        if (!movies.get(i).getImageUrl().equals("")) {
            downloadImageTask = new DownloadImageTask(this);
            downloadImageTask.execute(movies.get(i).getImageUrl());
        }else{
            iv.setImageBitmap(bitmap);
        }
        mainLayout.addView(movieSampleName,TextViewDetails);
        mainLayout.addView(iv,imageViewDetails);
        final CustomDialog customDialog=new CustomDialog(MainActivity.this,movies.get(i).getName(),counter);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "edit";
                Intent intent= new Intent (MainActivity.this,AddMovie.class);
                intent.putExtra("name",movieName);
                intent.putExtra("des",movieDesc);
                intent.putExtra("url",movieUrl);
                intent.putExtra("id",1);
                intent.putExtra("state","edit");
                startActivityForResult(intent,1);
            }
        });
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                customDialog.show();
                return false;
            }
        });
    }

    //Creating the Menu Bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        addMovieManually = menu.findItem(R.id.addMovieManually);
        addMovieByInternet = menu.findItem(R.id.addMovieByInternet);
        deleteAll = menu.findItem(R.id.deleteAll);
        exit = menu.findItem(R.id.exit);
        return true;
    }

    //Adding a movie to the Database
    public void addMovie(Intent data){
        movieName = data.getStringExtra("name");
        movieDesc = data.getStringExtra("description");
        movieUrl = data.getStringExtra("url");
        movieSample = new MovieSample(counter, movieName, movieDesc, movieUrl);
        db.addMovie(movieSample);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            addMovie(data);
        }
        this.recreate();
    }

    //When selecting from the menu bar
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menuItemSettings:
                return true;
            case R.id.addMovieManually: {
                Intent intent = new Intent(this, AddMovie.class);
                intent.putExtra("state", state);
                startActivityForResult(intent, requestCode);
                onActivityResult(requestCode, resultCode, intent);
                state = "add";
                counter++;
                return true;
            }
            case R.id.addMovieByInternet: {
                Intent intent = new Intent(this, InternetActivity.class);
                startActivityForResult(intent, requestCode);
                return true;
            }
            case R.id.deleteAll: {
                db.clear();
                restart();
                return true;
            }
            case R.id.exit: {
                finish();
                return true;
            }
        }
        return false;
    }

    public void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void onSuccess(Bitmap result) {
        iv.setImageBitmap(result);
    }
}
