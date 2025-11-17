import { useState, useEffect, useRef } from 'react';
import './css/InventoryUI.css';

const InventoryUI = ({ mapRef, arrowRef }) => {
  const [itemSelected, setItemSelected] = useState(null);
  const inventoryRef = useRef(null);

  // test items for now
  const playerItems = ["", "I", "I"];

  // prevent deselecting inventory when clicking map or arrow keys
  useEffect(() => {
    const handleClickOutside = (e) => {
      const clickedInventory = inventoryRef.current?.contains(e.target);
      const clickedMap = mapRef.current?.contains(e.target);
      const clickedArrows = arrowRef.current?.contains(e.target);

      // if clicking outside ALL 3 then set selected -> null
      if (!clickedInventory && !clickedMap && !clickedArrows) {
        setItemSelected(null);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [mapRef, arrowRef]);

  return (
    <>
      <div ref={inventoryRef} className="inventory-items">
        {playerItems.map((item, index) => {
          const isEmpty = item === "" || item === null;

          return (
            <button
              key={index}
              onClick={() => setItemSelected(index)}
              className={
                isEmpty
                  ? "inventory-item-cell-disabled"
                  : itemSelected === index
                    ? "inventory-item-cell-selected"
                    : "inventory-item-cell"
                }
            >
              {item || ""} {/* show nothing if empty */}
            </button>
          );
        })}
      </div>
    </>
  );
};

export default InventoryUI;