import axios from 'axios';
import type { ChatMessage, Incident, Notification, AgentProfile, TestEmailPayload } from '../types';

const INCIDENT_API = import.meta.env.VITE_INCIDENT_API ?? 'http://localhost:8080';
const NOTIFICATION_API = import.meta.env.VITE_NOTIFICATION_API ?? 'http://localhost:8081';
const SENTIMENT_API = import.meta.env.VITE_SENTIMENT_API ?? 'http://localhost:8082';

export const incidentClient = axios.create({
  baseURL: INCIDENT_API,
  withCredentials: true,
});

export const notificationClient = axios.create({
  baseURL: NOTIFICATION_API,
  withCredentials: true,
});

export const sentimentClient = axios.create({
  baseURL: SENTIMENT_API,
  withCredentials: true,
});

export async function fetchIncident(incidentId: string): Promise<Incident> {
  const response = await incidentClient.get<Incident>(`/incident/${incidentId}`);
  return response.data;
}

export async function sendUserMessage(message: ChatMessage) {
  return incidentClient.post('/botMessage', {
    incidentId: message.incidentId,
    senderType: message.senderType,
    messageBody: message.messageBody,
  });
}

export async function fetchNotifications(): Promise<Notification[]> {
  const response = await notificationClient.get<Notification[]>('/notifications');
  return response.data;
}

export async function acceptChat(incidentId: string) {
  return notificationClient.post(
    '/acceptChat',
    { incidentId },
    { withCredentials: true }
  );
}

export async function fetchAgentProfile(): Promise<AgentProfile> {
  const response = await notificationClient.get<AgentProfile>('/me');
  return response.data;
}

export async function sendTestEmail(payload: TestEmailPayload) {
  return incidentClient.post('/test-email', payload);
}
