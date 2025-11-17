import { useState, useEffect, useRef } from 'react';
import './css/App.css';
import GameControls from './components/GameControls.jsx';
import Map from './components/Map.jsx';
import Legend from './components/Legend.jsx';
import Modal from './components/Modal.jsx';
import TraderUI from './components/TraderUI.jsx';
import BrainUI from './components/BrainUI.jsx';
import InventoryUI from './components/InventoryUI.jsx';

const App = () => {
  const [gameState, setGameState] = useState(null);
  
  //**Fetching Data
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

  // todo:
  // add fetch playerInventory
  // add fetch brain hint answer
  // -----------------------------------------------------------

  //**Inventory functionality
  // prevent deselect of inventory item when clicking on map or arrows
  const mapRef = useRef();
  const arrowRef = useRef();
  
  //**Modal Functionality
  const [isTraderModalOpen, setIsTraderModalOpen] = useState(false); 
  const openTraderModal = () => setIsTraderModalOpen(true);
  const closeTraderModal = () => setIsTraderModalOpen(false);

  const [isBrainModalOpen, setIsBrainModalOpen] = useState(false);
  const openBrainModal = () => setIsBrainModalOpen(true);
  const closeBrainModal = () => setIsBrainModalOpen(false);

  // check if any modals are open
  const anyModalsOpen = () => {
    if (isTraderModalOpen 
      || isBrainModalOpen) {
      return true;
    }

    return false;
  }
  
  // disable scrolling when modals are open
  useEffect(() => {
    if (anyModalsOpen()) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "auto";
    }

    return () => {
      document.body.style.overflow = "auto";
    };
  }, [anyModalsOpen]);

  return (
    <div className="flex flex-col items-center justify-center pt-40 font-mono pb-10">
      <h1 className="text-5xl font-bold mb-8 -translate-x-20">WSS</h1>

      <div className="flex flex-row items-center justify-center gap-10">
        <div className="flex flex-row items-center justify-center -translate-x-5">
          <InventoryUI mapRef={mapRef} arrowRef={arrowRef} />
          <Map ref={mapRef} gameState={gameState} />
        </div>

        <GameControls ref={arrowRef} move={move} />
      </div>

      <div>
        <Legend />
      </div>

      {/* Test buttons for Modals*/}
      <div className="fixed right-8 flex flex-col gap-2 p-6 -translate-y-20">
        <div className="text-center">Dev buttons</div>
        <button
          onClick={openTraderModal}
          className="dev-button"
        >
          Open Trader UI 
        </button>
        <button
          onClick={openBrainModal}
          className="dev-button"
        >
          Open Brain UI 
        </button>
      </div>

      {/* Player Inventory */}
      
      
      {/* Modals */}
      <Modal isOpen={isTraderModalOpen} onClose={closeTraderModal}>
        <TraderUI />
      </Modal>
      <Modal isOpen={isBrainModalOpen} onClose={closeBrainModal}>
        <BrainUI />
      </Modal>
    </div>
  );
};

export default App;
