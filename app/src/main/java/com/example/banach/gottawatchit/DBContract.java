package com.example.banach.gottawatchit;
import android.provider.BaseColumns;

/**
 * Created by Artur on 31-Jul-16.
 */


public final class DBContract {
    public DBContract(){}

    public static abstract class FavouritesTable implements BaseColumns {
        public static final String FAVOURITE_MOVIES_TABLE = "FAVOURITE_MOVIES";
        public static final String COLUMN_NAME_ID = "ID_MAIN";
        public static final String COLUMN_NAME_API_ID = "API_ID";
        public static final String COLUMN_NAME_TITLE = "TITLE";
        public static final String COLUMN_NAME_POSTER = "POSTER";
        public static final String COLUMN_NAME_PLOT = "PLOT";
        public static final String COLUMN_NAME_RATING = "RATING";
        public static final String COLUMN_NAME_RELEASE = "RELEASE";
    }

}