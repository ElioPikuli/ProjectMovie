package com.example.elio.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieLab";
    public static final String TABLE_MOVIES = "Movies";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "movieName";
    public static final String KEY_DESCRIPTION = "movieDescription";
    public static final String KEY_URL = "movieURL";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating a Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_MOVIES
                + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_URL + " TEXT " + ")";
        db.execSQL(query);
    }

    //------------------Setting the Database Functions----------------------
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, null, null);
        db.execSQL("delete from " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        onCreate(db);
    }

    public void addMovie(MovieSample movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, movie.getName());
        values.put(KEY_DESCRIPTION, movie.getDescription());
        values.put(KEY_URL, movie.getImageUrl());
        db.insert(TABLE_MOVIES, null, values);
        db.close();
    }

    public void deleteMovie(int delID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, KEY_ID + "=" + delID, null);
        db.close();
    }

    public ArrayList<MovieSample> getAllMovieList() {
        ArrayList<MovieSample> MovieList = new ArrayList<MovieSample>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MovieSample movie = new MovieSample();
                movie.setId(Integer.parseInt(cursor.getString(0)));
                movie.setName(cursor.getString(1));
                movie.setDescription(cursor.getString(2));
                movie.setImageUrl(cursor.getString(3));
                MovieList.add(movie);
            } while (cursor.moveToNext());
        }
        return MovieList;
    }
}

