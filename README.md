# 🤖 AI FAQ Assistant

An **AI-powered FAQ web application** that helps businesses answer customer queries instantly using LLMs.  
Built with **Flask (Python)** for the backend and **React + Tailwind** for the frontend.

---

##  Features
-  Upload FAQs via CSV (`/api/upload`)
-  Chat UI powered by `/api/ask` (LLM)
-  Structured backend + React UI
-  Supports **Gemini** or **OpenAI** models
-  SQLite database (swappable, production-ready)
-  Deployment-ready for Vercel (frontend) + Render (backend)

---

##  Tech Stack
- **Frontend:** React + Tailwind CSS  
- **Backend:** Flask (Python)  
- **Database:** SQLite (default, scalable to Postgres/MySQL)  
- **LLMs:** Gemini / OpenAI (via API key)  
- **Deployment:** Vercel (frontend) + Render (backend)

---

##  Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/AXsavag/ai-faq-app.git
cd ai-faq-app

cd backend
pip install -r requirements.txt
cp .env.example .env   # Add your LLM API key here
python run.py

cd frontend
npm install
npm start

cd backend
pytest

