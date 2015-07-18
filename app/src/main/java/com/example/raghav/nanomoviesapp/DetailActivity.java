package com.example.raghav.nanomoviesapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Constants.DETAIL_ACTIVITY_INTENT)) {
                MovieData mCurrentMovie = intent.getParcelableExtra(Constants.DETAIL_ACTIVITY_INTENT);
                ((TextView) rootView.findViewById(R.id.movie_title_bar))
                        .setText(mCurrentMovie.getTitle());

                ImageView moviePoster = (ImageView)rootView.findViewById(R.id.detail_poster_image);
                moviePoster.setScaleType(ImageView.ScaleType.FIT_XY);

                Picasso.with(getActivity())
                        .load(mCurrentMovie.getFullImageUrl())
                        .resize(500,500)
                        .centerInside()
                        .into(moviePoster);

                TextView ratingText = (TextView)rootView.findViewById(R.id.rating_text);
                ratingText.setText("Rating: " + mCurrentMovie.getVoteAverage());

                TextView releaseDateText = (TextView)rootView.findViewById(R.id.release_date_text);
                releaseDateText.setText("Release Date: " + mCurrentMovie.getReleaseDate());

                TextView synopsisText = (TextView)rootView.findViewById(R.id.synopsis_text);
                synopsisText.setText(mCurrentMovie.getOverview());

            }
            return rootView;
        }
    }
}
