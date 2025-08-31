import os
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import google.generativeai as genai
import openai

# --- Globals ---
faq_df = None
vectorizer = None
faq_vectors = None

# --- FAQ Loading and Searching ---
def load_faq(filepath):
    """Loads FAQ data from a CSV and prepares it for searching."""
    global faq_df, vectorizer, faq_vectors
    faq_df = pd.read_csv(filepath)
    vectorizer = TfidfVectorizer()
    faq_vectors = vectorizer.fit_transform(faq_df['question'])

def find_similar_question(user_question):
    """Finds the most similar question from the FAQ."""
    if faq_df is None:
        return None
    user_question_vector = vectorizer.transform([user_question])
    similarities = cosine_similarity(user_question_vector, faq_vectors)
    most_similar_index = similarities.argmax()
    return faq_df.iloc[most_similar_index]

# --- LLM Interaction ---
def get_llm_provider():
    """Determines which LLM provider to use based on environment variables."""
    if os.getenv("GEMINI_API_KEY"):
        return "gemini"
    elif os.getenv("OPENAI_API_KEY"):
        return "openai"
    else:
        return "demo"

def ask(question):
    """Answers a user's question using the best available method."""
    provider = get_llm_provider()

    if provider == "demo":
        return "This is a demo answer. Please provide a GEMINI_API_KEY or OPENAI_API_KEY to get a real answer."

    # Find relevant context from FAQ
    similar_question = find_similar_question(question)
    context = ""
    if similar_question is not None:
        context = f"Context: The user might be asking about '{similar_question['question']}'. The answer is: {similar_question['answer']}"

    # Build the prompt
    prompt = f"""
    You are an FAQ assistant for a small business.
    Answer the user's question based on the provided context.
    If the context is not relevant, answer the question based on your general knowledge, but keep it concise.

    {context}

    User question: {question}
    """

    if provider == "gemini":
        genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
        model = genai.GenerativeModel('gemini-pro')
        response = model.generate_content(prompt)
        return response.text

    elif provider == "openai":
        openai.api_key = os.getenv("OPENAI_API_KEY")
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a helpful FAQ assistant."},
                {"role": "user", "content": prompt}
            ]
        )
        return response.choices[0].message['content']

    return "Something went wrong."
