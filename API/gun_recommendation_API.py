import pickle
import numpy as np
import pandas as pd
from flask import Flask, request, jsonify
from collections import OrderedDict

# Load the pickled model and preprocessing components
with open('gun_recommendation_model.pkl', 'rb') as file:
    model_data = pickle.load(file)

# Extract components
df = model_data['df']
encoder = model_data['encoder'] # encoding the features
scaler = model_data['scaler'] # for scaling the features 
processed_df = model_data['processed_df'] # processin the data
categorical_features = model_data['categorical_features'] # Categorial features importing
numerical_features = model_data['numerical_features'] # Numerical feature components

app = Flask(__name__)

# Define the exact order of attributes in the output
attribute_order = [
    "Name", "Type", "Manufacturer/Brand", "Caliber", "Barrel Length (mm)", "Effective Range (m)",
    "Magazine Capacity", "Weight (kg)", "Material", "Action Type", "Usage",
    "Introduced Year", "Price (INR)", "Popularity", "Legal Restrictions"
]

# Define the recommendation function
def recommend_guns(user_input):
    """Recommend top 5 guns based on user input."""

    # Ensure user input is passed with the same column names as the original dataset
    user_input_df = pd.DataFrame([user_input], columns=categorical_features + numerical_features)

    # Extract and encode categorical features from user input
    user_categorical_values = user_input_df[categorical_features].values.flatten()
    encoded_input = encoder.transform([user_categorical_values])[0]

    # Extract and scale numerical features from user input
    numerical_input = user_input_df[numerical_features].values
    scaled_numerical_input = scaler.transform(numerical_input).flatten()

    # Combine processed input features
    final_input = np.hstack((encoded_input, scaled_numerical_input))

    # Calculate similarity (using cosine similarity)
    similarity_scores = np.dot(processed_df, final_input) / (
        np.linalg.norm(processed_df, axis=1) * np.linalg.norm(final_input)
    )

    # Get top 5 recommendations based on the highest similarity scores
    top_5_indices = np.argsort(similarity_scores)[-5:][::-1]
    recommendations = df.iloc[top_5_indices]

    # Format the output strictly in the required order & convert to native Python types
    formatted_recommendations = OrderedDict()
    for i, (_, row) in enumerate(recommendations.iterrows()):
        gun_data = OrderedDict()
        for key in attribute_order:
            value = row[key]
            if isinstance(value, (np.integer, np.int64)):
                gun_data[key] = int(value)
            elif isinstance(value, (np.floating, np.float64)):
                gun_data[key] = float(value)
            else:
                gun_data[key] = str(value)

        formatted_recommendations[f"Gun {i+1}"] = gun_data

    return formatted_recommendations

@app.route('/guns_recommendation', methods=['POST'])
def recommend():
    try:
        data = request.json  # Get JSON input
        recommendations = recommend_guns(data)
        return jsonify({"recommendations": list(recommendations.values())})  # FIX: Convert dict to list
    except Exception as e:
        return jsonify({"error": str(e)})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5004, debug=True)
