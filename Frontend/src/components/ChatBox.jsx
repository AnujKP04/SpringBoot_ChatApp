import React, { useEffect, useRef, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient = null;

function ChatBox({ user }) {
  const [receiver, setReceiver] = useState('');
  const [chat, setChat] = useState([]);
  const [message, setMessage] = useState('');
  const chatBoxRef = useRef(null);

  // Connect to WebSocket and subscribe
  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws?user=' + user);

    stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('Connected to WebSocket as', user);

        stompClient.subscribe('/user/queue/messages', (msg) => {
          const body = JSON.parse(msg.body);
          console.log('Incoming message:', body);
          setChat(prev => [...prev, body]);
        });
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame.headers['message']);
      }
    });

    stompClient.activate();

    return () => {
      if (stompClient && stompClient.active) {
        stompClient.deactivate();
      }
    };
  }, [user]);

  useEffect(() => {
    if (receiver.trim()) {
      fetch(`http://localhost:8080/message?sender=${user}&receiver=${receiver}`)
        .then(res => res.json())
        .then(data => {
          setChat(data); 
        })
        .catch(err => console.error('Failed to load chat history', err));
    }
  }, [receiver]);

  const sendMessage = () => {
    if (!receiver || !message.trim()) return;

    const msg = {
      sender: user,
      receiver,
      content: message,
      timestamp: new Date().toISOString()
    };

    stompClient.publish({
      destination: '/app/chat',
      body: JSON.stringify(msg)
    });

    setChat(prev => [...prev, msg]);
    setMessage('');
  };

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
  }, [chat]);

  return (
    <div className="container d-flex justify-content-center align-items-center mt-5">
      <div className="card shadow-sm" style={{ width: "400px" }}>
        <div className="card-body">
          <h3 className="mb-4 text-center">Welcome, {user}</h3>

          <input
            value={receiver}
            onChange={e => setReceiver(e.target.value)}
            placeholder="Receiver name"
            className="form-control mb-3"
          />

          <div
            ref={chatBoxRef}
            className="border rounded bg-light p-3 mb-3"
            style={{ height: '300px', overflowY: 'scroll' }}
          >
            {chat.map((msg, i) => (
              <div
                key={i}
                className={`d-flex mb-2 ${msg.sender === user ? 'justify-content-end' : 'justify-content-start'}`}
              >
                <div
                  className={`p-2 rounded shadow-sm ${msg.sender === user ? 'bg-success text-white' : 'bg-white'}`}
                  style={{ maxWidth: '70%' }}
                >
                  <strong>{msg.sender}</strong>
                  <div>{msg.content}</div>
                  <small className="text-muted d-block text-end">
                    {new Date(msg.timestamp).toLocaleTimeString()}
                  </small>
                </div>
              </div>
            ))}
          </div>

          <div className="input-group">
            <input
              value={message}
              onChange={e => setMessage(e.target.value)}
              placeholder="Type a message"
              className="form-control"
            />
            <button onClick={sendMessage} className="btn btn-primary">
              Send
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChatBox;
