from flask import Flask, request, jsonify
import mysql.connector
from flask_cors import CORS
from werkzeug.security import check_password_hash

app = Flask(__name__)
CORS(app)  # Enable CORS

# Database Connection
db = mysql.connector.connect(
    host="localhost",
    user="root",  
    password="S@rvesh12345",  
    database="arcsight_defence"
)

@app.route('/login', methods=['POST'])
def login():
    data = request.json
    email = data.get("email")
    password = data.get("password")  # Get plain password

    cursor = db.cursor(dictionary=True)
    query = "SELECT * FROM users WHERE email = %s"
    cursor.execute(query, (email,))
    user = cursor.fetchone()
    cursor.close()

    if user and check_password_hash(user["password_hash"], password):  # Compare hashed passwords
        user.pop("password_hash", None)  # Remove password for security
        return jsonify({"status": "success", "message": "Login successful", "user": user}), 200
    else:
        return jsonify({"status": "error", "message": "Invalid email or password"}), 401

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5002, debug=True)  # Running on port 5002
