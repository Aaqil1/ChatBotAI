import { useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import ChatWindow from '../components/ChatWindow.tsx';
import { fetchIncident } from '../services/api';
import type { Incident } from '../types';

function UserChatPage() {
  const [searchParams] = useSearchParams();
  const incidentId = useMemo(() => searchParams.get('incidentId') ?? '', [searchParams]);
  const [incident, setIncident] = useState<Incident | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!incidentId) {
      setError('Invalid or missing incident identifier.');
      return;
    }
    setLoading(true);
    fetchIncident(incidentId)
      .then((data) => {
        setIncident(data);
        setError(null);
      })
      .catch(() => setError('Unable to load conversation for this incident.'))
      .finally(() => setLoading(false));
  }, [incidentId]);

  if (!incidentId) {
    return (
      <main className="page">
        <div className="empty-state">
          <h1>Invalid link</h1>
          <p>The incident reference is missing. Please use the link provided in the email invitation.</p>
        </div>
      </main>
    );
  }

  if (loading) {
    return (
      <main className="page loading">
        <p>Loading your conversation...</p>
      </main>
    );
  }

  if (error) {
    return (
      <main className="page error">
        <p>{error}</p>
      </main>
    );
  }

  return (
    <main className="page chat">
      {incident && (
        <ChatWindow incidentId={incident.id} history={incident.messages} senderType="USER" />
      )}
    </main>
  );
}

export default UserChatPage;
