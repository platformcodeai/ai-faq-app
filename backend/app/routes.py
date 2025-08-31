from flask import request, jsonify, Blueprint
import os
import pandas as pd
from . import llm, db
from .models import QAPair

api = Blueprint('api', __name__)

@api.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'}), 400
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400
    if file and file.filename.endswith('.csv'):
        try:
            # Save the uploaded file
            upload_folder = os.path.join(os.getcwd(), 'backend', 'uploads')
            if not os.path.exists(upload_folder):
                os.makedirs(upload_folder)
            filepath = os.path.join(upload_folder, 'faq.csv')
            file.save(filepath)

            # Validate CSV
            df = pd.read_csv(filepath)
            if 'question' not in df.columns or 'answer' not in df.columns:
                return jsonify({'error': 'CSV must have "question" and "answer" columns'}), 400

            # Load data for searching
            llm.load_faq(filepath)

            return jsonify({'message': 'File uploaded and processed successfully'}), 200
        except Exception as e:
            return jsonify({'error': str(e)}), 500
    else:
        return jsonify({'error': 'Invalid file type, please upload a CSV'}), 400

@api.route('/ask', methods=['POST'])
def ask_question():
    data = request.get_json()
    if not data or 'question' not in data:
        return jsonify({'error': 'Question not provided'}), 400

    question = data['question']
    # Sanitize input
    if len(question) > 500:
        return jsonify({'error': 'Question is too long'}), 400

    try:
        answer = llm.ask(question)

        # Save Q&A to database
        qa_pair = QAPair(question=question, answer=answer)
        db.session.add(qa_pair)
        db.session.commit()

        return jsonify({'answer': answer})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@api.route('/history', methods=['GET'])
def get_history():
    try:
        history = QAPair.query.order_by(QAPair.timestamp.desc()).all()
        return jsonify([item.to_dict() for item in history])
    except Exception as e:
        return jsonify({'error': str(e)}), 500
