import { useState } from 'react';
import './css/TraderUI.css';

// idea: traders will be events that disappear, so if user
// decides to close the trade, then they will miss their chance
const TraderUI = () => {
  const [tItemSelected, setTSelected] = useState(null);
  const [pItemSelected, setPSelected] = useState(null);

  // test items for now
  const traderItems = ["T-Item1", "T-Item2", "T-Item3"];
  const playerItems = ["P-Item1", "P-Item2", "P-Item3"];

  return (
    <>
      <div className="">
        <div className="trade-label">TRADE</div>
        <div className="info border-2 py-2 px-4">
          <div>Trader: Generous</div>
          <div>Currency: 27</div>
        </div>
        <div className="inventories flex flex-col items-center justify-center mt-6 mb-3">
          <div>Select an item to trade for</div>
          <div className="trader-items">
            {traderItems.map((item, index) => (
              <button
                key={index}
                onClick={() => setTSelected(index)}
                className={`${ tItemSelected === index ? "item-cell-selected" : "item-cell" }`}
              >
                {item}
              </button>
            ))}
          </div>
          <div className="mt-5">Select your item to trade</div>
          <div className="trader-items">
            {playerItems.map((item, index) => (
              <button
                key={index}
                onClick={() => setPSelected(index)}
                className={`${ pItemSelected === index ? "item-cell-selected": "item-cell" }`}
              >
                {item}
              </button>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default TraderUI;