package com.example.azzaahmed.movie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =10;
    private static final String DATABASE_NAME = "Movie.db";
    public static final String TABLE_Favorites= "favorites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MovieID = "Movie_id";
    public static final String COLUMN_Name = "name";
    public static final String COLUMN_vote = "vote";
    public static final String COLUMN_date = "date";
    public static final String COLUMN_overview = "overview";
    public static final String COLUMN_image = "image";




    public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_Favorites + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MovieID + " INTEGER UNIQUE NOT NULL, "+
                COLUMN_Name + " TEXT NOT NULL, " +
                COLUMN_vote +" INTEGER NOT NULL, " +
                COLUMN_date + " TEXT NOT NULL, "+
                COLUMN_overview + " TEXT NOT NULL, " +
                COLUMN_image + " TEXT NOT NULL "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Favorites);
        onCreate(db);
        Log.v("helpeer", "check  ");
    }

    //Add a new row to the database
    public void addMovie(MovieContract Movie){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MovieID, Movie.getMovieId());
        values.put(COLUMN_Name, Movie.getName());
        values.put(COLUMN_vote, Movie.getVote());
        values.put(COLUMN_date, Movie.getDate());
        values.put(COLUMN_overview,Movie.getOverview());
        values.put(COLUMN_image,Movie.getImage());


        SQLiteDatabase db = getWritableDatabase();

       db.insert(TABLE_Favorites, null, values);
        db.close();
    }
    public boolean checkInFavorites (int movieId){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites + " WHERE "+ COLUMN_MovieID + "=" + movieId ;
        Cursor c = db.rawQuery(query, null);
        if(c.getCount()>0){
           return true;
            }
        else {
           return false;
           }
    }

    //Delete a product from the database
    public void deleteMovie(int MovieId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Favorites + " WHERE " + COLUMN_MovieID + "=\"" + MovieId + "\";");
    }

    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites + " WHERE 1";

        //Cursor on the query result
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("name")) != null) {
                dbString += c.getString(c.getColumnIndex("name"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public String[] fetchFavoritesImage(){
        List<String> list = new ArrayList<String>();


        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites ;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        // after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_image)) != null) {
                list.add(c.getString(c.getColumnIndex(COLUMN_image)));

            }
            c.moveToNext();
        }
        String[] images = new String[list.size()];
        images = list.toArray(images);
        db.close();
        return images;
    }
    public int[] fetchFavoritesId(){
        List<Integer> list = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites ;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_MovieID)) != null) {
                list.add( c.getInt(c.getColumnIndex(COLUMN_MovieID)));

            }
            c.moveToNext();
        }
        int[] ids = new int[list.size()];
        for (int i=0; i < list.size(); i++)
        {
            ids[i] = list.get(i);
        }
        db.close();
        return ids;
    }
    public int[] fetchFavoritesVote(){
        List<Integer> list = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites ;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        //after the last row means the end of the results

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_vote)) != null) {
                list.add(c.getInt(c.getColumnIndex(COLUMN_vote)));
            }
            c.moveToNext();
        }
        int[] votes = new int[list.size()];
        for (int i=0; i < list.size(); i++)
        {
            votes[i] = list.get(i);
        }
        db.close();
        return votes;
    }
    public String[] fetchFavoritesDate(){
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites ;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        //after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_date)) != null) {
                list.add(c.getString(c.getColumnIndex(COLUMN_date)));
            }
            c.moveToNext();
        }
        String[] dates = new String[list.size()];
        dates = list.toArray(dates);
        db.close();
        return dates;
    }
    public String[] fetchFavoritesOverview(){
        List<String> list = new ArrayList<String>();


        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites ;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_overview)) != null) {
                list.add(c.getString(c.getColumnIndex(COLUMN_overview)));

            }
            c.moveToNext();
        }
        String[] overviews = new String[list.size()];
        overviews = list.toArray(overviews);
        db.close();
        return overviews;
    }
    public String[] fetchFavoritesName(){
        List<String> list = new ArrayList<String>();


        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Favorites ;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_Name)) != null) {
                list.add( c.getString(c.getColumnIndex(COLUMN_Name)));

            }
            c.moveToNext();
        }
        String[] names = new String[list.size()];
        names = list.toArray(names);
        db.close();
        return names;
    }

}