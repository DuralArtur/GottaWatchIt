package com.example.banach.gottawatchit;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements MovieClickListener, FavouriteChangeListener {
    public static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    public MoviesAdapter moviesAdapter;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ArrayList<Movie> movies = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(movies);
        moviesAdapter.onClickListener(this);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(moviesAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getThoseMovies();
    }

    public void updateAdapter(ArrayList<Movie> movieArrayList) {
        if (movieArrayList.size() > 0 && movieArrayList != null) {
            moviesAdapter.setItems(movieArrayList);
        } else {
            moviesAdapter.setItems(new ArrayList<Movie>());
        }
        moviesAdapter.notifyDataSetChanged();
    }

    public void getThoseMovies(){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String pref = sharedPrefs.getString("key_pref", getResources().getString(R.string.def_key));
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this, getContext());
        fetchMoviesTask.execute(pref);

    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (((MainActivity) getActivity()).mTwoPanes) {
            DetailFragment fragment = DetailFragment.newInstance(movie);
            fragment.setFavouriteChangeListener(this);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
            detailIntent.putExtra(Movie.PARCELABLE_KEY, movie);
            getActivity().startActivity(detailIntent);
        }
    }

    @Override
    public void onFavouriteClicked() {
        getThoseMovies();
    }
}
