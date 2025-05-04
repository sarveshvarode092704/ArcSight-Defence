package com.example.arcsightdefence;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText fullName, username, email, phoneNumber, password, confirmPassword;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.fullName);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        Button registerButton = findViewById(R.id.registerButton);
        TextView registerRedirect = findViewById(R.id.registerRedirect);

        requestQueue = Volley.newRequestQueue(this);

        registerButton.setOnClickListener(v -> registerUser());

        registerRedirect.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String fullNameText = fullName.getText().toString().trim();
        String usernameText = username.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String phoneText = phoneNumber.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullNameText) || TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(emailText) ||
                TextUtils.isEmpty(phoneText) || TextUtils.isEmpty(passwordText)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordText.equals(confirmPasswordText)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:5001/register"; // Update if hosted online

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("full_name", fullNameText);
            jsonParam.put("username", usernameText);
            jsonParam.put("email", emailText);
            jsonParam.put("phone_number", phoneText);
            jsonParam.put("password", passwordText);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON Error!", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParam,
                response -> {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                },
                error -> Toast.makeText(RegisterActivity.this, "Registration Failed! Check Server Connection.", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }
}
