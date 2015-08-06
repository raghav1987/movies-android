package com.example.raghav.nanomoviesapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.raghav.nanomoviesapp.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by raghav on 8/3/15.
 */
public class TestUtilities extends AndroidTestCase {
    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createFavoriteValues() {
        ContentValues favoriteValues = new ContentValues();
        favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_DESCRIPTION, "Some description");
        favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_IMAGE_URI, "Some URI");
        favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_NAME, "The faster and the furiouser");
        favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_RATING, 8.8);
        favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE, "2015-12-12");

        return favoriteValues;
    }

    static ContentValues createReviewValues(long id) {
        ContentValues favoriteValues = new ContentValues();
        favoriteValues.put(MovieContract.ReviewEntry.COLUMN_DESCRIPTION, "Some description");
        favoriteValues.put(MovieContract.ReviewEntry.COLUMN_FAVORITE_ID, id);
        return favoriteValues;
    }

    static ContentValues createTrailerValues(long id) {
        ContentValues favoriteValues = new ContentValues();
        favoriteValues.put(MovieContract.TrailerEntry.COLUMN_DESCRIPTION, "Some description");
        favoriteValues.put(MovieContract.TrailerEntry.COLUMN_FAVORITE_ID, id);
        favoriteValues.put(MovieContract.TrailerEntry.COLUMN_URI, "some uri");
        return favoriteValues;
    }

    static long insertRandomMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createFavoriteValues();

        long locationRowId;
        locationRowId = db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Random movie Values", locationRowId != -1);

        return locationRowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
