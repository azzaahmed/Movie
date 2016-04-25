package com.example.azzaahmed.movie.data;

/**
 * Created by azza ahmed on 4/22/2016.
 */
public class MovieContract {


    private int _id;
    private int MovieId;
    private String name;
    private String date;
    private String overview;
    private int vote;
    private String image;
    public MovieContract(){

    }
    public MovieContract(String date,String name,int vote,int MovieId,String overview,String image){
        this.MovieId = MovieId;
        this.date=date;
        this.vote=vote;
        this.overview=overview;
        this.name=name;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public int getMovieId() {
        return MovieId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getOverview() {
        return overview;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMovieId(int movieId) {
        MovieId = movieId;
    }
}