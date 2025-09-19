import type { Incident } from '../types';

interface Props {
  incident?: Incident;
}

function IncidentSummaryCard({ incident }: Props) {
  if (!incident) {
    return (
      <div className="summary-card">
        <h3>Select an incident</h3>
        <p>Choose an incident from the list to view conversation history.</p>
      </div>
    );
  }

  return (
    <div className="summary-card">
      <h3>Incident Summary</h3>
      <p><strong>ID:</strong> {incident.id}</p>
      <p><strong>Status:</strong> {incident.status}</p>
      <p><strong>Priority:</strong> {incident.priority}</p>
      <p><strong>Email:</strong> {incident.email}</p>
      <p><strong>Created:</strong> {new Date(incident.createdAt).toLocaleString()}</p>
      <p><strong>Updated:</strong> {new Date(incident.updatedAt).toLocaleString()}</p>
    </div>
  );
}

export default IncidentSummaryCard;
