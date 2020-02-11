package com.leonardo.spotify_api_player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leonardo.spotify_api_player.Connectors.SongService;
import com.leonardo.spotify_api_player.Model.Song;

import java.util.ArrayList;

/**
 * Class for playlist, class manage the user playlist from spotify
 * */
public class PlaylistActivity extends AppCompatActivity {

    private TextView songView;
    private Button addBtn;
    private Song song;

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        songService = new SongService(getApplicationContext());
        songView = (TextView) findViewById(R.id.song);

        addBtn = (Button) findViewById(R.id.add);

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

    /**
     * Method for change activity this go to MainActivity (account info)
     * */
    public void goToAccount(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
