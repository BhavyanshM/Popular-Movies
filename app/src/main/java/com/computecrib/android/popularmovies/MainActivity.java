package com.computecrib.android.popularmovies;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.computecrib.android.popularmovies.utilities.JsonUtilities;
import com.computecrib.android.popularmovies.utilities.RestfulUtilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindString(R.string.path_param_top_rated) String PATH_PARAM_TOP_RATED;
    @BindString(R.string.path_param_popular) String PATH_PARAM_POPULAR;

    @BindView(R.id.rv_movies) RecyclerView moviesRecyclerView;
    @BindView(R.id.tv_sort_order) TextView mSortOrderTextView;

    @BindString(R.string.base_rest_url) String BASE_REST_URL;
    @BindString(R.string.param_api_key) String PARAM_API_KEY;
    @BindString(R.string.api_key) String API_KEY;


    private String pathParam;
    private List<Movie> movies;
    private MoviesRecyclerAdapter adapter;
    private String mSortState;
    private Parcelable recyclerSavedState;
    private GridLayoutManager grid;

    private static final int FAVORITE_MOVIES_LOADER = 100;
    private static final String BUNDLE_RV_LAYOUT_LABEL = "recycler_layout";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("STATE", mSortState);
        //Courtesy: https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state
        //Date: 06/18/2018
        outState.putParcelable(BUNDLE_RV_LAYOUT_LABEL, grid.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Courtesy: https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state
        //Date: 06/18/2018
        if(savedInstanceState != null){
            recyclerSavedState = savedInstanceState.getParcelable(BUNDLE_RV_LAYOUT_LABEL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        movies = new ArrayList<>();
        getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);

        URL theMovieURL;
        String theOrderState = getString(R.string.popular_label);
        if(savedInstanceState != null){
            theOrderState = savedInstanceState.getString("STATE");
        }if(theOrderState == null){
            theOrderState = getString(R.string.popular_label);
        }

        switch (theOrderState){
            case "Top Rated":
                theMovieURL = buildTheMovieURL(PATH_PARAM_TOP_RATED);
                mSortOrderTextView.setText(R.string.top_rated_label);
                mSortState = getString(R.string.top_rated_label);
                if(RestfulUtilities.isConnected((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
                    new GetMovieTask().execute(theMovieURL);
                }else{
                    Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
                }
                break;

            case "Popular":
                theMovieURL = buildTheMovieURL(PATH_PARAM_POPULAR);
                mSortOrderTextView.setText(R.string.popular_label);
                mSortState = getString(R.string.popular_label);
                if(RestfulUtilities.isConnected((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
                    new GetMovieTask().execute(theMovieURL);
                }else{
                    Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
                }
                break;

            case "Favorite":
                mSortOrderTextView.setText(R.string.favorite_sort_label);
                mSortState = getString(R.string.favorite_sort_label);
                getLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER, null, this);
        }

        grid = new GridLayoutManager(this, 2);

        moviesRecyclerView.setLayoutManager(grid);
        adapter = new MoviesRecyclerAdapter(this, movies);
        moviesRecyclerView.setAdapter(adapter);

    }

    public URL buildTheMovieURL(String pathParam){
        return RestfulUtilities.buildUrlWithKey(
                BASE_REST_URL,
                pathParam,
                PARAM_API_KEY,
                API_KEY
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        URL theMovieURL;
        switch (item.getItemId()){
            case R.id.option_sort_top_rated:
                theMovieURL = buildTheMovieURL(PATH_PARAM_TOP_RATED);
                mSortOrderTextView.setText(R.string.top_rated_label);
                mSortState = getString(R.string.top_rated_label);
                if(RestfulUtilities.isConnected((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
                    new GetMovieTask().execute(theMovieURL);
                }else{
                    Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.option_sort_popularity:
                theMovieURL = buildTheMovieURL(PATH_PARAM_POPULAR);
                mSortOrderTextView.setText(R.string.popular_label);
                mSortState = getString(R.string.popular_label);
                if(RestfulUtilities.isConnected((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
                    new GetMovieTask().execute(theMovieURL);
                }else{
                    Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.option_sort_favorites:
                mSortOrderTextView.setText(R.string.favorite_sort_label);
                mSortState = getString(R.string.favorite_sort_label);
                getLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER, null, this);
        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id==FAVORITE_MOVIES_LOADER){
            return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, null, null, null, null );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor!=null && cursor.getCount()>0){
            Movie favMovie;
            List<Movie> favMovies = new ArrayList<Movie>();
            while(cursor.moveToNext()){
                favMovie = new Movie(
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING))
                );
                favMovies.add(favMovie);
            }
            adapter.setMovies(favMovies);
            adapter.notifyDataSetChanged();
            if(recyclerSavedState != null){
                grid.onRestoreInstanceState(recyclerSavedState);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
//        onCreate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @SuppressLint("StaticFieldLeak")
    public class GetMovieTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL fullURL = urls[0];
            String theMovieResults = null;
            try {
                theMovieResults = RestfulUtilities.getMovieResponse(fullURL);
            }catch (Exception e){
                e.printStackTrace();
            }
            return theMovieResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null && !s.equals("")){
                movies = JsonUtilities.getMoviesFromJSON(s);
                adapter.setMovies(movies);
                adapter.notifyDataSetChanged();
                if(recyclerSavedState != null){
                    grid.onRestoreInstanceState(recyclerSavedState);
                }
            }
        }


    }
}
