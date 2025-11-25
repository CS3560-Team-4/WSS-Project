import "./css/TraderUI.css";

const TraderUI = ({ trader, offer, playerGold, onAccept, onReject }) => {
  if (!trader || !offer) {
    return <div className="text-white">Loading trade...</div>;
  }

  const notEnoughGold = playerGold < offer.price;

  return (
    <div className="trader-ui flex flex-col items-center">

      <div className="trade-label">TRADE</div>

      {/* Trader Information */}
      <div className="info border-2 py-2 px-4 text-center mb-4">
        <div className="font-bold text-lg">{trader.name}</div>
        <div>Type: {trader.traderType}</div>
        <div>Mood: {trader.mood}</div>
        <div className="mt-3">Your Gold: {playerGold}</div>
      </div>

      {/* Offer Information */}
      <div className="offer-box border-2 py-3 px-4 text-center mb-6">
        <div className="text-sm">The trader offers:</div>
        <div className="font-bold text-xl mt-1">
          {offer?.itemType}
        </div>

        <div className="mt-2">
          <span className="text-sm">Price:</span>
          <span className="font-bold ml-2">{offer?.price} gold</span>
        </div>
      </div>

      {/**Not enough gold warning */}
      {playerGold < offer.price && (
        <div className="text-red-500 font-bold mt-3 mb-2">
          Not enough gold!
        </div>
      )}

      {/* Buttons */}
      <div className="flex gap-4">
        <button 
          className={
            `accept-button bg-green-500 
            hover:bg-green-600 text-white 
              px-4 py-2 rounded ${notEnoughGold ? "bg-gray-500 cursor-not-allowed"
              : "bg-green-500 hover:bg-green-600"
              }`
            }
          onClick={onAccept}
          disabled={notEnoughGold}
        >
          Accept
        </button>

        <button 
          className="reject-button bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded"
          onClick={onReject}
        >
          Reject
        </button>
      </div>
    </div>
  );
};

export default TraderUI;
