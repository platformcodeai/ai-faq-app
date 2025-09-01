import unittest
import os
import sys
import json
import time
from unittest.mock import patch

# Add the project root to the python path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app import create_app, db
from app.models import QAPair

class AppTestCase(unittest.TestCase):
    def setUp(self):
        self.app = create_app()
        self.app.config['TESTING'] = True
        self.app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///:memory:'
        self.client = self.app.test_client()

        with self.app.app_context():
            db.create_all()

    def tearDown(self):
        with self.app.app_context():
            db.session.remove()
            db.drop_all()

    def test_upload_csv(self):
        # Create a dummy CSV file
        csv_content = "question,answer\nWhat is your name?,My name is AI Assistant."
        with open("test.csv", "w") as f:
            f.write(csv_content)

        with open("test.csv", "rb") as f:
            response = self.client.post('/api/upload', data={'file': (f, 'test.csv')})

        self.assertEqual(response.status_code, 200)
        self.assertIn('File uploaded and processed successfully', response.get_data(as_text=True))

        os.remove("test.csv")

    @patch('app.llm.ask')
    def test_ask_question(self, mock_ask):
        mock_ask.return_value = "This is a mock answer."

        response = self.client.post('/api/ask', json={'question': 'What is your name?'})

        self.assertEqual(response.status_code, 200)
        data = json.loads(response.get_data(as_text=True))
        self.assertEqual(data['answer'], "This is a mock answer.")

        # Check if the Q&A pair was saved to the database
        with self.app.app_context():
            qa_pair = QAPair.query.first()
            self.assertIsNotNone(qa_pair)
            self.assertEqual(qa_pair.question, 'What is your name?')
            self.assertEqual(qa_pair.answer, 'This is a mock answer.')

    def test_get_history(self):
        # Add some data to the database
        with self.app.app_context():
            qa1 = QAPair(question="Q1", answer="A1")
            db.session.add(qa1)
            db.session.commit()

            time.sleep(0.1) # Ensure timestamps are different

            qa2 = QAPair(question="Q2", answer="A2")
            db.session.add(qa2)
            db.session.commit()

        response = self.client.get('/api/history')
        self.assertEqual(response.status_code, 200)
        data = json.loads(response.get_data(as_text=True))
        self.assertEqual(len(data), 2)
        self.assertEqual(data[0]['question'], 'Q2') # Ordered by desc timestamp

if __name__ == '__main__':
    unittest.main()
