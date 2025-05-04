from flask import Flask, request, jsonify
import pickle
import numpy as np

# Load the trained model
model_path = "gun_range_prediction_model.pkl"
with open(model_path, "rb") as file:
    model_package = pickle.load(file)

model = model_package['model']
scaler = model_package['scaler']
label_encoders = model_package['label_encoders']
features = model_package['features']

# Initialize Flask app
app = Flask(__name__)

@app.route('/gun_prediction', methods=['POST'])
def predict_gun_range():
    try:
        # Ensure the request is JSON
        if not request.is_json:
            return jsonify({"error": "Invalid input format, expected JSON"}), 400

        # Get JSON input
        data = request.get_json()

        if not data:
            return jsonify({"error": "No input data provided"}), 400

        # Extract features
        expected_features = [
            "Barrel Length (mm)", "Caliber", "Weight (kg)",
            "Magazine Capacity", "Action Type", "Price (INR)"
        ]
        
        input_values = []
        for feature in expected_features:
            if feature not in data:
                return jsonify({"error": f"Missing value for {feature}"}), 400
            
            value = data[feature]

            # Apply label encoding if necessary
            if feature in label_encoders:
                if value not in label_encoders[feature].classes_:
                    return jsonify({"error": f"Invalid value for {feature}: {value}"}), 400
                value = label_encoders[feature].transform([value])[0]

            input_values.append(value)

        # Convert to NumPy array and scale numerical features
        input_array = np.array(input_values).reshape(1, -1)
        input_scaled = scaler.transform(input_array)

        # Make prediction
        prediction = model.predict(input_scaled)[0]

        return jsonify({"predicted_effective_range": f"{prediction} meters"})
    
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5003, debug=True)

