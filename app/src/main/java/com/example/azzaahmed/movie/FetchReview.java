package com.example.azzaahmed.movie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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


public class FetchReview extends AsyncTask<String,Void,String[]> {  // id of review is String input, we want to return string so return type String

    String[] resultStrs;
    private  Context mContext;
    private View rootView;

        FetchReview(Context context, View v){
              mContext = context;
             rootView =v;
            }

    private  final String LOG_TAG = FetchTrailer.class.getSimpleName();


    private String[] getReviewFromJson(String ReviewJsonStr)
            throws JSONException {

        JSONObject reviewJson = new JSONObject(ReviewJsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray("results");

        resultStrs = new String[reviewArray.length()];

  for(int i = 0; i < reviewArray.length(); i++) {

            JSONObject review_info = reviewArray.getJSONObject(i);


            String Content = review_info.getString("content");

            String Author = review_info.getString("author");


            resultStrs[i] = Author+":"+"\n\n"+Content+"\n\n";
        }

        for (String s : resultStrs) {
            Log.v(LOG_TAG, "review entry: " + s);
        }
        return resultStrs;
    }


    @Override
    protected String[] doInBackground(String... params) {  // string params as input is string
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String ReviewJsonStr = null;

        try {

            String Trailer_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";

            final String APPID_PARAM = "api_key";
            Uri builtUri = Uri.parse(Trailer_BASE_URL)
                    .buildUpon()
                    .appendPath(params[0])
                    .appendPath("reviews")
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

                ReviewJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                ReviewJsonStr = null;
            }
            ReviewJsonStr = buffer.toString();

            Log.v(LOG_TAG, "review json string to check data:  " + ReviewJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the  data, there's no point in attempting
            // to parse it.
            ReviewJsonStr = null;
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
            return getReviewFromJson(ReviewJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {  //result = output of do in background= resultsStr is global to send it to detail activity

        if(result!=null){


            final int n = result.length;

            final TextView[] myTextViews = new TextView[n];

            for (int i = 0; i <n; i++) {
                // create a new textview
                final TextView rowTextView = new TextView(mContext);
                rowTextView.setText(result[i]);
                rootView.findViewById(R.id.reviewLabel).setVisibility(View.VISIBLE);
                // add the textview to the linearlayout
                ((LinearLayout) rootView.findViewById(R.id.view_review)).addView(rowTextView);;
//                // save a reference to the textview for later
//                myTextViews[i] = rowTextView;
            }


        }
    }
}

