package com.tripshots;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.tripshots.Data.sharedPref;

import java.util.HashMap;

public class ActivitySplash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    com.tripshots.Data.sharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = new sharedPref(getApplicationContext());


        if(!isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No internet connection is available",Toast.LENGTH_SHORT).show();
        }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if(sharedPref.isLoggedIn()){

                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(ActivitySplash.this, MainActivity.class);
                        ActivitySplash.this.startActivity(mainIntent);
                        ActivitySplash.this.finish();


                }

                else {
                    Intent mainIntent = new Intent(ActivitySplash.this, ActivitySignUp.class);
                    ActivitySplash.this.startActivity(mainIntent);
                    ActivitySplash.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public boolean isNetworkAvailable(Context ctx) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected() || activeNetworkInfo.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }
}
