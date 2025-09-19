import { useState } from 'react';
import type { FormEvent } from 'react';

interface MessageInputProps {
  onSubmit: (message: string) => void | Promise<void>;
  disabled?: boolean;
}

function MessageInput({ onSubmit, disabled }: MessageInputProps) {
  const [value, setValue] = useState('');

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    if (!value.trim()) {
      return;
    }
    await onSubmit(value.trim());
    setValue('');
  };

  return (
    <form className="message-input" onSubmit={handleSubmit}>
      <input
        type="text"
        value={value}
        disabled={disabled}
        placeholder="Type your message..."
        onChange={(event) => setValue(event.target.value)}
      />
      <button type="submit" disabled={disabled || !value.trim()}>
        Send
      </button>
    </form>
  );
}

export default MessageInput;
