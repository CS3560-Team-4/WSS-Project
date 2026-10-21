import './css/Legend.css';
import goalImg from '../assets/exit.png';

const terrainEntries = [
  { name: 'Basic', className: 'terrain-icon', water: '-1', energy: '-1' },
  { name: 'Desert', className: 'desert-icon', water: '-6', energy: '-2' },
  { name: 'DMV', className: 'dmv-icon', water: '-1', energy: '-10' },
  { name: 'Mountain', className: 'mountain-icon', water: '-2', energy: '-3' },
  { name: 'Frost', className: 'frost-icon', water: '-1', energy: '-6' },
  { name: 'Swamp', className: 'swamp-icon', water: '-1', energy: '-3' },
  { name: 'Goal', className: 'goal-icon', water: '-1', energy: '-1', goal: true },
];

const itemEntries = [
  { name: 'Medicine', sprite: new URL('../assets/items/MEDICINE.png', import.meta.url).href, resource: 'Health', amount: '+25' },
  { name: 'Turkey', sprite: new URL('../assets/items/TURKEY.png', import.meta.url).href, resource: 'Energy', amount: '+25' },
  { name: 'Water Bottle', sprite: new URL('../assets/items/WATER_BOTTLE.png', import.meta.url).href, resource: 'Water', amount: '+25' },
  { name: 'Energy Drink', sprite: new URL('../assets/items/ENERGY_DRINK.png', import.meta.url).href, resource: 'Energy', amount: '+10' },
];

const traderEntries = [
  { name: 'Friendly', sprite: new URL('../assets/traders/Friendly.png', import.meta.url).href },
  { name: 'Generous', sprite: new URL('../assets/traders/Generous.png', import.meta.url).href },
  { name: 'Greedy', sprite: new URL('../assets/traders/Greedy.png', import.meta.url).href },
  { name: 'Lowballer', sprite: new URL('../assets/traders/Lowballer.png', import.meta.url).href },
];

const Legend = () => {
  return (
    <section className="wss-guide">
      <section className="guide-section" aria-labelledby="guide-terrains">
        <h2 className="guide-section-title" id="guide-terrains">Terrains</h2>
        <div className="terrain-list">
          {terrainEntries.map(({ name, className, water, energy, goal }) => (
            <article className="terrain-entry" key={name}>
              <div className="terrain-name">
                <span className={`guide-terrain-swatch ${className}`} aria-hidden="true">
                  {goal && <img src={goalImg} alt="" width="16" height="16" />}
                </span>
                <span>{name}</span>
              </div>
              <dl className="guide-effects">
                <div>
                  <dt className="resource-water">Water</dt>
                  <dd>{water}</dd>
                </div>
                <div>
                  <dt className="resource-energy">Energy</dt>
                  <dd>{energy}</dd>
                </div>
              </dl>
            </article>
          ))}
        </div>
      </section>

      <section className="guide-section" aria-labelledby="guide-items">
        <h2 className="guide-section-title" id="guide-items">Items</h2>
        <div className="item-list">
          {itemEntries.map(({ name, sprite, resource, amount }) => (
            <article className="item-entry" key={name}>
              <div className="guide-entry-name">
                <img className="guide-sprite" src={sprite} alt="" width="32" height="32" />
                <span>{name}</span>
              </div>
              <dl className="guide-effects">
                <div>
                  <dt className={`resource-${resource.toLowerCase()}`}>{resource}</dt>
                  <dd>{amount}</dd>
                </div>
              </dl>
            </article>
          ))}
        </div>
      </section>

      <section className="guide-section" aria-labelledby="guide-traders">
        <h2 className="guide-section-title" id="guide-traders">Traders</h2>
        <div className="trader-list">
          {traderEntries.map(({ name, sprite }) => (
            <div className="trader-entry" key={name}>
              <img className="guide-sprite" src={sprite} alt="" width="32" height="32" />
              <span>{name}</span>
            </div>
          ))}
        </div>
      </section>
    </section>
  );
};

export default Legend;
