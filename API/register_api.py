from flask import Flask, request, jsonify
import mysql.connector
from flask_cors import CORS
from werkzeug.security import generate_password_hash

app = Flask(__name__)
CORS(app)  # Enable CORS

# Database Connection
db = mysql.connector.connect(
    host="localhost",
    user="root",  
    password="S@rvesh12345",  
    database="arcsight_defence"
)

@app.route('/register', methods=['POST'])
def register():
    data = request.json
    full_name = data.get("full_name")
    username = data.get("username")
    phone_number = data.get("phone_number")
    email = data.get("email")
    password = data.get("password")  # Get plain password

    cursor = db.cursor()

    # Check if user already exists
    cursor.execute("SELECT * FROM users WHERE email = %s OR username = %s", (email, username))
    existing_user = cursor.fetchone()
    
    if existing_user:
        return jsonify({"status": "error", "message": "Email or Username already registered"}), 409

    # Hash Password before storing
    hashed_password = generate_password_hash(password)

    # Insert new user
    query = "INSERT INTO users (full_name, username, phone_number, email, password_hash) VALUES (%s, %s, %s, %s, %s)"
    cursor.execute(query, (full_name, username, phone_number, email, hashed_password))
    db.commit()
    cursor.close()

    return jsonify({"status": "success", "message": "User registered successfully"}), 201

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True)  # Running on port 5001
