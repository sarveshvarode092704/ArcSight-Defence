package com.example.arcsightdefence;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GrenadeActivity extends AppCompatActivity {

    private EditText type, material, weight, explosiveType, blastRadius,
            activationMechanism, usage, effectiveness, price, durability;
    private EditText editTextResult;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grenade);

        // Initialize views
        initializeViews();
        requestQueue = Volley.newRequestQueue(this);

        Button recommendButton = findViewById(R.id.recommendButton);
        recommendButton.setOnClickListener(v -> getRecommendations());
    }

    private void initializeViews() {
        type = findViewById(R.id.type);
        material = findViewById(R.id.material);
        weight = findViewById(R.id.weight);
        explosiveType = findViewById(R.id.explosiveType);
        blastRadius = findViewById(R.id.blastRadius);
        activationMechanism = findViewById(R.id.activationMechanism);
        usage = findViewById(R.id.usage);
        effectiveness = findViewById(R.id.effectiveness);
        price = findViewById(R.id.price);
        durability = findViewById(R.id.durability);
        editTextResult = findViewById(R.id.editTextResult3); // THIS WAS MISSING
    }

    private void getRecommendations() {
        JSONObject requestData = new JSONObject();
        try {
            // Add all parameters to the request
            requestData.put("Type", type.getText().toString());
            requestData.put("Material", material.getText().toString());
            requestData.put("Weight (kg)", weight.getText().toString());
            requestData.put("Explosive Type", explosiveType.getText().toString());
            requestData.put("Blast Radius (m)", blastRadius.getText().toString());
            requestData.put("Activation Mechanism", activationMechanism.getText().toString());
            requestData.put("Usage", usage.getText().toString());
            requestData.put("Effectiveness (%)", effectiveness.getText().toString());
            requestData.put("Price (INR)", price.getText().toString());
            requestData.put("Durability (years)", durability.getText().toString());

            // Update this URL with your Flask server's IP:port
            String url = "http://10.0.2.2:5006/grenade_recommendation";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestData,
                    response -> displayResults(response),  // Pass JSONObject directly
                    error -> showError("Error: " + error.getMessage())
            );

            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            showError("Invalid input format");
        }
    }

    private void displayResults(JSONObject response) {
        try {
            StringBuilder formattedResult = new StringBuilder();

            // Loop through each grenade recommendation
            for (int i = 1; i <= 5; i++) {
                String grenadeKey = "Grenade " + i;
                if (response.has(grenadeKey)) {
                    JSONObject grenade = response.getJSONObject(grenadeKey);
                    formattedResult.append("=== ").append(grenadeKey).append(" ===\n");

                    // Add all grenade properties
                    formattedResult.append("Name: ").append(grenade.getString("Name")).append("\n");
                    formattedResult.append("Type: ").append(grenade.getString("Type")).append("\n");
                    formattedResult.append("Material: ").append(grenade.getString("Material")).append("\n");
                    formattedResult.append("Weight (kg): ").append(grenade.getDouble("Weight (kg)")).append("\n");
                    formattedResult.append("Blast Radius (m): ").append(grenade.getDouble("Blast Radius (m)")).append("\n");
                    formattedResult.append("Effectiveness (%): ").append(grenade.getDouble("Effectiveness (%)")).append("\n");
                    formattedResult.append("Price (INR): ").append(grenade.getDouble("Price (INR)")).append("\n");
                    formattedResult.append("Durability (years): ").append(grenade.getInt("Durability (years)")).append("\n");
                    formattedResult.append("Explosive Type: ").append(grenade.getString("Explosive Type")).append("\n");
                    formattedResult.append("Country of Origin: ").append(grenade.getString("Country of Origin")).append("\n\n");
                }
            }

            editTextResult.setText(formattedResult.toString());
        } catch (JSONException e) {
            showError("Error parsing response: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}