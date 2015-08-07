package com.example.raghav.nanomoviesapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by raghav on 8/3/15.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.raghav.nanomoviesapp.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_REVIEWS= "reviews";
    public static final String PATH_TRAILERS= "trailers";

    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_FAVORITES;

        // table name
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_NAME="movie_name";
        public static final String COLUMN_RATING="rating";
        public static final String COLUMN_RELEASE_DATE="release_date";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_IMAGE_URI="image_uri";

        public static Uri buildFavoriteMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildFavoriteMovieWithMovieName(String movieName) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_MOVIE_NAME,movieName)
                    .build();
        }
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_REVIEWS;

        // table name
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_FAVORITE_ID="favorite_id";

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_TRAILERS;

        // table name
        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_URI="uri";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_FAVORITE_ID="favorite_id";

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }


}
