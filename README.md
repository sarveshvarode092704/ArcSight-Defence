# ArcSight-Defence

•	Introduction:
ArcSight Defence is a mobile application designed to provide comprehensive and accurate information about the weapons, equipment, and grenades used by the Indian Paramilitary Forces (IPF). The app serves as a valuable resource for researchers, enthusiasts, analysts, and anyone interested in understanding the diverse arsenal employed by the IPF. By offering detailed insights into firearms, equipment, and explosive devices, ArcSight Defence ensures that users stay well-informed and knowledgeable.
The application features multiple modules that recommend weapons, grenades, and equipment based on user preferences. Additionally, it includes a gun range prediction module to estimate the effective range of various firearms. ArcSight Defence not only delivers reliable recommendations but also enhances the user's understanding of weapon characteristics and capabilities. With a user-friendly interface and robust data accuracy, it becomes an essential tool for those interested in studying the weaponry of the Indian Paramilitary Forces.


•	Objective:
The primary aim of ArcSight Defence is to provide a comprehensive platform that delivers accurate and reliable information about the weapons, equipment, and grenades used by the Indian Paramilitary Forces (IPF). The application aims to serve as a valuable resource for researchers, enthusiasts, analysts, and professionals who seek insights into the diverse arsenal employed by the IPF.

•	Key Features:
1.	Guns Recommendation System:
o	Recommends guns based on various input features.
o	Provides detailed information about the recommended guns, enabling users to make informed decisions.
2.	Grenades Recommendation System:
o	Recommends grenades by analyzing multiple input features.
o	Offers comprehensive information about the recommended grenades.
3.	Equipment Recommendation System:
o	Suggests suitable equipment based on specific input parameters.
o	Displays detailed information about the recommended equipment.
4.	Gun Range Predictor:
o	Predicts the effective range of guns in meters using multiple input features.
o	Empowers users with precise range calculations for better analysis.

•	System Requirements: 
1. Hardware Requirements
Client Device (Android Smartphone):
•	Processor: Quad-core 1.8 GHz or higher
•	RAM: 4 GB or higher
•	Storage: Minimum 100 MB of free space
•	Operating System: Android 8.0 (Oreo) or higher
•	Display: 5.0-inch screen or larger with a resolution of at least 720x1280 pixels
•	Network Connectivity: Stable internet connection (Wi-Fi or mobile data)
Development Environment (PC/Laptop):
•	Processor: Intel i5 (7th Gen) or higher / AMD equivalent
•	RAM: 8 GB or higher
•	Storage: Minimum 500 MB of free space for source code and dependencies
•	Operating System: Windows 10/11, macOS, or Linux
•	Graphics Card: Integrated or dedicated GPU (optional but recommended)

2. Software Requirements
Client-Side (Android Device):
•	Android OS Version: Android 8.0 (Oreo) or higher
•	App Package: ArcSight Defence APK
•	Permissions: 
o	Internet Access (for API communication)
o	Storage Access (for caching and saving data)
o	Location Access (if needed for location-based recommendations)
Server-Side (Backend and Model Hosting):
•	Python Version: 3.8 or higher
•	Flask Framework: For creating Flask APIs
•	Machine Learning Libraries:
o	Scikit-learn
o	NumPy
o	Pandas
o	Pickle
•	Database:
o	SQL (for storing user login and session data)

Development Environment (PC/Laptop):
•	Integrated Development Environment (IDE):
o	VS Code (for backend and API development)
o	Android Studio (for frontend development)
o	Jupyter Notebook (for model training and testing)
•	Version Control:
o	Git (for code management)
•	API Testing Tools:
o	Postman (for testing API endpoints)

3. Network Requirements
•	Internet Connectivity: Stable and fast connection to access APIs and fetch data
•	Server Hosting: Reliable hosting for Flask APIs (e.g., AWS, Heroku, or local server)

Steps to Execute this project :

1.Install Android studio and include all the frontend files provided in the ArcSight Defence repository. Paste all the contents of the "Android Frontend" after creating a project in the Android Studio. path : " ..\ArcSight Defence\app\src\main"
2.After pulling the ML model, open the ML Model folder an open Jupyter Notebook into that file. After Opening the Jupyter Notebook, execute all the python notebook files and pickle the models. Althought all the pickle files are provided in the ML models file, just to be safe repickle the model to reduce the compatibility issues.
3.Now after pulling the API folder, paste all the  ".pkl" and the datasets into the files into that API folder. Now open the API files into the VS code editior, Pycharm or any other code editor which you like and run the API Files.
4.After executing the API files, your API will be active and ready to communicate with your frontend. Now just open the Android Studio and then just run the project of yours in an android device emulator given by android studio itself.
(Note : if you want export the apk of the android app and want to run the app on your android device, you can just host the API by using any API hosting sevice or if you have your own server, just change the API link from localhost to global host that your APP will be able to communicate remotely with the server.Also the dataset include all the information regarding the guns and sensitive information as well, so misuse of this data could lead to legal action.)

This APP is Open Source yet sensitive as this app include information regarding all the guns and equipements used, it is free for everyone to add their own touch to the app and if anyone want to work with me for making this APP more efficient, you can just mail me. 
Mail : sarveshvarode545@gmail.com

I will look forward for your mail. 
Happy Coding :)
