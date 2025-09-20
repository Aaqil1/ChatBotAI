import { useState } from 'react';
import type { FormEvent } from 'react';

import { sendTestEmail } from '../services/api.ts';

const DEFAULT_FROM = 'user@example.com';

function EmailSimulationPage() {
  const [from, setFrom] = useState(DEFAULT_FROM);
  const [subject, setSubject] = useState('Need help with my account');
  const [body, setBody] = useState('Hi team, I cannot access my workspace.');
  const [status, setStatus] = useState<'idle' | 'sending' | 'success' | 'error'>('idle');
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setStatus('sending');
    setError(null);
    try {
      await sendTestEmail({ from, subject, body });
      setStatus('success');
    } catch (err) {
      setStatus('error');
      setError('Failed to send email. Please try again.');
    }
  };

  return (
    <div className="layout">
      <div className="card email-sim-card">
        <h1>Test Email Generator</h1>
        <p className="muted">
          Send a sample email into the fake SMTP inbox. The incident service will pick it up, create an
          incident, and reply with a chat link.
        </p>
        <form onSubmit={handleSubmit} className="form-grid">
          <label htmlFor="from">From</label>
          <input
            id="from"
            name="from"
            type="email"
            value={from}
            onChange={(event) => setFrom(event.target.value)}
            required
            placeholder="you@example.com"
          />

          <label htmlFor="subject">Subject</label>
          <input
            id="subject"
            name="subject"
            value={subject}
            onChange={(event) => setSubject(event.target.value)}
            required
            placeholder="Subject"
          />

          <label htmlFor="body">Body</label>
          <textarea
            id="body"
            name="body"
            rows={6}
            value={body}
            onChange={(event) => setBody(event.target.value)}
            required
          />

          <button type="submit" className="primary" disabled={status === 'sending'}>
            {status === 'sending' ? 'Sending…' : 'Send Email'}
          </button>
        </form>
        {status === 'success' && (
          <p className="success">Email sent! Check the incident portal for the new conversation.</p>
        )}
        {status === 'error' && <p className="error-text">{error}</p>}
      </div>
    </div>
  );
}

export default EmailSimulationPage;
