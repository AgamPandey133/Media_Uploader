📷 MediaUploader – Android + Flask App  

A full-stack Android application that allows users to select, upload, and view images/videos using a custom Flask backend. Designed for performance, clarity, and resume-quality presentation.  



🚀 Features :   

✅ Upload multiple images/videos from your Android device  

✅ View uploaded media in a scrollable grid  

✅ Media files stored on a custom Flask server  

✅ Thumbnail support for videos  

✅ Toast feedback for upload status  

✅ Full-screen image/video preview on tap  

✅ Clean, modular code structure using Kotlin and OkHttp  

✅ Delete functionality (under development)  




📱 Android Tech Stack :  

Language: Kotlin  

UI: RecyclerView, Glide  

Networking: OkHttp  

Other: Content Resolver, FilePicker, Toasts  



🖥️ Backend (Flask) :  

Language: Python  

Framework: Flask  

Modules: Flask-CORS, Werkzeug  

Endpoints:  

/upload – Handle uploads (POST)  

/media – List uploaded files (GET)  

/media/<filename> – Fetch file (GET)  

/delete – Delete selected files (POST)   



🔧 Setup Instructions :  


📲 Android Side-   

Clone this repo or download ZIP.  

Open in Android Studio.  

Replace IP http://192.168.X.X:5000 with your local machine IP (same Wi-Fi).  

Connect a real Android device.  

Run the app.  


💻 Flask Backend-
bash   

git clone https://github.com/YOUR_USERNAME/MediaUploader.git  

cd MediaUploader  

python -m venv venv  

venv\Scripts\activate        # or source venv/bin/activate on macOS/Linux  

pip install -r requirements.txt  

python app.py  



📁 Project Structure  

bash  

MediaUploader/  

│
├── app/                     # Android App Code  

│   ├── MainActivity.kt  

│   ├── MediaViewActivity.kt  

│   ├── MediaAdapter.kt  

│   └── ...  

│
├── app.py                   # Flask backend  

├── utils.py                 # Helper to check valid file types  

├── requirements.txt         # Flask dependencies  

└── media/                   # Uploaded files directory    




📌 Planned Features  


✅ Basic delete functionality  

🔒 Authentication (Login for admin/user roles)  

☁️ Hosting Flask backend on Render/Vercel or Railway
