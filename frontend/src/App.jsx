import { useState, useEffect, useRef } from 'react';
import './css/App.css';
import GameControls from './components/GameControls.jsx';
import Map from './components/Map.jsx';
import Legend from './components/Legend.jsx';
import Modal from './components/Modal.jsx';
import TraderUI from './components/TraderUI.jsx';
import BrainUI from './components/BrainUI.jsx';
import StatsUI from './components/StatsUI.jsx';
import DeathScreen from './components/DeathScreen.jsx';
import WinScreen from './components/WinScreen.jsx';
import WelcomeScreen from './components/WelcomeScreen.jsx';

const App = () => {
  // Welcome screen
  const [isWelcomeModalOpen, setIsWelcomeModalOpen] = useState(true);
  const closeWelcomeModal = () => setIsWelcomeModalOpen(false);
  const openWelcomeModal = () => setIsWelcomeModalOpen(true);

  const [gameState, setGameState] = useState(null);
  const [gameLevel, setGameLevel] = useState(0);
  const [gameOver, setGameOver] = useState(false);
  
  //**Fetching Data
  const fetchMapData = async () => {
    try {
      const response = await fetch('http://localhost:8080/state');
      const data = await response.json();
      
      setGameState(data);
      console.log("Full game state: ", data);
      
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
    } catch (err) {
      console.log(`Error fetching map state: ${err}`);
    }
  }
  
  // initial map
  useEffect(() => {
    fetchMapData();
    openWelcomeModal();
  }, []);

  // refresh the page when user reloads page
  useEffect(() => {
    const hasSession = sessionStorage.getItem("hasGameSession");

    if (!hasSession) {
      resetGame();
      sessionStorage.setItem("hasGameSession", "true")
    }
  }, []);

  useEffect(() => {
    const clearSession = () => {
      sessionStorage.removeItem("hasGameSession");
    };

    window.addEventListener("beforeunload", clearSession);

    return () => window.removeEventListener("beforeunload", clearSession);
  }, []);

  // check for player last move direction
  useEffect(() => {
    if (!gameLoaded) return;

    if (gameState.player.health <= 0 || gameState.player.won) {
      setGameOver(true);
    }
  }, [gameState]);
  
  const [lastMoveDirection, setLastMoveDirection] = useState("right");

  const move = async (direction) => {
    if (gameOver) return;

    try {
      setLastMoveDirection(direction); // track last direction

      const response = await fetch('http://localhost:8080/move', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ direction }),
      });
      
      const data = await response.json();
      console.log("MOVE RESPONSE: ", data);
      setGameState(data);
      
    } catch (err) {
      console.error(`Error moving player: ${err}`);
    }
  }

  const [brainHint, setBrainHint] = useState(null);
  const [brainLoading, setBrainLoading] = useState(false);

  const balancedBrainMove = async () => {
    try {
      setBrainLoading(true); // start animation

      const response = await fetch('http://localhost:8080/balancedbrain', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      const data = await response.json();

      console.log(`Brain says: ${data}`);
      setBrainHint(data.brainMove);

      // artificial delay 
      setTimeout(() => {
        setBrainLoading(false);
      }, 850); // <-- adjust delay

    } catch (err) {
      console.log(`Error getting brain move: ${err}`);
    } 
  }

  const explorerBrainMove = async () => {
    try {
      setBrainLoading(true); // start animation

      const response = await fetch('http://localhost:8080/explorerbrain', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      const data = await response.json();

      console.log(`Brain says: ${data}`);
      setBrainHint(data.brainMove);

      // artificial delay 
      setTimeout(() => {
        setBrainLoading(false);
      }, 850); // <-- adjust delay

    } catch (err) {
      console.log(`Error getting brain move: ${err}`);
    } 
  }

  const greedyBrainMove = async () => {
    try {
      setBrainLoading(true); // start animation

      const response = await fetch('http://localhost:8080/greedybrain', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      const data = await response.json();

      console.log(`Brain says: ${data}`);
      setBrainHint(data.brainMove);

      // artificial delay 
      setTimeout(() => {
        setBrainLoading(false);
      }, 850); // <-- adjust delay

    } catch (err) {
      console.log(`Error getting brain move: ${err}`);
    } 
  }

  const resetGame = async () => {
    setGameOver(false);

    try {
      const response = await fetch('http://localhost:8080/reset', {
        method: 'POST',
        headers: { 'Content-Type' : 'application/json' }
      });

      const data = await response.json();
      setGameState(data);
      setGameLevel(data.level);

      // Clear hint if open
      setBrainHint(null);
    } catch (err) {
      console.log(`Error resetting game: ${err}`);
    }

  }

  const nextLevel = async () => {
    setGameOver(false);

    try {
      const response = await fetch('http://localhost:8080/nextlevel', {
        method: 'POST',
        headers: { 'Content-Type' : 'application/json' }
      });

      const data = await response.json();
      setGameState(data);
      setGameLevel(data.level);

      // Clear hint if open
      setBrainHint(null);
    } catch (err) {
      console.log(`Error getting next level: ${err}`);
    }
  }

  // todo:
  // add fetch playerInventory
  // -----------------------------------------------------------

  //**Inventory functionality
  // prevent deselect of inventory item when clicking on map or arrows
  const mapRef = useRef();
  const arrowRef = useRef();
  
  //**Modal Functionality
  const [isTraderModalOpen, setIsTraderModalOpen] = useState(false); 
  const openTraderModal = () => setIsTraderModalOpen(true);
  const closeTraderModal = () => setIsTraderModalOpen(false);

  // trade calls to backend endpoints
  const acceptTrade = async () => {
    const response = await fetch("http://localhost:8080/accepttrade", {
      method: "POST"
    });
    
    fetchMapData(); // refresh game state
    closeTraderModal();
  };

  const rejectTrade = async () => {
    await fetch ("http://localhost:8080/rejecttrade", {
      method: "POST"
    });
    closeTraderModal();
  }

  // check if game is loaded
  const [gameLoaded, setGameLoaded] = useState(false);
  useEffect(() => {
    if (gameState && gameState.player && !gameLoaded) {
      setGameLoaded(true);
    }
  }, [gameState]);
  
  // death screen
  const [isDeathScreenOpen, setIsDeathScreenOpen] = useState(false);
  const openDeathScreen = () => setIsDeathScreenOpen(true);
  const closeDeathScreen = () => setIsDeathScreenOpen(false);

  useEffect(() => {
    if (!gameLoaded) return;

    if (gameState.player.health <= 0) {
      openDeathScreen();
    }
  }, [gameState]);

  // win screen
  const [isWinScreenOpen, setIsWinScreenOpen] = useState(false);
  const openWinScreen = () => setIsWinScreenOpen(true);
  const closeWinScreen = () => setIsWinScreenOpen(false);

  useEffect(() => {
    if (!gameLoaded) return;

    if (gameState.player.won === true) {
      console.log("win flag: ", gameState?.player?.win);
      openWinScreen();
    }
  }, [gameState]);

  // brainUI
  const [isBrainModalOpen, setIsBrainModalOpen] = useState(false);
  const openBrainModal = () => {
    setBrainHint(null);
    setBrainLoading(false);
    setIsBrainModalOpen(true);
  }
  const closeBrainModal = () => setIsBrainModalOpen(false);

  //**General Modal Functionality
  // might delete since current UI does not scroll already
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

  // check for player in trader tiles
  useEffect(() => {
    if (!gameState) return;

    if (gameState.activeTrader) {
      setIsTraderModalOpen(true);
    }
  }, [gameState?.activeTrader]);

  return (
    <div className="flex flex-col items-center justify-center font-mono pb-10">
      <Legend />

      <div className="flex flex-row items-center justify-center gap-10">
        <div className="flex flex-col items-center justify-center">
          <Map ref={mapRef} gameState={gameState} lastMove={lastMoveDirection} />
          
          <StatsUI gameState={gameState} />
          <div className="mt-2">
            Level: {`${gameLevel}`}
          </div>
        </div>

        <div className={gameOver ? "pointer-events-none opacity-50" : ""}>
          <GameControls ref={arrowRef} move={move} />
        </div>
      </div>

      {/* Test buttons for Modals*/}
      <div className="fixed right-8 flex flex-col gap-2 p-6">
        <div className="text-center">Menu</div>
        <button
          onClick={openBrainModal}
          className="dev-button"
        >
          The Brain
        </button>

        <button
          onClick={resetGame}
          className="dev-button bg-red-600 hover:bg-red-700"
        >
          Reset Game
        </button>
      </div>
      
      {/* Modals */}
      <Modal isOpen={isWelcomeModalOpen} onClose={closeWelcomeModal} closeButton={false}>
        <WelcomeScreen onStart={closeWelcomeModal} />
      </Modal>

      <Modal isOpen={isTraderModalOpen} onClose={closeTraderModal} closeButton={false}>
        <TraderUI
          trader={gameState?.activeTrader ?? null}
          offer={gameState?.activeOffer ?? null}
          playerGold={gameState?.player?.gold ?? 0}
          onAccept={acceptTrade}
          onReject={rejectTrade}
        />
      </Modal>

      <Modal isOpen={isBrainModalOpen} onClose={closeBrainModal} closeButton={true}>
        <BrainUI 
          hint={brainHint} 
          loading={brainLoading} 
          onBalanced={balancedBrainMove}
          onExplorer={explorerBrainMove}
          onGreedy={greedyBrainMove}
          gold={gameState?.player?.gold}
        />
      </Modal>

      <Modal isOpen={isDeathScreenOpen} onClose={closeDeathScreen} closeButton={true}>
        <DeathScreen resetGame={resetGame} closeDeathScreen={closeDeathScreen}/>
      </Modal>

      <Modal isOpen={isWinScreenOpen} onClose={closeWinScreen} closeButton={true}>
        <WinScreen nextLevel={nextLevel} closeWinScreen={closeWinScreen}/>
      </Modal>
    </div>
  );
};

export default App;
