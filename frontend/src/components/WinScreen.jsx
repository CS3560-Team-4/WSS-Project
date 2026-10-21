import './css/ResultScreen.css';

const WinScreen = ({ nextLevel, closeWinScreen }) => {
  const handleClick = () => {
    nextLevel();
    closeWinScreen();
  };

  return (
    <section className="result-screen">
      <h1 className="dialog-title result-title result-title--win">Level Complete!</h1>
      <button type="button" onClick={handleClick} className="primary-button">
        Next Level
      </button>
    </section>
  );
};

export default WinScreen;
