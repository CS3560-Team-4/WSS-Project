import { forwardRef } from 'react';
import "./css/Map.css";
import playerImg from '../assets/player.png';

const Map = forwardRef(({ gameState }, ref) => {
  if (!gameState) {
    return (
      <div 
        ref={ref}
        className="border-gray-500 font-mono w-121 h-121 flex items-center justify-center border text-center"
      >
        <div className="text-white">
          Loading map...
        </div>
      </div>
    );
  }
  
  const mapData = gameState.board;

  const getTerrainClass = (char) => {
    switch (char) {
      case 'T': return 'terrain-cell';
      case 'D': return 'desert-cell';
      case '!': return 'dmv-cell';
      case 'F': return 'frost-cell';
      case 'M': return 'mountain-cell';
      case 'S': return 'swamp-cell';
      case 'E': return 'goal-cell';
      default: return 'empty-cell';
    }
  }

  return (
    <div ref={ref} className="flex flex-col border-3 border-amber-500">
      {mapData.map((row, y) => (
        <div key={y} className="flex">
          {row.map((cell, x) => {
            const isPlayer = cell === 'P';

            // If player occupies this tile, use underlying terrain
            const baseTerrain = isPlayer
              ? gameState.player.terrainStringBuffer
              : cell;

            const terrainClass = getTerrainClass(baseTerrain);

            return (
              <div
                key={x}
                className={`relative w-11 h-11 flex items-center justify-center ${terrainClass}`}
              >
                {/* ⭐ Overlay player sprite */}
                {isPlayer && (
                  <img
                    src={playerImg}
                    alt="Player"
                    className="absolute left-1/2 top-1/2 w-8 h-8 
                               -translate-x-1/2 -translate-y-1/2 
                               pixelated pointer-events-none"
                  />
                )}
              </div>
            );
          })}
        </div>
      ))}
    </div>
  );
});

export default Map;