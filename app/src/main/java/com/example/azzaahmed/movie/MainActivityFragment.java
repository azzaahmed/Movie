package com.example.azzaahmed.movie;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.azzaahmed.movie.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }
        private ImageView mainImage;
    private GridView gridview;

    private ImageListAdapter imageAdapter;
    private ArrayList<GetMovieInfo> MoviesArray = new ArrayList<GetMovieInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

         gridview = (GridView) rootView.findViewById(R.id.gridview);
         imageAdapter =new ImageListAdapter(getActivity(), new ArrayList<String>());
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String forecast =imageAdapter.getItem(position);
                GetMovieInfo MovObj = MoviesArray.get(position);

//                Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("My Class", MovObj);
//                startActivity(intent);
               // mListener.onMovieSelect(MovObj);
                ((MainActivity) getActivity()).onMovieSelect(MovObj);

            }
        });

        return rootView;
    }
    public class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private ArrayList<String> imageUrls;

        public ImageListAdapter(Context context, ArrayList<String> imageUrls) {
            super(context, R.layout.image_item, imageUrls);

            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.image_item, parent, false);
            }

            Picasso.with(context)
                    .load(imageUrls.get(position)).resize(300, 450)
                    .into((ImageView) convertView.findViewById(R.id.grid_item_imageview));

            return convertView;
        }
    }



    private void updateMovies() {
// check internet connection to avoid crash
            final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED)
            {

        FetchMovieTask movieTask = new FetchMovieTask();
   //     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        movieTask.execute();}
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }


    public class FetchMovieTask extends AsyncTask<Void,Void,String[]> {  // we want to return array of movies so return type String[]
        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.

        private  final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        private String[] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String Poster_Path = "poster_path";
            final String Original_title = "original_title";
            final String Overview = "overview";
            final String Released_Date = "release_date";
            final String Vote_average = "vote_average";
            final String Results = "results";
            final String Id = "id";
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(Results);


           String[] resultStrs = new String[movieArray.length()];




            MoviesArray.clear();  // if I did not clear the array first when i change the sort setting and go back it changes the pictures but when click details it would be the old data
            for(int i = 0; i < movieArray.length(); i++) {


                // Get the JSON object representing the day
                JSONObject movie_info = movieArray.getJSONObject(i);

                String Path = movie_info.getString(Poster_Path);
                String OriginalTitle = movie_info.getString(Original_title);
                String Overviews = movie_info.getString(Overview);
                int Movie_id = movie_info.getInt(Id);
                int Vote = movie_info.getInt(Vote_average);
                String Released_date = movie_info.getString(Released_Date);
                MoviesArray.add(new GetMovieInfo(Path,OriginalTitle,Overviews,Released_date,Vote,Movie_id));

                resultStrs[i] = "http://image.tmdb.org/t/p/w185"+Path;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "movie entry: " + s);
            }
            return resultStrs;

        }
        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

           boolean flag=true;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String Sort_value = sharedPrefs.getString(
                    getString(R.string.pref_sort_key),
                    getString(R.string.pref_most_popular_default));
            String Movie_BASE_URL="";
            if(Sort_value.equals("Most popular")) {
                Movie_BASE_URL =
                        "http://api.themoviedb.org/3/movie/popular?";

            }
            else if(Sort_value.equals("Highest Rate")){
                Movie_BASE_URL =
                        "http://api.themoviedb.org/3/movie/top_rated?";
            }
            else{
                flag=false;
            }
               if(flag) {
                   try {


                       final String APPID_PARAM = "api_key";
                       Uri builtUri = Uri.parse(Movie_BASE_URL)
                               .buildUpon()
                               .appendQueryParameter(APPID_PARAM, "")
                               .build();
                       URL url = new URL(builtUri.toString());
                       Log.v(LOG_TAG, "built URI" + builtUri.toString());

                       urlConnection = (HttpURLConnection) url.openConnection();
                       urlConnection.setRequestMethod("GET");
                       urlConnection.connect();

                       // Read the input stream into a String
                       InputStream inputStream = urlConnection.getInputStream();
                       StringBuffer buffer = new StringBuffer();
                       if (inputStream == null) {

                           movieJsonStr = null;
                       }
                       reader = new BufferedReader(new InputStreamReader(inputStream));

                       String line;
                       while ((line = reader.readLine()) != null) {

                           buffer.append(line + "\n");
                       }

                       if (buffer.length() == 0) {

                           movieJsonStr = null;
                       }
                       movieJsonStr = buffer.toString();

                       Log.v(LOG_TAG, "movie json string to check data:  " + movieJsonStr);
                   } catch (IOException e) {
                       Log.e(LOG_TAG, "Error ", e);

                       movieJsonStr = null;
                   } finally {
                       if (urlConnection != null) {
                           urlConnection.disconnect();
                       }
                       if (reader != null) {
                           try {
                               reader.close();
                           } catch (final IOException e) {
                               Log.e(LOG_TAG, "Error closing stream", e);
                           }
                       }
                   }
                   try {
                       return getMovieDataFromJson(movieJsonStr);
                   } catch (JSONException e) {
                       Log.e(LOG_TAG, e.getMessage(), e);
                       e.printStackTrace();
                   }
               }
            else if(!flag){
                   MoviesArray.clear();
                   MovieDbHelper MovieHelper = new MovieDbHelper (getActivity(),null,null,1);

                 String  path[]= MovieHelper.fetchFavoritesImage();
                  String title[]=MovieHelper.fetchFavoritesName();
                  String overview[]=MovieHelper.fetchFavoritesOverview();
                  String date[]=MovieHelper.fetchFavoritesDate();
                  int  vote[]= MovieHelper.fetchFavoritesVote();
                  int movieId[]=MovieHelper.fetchFavoritesId();
                   for(int i = 0; i < MovieHelper.fetchFavoritesImage().length; i++) {

                       MoviesArray.add(new GetMovieInfo(path[i],title[i],overview[i],date[i],vote[i],movieId[i]));
                   }
                   return  MovieHelper.fetchFavoritesImage();
               }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(result!=null){
              imageAdapter.clear();  // clear all data in the adapter

                for(String x:result)
                {
                 imageAdapter.add(x); // add one by one to the adapter

                }


            }
        }
    }

}
