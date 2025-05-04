from flask import Flask, request, jsonify
import pandas as pd
from flask_cors import CORS
import pickle
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

app = Flask(__name__)
CORS(app)

# Load dataset
data_path = "Equipments.csv"
df = pd.read_csv(data_path)

# Handle missing values in dataset (fill NaN with default values)
df.fillna("Unknown", inplace=True)

# Load recommendation model
model_path = "equipment_recommendation_model.pkl"
with open(model_path, "rb") as file:
    model = pickle.load(file)

# Select relevant feature columns for similarity calculations
feature_columns = [
    "Type", "Material", "Weight (kg)", "Durability (years)", 
    "Protection Level", "Special Features", "Price (INR)"
]

# Convert categorical features into numerical (if necessary)
df_encoded = pd.get_dummies(df[feature_columns])

# Handle missing values in encoded data
df_encoded.fillna(0, inplace=True)

# Compute similarity matrix
similarity_matrix = cosine_similarity(df_encoded, df_encoded)

@app.route('/equipment_recommendation', methods=['POST'])
def recommend_equipment():
    try:
        # Get input JSON data
        data = request.json

        # Ensure all required fields are provided
        required_fields = feature_columns
        for field in required_fields:
            if field not in data:
                return jsonify({"error": f"Missing '{field}' in request"}), 400

        # Handle NaN values in input (replace with default values)
        for key, value in data.items():
            if pd.isna(value) or value is None:
                data[key] = "Unknown" if isinstance(value, str) else 0

        # Convert input into a dataframe
        input_df = pd.DataFrame([data])

        # Encode input similar to dataset
        input_encoded = pd.get_dummies(input_df[feature_columns])
        input_encoded = input_encoded.reindex(columns=df_encoded.columns, fill_value=0)

        # Compute similarity with dataset
        similarities = cosine_similarity(input_encoded, df_encoded)
        similar_indices = similarities[0].argsort()[-5:][::-1]  # Top 5 recommendations

        # Fetch recommended equipment with ranking
        recommended_equipments = {
            f"Equipment {i+1}": {
                key: ("" if pd.isna(value) else value)  # Convert NaN to empty string
                for key, value in df.iloc[idx].to_dict().items()
            }
            for i, idx in enumerate(similar_indices)
        }

        return jsonify(recommended_equipments)

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5005, debug=True)
