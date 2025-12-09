import { useState, useEffect, useRef, useCallback } from 'react';
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
  //**State Initialization */
  // Welcome screen
  const [isWelcomeModalOpen, setIsWelcomeModalOpen] = useState(true);
  const closeWelcomeModal = () => setIsWelcomeModalOpen(false);
  const openWelcomeModal = () => setIsWelcomeModalOpen(true);
  
  // game info
  const [gameState, setGameState] = useState(null);
  const [gameLevel, setGameLevel] = useState(1);
  const [currentScore, setCurrentScore] = useState(0);
  const [highScore, setHighScore] = useState(0);
  const [gameOver, setGameOver] = useState(false);
  
  // death screen
  const [isDeathScreenOpen, setIsDeathScreenOpen] = useState(false);
  const openDeathScreen = () => setIsDeathScreenOpen(true);
  const closeDeathScreen = () => setIsDeathScreenOpen(false);

  // win screen
  const [isWinScreenOpen, setIsWinScreenOpen] = useState(false);
  const openWinScreen = () => setIsWinScreenOpen(true);
  const closeWinScreen = () => setIsWinScreenOpen(false);

  // trader modal
  const [isTraderModalOpen, setIsTraderModalOpen] = useState(false); 
  const openTraderModal = () => setIsTraderModalOpen(true);
  const closeTraderModal = () => setIsTraderModalOpen(false);
  
  // check game load
  const [gameLoaded, setGameLoaded] = useState(false);
  
  // brain hint and loading
  const [brainHint, setBrainHint] = useState(null);
  const [brainLoading, setBrainLoading] = useState(false);
  
  // legend modal
  const [isLegendModalOpen, setIsLegendModalOpen] = useState(false);
  const openLegendModal = () => setIsLegendModalOpen(true);
  const closeLegendModal = () => setIsLegendModalOpen(false);

  // brain modal
  const [isBrainModalOpen, setIsBrainModalOpen] = useState(false);

  // vision modal
  const [isVisionModalOpen, setIsVisionModalOpen] = useState(false);
  
  // default last direction
  const [lastMoveDirection, setLastMoveDirection] = useState("right");

  // auto-play
  const [autoPlayEnabled, setAutoPlayEnabled] = useState(false);
  const [autoPlayMode, setAutoPlayMode] = useState(null);
  const autoMoveRef = useRef();

  //**Fetching Data
  const fetchMapData = async () => {
    try {
      const response = await fetch('http://localhost:8080/state');
      const data = await response.json();
      
      setGameState(data);
      setCurrentScore(data.currentscore);
      setHighScore(data.highscore);
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
  

  // call for player movement
  const move = useCallback(async (direction) => {
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
  }, [gameOver]);

  useEffect(() => {
    autoMoveRef.current = move;
  }, [move]);

  // assign move to arrow keys and wasd keys
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (isDeathScreenOpen) return;
      if (isWinScreenOpen) return;
      if (isBrainModalOpen) return;
      if (isVisionModalOpen) return;
      if (isLegendModalOpen) return;
      if (isTraderModalOpen) return;
      if (isWelcomeModalOpen) return;

      if (e.key === "ArrowUp" || e.key === "w") move("up");
      if (e.key === "ArrowDown" || e.key === "s") move("down");
      if (e.key === "ArrowLeft" || e.key === "a") move("left");
      if (e.key === "ArrowRight" || e.key === "d") move("right");
    };

    window.addEventListener("keydown", handleKeyDown);

    return () => window.removeEventListener("keydown", handleKeyDown);
  }, 
    [
      move,
      isDeathScreenOpen,
      isWinScreenOpen,
      isBrainModalOpen,
      isVisionModalOpen,
      isLegendModalOpen,
      isTraderModalOpen,
      isWelcomeModalOpen
    ]
  );

  useEffect(() => {
    if (!autoPlayEnabled) return;
    if (anyModalsOpen()) return;
    if (!gameLoaded) return;

    const interval = setInterval(() => {
      autoPlayStep();
    }, 350); // speed of auto-play

    return () => clearInterval(interval);
  }, 
  [
    autoPlayEnabled,
    autoPlayMode,
    gameState,
    isTraderModalOpen,
    isBrainModalOpen,
    isVisionModalOpen,
    isDeathScreenOpen,
    isLegendModalOpen,
    isWinScreenOpen,
    isWelcomeModalOpen,
    gameLoaded
  ]);

  useEffect(() => {
    if (!autoPlayEnabled) return;

    const interval = setInterval(() => {
      autoMoveRef.current("down");
    }, 500);

    return () => clearInterval(interval);
  }, [autoPlayEnabled]);

  //**Brain Functionality */
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

  //**Reset and Next Level */
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
      setCurrentScore(data.currentscore);
      setHighScore(data.highscore);

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
      setCurrentScore(data.currentscore);
      setHighScore(data.highscore);

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
  useEffect(() => {
    if (gameState && gameState.player && !gameLoaded) {
      setGameLoaded(true);
    }
  }, [gameState]);
  
  // death screen
  useEffect(() => {
    if (!gameLoaded) return;

    if (gameState.player.health <= 0) {
      openDeathScreen();
    }
  }, [gameState]);

  // win screen
  useEffect(() => {
    if (!gameLoaded) return;

    if (gameState.player.won === true) {
      console.log("win flag: ", gameState?.player?.win);
      openWinScreen();
    }
  }, [gameState]);

  // brainUI
  const openBrainModal = () => {
    setBrainHint(null);
    setBrainLoading(false);
    setIsBrainModalOpen(true);
  }
  const closeBrainModal = () => setIsBrainModalOpen(false);

    // brainUI
  const openVisionModal = () => {
    setIsVisionModalOpen(true);
  }
  const closeVisionModal = () => setIsVisionModalOpen(false);

  // TraderUI 
  useEffect(() => {
    if (!gameState) return;

    if (gameState.activeTrader) {
      setIsTraderModalOpen(true);
    }
  }, [gameState?.activeTrader]);

  // auto-play
  const autoPlayStep = async () => {
    if (!autoPlayEnabled) return;
    if (anyModalsOpen()) return;
    if (!gameLoaded) return;

    try {
      let endpoint = "";
      if (autoPlayMode === "balanced") endpoint = "/balancedbrain";
      if (autoPlayMode === "explorer") endpoint = "/explorerbrain";
      if (autoPlayMode === "greedy") endpoint = "/greedybrain";

      const response = await fetch(`http://localhost:8080${endpoint}`, {
        method: "POST",
        headers: { "Content-Type" : "application/json" }
      });

      const data = await response.json();

      const normalizeBrainMove = (d) => {
        switch (d) {
          case "MoveNorth": return "up";
          case "MoveSouth": return "down";
          case "MoveEast": return "right";
          case "MoveWest": return "left";
          default: return null;
        }
      };

      const moveDir = normalizeBrainMove(data.brainMove);
      if (moveDir) await move(moveDir);
      
    } catch (err) {
      console.log("Auto-play error:", err);
    }
  };
  
  //**General Modal Functionality
  // might delete since current UI does not scroll already
  // check if any modals are open
  const anyModalsOpen = () => {
    if (isTraderModalOpen 
      || isBrainModalOpen
      || isVisionModalOpen
      || isDeathScreenOpen
      || isLegendModalOpen
      || isWinScreenOpen
      || isWelcomeModalOpen
      ) {
      return true;
    }

    return false;
  };
  
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

  const setVision = async (type) => {
    try {
      const endpointMap = {
        cautious: "/cautious-vision",
        keen: "/keen-vision",
        narrow: "/narrow-vision",
        queen: "/queen-vision",
      };

      const res = await fetch(`http://localhost:8080${endpointMap[type]}`, {
        method: "POST"
      });

      if (!res.ok) throw new Error (`Failed to set vision ${type}`);

      // Refresh game state after changing vision
      fetchMapData();
      closeVisionModal();
    } catch (err) {
      console.error(err);
    } // end try catch
  } // end setVision

  return (
    <div className="flex flex-col items-center justify-center font-mono pb-10 pt-2">
      <div className="text-[75px] -translate-x-20 font-bold italic">
        WSS
      </div>
      <div className="flex flex-row items-center justify-center gap-10">
        <div className="flex flex-col items-center justify-center">

          <div className="mb-2 flex flex-row items-center justify-center gap-10">
            <div>
              Level: {`${gameLevel}`}
            </div>
            <div>
              Score: {`${currentScore}`}
            </div>
            <div>
              High Score: {`${highScore}`}
            </div>
          </div>

          <Map ref={mapRef} gameState={gameState} lastMove={lastMoveDirection} />
          
          <StatsUI gameState={gameState} />

          {autoPlayEnabled && (
            <div className="text-white text-xl font-bold border-white rounded shadow">
              Auto-Play: {autoPlayMode ? autoPlayMode[0].toUpperCase() + autoPlayMode.slice(1) : ""}
            </div>
          )}
        </div>

        <div className={gameOver ? "pointer-events-none opacity-50" : ""}>
          <GameControls ref={arrowRef} move={move} />
        </div>
      </div>

      {/** Menu buttons */}
      <div className="fixed right-8 flex flex-col gap-2 p-6">
        <div className="text-center">Menu</div>
        <button
          onClick={openLegendModal}
          className="dev-button"
        >
          Guide
        </button>

        <button
          onClick={openBrainModal}
          className="dev-button"
        >
          The Brain
        </button>

        {/* Vision settings */}
        <button
          onClick={openVisionModal}
          className="dev-button"
        >
          Set Vision
        </button>

        <button
          className="dev-button"
          onClick={() => {
            setAutoPlayMode("balanced")
            setAutoPlayEnabled(true);
          }}
        >
          Auto-Play (Balanced)
        </button>
        <button
          className="dev-button"
          onClick={() => {
            setAutoPlayMode("explorer")
            setAutoPlayEnabled(true);
          }}
        >
          Auto-Play (Explorer)
        </button>
        <button
          className="dev-button"
          onClick={() => {
            setAutoPlayMode("greedy")
            setAutoPlayEnabled(true);
          }}
        >
          Auto-Play (Greedy)
        </button>

        {autoPlayEnabled && (
          <button
            className="dev-button"
            onClick={() => {
              setAutoPlayEnabled(false);
              setAutoPlayMode(null);
            }}
          >
            Stop Auto-Play
          </button>
        )}

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

      {/* Vision Modal */}
      <Modal isOpen={isVisionModalOpen} onClose={closeVisionModal} closeButton={true} width="max-w-md">
        <div className="flex flex-col gap-2">
          <h2 className="text-xl font-bold text-center mb-4">Set Player Vision</h2>
          <button className="dev-button" onClick={() => setVision("cautious")}>Cautious Vision</button>
          <button className="dev-button" onClick={() => setVision("keen")}>Keen Vision</button>
          <button className="dev-button" onClick={() => setVision("narrow")}>Narrow Vision</button>
          <button className="dev-button" onClick={() => setVision("queen")}>Queen Vision</button>
        </div>
      </Modal>

      <Modal isOpen={isLegendModalOpen} onClose={closeLegendModal} closeButton={true} width={'max-w-6xl'}>
        <Legend />
      </Modal>

      <Modal 
        isOpen={isDeathScreenOpen} 
        onClose={closeDeathScreen} 
        closeButton={true}
      >
        <DeathScreen resetGame={resetGame} closeDeathScreen={closeDeathScreen} />
      </Modal>

      <Modal isOpen={isWinScreenOpen} onClose={closeWinScreen} closeButton={false}>
        <WinScreen nextLevel={nextLevel} closeWinScreen={closeWinScreen}/>
      </Modal>
    </div>
  );
};

export default App;
