import { render, screen } from '@testing-library/react';
import App from './App';

test('renders AI FAQ Assistant title', () => {
  render(<App />);
  const titleElement = screen.getByText(/AI FAQ Assistant/i);
  expect(titleElement).toBeInTheDocument();
});
