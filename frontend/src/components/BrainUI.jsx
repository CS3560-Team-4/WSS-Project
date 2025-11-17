import './css/BrainUI.css';

const BrainUI = () => {
  return (
    <div className="flex flex-col items-center justify-center">
      <div className="brain-label">BRAIN</div>

      {/* Hint will replace this and go here */}
      <div>
        <div className="brain-desc">
          The brain will point you towards the right direction...
        </div>
        <div className="brain-desc mt-4">
          Cost: 15 currency
        </div>
      </div>
      
      <button className="hint-button mt-6">
        Generate hint
      </button>
    </div>
  );
};

export default BrainUI;