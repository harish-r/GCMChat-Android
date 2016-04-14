package com.appdev.harish.gcmchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    boolean isLoggedIn;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = this.getSharedPreferences("GCMChat", Context.MODE_PRIVATE);
        editor = preferences.edit();

        isLoggedIn = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is logged in using IS_LOGGED_IN boolean in shared preferences
        isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);

        // If user is already logged in, redirect to contacts activity
        if (isLoggedIn != false) {
            Intent contactsIntent = new Intent(this, ContactsActivity.class);
            startActivity(contactsIntent);
            finish();
        }
        // Else, redirect to login page
        else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
