import React, { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
  const [question, setQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [history, setHistory] = useState([]);
  const [file, setFile] = useState(null);

  const handleAsk = async () => {
    try {
      const res = await axios.post(`${process.env.REACT_APP_BACKEND_API_URL}/ask`, { question });
      setAnswer(res.data.answer);
      loadHistory();
    } catch (error) {
      console.error('Error asking question:', error);
      setAnswer('Error: Could not get an answer.');
    }
  };

  const handleFileUpload = async () => {
    if (!file) {
      alert('Please select a file to upload.');
      return;
    }
    const formData = new FormData();
    formData.append('file', file);

    try {
      await axios.post(`${process.env.REACT_APP_BACKEND_API_URL}/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      alert('File uploaded successfully!');
    } catch (error) {
      console.error('Error uploading file:', error);
      alert('Error uploading file.');
    }
  };

  const loadHistory = async () => {
    try {
      const res = await axios.get(`${process.env.REACT_APP_BACKEND_API_URL}/history`);
      setHistory(res.data);
    } catch (error) {
      console.error('Error loading history:', error);
    }
  };

  useEffect(() => {
    loadHistory();
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col items-center p-4">
      <div className="w-full max-w-4xl bg-white rounded-lg shadow-md p-6">
        <h1 className="text-3xl font-bold text-center mb-6">AI FAQ Assistant</h1>

        {/* File Upload */}
        <div className="mb-6 p-4 border rounded-lg">
          <h2 className="text-xl font-semibold mb-2">Upload FAQ CSV</h2>
          <input type="file" onChange={(e) => setFile(e.target.files[0])} className="mb-2"/>
          <button onClick={handleFileUpload} className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600">
            Upload
          </button>
        </div>

        {/* Chat Box */}
        <div className="mb-6 p-4 border rounded-lg">
          <h2 className="text-xl font-semibold mb-2">Ask a Question</h2>
          <div className="flex">
            <input
              type="text"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              className="flex-grow p-2 border rounded-l-lg"
              placeholder="Type your question here..."
            />
            <button onClick={handleAsk} className="bg-green-500 text-white px-4 py-2 rounded-r-lg hover:bg-green-600">
              Ask
            </button>
          </div>
          {answer && (
            <div className="mt-4 p-4 bg-gray-100 rounded-lg">
              <p className="font-semibold">Answer:</p>
              <p>{answer}</p>
            </div>
          )}
        </div>

        {/* History View */}
        <div className="p-4 border rounded-lg">
          <h2 className="text-xl font-semibold mb-2">Q&A History</h2>
          <div className="space-y-4">
            {history.map((item) => (
              <div key={item.id} className="p-4 bg-gray-50 rounded-lg">
                <p className="font-semibold text-gray-700">Q: {item.question}</p>
                <p className="text-gray-600">A: {item.answer}</p>
                <p className="text-xs text-gray-400 text-right">{new Date(item.timestamp).toLocaleString()}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
