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

    if (selectedBrain === 'balanced') onBalanced();
    if (selectedBrain === 'explorer') onExplorer();
    if (selectedBrain === 'greedy') onGreedy();
  };

  return (
    <section className="brain-ui">
      {!hint && !loading && (
        <div className="brain-intro">
          <p className="dialog-copy">The brain will point you towards the right direction...</p>
          <p className="brain-cost">Cost: <span>{hintCost} gold</span></p>
          <p className="brain-instruction">Select your brain type</p>
        </div>
      )}

      <div className="brain-choices" role="group" aria-label="Brain type">
        <button
          type="button"
          onClick={() => handleSelect('balanced')}
          className={`brain-choice ${selectedBrain === 'balanced' ? 'brain-choice--selected' : ''}`}
          aria-pressed={selectedBrain === 'balanced'}
        >
          Balanced
        </button>
        <button
          type="button"
          onClick={() => handleSelect('explorer')}
          className={`brain-choice ${selectedBrain === 'explorer' ? 'brain-choice--selected' : ''}`}
          aria-pressed={selectedBrain === 'explorer'}
        >
          Explorer
        </button>
        <button
          type="button"
          onClick={() => handleSelect('greedy')}
          className={`brain-choice ${selectedBrain === 'greedy' ? 'brain-choice--selected' : ''}`}
          aria-pressed={selectedBrain === 'greedy'}
        >
          Greedy
        </button>
      </div>

      {selectedBrain && gold < hintCost && (
        <p className="brain-warning" role="status">Not enough gold to use hints.</p>
      )}

      {selectedBrain && gold >= hintCost && (
        <div className="brain-result">
          <p className="brain-selected-label">{selectedBrain} Brain Selected</p>

          {loading && <div className="brain-spinner" role="status" aria-label="Generating hint" />}

          {hint && !loading && (
            <p className="brain-hint" aria-live="polite">
              Suggested Move: {formatMove(hint)}
            </p>
          )}

          <button type="button" className="primary-button brain-generate" onClick={triggerHint}>
            Generate Hint
          </button>
        </div>
      )}
    </section>
  );
};

export default BrainUI;
