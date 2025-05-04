package com.example.arcsightdefence;

import android.os.Bundle;
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

public class PredictorActivity extends AppCompatActivity {
    private EditText barrelLength, caliber, weight, magazineCapacity, actionType, price;
    private Button predictButton;
    private TextView resultText;
    private static final String API_URL = "http://10.0.2.2:5003/gun_prediction"; // Local API URL for emulator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictor);

        barrelLength = findViewById(R.id.barrelLength);
        caliber = findViewById(R.id.caliber);
        weight = findViewById(R.id.weight);
        magazineCapacity = findViewById(R.id.magazineCapacity);
        actionType = findViewById(R.id.actionType);
        price = findViewById(R.id.price);
        predictButton = findViewById(R.id.recommendButton);
        resultText = findViewById(R.id.editTextResult);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictGunRange();
            }
        });
    }

    private void predictGunRange() {
        String barrel = barrelLength.getText().toString().trim();
        String cal = caliber.getText().toString().trim();
        String wt = weight.getText().toString().trim();
        String magCap = magazineCapacity.getText().toString().trim();
        String action = actionType.getText().toString().trim();
        String cost = price.getText().toString().trim();

        if (barrel.isEmpty() || cal.isEmpty() || wt.isEmpty() || magCap.isEmpty() || action.isEmpty() || cost.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject requestData = new JSONObject();
            requestData.put("Barrel Length (mm)", barrel);
            requestData.put("Caliber", cal);
            requestData.put("Weight (kg)", wt);
            requestData.put("Magazine Capacity", magCap);
            requestData.put("Action Type", action);
            requestData.put("Price (INR)", cost);

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_URL, requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.has("predicted_effective_range")) {
                                    String predictedRange = response.getString("predicted_effective_range");
                                    resultText.setText("Predicted Range: " + predictedRange);
                                } else if (response.has("error")) {
                                    resultText.setText("Error: " + response.getString("error"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                resultText.setText("Error parsing response");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            resultText.setText("Error: Unable to connect to the server");
                        }
                    });

            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error forming request", Toast.LENGTH_SHORT).show();
        }
    }
}
