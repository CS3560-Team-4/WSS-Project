import './css/ResultScreen.css';

const DeathScreen = ({ resetGame, closeDeathScreen }) => {
  const handleClick = () => {
    resetGame();
    closeDeathScreen();
  };

  return (
    <section className="result-screen">
      <button type="button" onClick={handleClick} className="primary-button">
        Play Again
      </button>
    </section>
  );
};

export default DeathScreen;
