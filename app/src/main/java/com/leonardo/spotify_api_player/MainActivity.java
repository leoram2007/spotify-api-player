package com.leonardo.spotify_api_player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Main class which will be a account information to the user
 * */
public class MainActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private TextView country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //songService = new SongService(getApplicationContext());
        //userView = (TextView) findViewById(R.id.user);

        //user
        name = (TextView) findViewById(R.id.txt_name);
        email = (TextView) findViewById(R.id.txt_email);
        country = (TextView) findViewById(R.id.txt_country);

        //Set data user
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        //User Id not showed in layout
        //userView.setText(sharedPreferences.getString("userid", "No User"));
        email.setText(sharedPreferences.getString("userEmail", "No email"));
        name.setText(sharedPreferences.getString("userName", "No Name"));
        country.setText(sharedPreferences.getString("userCountry", "No Country"));


    }
    /**
     * Method to change the activity and go to activity playlist
     * */
    public void goToPlaylist(View view){
        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }

}