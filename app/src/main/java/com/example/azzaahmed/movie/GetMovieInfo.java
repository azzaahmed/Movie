package com.example.azzaahmed.movie;

import java.io.Serializable;

/**
 * Created by azza ahmed on 3/25/2016.
 */
public class GetMovieInfo implements Serializable {
        private String Poster_path;
        private String Original_title ;
        private String Overview ;
        private String Released_Date ;
        private int Vote_average;
        GetMovieInfo(String Poster_path,String Original_title,String Overview,String Released_Date,int Vote_average){
            this.Original_title=Original_title;
            this.Overview=Overview;
            this.Poster_path="http://image.tmdb.org/t/p/w185"+Poster_path;
            this.Released_Date=Released_Date;
            this.Vote_average=Vote_average;
        }
        public String getOriginal_title() {
            return Original_title;
        }
        public String getOverview() {
            return Overview;
        }
        public String getPoster_path() {
            return Poster_path;
        }
        public String getReleased_Date() {
            return Released_Date;
        }
        public int getVote_average() {
            return Vote_average;
        }


}


