import "./Map.css";

const Map = ({ gameState }) => {
  if (!gameState) {
    return <div className="text-gray-500">Loading map...</div>;
  }
  
  const map = gameState.map;

  return (
    <div className="flex flex-col border-3 border-amber-500">
      {map.map((row, y) => (
        <div key={y} className="flex">
          {row.map((cell, x) => (
            <div
              key={x}
              className={`w-11 h-11 flex items-center justify-center font-mono text-3xl
                ${
                  cell === '#' ? 'border-cell' :
                  cell === 'P' ? 'player-cell' :
                  'empty-cell'
                }`}
            >
              {cell === '#' ? '' : (cell === 'P' ? 'P' : '')}
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

export default Map;