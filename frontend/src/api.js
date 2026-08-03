const defaultApiBaseUrl = import.meta.env.DEV
  ? 'http://localhost:8080'
  : 'https://netricsports.us/game-api';

const apiBaseUrl = (import.meta.env.VITE_API_BASE_URL || defaultApiBaseUrl)
  .replace(/\/$/, '');

const sessionStorageKey = 'wss-game-session-id';

const gameSessionId = () => {
  let sessionId = sessionStorage.getItem(sessionStorageKey);

  if (!sessionId) {
    sessionId = crypto.randomUUID();
    sessionStorage.setItem(sessionStorageKey, sessionId);
  }

  return sessionId;
};

export const apiUrl = (path) => {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  return `${apiBaseUrl}${normalizedPath}`;
};

export const apiFetch = (path, options = {}) => {
  const headers = new Headers(options.headers);
  headers.set('X-Game-Session', gameSessionId());

  return fetch(apiUrl(path), {
    ...options,
    headers,
  });
};
