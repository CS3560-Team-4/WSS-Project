import { useState, useEffect } from 'react';
import './App.css';
import GameControls from './components/GameControls.jsx';
import Map from './components/Map.jsx';

const App = () => {
  const [gameState, setGameState] = useState(null);

  const fetchMapData = async () => {
    try {
      const response = await fetch('http://localhost:8080/state');
      const data = await response.json();

      setGameState(data);

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
    } catch (err) {
      console.log(`Error fetching map state: ${err}`);
    }
  }

  useEffect(() => {
    fetchMapData();
  }, []);

  const move = async (direction) => {
    try {
      const response = await fetch('http://localhost:8080/move', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ direction }),
      });

      const data = await response.json();
      setGameState(data);

    } catch (err) {
      console.error(`Error moving player: ${err}`);
    }
  }

  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="text-5xl font-bold mb-8 -translate-x-20">WSS Map</h1>

      <div className="flex flex-row items-center justify-center gap-10">
        <Map gameState={gameState} />

        <GameControls move={move} />
      </div>
    </div>
  )
};

export default App;
