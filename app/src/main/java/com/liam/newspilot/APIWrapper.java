package com.liam.newspilot;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/**
 * Get the articles given a certain url search
 */
@SuppressWarnings("deprecation")

public class APIWrapper extends AsyncTask<URL, Void, ArrayList<Article>> {
    /**
     * @param urls the url to get the search
     * @return the list of articles gotten from the search
     */

    @Override
    protected ArrayList<Article> doInBackground(URL... urls) {
        Result result;
        String jsonResult = "";

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            InputStream inputStream;
            int status = urlConnection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)  {
                inputStream = urlConnection.getErrorStream();
            }
            else  {
                inputStream = urlConnection.getInputStream();
            }

            jsonResult = convertInputStreamToString(inputStream);
            //create deserializer
            Gson gson = new Gson();
            result = gson.fromJson(jsonResult, Result.class);

            return result.articles;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TODO create loading spinner
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        super.onPostExecute(articles);
        //TODO create cardviews with articles list
    }

    /** Convert an inputStream to a String
     * @param inputStream the inputStream
     * @return string
     */
    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
