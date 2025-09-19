import type { ChatMessage, SenderType } from '../types';

interface MessageListProps {
  messages: ChatMessage[];
  perspective: SenderType;
}

const prettyTime = (value?: string) => (value ? new Date(value).toLocaleTimeString() : '');

function MessageList({ messages, perspective }: MessageListProps) {
  return (
    <div className="message-list">
      {messages.map((message) => {
        const isOwn = perspective === message.senderType;
        return (
          <div key={`${message.timestamp}-${message.messageBody}`} className={`message ${isOwn ? 'own' : ''}`}>
            <div className="meta">
              <span className="sender">{message.senderType}</span>
              <span className="time">{prettyTime(message.timestamp)}</span>
              {typeof message.sentimentScore === 'number' && (
                <span className="sentiment">Sentiment: {message.sentimentScore.toFixed(2)}</span>
              )}
            </div>
            <p>{message.messageBody}</p>
          </div>
        );
      })}
    </div>
  );
}

export default MessageList;
