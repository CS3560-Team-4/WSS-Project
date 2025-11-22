import { useState } from 'react';
import './css/TraderUI.css';

// idea: traders will be events that disappear, so if user
// decides to close the trade, then they will miss their chance
const TraderUI = () => {
  const [tItemSelected, setTSelected] = useState(null);
  const [pItemSelected, setPSelected] = useState(null);

  // trader info test variable
  const traderType = 'Generous';

  // player currency test variable
  const playerCurrency = 27;

  // test items for now
  const traderItems = ["I", "I", "I"];
  const playerItems = ["I", "I", "I"];

  return (
    <>
      <div className="trade-label">TRADE</div>
      <div className="info border-2 py-2 px-4">
        <div>Trader: {`${traderType}`}</div>
        <div>Your gold: {`${playerCurrency}`}</div>
      </div>
      <div className="inventories flex flex-col items-center justify-center mt-6 mb-3">
        <div>Select an item to trade for</div>
        <div className="trade-items">
          {traderItems.map((item, index) => (
            <button
              key={index}
              onClick={() => setTSelected(index)}
              className={`${ tItemSelected === index ? "trade-item-cell-selected" : "trade-item-cell" }`}
            >
              {item}
            </button>
          ))}
        </div>
        <div className="mt-5">Select your item to trade</div>
        <div className="trade-items">
          {playerItems.map((item, index) => (
            <button
              key={index}
              onClick={() => setPSelected(index)}
              className={`${ pItemSelected === index ? "trade-item-cell-selected": "trade-item-cell" }`}
            >
              {item}
            </button>
          ))}
        </div>
      </div>
    </>
  );
};

export default TraderUI;