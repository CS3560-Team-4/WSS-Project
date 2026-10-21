import './css/StatsUI.css';
import Icon from './Icon.jsx';

const MAX_RESOURCE = 100;

const StatsUI = ({ gameState }) => {
  if (!gameState?.player) return null;

  const health = Math.max(0, gameState.player.health);
  const water = Math.max(0, gameState.player.water);
  const energy = Math.max(0, gameState.player.energy);
  const gold = Math.max(0, gameState.player.gold);

  const resources = [
    { label: 'Health', value: health, fillClass: 'stat-fill-health' },
    { label: 'Water', value: water, fillClass: 'stat-fill-water' },
    { label: 'Energy', value: energy, fillClass: 'stat-fill-energy' },
  ];

  return (
    <div className="stats-ui" role="group" aria-label="Player stats">
      {resources.map(({ label, value, fillClass }) => {
        const meterValue = Math.min(value, MAX_RESOURCE);
        const fillWidth = `${(meterValue / MAX_RESOURCE) * 100}%`;

        return (
          <div className="stat-resource" key={label}>
            <div className="stat-copy">
              <span className="stat-label">{label}</span>
              <span className="stat-value">{value}</span>
            </div>
            <div
              className="stat-meter"
              role="progressbar"
              aria-label={label}
              aria-valuemin={0}
              aria-valuemax={MAX_RESOURCE}
              aria-valuenow={meterValue}
              aria-valuetext={`${value}`}
            >
              <span className={`stat-meter-fill ${fillClass}`} style={{ width: fillWidth }} />
            </div>
          </div>
        );
      })}
      <div className="stat-gold">
        <span className="stat-label">Gold</span>
        <span className="stat-gold-amount"><span className="stat-value">{gold}</span><Icon name="coins" className="stat-gold-icon" /></span>
      </div>
    </div>
  );
};

export default StatsUI;
