package com.leonardo.spotify_api_player;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.leonardo.spotify_api_player.Connectors.UserService;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.leonardo.spotify_api_player.Model.User;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class SplashActivity will be the first Activity for the user, this will authenticate Spotify
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Constants with the SharedPreferences which is our persistent storage
     * */
    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    /**
     * Constants used to connect the application with spotify API
     * */
    private static final String CLIENT_ID = "3c3d8e82c96549679e1e1c888eb3a41d";
    private static final String REDIRECT_URI = "com.spotifyapiplayer://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-library-read,playlist-modify-public,playlist-modify-private,user-read-email,user-read-private,playlist-read-private,playlist-read-collaborative";

    //Layout variables
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(mListener);


        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }

    /**
     * Here we use the user model for our users with the information we are going to receive from the Spotify API
     * */
    private void waitForUserInfo() {
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            editor.putString("userEmail", user.email);
            editor.putString("userName", user.display_name);
            editor.putString("userCountry", user.country);
            Log.d("STARTING", "GOT USER INFORMATION");
            Log.d("EMAIL: ", user.email );
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();
            startMainActivity();
        });
    }

    /**
     * This method will redirect to MainActivity (user account) when the user information from Spotify API is ready
     * */
    private void startMainActivity() {
        Intent newintent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(newintent);
    }

    /**
     * This method will take the constants for authentication, and the response we want (in this case an authentication token)
     * also we set our requested scopes (These are different permissions we need to request)
     * */
    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    /**
     * Method to open the Spotify WebView where the user has to log in or will open Spotify (if it’s installed)
     * */
    View.OnClickListener mListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login:
                    authenticateSpotify();
                    break;
            }
        }
    };


}
