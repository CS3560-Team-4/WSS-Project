import Icon from './Icon.jsx';

const modes = ['balanced', 'explorer', 'greedy'];

const GameSidebar = ({ onGuide, onBrain, onVision, onReset, autoPlayEnabled, autoPlayMode, onAutoPlay, onStop }) => (
  <aside className="field-sidebar" aria-label="Game tools">
    <section className="sidebar-section">
      <h2>Your field kit</h2>
      <nav className="field-tools" aria-label="Field kit">
        <button onClick={onGuide} aria-label="Guide"><Icon name="book" /><span>Guide</span><Icon name="arrow" className="tool-arrow" /></button>
        <button onClick={onBrain} aria-label="The Brain"><Icon name="brain" /><span>The Brain</span><Icon name="arrow" className="tool-arrow" /></button>
        <button onClick={onVision} aria-label="Set Vision"><Icon name="eye" /><span>Set Vision</span><Icon name="arrow" className="tool-arrow" /></button>
      </nav>
    </section>
    <section className="sidebar-section autoplay-section">
      <h2>Auto-Play</h2>
      <p>Let a brain lead the way.</p>
      <div className="autoplay-options">
        {modes.map((mode) => (
          <button key={mode} onClick={() => onAutoPlay(mode)} aria-label={`Auto-Play (${mode[0].toUpperCase() + mode.slice(1)})`} aria-pressed={autoPlayEnabled && autoPlayMode === mode}>
            <span className="mode-indicator" aria-hidden="true" />
            <span className="mode-label">{mode}</span>
            <span className="mode-action" aria-hidden="true">{autoPlayEnabled && autoPlayMode === mode ? 'on' : 'play'}</span>
          </button>
        ))}
      </div>
      {autoPlayEnabled && <button className="stop-button" onClick={onStop}><Icon name="pause" />Stop Auto-Play</button>}
    </section>
    <button onClick={onReset} className="reset-button"><Icon name="reset" />Reset Game</button>
  </aside>
);

export default GameSidebar;
