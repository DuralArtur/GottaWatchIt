package com.example.banach.gottawatchit;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by BANACH on 29.08.2016.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>>{
    private TrailerCallback mTrailerCallback;
    final ArrayList<Trailer> list = new ArrayList<>();
    private String myKey = BuildConfig.TMDB_API_KEY;
    private Context mContext;
    private Cursor cursor;

    FetchTrailersTask(TrailerCallback TrailerCallback) {
        mTrailerCallback = TrailerCallback;
            }

    @Override
    protected void onPostExecute(ArrayList<Trailer> items) {
        mTrailerCallback.onTaskDone(list);
        }


    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        list.clear();
            String search = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?api_key=" + myKey;
            URL url = createUrl(search);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e("MainActivity", "Error: " + e);
            }
            // Extract relevant fields from the JSON response and create an {@link Book} object
            extractFeatureFromJson(jsonResponse);
        return list;
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("MainActivity", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = null;
            }
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            Log.e("MainActivity", "Error with creating URL " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private ArrayList<Movie> extractFeatureFromJson(String newsJSON) {
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("results");

            if (featureArray.length() > 0) {
                list.clear();

                for (int i = 0; i < featureArray.length(); i++) {
                    JSONObject firstFeature = featureArray.getJSONObject(i);
                    String key = firstFeature.getString("key");
                    list.add(new Trailer("Trailer " + (i+1),"http://www.youtube.com/watch?v="+key));
                }
            }
        } catch (JSONException e) {
            Log.e("MainActivity", "Problem parsing the news JSON results", e);
        }
        return null;
    }


}


