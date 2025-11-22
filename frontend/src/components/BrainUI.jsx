import './css/BrainUI.css';

const BrainUI = ({ hint, onHintRequest, loading }) => {
  const formatMove = (move) => {
    return move.replace(/([A-Z])/g, ' $1').trim();
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <div className="brain-label">BRAIN</div>

      {/* Hint will replace this and go here */}
      {!hint ? (
        <div className="flex flex-col items-center">
          <div className="brain-desc">
            The brain will point you towards the right direction...
          </div>
          <div className="brain-desc mt-4">
            Cost: 15 gold
          </div>
        </div>
      ) : null}

      {/**LOADING SPINNER */}
      {loading && (
        <div className="spinner"></div>
      )}

      {/**HINT RESULT */}
      {hint && !loading && (
        <div className="brain-desc hint-animate">
          Suggested Move: {formatMove(hint)}
        </div>
      )}
      
      <button className="hint-button mt-6" onClick={onHintRequest}>
        Generate hint
      </button>
    </div>
  );
};

export default BrainUI;