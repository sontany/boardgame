import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import GameStart from './pages/GameStart';
import GamePlay from './pages/GamePlay';
import GameComplete from './pages/GameComplete';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<GameStart />} />
          <Route path="/game/:gameId" element={<GamePlay />} />
          <Route path="/game/:gameId/complete" element={<GameComplete />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
