import './css/Legend.css'
import goalImg from '../assets/exit.png';

const Legend = () => {

  return (
    <div className="flex flex-col items-center justify-center">
      <div className="text-center w-full font-bold text-2xl mb-1">Legend</div>
      <div className="flex flex-col items-center justify-center p-3 border gap-5 mb-8 mt-2">
        <div className="flex flex-row items-center justify-center gap-4 w-full h-full">
          <div className="icon-div">
            Terrain
            <div className="legend-icon terrain-icon">
            </div>
          </div>

          <div className="icon-div">
            Desert
            <div className="legend-icon desert-icon">
            </div>
          </div>
          
          <div className="icon-div">
            DMV
            <div className="legend-icon dmv-icon">
            </div>
          </div>

          <div className="icon-div">
            Frost
            <div className="legend-icon frost-icon">
            </div>
          </div>

          <div className="icon-div">
            Mountain
            <div className="legend-icon mountain-icon">
            </div>
          </div>

          <div className="icon-div">
            Swamp
            <div className="legend-icon swamp-icon">
            </div>
          </div>

          <div className="icon-div">
            Goal
            <div className="legend-icon goal-icon">
              <img src={goalImg} className="invert brightness-200 size-4" />
            </div>
          </div>
          
        </div>
      </div>
    </div>
  )
};

export default Legend;