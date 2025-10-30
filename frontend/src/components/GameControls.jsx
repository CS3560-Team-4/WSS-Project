import './GameControls.css';

const GameControls = ({ move }) => { 
  return (
    <div className="mt-8 flex flex-col items-center gap-2 mb-20">
      <button 
        className="button" 
        onClick={() => move('up')}
      >^</button>
      <div className="flex gap-12">
        <button className="button" onClick={() => move('left')}>{"<"}</button>
        <button className="button" onClick={() => move('right')}>{">"}</button>
      </div>
      <button className="button" onClick={() => move('down')}>⌄</button>
    </div>
  );
}

export default GameControls;