import React, { useState } from 'react';
import ChatBox from './components/ChatBox';
import 'bootstrap/dist/css/bootstrap.min.css';


function App() {
  const [user, setUser] = useState('');
  const [loggedIn, setLoggedIn] = useState(false);

  const handleLogin = () => {
    if (user.trim()) {
      setLoggedIn(true);
    }
  };

  if (!loggedIn) {
    return (
      <div style={{ padding: 50 }}>
        <input
          placeholder="Enter your name"
          value={user}
          onChange={e => setUser(e.target.value)}
        />
        <button onClick={handleLogin}>Join Chat</button>
      </div>
    );
  }

  return <ChatBox user={user} />;
}

export default App;
