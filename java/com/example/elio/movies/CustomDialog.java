package com.example.elio.movies;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import java.util.ArrayList;

//Creating the dialog for edit and delete movies
public class CustomDialog extends Dialog implements View.OnClickListener {

    public Activity activity;
    public Dialog d;
    public Button delete, edit;
    public String NAME;
    public int ID;
    DataBaseHandler db;
    ArrayList<MovieSample> names;

    public CustomDialog(Activity activity, String name, int id) {
        super(activity);
        NAME=name;
        ID=id;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sure);

        delete = (Button) findViewById(R.id.dialogDelete);
        edit = (Button) findViewById(R.id.dialogEdit);
        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
        db = new DataBaseHandler(activity);
        names=db.getAllMovieList();
    }

    //Setting onClick function for when each button is clicked
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogDelete:
                db.deleteMovie(ID);
                activity.recreate();
                break;
            case R.id.dialogEdit:
                for(int i=0 ; i<names.size() ; i++){
                    if(NAME.equals(names.get(i).getName())){
                        String title = names.get(i).getName();
                        String des = names.get(i).getDescription();
                        String url = names.get(i).getImageUrl();
                        int id = names.get(i).getId();

                        Intent editActivity = new Intent(activity,AddMovie.class);
                        editActivity.putExtra("name",title);
                        editActivity.putExtra("des",des);
                        editActivity.putExtra("url",url);
                        editActivity.putExtra("id",id);
                        activity.startActivityForResult(editActivity,1);
                        break;
                    }
                }
            default:
                break;
        }
        dismiss();
    }
}
