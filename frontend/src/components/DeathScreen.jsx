import '../css/App.css';

const DeathScreen = ({ resetGame, closeDeathScreen }) => {
  const handleClick = () => {
    resetGame();
    closeDeathScreen();
  };

  return (
    <div className="flex flex-col items-center justify-center gap-4">
      <div className="text-xl font-bold">You have died</div>
      <button
        onClick={handleClick}
        className="dev-button"
      >
        Play Again
      </button>
    </div>
  )
}

export default DeathScreen;