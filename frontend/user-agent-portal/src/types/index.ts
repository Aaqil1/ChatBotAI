export type SenderType = 'USER' | 'BOT' | 'AGENT';

export interface ChatMessage {
  id?: string;
  incidentId: string;
  senderType: SenderType;
  messageBody: string;
  sentimentScore?: number;
  timestamp?: string;
}

export interface Incident {
  id: string;
  email: string;
  status: 'OPEN' | 'ESCALATED' | 'CLOSED';
  priority: string;
  createdAt: string;
  updatedAt: string;
  messages: ChatMessage[];
}

export interface Notification {
  id: string;
  incidentId: string;
  summary: string;
  type: string;
  acknowledged: boolean;
  createdAt: string;
  acknowledgedAt?: string;
}

export interface AgentProfile {
  id: string;
  name: string;
  email: string;
  assignedIncidents: string[];
}

export interface TestEmailPayload {
  from: string;
  subject: string;
  body: string;
}
