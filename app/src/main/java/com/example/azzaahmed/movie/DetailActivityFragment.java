package com.example.azzaahmed.movie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.azzaahmed.movie.data.MovieContract;
import com.example.azzaahmed.movie.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import static com.example.azzaahmed.movie.R.layout.fragment_detail;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String[] keys;
    private String name,overview,rDate,image;
    private int voteAv;
    private FetchTrailer  trailer;
    private FetchReview  review;
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private String id;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(fragment_detail, container, false);


            GetMovieInfo MovieObj = (GetMovieInfo) getArguments().getSerializable("My Class");
            // fetch the id of the movie to use it in the trailer
            id= MovieObj.getMovie_id()+"";
            name=MovieObj.getOriginal_title();
            voteAv=MovieObj.getVote_average();
           overview= MovieObj.getOverview();
            rDate=MovieObj.getReleased_Date();
            image=MovieObj.getPoster_path();
            ((TextView) rootView.findViewById(R.id.detail_text1)).setText(MovieObj.getOriginal_title());
            ((TextView) rootView.findViewById(R.id.detail_text2)).setText("vote: "+MovieObj.getVote_average()+"/10");
            ((TextView) rootView.findViewById(R.id.detail_text3)).setText( MovieObj.getOverview());
            ((TextView) rootView.findViewById(R.id.detail_text4)).setText("Released: "+MovieObj.getReleased_Date());
            Picasso.with(getActivity()).load(MovieObj.getPoster_path()).resize(400, 550) // resizes the image to these dimensions (in pixel)
                    .into((ImageView) rootView.findViewById(R.id.detail_image));

// check internet coonection to avoid crash
        final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {
// init fetchreview
            review = new FetchReview(getActivity(), rootView);
            // send id as the first param (string)
            review.execute(id);  // after execute the adapter is returned   and  the resultsStr array is ready

// init fetchTrailer
            //    trailer = new FetchTrailer(getActivity(), Adapter);
            trailer = new FetchTrailer(getActivity(), rootView);
            // send id as the first param (string)
            trailer.execute(id);  // after execute the adapter is returned   and  the resultsStr array is ready
        }
        final Button button= (Button) rootView.findViewById(R.id.btn1);

        boolean check;
        MovieDbHelper checkMovieHelper = new MovieDbHelper (getActivity(),null,null,1);
        check=checkMovieHelper.checkInFavorites(Integer.parseInt(id));
        if(!check){
            button.setText("fav");
        }
        else
        {   button.setText("unfav");}
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check;
                int mid=Integer.parseInt(id);
                MovieContract Movie = new MovieContract (rDate,name,voteAv,mid,overview,image);
                MovieDbHelper MovieHelper = new MovieDbHelper (getActivity(),null,null,1);
                check=MovieHelper.checkInFavorites(Integer.parseInt(id));
                if(!check){
                    MovieHelper.addMovie(Movie);
                    button.setText("unfav");}
                else {
                    MovieHelper.deleteMovie(mid);
                    button.setText("fav");
                }

                Log.v("DetailActivityFragment", "check btn: " + MovieHelper.databaseToString());
            }
        });

        return rootView;
    }



}
