package com.computecrib.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity {

    @BindString(R.string.path_param_top_rated) String PATH_PARAM_TOP_RATED;
    @BindString(R.string.path_param_popular) String PATH_PARAM_POPULAR;

    @BindView(R.id.rv_movies) RecyclerView moviesRecyclerView;

    private String pathParam;
    private List<Movie> movies;
    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        URL theMovieURL = RestfulUtilities.buildUrlWithKey(PATH_PARAM_POPULAR);
        if(isConnected()){
            new GetMovieTask().execute(theMovieURL);
        }else{
            Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
        }

        movies = new ArrayList<>();
        GridLayoutManager grid = new GridLayoutManager(this, 2);

        moviesRecyclerView.setLayoutManager(grid);
        adapter = new MoviesRecyclerAdapter(this, movies);
        moviesRecyclerView.setAdapter(adapter);

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
                theMovieURL = RestfulUtilities.buildUrlWithKey(PATH_PARAM_TOP_RATED);
                if(isConnected()){
                    new GetMovieTask().execute(theMovieURL);
                }else{
                    Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.option_sort_popularity:
                theMovieURL = RestfulUtilities.buildUrlWithKey(PATH_PARAM_POPULAR);
                if(isConnected()){
                    new GetMovieTask().execute(theMovieURL);
                }else{
                    Toast.makeText(this, R.string.network_unavailable_error, Toast.LENGTH_LONG).show();
                }
                break;
        }
        return true;
    }

    //Inspired by StackOverflow thread:
    //https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //Date: 1 May 2018
    private boolean isConnected() {
        ConnectivityManager cm
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkStatus = null;
        if (cm != null) {
            networkStatus = cm.getActiveNetworkInfo();
        }
        return networkStatus != null && networkStatus.isConnected();
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
                for (Movie movie : movies)
                    Log.e("RESPONSE:", movie.getPosterPath()+"\n");
                adapter.setMovies(movies);
                adapter.notifyDataSetChanged();
//                adapter = new MoviesRecyclerAdapter(getApplicationContext(), movies);
//                moviesRecyclerView.setAdapter(adapter);
//                moviesRecyclerView.invalidate();
            }
        }
    }
}
