package com.example.banach.gottawatchit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Artur on 31-Jul-16.
 */


public class FavouritesDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myDB.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private SQLiteDatabase db;

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE + " ("
                    + DBContract.FavouritesTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContract.FavouritesTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    DBContract.FavouritesTable.COLUMN_NAME_API_ID + TEXT_TYPE + COMMA_SEP +
                    DBContract.FavouritesTable.COLUMN_NAME_POSTER + TEXT_TYPE + COMMA_SEP +
                    DBContract.FavouritesTable.COLUMN_NAME_PLOT + TEXT_TYPE + COMMA_SEP +
                    DBContract.FavouritesTable.COLUMN_NAME_RATING + TEXT_TYPE + COMMA_SEP +
                    DBContract.FavouritesTable.COLUMN_NAME_RELEASE + TEXT_TYPE + " )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE;

    public FavouritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteDatabase(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addEntry(Movie movie) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.FavouritesTable.COLUMN_NAME_TITLE, movie.getmTitle());
        values.put(DBContract.FavouritesTable.COLUMN_NAME_API_ID, movie.getmDBID());
        values.put(DBContract.FavouritesTable.COLUMN_NAME_POSTER, movie.getmPoster());
        values.put(DBContract.FavouritesTable.COLUMN_NAME_PLOT, movie.getmPlot());
        values.put(DBContract.FavouritesTable.COLUMN_NAME_RATING, movie.getmRating());
        values.put(DBContract.FavouritesTable.COLUMN_NAME_RELEASE, movie.getmRelease());
        db.insert(DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public Cursor getMovie(String id) {
        db = this.getReadableDatabase();

        Cursor cursor = db.query
                (DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE,
                        null,
                        DBContract.FavouritesTable.COLUMN_NAME_API_ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        null,
                        null);
        if (cursor != null)
            cursor.moveToFirst();
        db.close();
        return cursor;

    }

    public Cursor getMovies() {
        db = this.getReadableDatabase();
        Cursor cursor = db.query
                (DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        if (cursor != null)
            cursor.moveToFirst();
        db.close();
        return cursor;

    }


    public void deleteMovies() {
        db = this.getWritableDatabase();
        db.delete(DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE, null, null);
        db.close();
    }

    public void deleteMovie(String id) {
        db = this.getWritableDatabase();
        db.delete(DBContract.FavouritesTable.FAVOURITE_MOVIES_TABLE, DBContract.FavouritesTable.COLUMN_NAME_API_ID + "='" + id+ "'", null);
        db.close();

    }
}

