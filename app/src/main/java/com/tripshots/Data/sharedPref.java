package com.tripshots.Data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tripshots.ActivitySignUp;

import java.util.HashMap;

public class sharedPref {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Pref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String IS_ADDRESS = "IsAddress";

    // User name (make variable public to access from outside)

    // Email address (make variable public to access from outside)
    // public static final String KEY_EMAIL = "email";

    // Constructor
    public sharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession() {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing token in pref

        // commit changes
        editor.commit();
    }


    /**
     * Check login method wil check user1 login status
     * If false it will redirect user1 to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user1 is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, ActivitySignUp.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();


        // user1 email id
        //   user1.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user1
        return user;
    }





    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After redirect user1 to Main Activity
        Intent i = new Intent(_context, ActivitySignUp.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isFirstTime() {
        boolean firstTime = pref.getBoolean("firstTime", true);
        if (firstTime) {
            editor.putBoolean("firstTime", false);
            editor.commit();
        }
        return firstTime;
    }

}
