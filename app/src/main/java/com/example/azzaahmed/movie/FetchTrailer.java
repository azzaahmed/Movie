package com.example.azzaahmed.movie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by azza ahmed on 4/22/2016.
 */


    public class FetchTrailer extends AsyncTask<String,Void,String[]> {  // id of movie is String input, we want to return string so return type String
        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.

    String[] resultStrs;

    private ArrayAdapter<String> Adapter;
    private  Context mContext;

     FetchTrailer(Context context, ArrayAdapter<String> mAdapter) {
        mContext = context;
        Adapter = mAdapter;
    }
        private  final String LOG_TAG = FetchTrailer.class.getSimpleName();


        private String[] getTrailerFromJson(String TrailerJsonStr)
                throws JSONException {

            JSONObject trailerJson = new JSONObject(TrailerJsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");


            resultStrs = new String[trailerArray.length()];


           // MoviesArray.clear();  // if I did not clear the array first when i change the sort setting and go back it changes the pictures but when click details it would be the old data
            for(int i = 0; i < trailerArray.length(); i++) {


                // Get the JSON object representing the day
                JSONObject trailer_info = trailerArray.getJSONObject(i);

                String Key = trailer_info.getString("key");



                resultStrs[i] = Key;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "trailer entry: " + s);
            }
            return resultStrs;

        }


        @Override
        protected String[] doInBackground(String... params) {  // string params as input is string
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String TrailerJsonStr = null;

            try {


                String Trailer_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";

                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(Trailer_BASE_URL)
                        .buildUpon()
                        .appendPath(params[0])
                        .appendPath("videos")
                        .appendQueryParameter(APPID_PARAM, "")
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG,"built URI"+builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    TrailerJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    TrailerJsonStr = null;
                }
                TrailerJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Trailer json string to check data:  " + TrailerJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                TrailerJsonStr = null;
            } finally{
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
                return getTrailerFromJson(TrailerJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {  //result = output od do in background= resultsStr is global to send it to detail activity

            if(result!=null){
                Adapter.clear();

                for(int i=0;i<result.length;i++)
                {
                    Adapter.add("trailer"+(i+1)); // add one by one to the adapter

                }

            }
        }
    }
