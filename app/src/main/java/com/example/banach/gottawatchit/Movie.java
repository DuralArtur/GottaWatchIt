package com.example.banach.gottawatchit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Artur on 17-Aug-16.
 */
public class Movie implements Parcelable {
    private String mTitle;
    private String mDBID;
    private String mPoster;
    private String mPlot;
    private String mRating;
    private String mRelease;


    /**
     * Create a new Movie object
     * @param title   is the title
     * @param poster  is the poster url
     * @param plot    is the plot of the movie
     * @param rating  is the movie's rating
     * @param release is the date when the movie was originally released
     */

    public Movie(String title, String dbid, String poster, String plot, String rating, String release) {
        mTitle = title;
        mDBID = dbid;
        mPoster = poster;
        mPlot = plot;
        mRating = rating;
        mRelease = release;
    }

    public Movie(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        this.mTitle = data[0];
        this.mDBID = data[1];
        this.mPoster = data[2];
        this.mPlot = data[3];
        this.mRating = data[4];
        this.mRelease = data[5];
    }
    public static final String PARCELABLE_KEY = "movie";
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]
                {this.mTitle,
                        this.mDBID,
                        this.mPoster,
                        this.mPlot,
                        this.mRating,
                        this.mRelease});
    }

    public static final Creator CREATOR = new Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getmTitle() {
        return mTitle;
    }

    public String getmDBID(){return mDBID;}

    public String getmPoster() {
        return mPoster;
    }

    public String getmPlot() {
        return mPlot;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmRelease() {
        return mRelease;
    }


}
