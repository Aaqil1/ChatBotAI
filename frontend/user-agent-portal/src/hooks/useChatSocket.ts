import { Client } from '@stomp/stompjs';
import type { IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useEffect, useRef } from 'react';
import type { ChatMessage } from '../types';

interface Options {
  incidentId: string;
  onMessage: (message: ChatMessage) => void;
  senderType: string;
}

const INCIDENT_API = import.meta.env.VITE_INCIDENT_API ?? 'http://localhost:8080';

export function useChatSocket({ incidentId, onMessage, senderType }: Options) {
  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    if (!incidentId) {
      return;
    }

    const socket = new SockJS(`${INCIDENT_API}/ws-chat`);
    const client = new Client({
      webSocketFactory: () => socket as unknown as WebSocket,
      reconnectDelay: 5000,
      debug: () => undefined,
    });

    client.onConnect = () => {
      client.subscribe(`/topic/incident/${incidentId}`, (message: IMessage) => {
        try {
          const payload = JSON.parse(message.body) as ChatMessage;
          onMessage(payload);
        } catch (error) {
          console.error('Failed to parse message', error);
        }
      });
    };

    client.activate();
    clientRef.current = client;

    return () => {
      client.deactivate();
      clientRef.current = null;
    };
  }, [incidentId, onMessage, senderType]);

  const sendMessage = (message: Omit<ChatMessage, 'id'>) => {
    if (!clientRef.current || !clientRef.current.connected) {
      return;
    }
    clientRef.current.publish({
      destination: `/app/incident/${incidentId}`,
      body: JSON.stringify(message),
    });
  };

  return { sendMessage };
}
