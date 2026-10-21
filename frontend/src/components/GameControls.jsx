import { forwardRef } from 'react';
import './css/GameControls.css';

const Arrow = ({ direction }) => {
  const rotation = {
    up: 0,
    right: 90,
    down: 180,
    left: 270,
  }[direction];

  return (
    <svg viewBox="0 0 24 24" aria-hidden="true" focusable="false">
      <path
        d="M12 19V5m0 0L6 11m6-6 6 6"
        transform={`rotate(${rotation} 12 12)`}
      />
    </svg>
  );
};

const GameControls = forwardRef(({ move }, ref) => (
  <div ref={ref} className="game-controls" role="group" aria-label="Movement controls">
    <button
      className="movement-button movement-up"
      type="button"
      aria-label="Move up"
      onClick={() => move('up')}
    >
      <Arrow direction="up" />
    </button>
    <button
      className="movement-button movement-left"
      type="button"
      aria-label="Move left"
      onClick={() => move('left')}
    >
      <Arrow direction="left" />
    </button>
    <span className="movement-hub" aria-hidden="true" />
    <button
      className="movement-button movement-right"
      type="button"
      aria-label="Move right"
      onClick={() => move('right')}
    >
      <Arrow direction="right" />
    </button>
    <button
      className="movement-button movement-down"
      type="button"
      aria-label="Move down"
      onClick={() => move('down')}
    >
      <Arrow direction="down" />
    </button>
  </div>
));

export default GameControls;
