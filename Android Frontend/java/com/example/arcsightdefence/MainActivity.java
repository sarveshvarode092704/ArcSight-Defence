package com.example.arcsightdefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button rangePredictorBtn, gunRecommendBtn, equipmentRecommendBtn, grenadeRecommendBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        rangePredictorBtn = findViewById(R.id.RangePredictionButton);
        gunRecommendBtn = findViewById(R.id.GunRecommendationButton);
        equipmentRecommendBtn = findViewById(R.id.EquipmentRecommendationButton);
        grenadeRecommendBtn = findViewById(R.id.GrenadeRecommendationButton);
        logoutBtn = findViewById(R.id.Logout);

        // Set click listeners
        rangePredictorBtn.setOnClickListener(view -> openActivity(PredictorActivity.class));
        gunRecommendBtn.setOnClickListener(view -> openActivity(GunrecommendActivity.class));
        equipmentRecommendBtn.setOnClickListener(view -> openActivity(EquipmentActivity.class));
        grenadeRecommendBtn.setOnClickListener(view -> openActivity(GrenadeActivity.class));

        // Logout button click listener
        logoutBtn.setOnClickListener(view -> logoutUser());
    }

    // Method to open a new activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }

    // Logout method
    private void logoutUser() {
        // Clear saved login session (if using SharedPreferences)
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login screen
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Prevents going back
        startActivity(intent);
        finish();
    }
}
