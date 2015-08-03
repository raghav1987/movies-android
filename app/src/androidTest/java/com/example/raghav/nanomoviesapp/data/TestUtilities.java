package com.example.raghav.nanomoviesapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

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
}
