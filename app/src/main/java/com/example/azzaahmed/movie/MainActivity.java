package com.example.azzaahmed.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity  {
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout flPanel2 = (FrameLayout) findViewById(R.id.flPanel_Two);
        if (null == flPanel2) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }

        if (null == savedInstanceState) {
            MainActivityFragment Fragment = new MainActivityFragment();

            Log.v("check rotate", " rotate in if " );
            getSupportFragmentManager().beginTransaction().add(R.id.flPanel_One, Fragment).commit();

        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onMovieSelect(GetMovieInfo obj) {
                if (mTwoPane) {

            Bundle args = new Bundle();

            args.putSerializable("My Class", obj);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flPanel_Two, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class).putExtra("My Class", obj);
            startActivity(intent);
        }
    }


}
