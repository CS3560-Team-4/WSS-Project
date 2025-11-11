import "./Map.css";

const Map = ({ gameState }) => {
  if (!gameState) {
    return <div className="text-gray-500">Loading map...</div>;
  }
  
  const mapData = gameState.board;

  return (
    <div className="flex flex-col border-3 border-amber-500">
      {mapData.map((row, y) => (
        <div key={y} className="flex">
          {row.map((cell, x) => (
            <div
              key={x}
              className={`w-11 h-11 flex items-center justify-center font-mono text-3xl
                ${
                  cell === 'T' ? 'terrain-cell' :
                  cell === 'P' ? 'player-cell' :
                  cell === 'D' ? 'desert-cell' :
                  cell === '!' ? 'dmv-cell' :
                  cell === 'F' ? 'frost-cell' :
                  cell === 'M' ? 'mountain-cell' :
                  cell === 'S' ? 'swamp-cell' :
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