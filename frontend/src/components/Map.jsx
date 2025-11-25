import { forwardRef } from 'react';
import "./css/Map.css";
import playerImg from '../assets/player.png';
import goalImg from '../assets/exit.png'

const itemSprites = {
  WATER_BOTTLE: new URL('../assets/items/WATER_BOTTLE.png', import.meta.url).href,
  MEDICINE: new URL('../assets/items/MEDICINE.png', import.meta.url).href,
  ENERGY_DRINK: new URL('../assets/items/ENERGY_DRINK.png', import.meta.url).href,
  TURKEY: new URL('../assets/items/TURKEY.png', import.meta.url).href,
};

const traderSprites = {
  Friendly: new URL('../assets/traders/Friendly.png', import.meta.url).href,
  Generous: new URL('../assets/traders/Generous.png', import.meta.url).href,
  Greedy: new URL('../assets/traders/Greedy.png', import.meta.url).href,
  Lowballer: new URL('../assets/traders/Lowballer.png', import.meta.url).href,
};

const Map = forwardRef(({ gameState, lastMove }, ref) => {


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
            const terrain = cell.terrain;
            const tileObject = cell.tileObject;

            const isPlayer = terrain === 'P';
            const isGoal = terrain === 'E';

            // If player occupies this tile, use underlying terrain
            const baseTerrain = isPlayer
              ? gameState.player.terrainStringBuffer
              : terrain;

            const terrainClass = getTerrainClass(baseTerrain);

            return (
              <div
                key={x}
                className={`relative w-11 h-11 flex items-center justify-center ${terrainClass}`}
              >
                {/** Goal Sprite*/}
                {isGoal && (
                  <img
                    src={goalImg}
                    alt="Goal"
                    className="invert brightness-200 w-8 h-8"
                  />
                )}

                {/**Render item */}
                {tileObject && tileObject.type === "ITEM" && !isPlayer && (
                  <img 
                    src={itemSprites[tileObject.itemType]}
                    alt={tileObject.itemType}
                    className="absolute w-7 h-7 pixelated"
                  />
                )}

                {/**Render trader */}
                {tileObject && tileObject.type === "TRADER" && !isPlayer && (
                  <img 
                    src={traderSprites[tileObject.traderType]}
                    alt={tileObject.traderType}
                    className="absolute w-10 h-10 pixelated"
                  />
                )}

                {/* Overlay player sprite */}
                {isPlayer && (
                  <img
                    src={playerImg}
                    alt="Player"
                    className={`absolute left-1/2 top-1/2 w-8 h-8 
                               -translate-x-1/2 -translate-y-1/2 
                               pixelated pointer-events-none
                               ${lastMove === 'left' ? 'flip-horizontal' : ''}`}
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