@app.route("/")
def home():
    return {"message": "Backend is running!"}