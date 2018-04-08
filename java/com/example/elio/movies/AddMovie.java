package com.example.elio.movies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//Creating a class for adding movies manually

public class AddMovie extends AppCompatActivity implements DownloadImageTask.CallBack {

    String state;
    DownloadImageTask downloadImageTask;
    ImageView imageView;
    EditText urlET;
    EditText title;
    EditText description;
    String movieName;
    String desc;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting the content view to the "activity_edit" activity
        setContentView(R.layout.activity_edit);

        imageView=findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.descrition);
        urlET = findViewById(R.id.url);
        Intent i = getIntent();

        state = i.getStringExtra("state");
        if(state.equals("edit")) {
            movieName = i.getStringExtra("name");
            desc = i.getStringExtra("des");
            imageUrl = i.getStringExtra("url");
            title.setText(movieName);
            description.setText(desc);
            urlET.setText(imageUrl);
        }
    }

    //---------------------Setting onClick functions----------------------
    public void onShowClick(View view) {
        urlET = findViewById(R.id.url);
        String url=urlET.getText().toString();
        downloadImageTask=new DownloadImageTask(this);
        downloadImageTask.execute(url);
    }

    public void onClickOk(View view) {
        Intent intent = getIntent();
        movieName = title.getText().toString();
        desc = description.getText().toString();
        imageUrl = urlET.getText().toString();
        intent.putExtra("name", movieName);
        intent.putExtra("description", desc);
        intent.putExtra("url", imageUrl);
        if(movieName.equals("")){
            Toast toast = Toast.makeText(this, "You must enter a title", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onClickCancel(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    //--------------------implementing the onPreExecute and onSuccess interfaces----------------------
    @Override
    public void onPreExecute() {
    }

    @Override
    public void onSuccess(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
