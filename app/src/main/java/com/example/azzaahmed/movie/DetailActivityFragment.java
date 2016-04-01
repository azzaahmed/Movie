package com.example.azzaahmed.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.azzaahmed.movie.R.layout.fragment_detail;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(fragment_detail, container, false);

        Intent intent =getActivity().getIntent();
        if (intent != null && intent.hasExtra("My Class")) {

            // String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
           GetMovieInfo MovieObj = (GetMovieInfo) intent.getSerializableExtra("My Class");

           ((TextView) rootView.findViewById(R.id.detail_text1)).setText(MovieObj.getOriginal_title());
            ((TextView) rootView.findViewById(R.id.detail_text2)).setText("vote rate : "+MovieObj.getVote_average()+"");
            ((TextView) rootView.findViewById(R.id.detail_text3)).setText( MovieObj.getOverview());
            ((TextView) rootView.findViewById(R.id.detail_text4)).setText("Released date : "+MovieObj.getReleased_Date());
       Picasso.with(getActivity()).load(MovieObj.getPoster_path()).into((ImageView) rootView.findViewById(R.id.detail_image));

        }
        return rootView;
    }
}
