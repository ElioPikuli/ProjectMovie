package com.example.elio.movies;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Creating an HttpRequest class for getting info from the internet
public class HttpRequest extends AsyncTask<String, Void, String> {

    public final static int GET = 1;
    public final static int POST = 2;

    private Callbacks callbacks;
    private String errorMessage = null;
    private int httpMethod;

    public HttpRequest(Callbacks callbacks) {
        this.callbacks = callbacks;
        this.httpMethod = GET;
    }

    public HttpRequest(Callbacks callbacks, int httpMethod) {
        this.callbacks = callbacks;
        this.httpMethod = httpMethod;
    }

    protected void onPreExecute() {
        callbacks.onAboutToStart();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (httpMethod == POST) {
                String parameters = params[1]; // Url Parameters
                byte[] parameterBytes = parameters.getBytes();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Length", Integer.toString(parameterBytes.length));
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(parameterBytes);
            }

            int httpStatusCode = connection.getResponseCode();

            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                errorMessage = connection.getResponseMessage();
                return null;
            }

            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String downloadedText = "";
            String oneLine = bufferedReader.readLine();

            while (oneLine != null) {
                downloadedText += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }
            return downloadedText;
        }
        catch (Exception ex) {
            errorMessage = ex.getMessage();
            return null;
        }
        finally {
            if (bufferedReader != null) try {
                bufferedReader.close();
            }
            catch (Exception e) {
            }
            if (inputStreamReader != null) try {
                inputStreamReader.close();
            }
            catch (Exception e) {
            }
            if (inputStream != null) try {
                inputStream.close();
            }
            catch (Exception e) {
            }
        }
    }

    protected void onPostExecute(String downloadedText) {
        if (errorMessage == null)
            callbacks.onSuccess(downloadedText);
        else
            callbacks.onError(errorMessage);
    }

    public interface Callbacks {
        void onAboutToStart();

        void onSuccess(String downloadedText);

        void onError(String errorMessage);
    }
}
