package com.example.arcsightdefence;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
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

public class GunrecommendActivity extends AppCompatActivity {
    private EditText type, caliber, barrelLength, effectiveRange, magazineCapacity,
            weight, actionType, price, editTextResult;
    private Button recommendButton;
    private static final String API_URL = "http://10.0.2.2:5004/guns_recommendation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gunrecommend);

        // Initialize input fields
        type = findViewById(R.id.type);
        caliber = findViewById(R.id.caliber);
        barrelLength = findViewById(R.id.barrelLength);
        effectiveRange = findViewById(R.id.effectiveRange);
        magazineCapacity = findViewById(R.id.magazineCapacity);
        weight = findViewById(R.id.weight);
        actionType = findViewById(R.id.actionType);
        price = findViewById(R.id.price);
        editTextResult = findViewById(R.id.editTextResult);
        recommendButton = findViewById(R.id.recommendButton);

        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGunRecommendations();
            }
        });
    }

    private void getGunRecommendations() {
        // Get input values
        String typeInput = type.getText().toString().trim();
        String caliberInput = caliber.getText().toString().trim();
        String barrelLengthInput = barrelLength.getText().toString().trim();
        String effectiveRangeInput = effectiveRange.getText().toString().trim();
        String magazineCapacityInput = magazineCapacity.getText().toString().trim();
        String weightInput = weight.getText().toString().trim();
        String actionTypeInput = actionType.getText().toString().trim();
        String priceInput = price.getText().toString().trim();

        // Validate inputs
        if (typeInput.isEmpty() || caliberInput.isEmpty() || barrelLengthInput.isEmpty() ||
                effectiveRangeInput.isEmpty() || magazineCapacityInput.isEmpty() ||
                weightInput.isEmpty() || actionTypeInput.isEmpty() || priceInput.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        // Create JSON request body
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Type", typeInput);
            jsonObject.put("Caliber", caliberInput);
            jsonObject.put("Barrel Length (mm)", barrelLengthInput);
            jsonObject.put("Effective Range (m)", effectiveRangeInput);
            jsonObject.put("Magazine Capacity", magazineCapacityInput);
            jsonObject.put("Weight (kg)", weightInput);
            jsonObject.put("Action Type", actionTypeInput);
            jsonObject.put("Price (INR)", priceInput);
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

        // Execute API call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(GunrecommendActivity.this, "API request failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData);
                        JSONArray recommendations = jsonResponse.getJSONArray("recommendations");

                        StringBuilder resultBuilder = new StringBuilder();
                        for (int i = 0; i < recommendations.length(); i++) {
                            JSONObject gunData = recommendations.getJSONObject(i);
                            resultBuilder.append("Gun ").append(i + 1).append(":\n")
                                    .append("Name: ").append(gunData.optString("Name", "N/A")).append("\n")
                                    .append("Type: ").append(gunData.optString("Type", "N/A")).append("\n")
                                    .append("Manufacturer/Brand: ").append(gunData.optString("Manufacturer/Brand", "N/A")).append("\n")
                                    .append("Caliber: ").append(gunData.optString("Caliber", "N/A")).append("\n")
                                    .append("Barrel Length: ").append(gunData.optString("Barrel Length (mm)", "N/A")).append("mm\n")
                                    .append("Effective Range: ").append(gunData.optString("Effective Range (m)", "N/A")).append("m\n")
                                    .append("Magazine Capacity: ").append(gunData.optString("Magazine Capacity", "N/A")).append("\n")
                                    .append("Weight: ").append(gunData.optString("Weight (kg)", "N/A")).append("kg\n")
                                    .append("Material: ").append(gunData.optString("Material", "N/A")).append("\n")
                                    .append("Action Type: ").append(gunData.optString("Action Type", "N/A")).append("\n")
                                    .append("Price: ₹").append(gunData.optString("Price (INR)", "N/A")).append("\n\n");
                        }

                        runOnUiThread(() ->
                                editTextResult.setText(resultBuilder.toString())
                        );

                    } catch (JSONException e) {
                        runOnUiThread(() ->
                                Toast.makeText(GunrecommendActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(GunrecommendActivity.this, "API request unsuccessful", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}