package com.example.banach.gottawatchit;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    static final String DETAIL_URI = "URI";
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private ImageView favouriteView;
    private FavouriteChangeListener callback;
    private Movie mMovie;
    private TrailerAdapter itemsAdapter;
    private ReviewAdapter reviewAdapter;

    public static DetailFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(Movie.PARCELABLE_KEY, movie);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovie = bundle.getParcelable(Movie.PARCELABLE_KEY);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviews = new ArrayList<>();
        trailers = new ArrayList<>();
        if (mMovie != null) {
            TextView empty = (TextView) getView().findViewById(R.id.emptyText);
            empty.setVisibility(View.GONE);
            FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(new TrailerCallback() {
                @Override
                public void onTaskDone(ArrayList<Trailer> list) {
                    updateTrailers(list);
                }
            });
            FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(new ReviewCallback() {
                @Override
                public void onTaskDone(ArrayList<Review> reviews) {
                    updateReviews(reviews);
                }
            });
            fetchTrailersTask.execute(mMovie.getmDBID());
            fetchReviewsTask.execute(mMovie.getmDBID());
            ImageView posterView = (ImageView) getView().findViewById(R.id.poster_image_view);
            TextView titleView = (TextView) getView().findViewById(R.id.title_text_view);
            TextView plotView = (TextView) getView().findViewById(R.id.plot_text_view);
            TextView ratingView = (TextView) getView().findViewById(R.id.rating_text_view);
            TextView releaseView = (TextView) getView().findViewById(R.id.release_text_view);

            favouriteView = (ImageView) getView().findViewById(R.id.fav_image_view);
            Picasso.with(getContext()).load(mMovie.getmPoster()).into(posterView);
            titleView.setText(mMovie.getmTitle());
            plotView.setText(mMovie.getmPlot());
            ratingView.setText(mMovie.getmRating());
            releaseView.setText(mMovie.getmRelease());
            FavouritesDBHelper favouritesDBHelper = new FavouritesDBHelper(getContext());
            if (favouritesDBHelper.getMovie(mMovie.getmDBID()) == null || favouritesDBHelper.getMovie(mMovie.getmDBID()).getCount() == 0) {
                favouriteView.setImageResource(R.drawable.ic_star_border_black_48dp);
            } else {
                favouriteView.setImageResource(R.drawable.ic_star_black_48dp);
            }

            favouriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pref = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("key_pref", getResources().getString(R.string.def_key));
                    changeEntry(mMovie.getmTitle(), mMovie.getmDBID(), mMovie.getmPoster(), mMovie.getmPlot(), mMovie.getmRating(), mMovie.getmRelease());
                    if (callback != null && pref.equals(getResources().getString(R.string.favs_key))) {
                        callback.onFavouriteClicked();
                    }
                }
            });
        } else {
            ScrollView scrollView = (ScrollView) getView().findViewById(R.id.MovieScrollView);
            scrollView.setVisibility(View.GONE);
        }
    }

    private void changeEntry(String title, String tmdbId, String poster, String plot, String rating, String release) {
        FavouritesDBHelper favouritesDBHelper = new FavouritesDBHelper(getContext());
        Movie newMovie = new Movie(title, tmdbId, poster, plot, rating, release);
        if (favouritesDBHelper.getMovie(tmdbId) == null || favouritesDBHelper.getMovie(tmdbId).getCount() == 0) {
            favouritesDBHelper.addEntry(newMovie);
            favouriteView.setImageResource(R.drawable.ic_star_black_48dp);
            Toast toast = Toast.makeText(getContext(), "Movie added to favourites", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            favouritesDBHelper.deleteMovie(tmdbId);
            favouriteView.setImageResource(R.drawable.ic_star_border_black_48dp);
            Toast toast = Toast.makeText(getContext(), "Movie removed from favourites", Toast.LENGTH_SHORT);
            toast.show();
        }

        favouritesDBHelper.close();
    }

    public void updateTrailers(ArrayList<Trailer> items) {
        trailers.clear();
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {

                trailers.add(items.get(i));

            }
            itemsAdapter =
                    new TrailerAdapter(getContext(), trailers);
            LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.list_layout);
            final int adapterCount = itemsAdapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                View item = itemsAdapter.getView(i, null, null);
                linearLayout.addView(item);
            }
        }
    }

    public void updateReviews(ArrayList<Review> items) {
        trailers.clear();
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {

                reviews.add(items.get(i));

            }
            reviewAdapter =
                    new ReviewAdapter(getContext(), reviews);
            LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.reviews_layout);
            final int reviewCount = reviewAdapter.getCount();
            for (int i = 0; i < reviewCount; i++) {
                View item = reviewAdapter.getView(i, null, null);
                linearLayout.addView(item);
            }
        }
    }

    public void setFavouriteChangeListener(FavouriteChangeListener favouriteChangeListener) {
        this.callback = favouriteChangeListener;
    }


}
