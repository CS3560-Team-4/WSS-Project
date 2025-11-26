import './css/Legend.css'
import goalImg from '../assets/exit.png';

const Legend = () => {

  const itemSprites = {
    WATER_BOTTLE: new URL('../assets/items/WATER_BOTTLE.png', import.meta.url).href,
    MEDICINE: new URL('../assets/items/MEDICINE.png', import.meta.url).href,
    ENERGY_DRINK: new URL('../assets/items/ENERGY_DRINK.png', import.meta.url).href,
    TURKEY: new URL('../assets/items/TURKEY.png', import.meta.url).href,
  };

  const traderSprites = {
    Friendly: new URL('../assets/traders/Friendly.png', import.meta.url).href,
    Generous: new URL('../assets/traders/Generous.png', import.meta.url).href,
    Greedy: new URL('../assets/traders/Greedy.png', import.meta.url).href,
    Lowballer: new URL('../assets/traders/Lowballer.png', import.meta.url).href,
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <div className="text-center w-full font-bold text-3xl mb-1">The WSS Guide</div>
      <div className="flex flex-col items-center justify-center p-2 gap-5 mb-8 mt-2">
        <div className="flex flex-col items-center justify-center gap-4 w-full h-full">
          <div className="legend-sub-heading">
            Terrains
          </div>
          <div className="legend-section">
            <div className="icon-div">
              <div className="flex gap-2">
                <div className="legend-icon terrain-icon">
                </div>
                Basic
              </div>
              <div className="icon-desc">
                <div>
                  <span className="text-blue-300">Water</span> -1
                </div>
                <div>
                  <span className="text-green-400">Energy</span> -1
                </div>
              </div>
            </div>
            <div className="icon-div">
              <div className="flex gap-2">
                <div className="legend-icon desert-icon">
                </div>
                Desert
              </div>
              <div className="icon-desc">
                <div>
                  <span className="text-blue-300">Water</span> -6
                </div>
                <div>
                  <span className="text-green-400">Energy</span> -2
                </div>
              </div>
            </div>
            <div className="icon-div">
              <div className="flex gap-2">
                <div className="legend-icon dmv-icon">
                </div>
                DMV
              </div>
              <div className="icon-desc">
                <div>
                  <span className="text-blue-300">Water</span> -1
                </div>
                <div>
                  <span className="text-green-400">Energy</span> -10
                </div>
              </div>
            </div>
            <div className="icon-div">
              <div className="flex gap-2">
                <div className="legend-icon mountain-icon">
                </div>
                Mountain
              </div>
              <div className="icon-desc">
                <div>
                  <span className="text-blue-300">Water</span> -2
                </div>
                <div>
                  <span className="text-green-400">Energy</span> -3
                </div>
              </div>
            </div>
          </div>

          <div className="legend-section">
            <div className="icon-div">
              <div className="flex gap-2">
                <div className="legend-icon frost-icon">
                </div>
                Frost
              </div>
              <div className="icon-desc">
                <div>
                  <span className="text-blue-300">Water</span> -1
                </div>
                <div>
                  <span className="text-green-400">Energy</span> -6
                </div>
              </div>
            </div>
            <div className="icon-div">
              <div className="flex gap-2">
                <div className="legend-icon swamp-icon">
                </div>
                Swamp
              </div>
              <div className="icon-desc">
                <div>
                  <span className="text-blue-300">Water</span> -1
                </div>
                <div>
                  <span className="text-green-400">Energy</span> -3
                </div>
              </div>
            </div>
            <div className="icon-div">
              <div className="flex gap-2">
                <div className="legend-icon goal-icon">
                  <img src={goalImg} className="invert brightness-200 size-4" />
                </div>
                Goal
              </div>
              <div className="icon-desc">
                  <div>
                    <span className="text-blue-300">Water</span> -1
                  </div>
                  <div>
                    <span className="text-green-400">Energy</span> -1
                  </div>
                </div>
            </div>
          </div>

          <div className="flex flex-col items-center justify-center gap-4">
            <div className="legend-sub-heading mt-3">
              Items
            </div>
            <div className="legend-section">
              <div className="icon-div min-w-70">
                <div className="flex gap-2">
                  <img src={itemSprites["MEDICINE"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Medicine
                  </div>
                </div>
                  <div className="icon-desc">
                    <span className="text-red-400">Health</span> +25
                  </div>
              </div>
              <div className="icon-div min-w-70">
                <div className="flex gap-2">
                  <img src={itemSprites["TURKEY"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Turkey
                  </div>
                </div>
                  <div className="icon-desc">
                    <span className="text-green-400">Energy</span> +25
                  </div>
              </div>
            </div>
            <div className="legend-section">
              <div className="icon-div min-w-70">
                <div className="flex gap-2">
                  <img src={itemSprites["WATER_BOTTLE"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Water Bottle
                  </div>
                </div>
                  <div className="icon-desc">
                    <span className="text-blue-300">Water</span> +25
                  </div>
              </div>
              <div className="icon-div min-w-70">
                <div className="flex gap-2">
                  <img src={itemSprites["ENERGY_DRINK"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Energy Drink
                  </div>
                </div>
                  <div className="icon-desc">
                    <span className="text-green-400">Energy</span> +10
                  </div>
              </div>
            </div>
          </div>

          <div className="flex flex-col items-center justify-center gap-4">
            <div className="legend-sub-heading mt-3">
              Traders
            </div>
            <div className="legend-section">
              <div className="trader-div">
                <div className="flex gap-2">
                  <img src={traderSprites["Friendly"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Friendly
                  </div>
                </div>
              </div>
              <div className="trader-div">
                <div className="flex gap-2">
                  <img src={traderSprites["Generous"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Generous
                  </div>
                </div>
              </div>
              <div className="trader-div">
                <div className="flex gap-2">
                  <img src={traderSprites["Greedy"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Greedy
                  </div>
                </div>
              </div>
              <div className="trader-div">
                <div className="flex gap-2">
                  <img src={traderSprites["Lowballer"]} className="item-icon"/>
                  <div className="translate-y-1">
                    Lowballer
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
};

export default Legend;