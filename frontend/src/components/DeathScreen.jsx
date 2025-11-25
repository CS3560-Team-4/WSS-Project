import '../css/App.css';

const DeathScreen = ({ resetGame, closeDeathScreen }) => {
  const handleClick = () => {
    resetGame();
    closeDeathScreen();
  };

  return (
    <div className="flex flex-col items-center justify-center gap-4">
      <div className="text-3xl text-red-600 [--stroke:0.2px_white] [-webkit-text-stroke:var(--stroke)] font-bold">You have died</div>
      <button
        onClick={handleClick}
        className="dev-button text-xl"
      >
        Play Again
      </button>
    </div>
  )
}

export default DeathScreen;