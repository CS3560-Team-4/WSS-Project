import playerImg from '../assets/player.png';
import './css/WelcomeScreen.css';

const WelcomeScreen = ({ onStart }) => {
  return (
    <section className="welcome-screen">
      <div className="welcome-heading-row">
        <img className="welcome-player" src={playerImg} alt="" />
        <div>
          <h1 className="dialog-title">Welcome to the WSS</h1>
          <p className="welcome-expansion">Wilderness Survival System</p>
        </div>
      </div>

      <div className="welcome-copy">
        <p className="dialog-copy">
          Navigate through the terrain and use as little resources as possible to get to the end.
        </p>
        <p className="dialog-copy">
          Pick up some items along the way to assist in your journey. Don't forget to stop by the traders as well to exchange your gold for items. But be weary, they might try and take your money!
        </p>
        <p className="welcome-prompt">Are you ready for the challenge?</p>
      </div>

      <button type="button" onClick={onStart} className="primary-button welcome-start">
        Start Game
      </button>
    </section>
  );
};

export default WelcomeScreen;
