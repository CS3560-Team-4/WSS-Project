import { useState } from 'react';
import './css/BrainUI.css';

const BrainUI = ({ hint, loading, onBalanced, onExplorer, onGreedy, gold }) => {
  const [selectedBrain, setSelectedBrain] = useState(null);
  const hintCost = 5;

  const handleSelect = (brain) => {
    setSelectedBrain(brain);
  };

  const formatMove = (move) => {
    return move.replace(/([A-Z])/g, ' $1').trim();
  };

  const triggerHint = () => {
    if (gold < hintCost) return;

    if (selectedBrain === "balanced") onBalanced();
    if (selectedBrain === "explorer") onExplorer();
    if (selectedBrain === "greedy") onGreedy();
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <div className="brain-label">THE BRAIN</div>

      {/* Default desc when no hint */}
      {!hint && !loading && (
        <div className="flex flex-col items-center">
          <div className="brain-desc">
            The brain will point you towards the right direction...
          </div>
          <div className="brain-desc mt-4">
            Cost: {`${hintCost}`} <span className="text-amber-300">gold</span>
          </div>
          <div className="brain-desc mt-8 mb-2">
            Select your brain type
          </div>
        </div>
      )}

      {/**Brain Selector Buttons */}
      <div className="flex gap-4 mb-1">
        <button
          onClick={() => handleSelect("balanced")}
          className={`hint-button ${selectedBrain === "balanced" ? "brain-selected" : ""}`}
        >
          Balanced
        </button>

        <button
          onClick={() => handleSelect("explorer")}
          className={`hint-button ${selectedBrain === "explorer" ? "brain-selected" : ""}`}
        >
          Explorer
        </button>

        <button
          onClick={() => handleSelect("greedy")}
          className={`hint-button ${selectedBrain === "greedy" ? "brain-selected" : ""}`}
        >
          Greedy
        </button>
      </div>

      {/**Not enough gold message */}
      {selectedBrain && gold < hintCost && (
        <div className="brain-desc text-red-600 mt-2">
          Not enough gold to use hints.
        </div>
      )}

      {/** Collapsible Brain Panel */}
      {selectedBrain && gold >= hintCost && (
        <div className="w-full flex flex-col items-center justify-center px-4 py-3 mt-5 border border-gray-600 rounded-lg bg-black-40">
          <div className="brain-desc capitalize text-center">
            {selectedBrain} Brain Selected
          </div>

          {/**Loading Spinner */}
          {loading && (
            <div className="spinner"></div>
          )}

          {/**Hint Result */}
          {hint && !loading && (
            <div className="brain-desc hint-animate">
              Suggested Move: {formatMove(hint)}
            </div>
          )}

          <button
            className="hint-button mt-4 mb-2"
            onClick={triggerHint}
          >
            Generate Hint
          </button>
          
        </div>
      )}

      
    </div>
  );
};

export default BrainUI;