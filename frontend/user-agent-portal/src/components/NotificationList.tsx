import type { Notification } from '../types';

interface Props {
  notifications: Notification[];
  onSelect: (notification: Notification) => void;
}

function NotificationList({ notifications, onSelect }: Props) {
  return (
    <div className="notifications">
      <h3>Escalations</h3>
      {notifications.length === 0 && <p>No pending notifications.</p>}
      <ul>
        {notifications.map((notification) => (
          <li key={notification.id}>
            <button type="button" onClick={() => onSelect(notification)}>
              <span className="incident">Incident {notification.incidentId}</span>
              <span className="summary">{notification.summary}</span>
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default NotificationList;
