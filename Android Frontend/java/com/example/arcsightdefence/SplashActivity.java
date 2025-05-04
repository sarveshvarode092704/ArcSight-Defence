package com.example.arcsightdefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);  // Create this XML file

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserStatus();
            }
        }, SPLASH_DELAY);
    }

    private void checkUserStatus() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isRegistered = prefs.getBoolean("isRegistered", false);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isRegistered) {
            // User is not registered -> Show Registration Page
            startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
        } else if (!isLoggedIn) {
            // User is registered but not logged in -> Show Login Page
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            // User is already logged in -> Go to MainActivity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }

        finish(); // Close SplashActivity
    }
}
