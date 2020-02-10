package com.leonardo.spotify_api_player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leonardo.spotify_api_player.Connectors.SongService;
import com.leonardo.spotify_api_player.Model.Song;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.leonardo.spotify_api_player.Connectors.SongService;

import com.leonardo.spotify_api_player.Model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;

import androidx.appcompat.app.AppCompatActivity;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private TextView userView;
    private TextView songView;
    private TextView name;
    private TextView email;
    private Button addBtn;
    private Song song;

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songService = new SongService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);

        //user
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

        addBtn = (Button) findViewById(R.id.add);

        //Set data user
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));
        email.setText(sharedPreferences.getString("userEmail", "No email"));
        name.setText(sharedPreferences.getString("userName", "No Name"));

        getTracks();

        addBtn.setOnClickListener(addListener);
    }

    private View.OnClickListener addListener = v -> {
        songService.addSongToLibrary(this.song);
        if (recentlyPlayedTracks.size() > 0) {
            recentlyPlayedTracks.remove(0);
        }
        updateSong();
    };


    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getSongs();
            updateSong();
        });
    }

    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            songView.setText(recentlyPlayedTracks.get(0).getName());
            song = recentlyPlayedTracks.get(0);
        }
    }

}