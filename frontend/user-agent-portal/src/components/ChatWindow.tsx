import { useCallback, useEffect, useMemo, useState } from 'react';
import type { ChatMessage, SenderType } from '../types';
import { useChatSocket } from '../hooks/useChatSocket';
import { sendUserMessage } from '../services/api';
import MessageList from './MessageList.tsx';
import MessageInput from './MessageInput.tsx';

interface ChatWindowProps {
  incidentId: string;
  history: ChatMessage[];
  senderType: SenderType;
  disabled?: boolean;
}

const sortByTimestamp = (messages: ChatMessage[]) =>
  [...messages].sort((a, b) => new Date(a.timestamp ?? 0).getTime() - new Date(b.timestamp ?? 0).getTime());

function ChatWindow({ incidentId, history, senderType, disabled }: ChatWindowProps) {
  const [messages, setMessages] = useState<ChatMessage[]>(() => sortByTimestamp(history));

  useEffect(() => {
    setMessages(sortByTimestamp(history));
  }, [history]);

  const handleIncoming = useCallback((message: ChatMessage) => {
    setMessages((prev) => sortByTimestamp([...prev, message]));
  }, []);

  const { sendMessage } = useChatSocket({ incidentId, onMessage: handleIncoming, senderType });

  const onSubmit = async (body: string) => {
    const payload: ChatMessage = {
      incidentId,
      senderType,
      messageBody: body,
    };
    handleIncoming({ ...payload, timestamp: new Date().toISOString() });
    if (senderType === 'USER') {
      await sendUserMessage(payload);
    } else {
      sendMessage(payload);
    }
  };

  const title = useMemo(() => (senderType === 'USER' ? 'Support Assistant' : 'Agent Chat Console'), [senderType]);

  return (
    <div className="chat-window">
      <header>
        <h2>{title}</h2>
        <span className="incident-label">Incident: {incidentId}</span>
      </header>
      <MessageList messages={messages} perspective={senderType} />
      <MessageInput onSubmit={onSubmit} disabled={disabled} />
    </div>
  );
}

export default ChatWindow;
