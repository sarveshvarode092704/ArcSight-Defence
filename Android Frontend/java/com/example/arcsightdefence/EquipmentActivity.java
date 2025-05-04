package com.example.arcsightdefence;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentActivity extends AppCompatActivity {
    private EditText type, material, weight, durability, protectionLevel, specialFeatures, price, editTextResult;
    private Button recommendButton;
    private static final String API_URL = "http://10.0.2.2:5005/equipment_recommendation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        type = findViewById(R.id.type);
        material = findViewById(R.id.material);
        weight = findViewById(R.id.weight);
        durability = findViewById(R.id.durability);
        protectionLevel = findViewById(R.id.protectionLevel);
        specialFeatures = findViewById(R.id.specialFeatures);
        price = findViewById(R.id.price);
        editTextResult = findViewById(R.id.editTextResult2);
        recommendButton = findViewById(R.id.recommendButton);

        recommendButton.setOnClickListener(v -> getEquipmentRecommendations());
    }

    private void getEquipmentRecommendations() {
        String typeInput = type.getText().toString().trim();
        String materialInput = material.getText().toString().trim();
        String weightInput = weight.getText().toString().trim();
        String durabilityInput = durability.getText().toString().trim();
        String protectionLevelInput = protectionLevel.getText().toString().trim();
        String specialFeaturesInput = specialFeatures.getText().toString().trim();
        String priceInput = price.getText().toString().trim();

        if (typeInput.isEmpty() || materialInput.isEmpty() || weightInput.isEmpty() || durabilityInput.isEmpty() ||
                protectionLevelInput.isEmpty() || specialFeaturesInput.isEmpty() || priceInput.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert number fields safely
        double weightValue;
        int durabilityValue, priceValue;
        try {
            weightValue = Double.parseDouble(weightInput);
            durabilityValue = Integer.parseInt(durabilityInput);
            priceValue = Integer.parseInt(priceInput);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format in input fields", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Type", typeInput);
            jsonObject.put("Material", materialInput);
            jsonObject.put("Weight (kg)", weightValue);
            jsonObject.put("Durability (years)", durabilityValue);
            jsonObject.put("Protection Level", protectionLevelInput);
            jsonObject.put("Special Features", specialFeaturesInput);
            jsonObject.put("Price (INR)", priceValue);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error forming JSON request", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(EquipmentActivity.this, "API request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData);

                        StringBuilder resultBuilder = new StringBuilder();
                        for (int i = 1; i <= 5; i++) {
                            String recommendationKey = "Equipment " + i; // Updated key format
                            if (jsonResponse.has(recommendationKey)) {
                                JSONObject equipmentData = jsonResponse.getJSONObject(recommendationKey);
                                resultBuilder.append(recommendationKey).append(":\n")
                                        .append("Name: ").append(equipmentData.optString("Name", "N/A")).append("\n")
                                        .append("Manufacturer: ").append(equipmentData.optString("Manufacturer/Brand", "N/A")).append("\n")
                                        .append("Weight: ").append(equipmentData.optString("Weight (kg)", "N/A")).append(" kg\n")
                                        .append("Material: ").append(equipmentData.optString("Material", "N/A")).append("\n")
                                        .append("Durability: ").append(equipmentData.optString("Durability (years)", "N/A")).append(" years\n")
                                        .append("Protection Level: ").append(equipmentData.optString("Protection Level", "N/A")).append("\n")
                                        .append("Special Features: ").append(equipmentData.optString("Special Features", "N/A")).append("\n")
                                        .append("Price: ₹").append(equipmentData.optString("Price (INR)", "N/A")).append("\n\n");
                            }
                        }

                        runOnUiThread(() -> editTextResult.setText(resultBuilder.toString()));
                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(EquipmentActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(EquipmentActivity.this, "API request unsuccessful: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
