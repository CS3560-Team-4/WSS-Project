import './css/StatsUI.css';

const StatsUI = () => {

  // test variables for now
  const playerHealth = 5;
  const playerWater = 5;
  const playerGold = 10;

  return (
    <div className="flex gap-10 mt-2">
      <div className="text-red-400">
        Health: <span className="text-white">{`${playerHealth}`}</span>
      </div>
      <div className="text-blue-300">
        Water: <span className="text-white">{`${playerWater}`}</span>
      </div>
      <div className="text-yellow-300">
        Gold: <span className="text-white">{`${playerGold}`}</span>
      </div>
    </div>
  );
}

export default StatsUI;