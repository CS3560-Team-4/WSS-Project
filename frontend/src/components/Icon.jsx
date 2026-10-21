const paths = {
  book: 'M4 4h6l2 2 2-2h6v15h-6l-2 2-2-2H4V4Zm8 2v15',
  brain: 'M9 18H7a3 3 0 0 1-3-3 3 3 0 0 1 0-5 3 3 0 0 1 3-4 3 3 0 0 1 5-2 3 3 0 0 1 5 2 3 3 0 0 1 3 4 3 3 0 0 1 0 5 3 3 0 0 1-3 3h-2M12 4v17M7 10l2 2m8-2-2 2M7 16l2-2m8 2-2-2',
  eye: 'M2 12s4-7 10-7 10 7 10 7-4 7-10 7S2 12 2 12Zm13 0a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z',
  reset: 'M4 10a8 8 0 1 1 1 8M4 4v6h6',
  arrow: 'M5 12h14m-5-5 5 5-5 5',
  pause: 'M8 5v14M16 5v14',
  compass: 'm16 8-3 5-5 3 3-5 5-3ZM21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z',
  coins: 'M22 16a6 6 0 1 1-12 0 6 6 0 0 1 12 0ZM14 8A6 6 0 1 1 2 8a6 6 0 0 1 12 0ZM8 5v6M16 13v6',
};

const Icon = ({ name, className = '' }) => (
  <svg className={`ui-icon ${className}`} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
    <path d={paths[name] || paths.compass} />
  </svg>
);

export default Icon;
