import { useCallback, useEffect, useMemo, useState } from 'react';
import AgentLoginForm from '../components/AgentLoginForm.tsx';
import ChatWindow from '../components/ChatWindow.tsx';
import IncidentSummaryCard from '../components/IncidentSummaryCard.tsx';
import NotificationList from '../components/NotificationList.tsx';
import { acceptChat, fetchAgentProfile, fetchIncident, fetchNotifications, notificationClient } from '../services/api';
import type { AgentProfile, Incident, Notification } from '../types';

function AgentDashboardPage() {
  const [profile, setProfile] = useState<AgentProfile | null>(null);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [selectedIncident, setSelectedIncident] = useState<Incident | null>(null);
  const [loadingIncident, setLoadingIncident] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const authenticated = useMemo(() => Boolean(profile), [profile]);

  const loadNotifications = useCallback(() => {
    fetchNotifications()
      .then((data) => setNotifications(data))
      .catch(() => setNotifications([]));
  }, []);

  useEffect(() => {
    if (!authenticated) {
      return;
    }
    loadNotifications();
    const interval = setInterval(loadNotifications, 10000);
    return () => clearInterval(interval);
  }, [authenticated, loadNotifications]);

  const handleLogin = async (username: string, password: string) => {
    const form = new URLSearchParams();
    form.append('username', username);
    form.append('password', password);
    await notificationClient.post('/login', form, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });
    const agent = await fetchAgentProfile();
    setProfile(agent);
  };

  const handleSelectNotification = async (notification: Notification) => {
    setLoadingIncident(true);
    try {
      const incident = await fetchIncident(notification.incidentId);
      setSelectedIncident(incident);
      setError(null);
      await acceptChat(notification.incidentId);
    } catch (err) {
      setError('Unable to load incident details.');
    } finally {
      setLoadingIncident(false);
    }
  };

  if (!authenticated) {
    return (
      <main className="page agent-login">
        <AgentLoginForm onSubmit={handleLogin} />
      </main>
    );
  }

  return (
    <main className="page agent-dashboard">
      <section className="sidebar">
        <div className="profile-card">
          <h2>Welcome, {profile?.name}</h2>
          <p>{profile?.email}</p>
          <p>Assigned incidents: {profile?.assignedIncidents.length ?? 0}</p>
        </div>
        <NotificationList notifications={notifications} onSelect={handleSelectNotification} />
      </section>
      <section className="workspace">
        <IncidentSummaryCard incident={selectedIncident ?? undefined} />
        {error && <p className="error">{error}</p>}
        {loadingIncident && <p>Loading incident details...</p>}
        {selectedIncident && (
          <ChatWindow incidentId={selectedIncident.id} history={selectedIncident.messages} senderType="AGENT" />
        )}
      </section>
    </main>
  );
}

export default AgentDashboardPage;
