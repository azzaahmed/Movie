package com.example.azzaahmed.movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    public class FetchTrailer extends AsyncTask<String,Void,String[]> {

    String[] resultStrs;
    private  Context mContext;
    private View rootView;
   //TextView[] myTextViews;
    FetchTrailer (Context context, View v){
        mContext=context;
        rootView=v;
    }
        private  final String LOG_TAG = FetchTrailer.class.getSimpleName();


        private String[] getTrailerFromJson(String TrailerJsonStr)
                throws JSONException {

            JSONObject trailerJson = new JSONObject(TrailerJsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");


            resultStrs = new String[trailerArray.length()];

       for(int i = 0; i < trailerArray.length(); i++) {

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

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    TrailerJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    TrailerJsonStr = null;
                }
                TrailerJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Trailer json string to check data:  " + TrailerJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

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
        protected void onPostExecute(final String[] result) {  //result = output od do in background= resultsStr is global to send it to detail activity

            if(result!=null){

                    int n= result.length;
             //   myTextViews = new TextView[n];
                for(int i=0;i <n;i++) {
                    // create a new textview
                    final TextView rowTextView = new TextView(mContext);
                    rowTextView.setMinHeight(120);
                    rowTextView.setTextSize(18);
                    rowTextView.setGravity(Gravity.CENTER);
                    final int x=i;
                    // on click on trailer is moved from detail fragment as i can not fetch the textView when clicked there
                    rowTextView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent video = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + result[x]));
                                  mContext.startActivity(video);
                        }
                    });
                    rowTextView.setText("Trailer " + (i + 1));
                    rootView.findViewById(R.id.trailerLabel).setVisibility(View.VISIBLE);
                    // add the textview to the linearlayout
                    ((LinearLayout) rootView.findViewById(R.id.view_trailer)).addView(rowTextView);

                    // save a reference to the textview for later
                       //   myTextViews[i] = rowTextView;

                }


            }
        }
    }

