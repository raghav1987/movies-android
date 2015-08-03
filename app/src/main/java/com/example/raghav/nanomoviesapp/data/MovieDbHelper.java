package com.example.raghav.nanomoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by raghav on 8/3/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                MovieContract.FavoriteEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.FavoriteEntry.COLUMN_MOVIE_NAME + " TEXT UNIQUE NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_RATING + " REAL NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_IMAGE_URI + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                MovieContract.ReviewEntry.TABLE_NAME + " (" +

                MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                MovieContract.ReviewEntry.COLUMN_FAVORITE_ID + " INTEGER NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_FAVORITE_ID + ") REFERENCES " +
                MovieContract.FavoriteEntry.TABLE_NAME +
                " (" + MovieContract.FavoriteEntry._ID + "));";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " +
                MovieContract.TrailerEntry.TABLE_NAME + " (" +

                MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                MovieContract.TrailerEntry.COLUMN_FAVORITE_ID + " INTEGER NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_FAVORITE_ID + ") REFERENCES " +
                MovieContract.FavoriteEntry.TABLE_NAME +
                " (" + MovieContract.FavoriteEntry._ID + "));";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
