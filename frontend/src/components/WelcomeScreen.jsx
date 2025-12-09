const WelcomeScreen = ({ onStart }) => {
  return (
    <div className="flex flex-col items-center justify-center text-center p-6">
      <div className="flex flex-col mb-6 gap-2">
        <div className="text-4xl font-bold text-white">Welcome to the WSS</div>
        <div>(Wilderness Survival System)</div>
      </div>

      <p className="text-lg text-gray-300 mb-5 max-w-xs">
        Navigate through the terrain and use as little resources as possible to
        get to the end.
      </p>
      <p className="text-lg text-gray-300 mb-5 max-w-xs">
        Pick up some items along the way to assist in your journey. Don't forget
        to stop by the traders as well to exchange your gold for items. But be
        weary, they might try and take your money!
      </p>
      <p className="text-lg text-gray-300 mb-6 max-w-xs">
        Are you ready for the challenge?
      </p>

      <button
        onClick={onStart}
        className="text-xl font-bold hover:outline-2 
          cursor-pointer bg-green-500 hover:bg-green-600 
          text-white px-6 py-2 rounded-lg hover:scale-110 transition-transform"
      >
        Start Game
      </button>
    </div>
  );
};

export default WelcomeScreen;
