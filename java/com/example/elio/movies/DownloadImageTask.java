package com.example.elio.movies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//Creating an AsyncTask Class to download the movies images
public class DownloadImageTask  extends AsyncTask<String, Void, Bitmap> {
    public interface CallBack{
        void onPreExecute();
        void onSuccess(Bitmap result);
    }
    private CallBack callBack;

    public DownloadImageTask(CallBack callback) {
        this.callBack = callback;
    }

    @Override
    protected void onPreExecute() {
        callBack.onPreExecute();
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap bitmap = null;
        try {
            URL url=new URL(urlDisplay);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input=connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        callBack.onSuccess(result);
    }
}
