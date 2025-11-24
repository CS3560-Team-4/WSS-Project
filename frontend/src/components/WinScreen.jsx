import '../css/App.css';

const WinScreen = ({ nextLevel, closeWinScreen }) => {
  const handleClick = () => {
    nextLevel();
    closeWinScreen();
  };

  return (
    <div className="flex flex-col items-center justify-center gap-4">
      <div className="text-3xl font-extrabold text-amber-600">Level Complete!</div>
      <button
        onClick={handleClick}
        className="dev-button text-xl"
      >
        Next Level
      </button>
    </div>
  )
}

export default WinScreen;