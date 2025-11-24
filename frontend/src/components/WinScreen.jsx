import '../css/App.css';

const WinScreen = ({ nextLevel, closeWinScreen }) => {
  const handleClick = () => {
    nextLevel();
    closeWinScreen();
  };

  return (
    <div className="flex flex-col items-center justify-center gap-4">
      <div className="text-xl font-bold text-amber-600">You made it!</div>
      <button
        onClick={handleClick}
        className="dev-button"
      >
        Play Again
      </button>
    </div>
  )
}

export default WinScreen;