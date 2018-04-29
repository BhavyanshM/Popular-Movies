package com.computecrib.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.computecrib.android.popularmovies.utilities.RestfulUtilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView hw;
    private String pathParam;
    private RecyclerView moviesRecyclerView;
    private List<Movie> movies;
    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        hw = findViewById(R.id.hw_tv);
//        pathParam = "337167";
//        URL theMovieURL = RestfulUtilities.buildUrlWithKey(pathParam);
//        new GetMovieTask().execute(theMovieURL);
        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        movies = new ArrayList<>();
        GridLayoutManager grid = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(grid);
        adapter = new MoviesRecyclerAdapter(this, movies);
        moviesRecyclerView.setAdapter(adapter);

    }

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
                hw.setText(s);
            }
        }
    }


}
