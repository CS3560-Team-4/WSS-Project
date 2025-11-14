import './Legend.css'

const Legend = () => {

  return (
    <div className="flex flex-col items-center justify-center p-5 mt-10 border gap-5 -translate-x-20">
      <div className="text-center w-full font-bold text-2xl mb-1">Legend</div>
      <div className="flex flex-row items-center justify-center gap-4 w-full h-full">
        <div className="icon-div">
          Player
          <div className="legend-icon player-icon">
            P
          </div>
        </div>

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
      </div>
      <div className="flex flex-row gap-4 w-full h-full">
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
      </div>
    </div>
  )
};

export default Legend;