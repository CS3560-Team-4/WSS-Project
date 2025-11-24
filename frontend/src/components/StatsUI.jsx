
const StatsUI = ({ gameState }) => {
  let playerHealth = 100;
  let playerWater = 100;
  let playerEnergy = 100;

  if (!gameState || !gameState.player) return null;

  playerHealth = gameState.player.health;
  playerWater = gameState.player.water;
  playerEnergy = gameState.player.energy;

  if (playerHealth <= 0) playerHealth = 0;
  if (playerWater <= 0) playerWater = 0;
  if (playerEnergy <= 0) playerEnergy = 0;

  // test variable for now
  let playerGold = 10;

  return (
    <div className="flex gap-10 mt-2">
      <div className="text-red-400">
        Health: <span className="text-white">{`${playerHealth}`}</span>
      </div>
      <div className="text-blue-300">
        Water: <span className="text-white">{`${playerWater}`}</span>
      </div>
      <div className='text-green-400'>
        Energy: <span className="text-white">{`${playerEnergy}`}</span>
      </div>
      <div className="text-yellow-300">
        Gold: <span className="text-white">{`${playerGold}`}</span>
      </div>
    </div>
  );
}

export default StatsUI;