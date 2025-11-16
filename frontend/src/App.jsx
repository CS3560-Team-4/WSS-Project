import { useState, useEffect } from 'react';
import './css/App.css';
import GameControls from './components/GameControls.jsx';
import Map from './components/Map.jsx';
import Legend from './components/Legend.jsx';
import Modal from './components/Modal.jsx';
import TraderUI from './components/TraderUI.jsx';

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
  
  // Modal Functionality
  const [isModalOpen, setIsModalOpen] = useState(false); 
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  // disable scrolling when modals are open
  useEffect(() => {
    if (isModalOpen) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "auto";
    }

    return () => {
      document.body.style.overflow = "auto";
    };
  }, [isModalOpen]);

  return (
    <div className="flex flex-col items-center justify-center pt-40 font-mono pb-10">
      <h1 className="text-5xl font-bold mb-8 -translate-x-20 pt-20">WSS Map</h1>

      <div className="flex flex-row items-center justify-center gap-10">
        <Map gameState={gameState} />

        <GameControls move={move} />
      </div>

      <div>
        <Legend />
      </div>

      {/* Test buttons for Modals*/}
      <div className="p-6">
        <button
          onClick={openModal}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg cursor-pointer"
        >
          Open Trader UI 
        </button>
      </div>
      
      {/* Modals */}
      <div>
        <Modal isOpen={isModalOpen} onClose={closeModal}>
          <TraderUI />
        </Modal>
      </div>
    </div>
  );
};

export default App;
