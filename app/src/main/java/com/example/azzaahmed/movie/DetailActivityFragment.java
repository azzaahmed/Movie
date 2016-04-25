package com.example.azzaahmed.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.azzaahmed.movie.data.MovieContract;
import com.example.azzaahmed.movie.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.azzaahmed.movie.R.layout.fragment_detail;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String[] keys;
    private String name,overview,Rdate,image;
    private int voteav;
    private FetchTrailer  trailer;
    private FetchReview  review;
    private ArrayAdapter<String> Adapter;
    private ArrayAdapter<String> ReviewAdapter;
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private String id;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(fragment_detail, container, false);


        Intent intent =getActivity().getIntent();
        if (intent != null && intent.hasExtra("My Class")) {

            // receive and display  all info
            GetMovieInfo MovieObj = (GetMovieInfo) intent.getSerializableExtra("My Class");
            // fetch the id of the movie to use it in the trailer
            id= MovieObj.getMovie_id()+"";
            name=MovieObj.getOriginal_title();
            voteav=MovieObj.getVote_average();
           overview= MovieObj.getOverview();
            Rdate=MovieObj.getReleased_Date();
            image=MovieObj.getPoster_path();
            ((TextView) rootView.findViewById(R.id.detail_text1)).setText(MovieObj.getOriginal_title());
            ((TextView) rootView.findViewById(R.id.detail_text2)).setText("vote rate : "+MovieObj.getVote_average()+"");
            ((TextView) rootView.findViewById(R.id.detail_text3)).setText( MovieObj.getOverview());
            ((TextView) rootView.findViewById(R.id.detail_text4)).setText("Released date : "+MovieObj.getReleased_Date());
            Picasso.with(getActivity()).load(MovieObj.getPoster_path()).into((ImageView) rootView.findViewById(R.id.detail_image));

        }



// define the adapter in order to send it to the fetchTrailer
        Adapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.trailer, // The name of the layout ID.
                        R.id.trailer_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        ListView list = (ListView) rootView.findViewById(R.id.listview_trailer); // list of the adapters
        list.setAdapter(Adapter);

// define the adapter inorder to send it to the fetchReview
        ReviewAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.trailer, // The name of the layout ID.
                        R.id.trailer_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        ListView listr = (ListView) rootView.findViewById(R.id.listview_review); // list of the adapters
        listr.setAdapter(ReviewAdapter);


// init fetchreview
        review = new FetchReview(getActivity(), ReviewAdapter);
        // send id as the first param (string)
        review.execute(id);  // after execute the adapter is returned   and  the resultsStr array is ready

// init fetchTrailer
          trailer = new FetchTrailer(getActivity(), Adapter);
        // send id as the first param (string)
        trailer.execute(id);  // after execute the adapter is returned   and  the resultsStr array is ready


// once click on an item in the list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //String trailers = Adapter.getItem(position);
                keys= trailer.resultStrs;
           Intent video =new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+keys[position]));
                startActivity(video);
            }
        });



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
                MovieContract Movie = new MovieContract (Rdate,name,voteav,mid,overview,image);
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



    public void onBtnClicked (View rootView) {

    }

}
