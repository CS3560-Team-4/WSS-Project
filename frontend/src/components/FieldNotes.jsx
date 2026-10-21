import Icon from './Icon.jsx';
import goalImg from '../assets/exit.png';

const FieldNotes = () => (
  <section className="field-notes">
    <Icon name="compass" />
    <h2>A note for the road</h2>
    <p>Find the exit. Keep an eye on your resources. The shortest path isn’t always the kindest.</p>
    <div className="destination-note"><img src={goalImg} alt="" /><span>Your destination</span></div>
    <div className="field-note-rule" />
    <p className="field-tip">Supplies help you go further. Traders might, too.</p>
  </section>
);

export default FieldNotes;
