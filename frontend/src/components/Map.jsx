import { forwardRef } from 'react';
import './css/Map.css';
import playerImg from '../assets/player.png';
import goalImg from '../assets/exit.png';

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
};

const Map = forwardRef(({ gameState, lastMove }, ref) => {
  if (!gameState) {
    return (
      <div ref={ref} className="map-board map-loading" role="status">
        Loading map...
      </div>
    );
  }

  const mapData = gameState.board;

  return (
    <div ref={ref} className="map-board" role="group" aria-label="Game map">
      {mapData.map((row, y) => (
        <div
          key={y}
          className="map-row"
          style={{ '--map-columns': row.length }}
        >
          {row.map((cell, x) => {
            const terrain = cell.terrain;
            const tileObject = cell.tileObject;
            const playerX = gameState.player.x;
            const playerY = gameState.player.y;
            const isPlayer = x === playerX && y === playerY;
            const isGoal = terrain === 'E';
            const baseTerrain = isPlayer ? gameState.player.terrainStringBuffer : terrain;
            const terrainClass = getTerrainClass(baseTerrain);

            const isVisible = (x === gameState.player.x && y === gameState.player.y) || gameState.visibleTiles.some(
              ([vx, vy]) => vx === x && vy === y
            ) || false;

            return (
              <div
                key={x}
                className={`map-tile ${terrainClass} ${isVisible ? terrainClass : 'fog-tile'}`}
              >
                {isGoal && (
                  <img
                    src={goalImg}
                    alt="Goal"
                    className="map-sprite map-sprite-goal"
                  />
                )}

                {tileObject?.type === 'ITEM' && !isPlayer && (
                  <img
                    src={itemSprites[tileObject.itemType]}
                    alt={tileObject.itemType}
                    className="map-sprite map-sprite-object"
                  />
                )}

                {tileObject?.type === 'TRADER' && !isPlayer && (
                  <img
                    src={traderSprites[tileObject.traderType]}
                    alt={tileObject.traderType}
                    className="map-sprite map-sprite-object"
                  />
                )}

                {isVisible && isPlayer && (
                  <img
                    src={playerImg}
                    alt="Player"
                    className={`map-sprite map-sprite-player ${lastMove === 'left' ? 'flip-horizontal' : ''}`}
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
