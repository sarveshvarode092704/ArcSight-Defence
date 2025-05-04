package com.example.arcsightdefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private CheckBox rememberMeCheckBox;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        rememberMeCheckBox = findViewById(R.id.rememberMe);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        loadSavedCredentials();

        loginButton.setOnClickListener(v -> userLogin());
    }

    private void loadSavedCredentials() {
        boolean isRemembered = sharedPreferences.getBoolean("remember_me", false);
        if (isRemembered) {
            emailInput.setText(sharedPreferences.getString("user_email", ""));
            passwordInput.setText(sharedPreferences.getString("user_password", ""));
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void userLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:5002/login"; // Update with your Flask API URL

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON Error!", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                response -> handleLoginResponse(response, email, password),
                error -> Toast.makeText(LoginActivity.this, "Login Failed! Check Server Connection.", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }

    private void handleLoginResponse(JSONObject response, String email, String password) {
        try {
            String status = response.getString("status");
            if ("success".equals(status)) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                saveCredentials(email, password);
                navigateToMainActivity();
            } else {
                Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Parsing Response!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);

        if (rememberMeCheckBox.isChecked()) {
            editor.putString("user_password", password);
            editor.putBoolean("remember_me", true);
        } else {
            editor.remove("user_password");
            editor.putBoolean("remember_me", false);
        }
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
