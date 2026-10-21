import "./css/TraderUI.css";

const TraderUI = ({ trader, offer, playerGold, onAccept, onReject }) => {
  if (!trader || !offer) {
    return <p className="trade-loading" role="status">Loading trade...</p>;
  }

  const notEnoughGold = playerGold < offer.price;

  return (
    <section className="trader-ui">
      <h1 className="dialog-title trade-title">Trade</h1>

      <div className="trade-parties">
        <section className="trade-person" aria-label="Trader information">
          <h2 className="trade-person-name">{trader.name}</h2>
          <dl className="trade-details">
            <div><dt>Type</dt><dd>{trader.traderType}</dd></div>
            <div><dt>Mood</dt><dd>{trader.mood}</dd></div>
            <div><dt>Your Gold</dt><dd>{playerGold}</dd></div>
          </dl>
        </section>

        <section className="trade-offer" aria-label="Offer information">
          <p className="trade-overline">The trader offers:</p>
          <p className="trade-item">{offer?.itemType}</p>
          <p className="trade-price">
            <span>Price:</span>
            <strong>{offer?.price} gold</strong>
          </p>
        </section>
      </div>

      {notEnoughGold && (
        <p className="trade-warning" role="status">Not enough gold!</p>
      )}

      <div className="trade-actions">
        <button
          type="button"
          className="primary-button trade-accept"
          onClick={onAccept}
          disabled={notEnoughGold}
        >
          Accept
        </button>
        <button type="button" className="secondary-button" onClick={onReject}>
          Reject
        </button>
      </div>
    </section>
  );
};

export default TraderUI;
