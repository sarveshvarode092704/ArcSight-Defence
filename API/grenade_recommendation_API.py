from flask import Flask, request, jsonify
import pickle
import numpy as np
import pandas as pd

# Load the pickled model
with open("grenade_recommendation_model.pkl", "rb") as file:
    grenade_model = pickle.load(file)

grenades_df = grenade_model['grenades_df'] # grenade dataframe importing
encoder = grenade_model['encoder'] #encoder module
scaler = grenade_model['scaler'] #scaler module
processed_features = grenade_model['processed_features'] # feature processor module
categorical_features = grenade_model['categorical_features']  # coverting categorical features
numerical_features = grenade_model['numerical_features'] # numerical feature convertor

app = Flask(__name__)

@app.route("/grenade_recommendation", methods=["POST"])
def recommend():
    data = request.get_json()
    
    try:
        user_categorical = [data[feature] for feature in categorical_features] #feature coversions to numerical and categorical
        user_numerical = [data[feature] for feature in numerical_features]
        
        encoded_user_categorical = encoder.transform([user_categorical])    # Inline datatransforamtion of categorical and numerical
        scaled_user_numerical = scaler.transform([user_numerical])
        user_processed = np.hstack((encoded_user_categorical, scaled_user_numerical)) #stacking features
        
        similarity_scores = np.dot(processed_features, user_processed.T).flatten() / (  # Calculating similarity scores of the input and the data inside dataset
            np.linalg.norm(processed_features, axis=1) * np.linalg.norm(user_processed)
        )
        
        top_indices = np.argsort(similarity_scores)[-5:][::-1]  # sorting top 5 recommendation
        recommendations = grenades_df.iloc[top_indices]
        
        result = {}
        for rank, (_, row) in enumerate(recommendations.iterrows(), start=1):
            result[f"Grenade {rank}"] = row.to_dict()
        
        return jsonify(result)
    except Exception as e:  #exception handling
        return jsonify({"error": str(e)})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5006, debug=True)
