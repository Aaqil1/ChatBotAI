import { useState } from 'react';
import type { FormEvent } from 'react';

interface Props {
  onSubmit: (username: string, password: string) => Promise<void>;
}

function AgentLoginForm({ onSubmit }: Props) {
  const [username, setUsername] = useState('agent@example.com');
  const [password, setPassword] = useState('password');
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    try {
      await onSubmit(username, password);
      setError(null);
    } catch (err) {
      setError('Login failed. Check credentials.');
    }
  };

  return (
    <form className="login-form" onSubmit={handleSubmit}>
      <h2>Agent Login</h2>
      <label>
        Email
        <input value={username} onChange={(event) => setUsername(event.target.value)} type="email" required />
      </label>
      <label>
        Password
        <input value={password} onChange={(event) => setPassword(event.target.value)} type="password" required />
      </label>
      {error && <p className="error">{error}</p>}
      <button type="submit">Sign in</button>
    </form>
  );
}

export default AgentLoginForm;
