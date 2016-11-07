package com.example.banach.gottawatchit;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    final ArrayList<Movie> list = new ArrayList<Movie>();
    private String myKey = BuildConfig.TMDB_API_KEY;
    private MoviesFragment moviesFragment;
    private Context mContext;
    private Cursor cursor;

    FetchMoviesTask(MoviesFragment moviesFragment, Context context) {
        this.moviesFragment = moviesFragment;
        mContext=context;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        list.clear();
        if (!params[0].equals("favourites")){
        String search = "http://api.themoviedb.org/3/movie/" + params[0] + "?api_key=" + myKey;
        URL url = createUrl(search);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("MainActivity", "Error: " + e);
        }
        // Extract relevant fields from the JSON response and create an {@link Book} object
        extractFeatureFromJson(jsonResponse);}
        else {
            String currentTMDBId;
            String currentTitle;
            String currentPoster;
            String currentPlot;
            String currentRating;
            String currentRelease;
            FavouritesDBHelper productDBHelper = new FavouritesDBHelper(mContext);
            SQLiteDatabase db = productDBHelper.getReadableDatabase();
            long numRows = DatabaseUtils.queryNumEntries(db, DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE);
            if (numRows > 0) {
                cursor = productDBHelper.getMovies();
                Log.v("MainActivity", "" + cursor.getCount());
                do {
                    currentTMDBId = cursor.getString(cursor.getColumnIndex(DBContract.FavouritesTable.COLUMN_NAME_API_ID));
                    currentTitle = cursor.getString(cursor.getColumnIndex(DBContract.FavouritesTable.COLUMN_NAME_TITLE));
                    currentPoster = cursor.getString(cursor.getColumnIndex(DBContract.FavouritesTable.COLUMN_NAME_POSTER));
                    currentPlot = cursor.getString(cursor.getColumnIndex(DBContract.FavouritesTable.COLUMN_NAME_PLOT));
                    currentRating = cursor.getString(cursor.getColumnIndex(DBContract.FavouritesTable.COLUMN_NAME_RATING));
                    currentRelease = cursor.getString(cursor.getColumnIndex(DBContract.FavouritesTable.COLUMN_NAME_RELEASE));
                    list.add(new Movie(currentTitle, currentTMDBId, currentPoster, currentPlot, currentRating, currentRelease));
                } while (cursor.moveToNext());
            }
        }
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
                    String title = firstFeature.getString("original_title");
                    String tmdbID = firstFeature.getString("id");
                    String poster = "http://image.tmdb.org/t/p/w342/" + firstFeature.getString("poster_path");
                    String plot = firstFeature.getString("overview");
                    String rating = firstFeature.getString("vote_average");
                    String release = firstFeature.getString("release_date");
                    list.add(new Movie(title, tmdbID, poster, plot, rating, release));
                }
            }
        } catch (JSONException e) {
            Log.e("MainActivity", "Problem parsing the news JSON results", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        if (result.size() > 0 && result != null) {
            moviesFragment.updateAdapter(result);
        } else {
            moviesFragment.updateAdapter(new ArrayList<Movie>());
        }

    }
}


