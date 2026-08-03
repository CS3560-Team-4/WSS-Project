const defaultApiBaseUrl = import.meta.env.DEV
  ? 'http://localhost:8080'
  : 'https://netricsports.us/game-api';

const apiBaseUrl = (import.meta.env.VITE_API_BASE_URL || defaultApiBaseUrl)
  .replace(/\/$/, '');

// sessionStorage normally belongs to one tab, but browsers copy it when a tab
// is duplicated. Coordinate with other open tabs so a copied ID is replaced
// before the first API request is sent.
const sessionStorageKey = 'wss-game-session-id-v2';
const sessionChannelName = 'wss-game-session-coordination-v2';
const sessionIdPattern = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
const tabId = crypto.randomUUID();
const sessionChannel = typeof BroadcastChannel === 'function'
  ? new BroadcastChannel(sessionChannelName)
  : null;

let activeSessionId = null;
let copiedSessionDetected = false;

if (sessionChannel) {
  sessionChannel.addEventListener('message', ({ data }) => {
    if (!data || data.tabId === tabId) return;

    if (data.type === 'session-probe' && data.sessionId === activeSessionId) {
      sessionChannel.postMessage({
        type: 'session-occupied',
        sessionId: activeSessionId,
        tabId,
        targetTabId: data.tabId,
      });
    }

    if (
      data.type === 'session-occupied'
      && data.targetTabId === tabId
      && data.sessionId === activeSessionId
    ) {
      copiedSessionDetected = true;
    }
  });
}

const newSessionId = () => {
  const sessionId = crypto.randomUUID();
  sessionStorage.setItem(sessionStorageKey, sessionId);
  activeSessionId = sessionId;
  return sessionId;
};

const establishGameSessionId = async () => {
  const storedSessionId = sessionStorage.getItem(sessionStorageKey);

  if (!storedSessionId || !sessionIdPattern.test(storedSessionId)) {
    return newSessionId();
  }

  activeSessionId = storedSessionId;

  // Without BroadcastChannel, favor isolation over preserving a game across a
  // reload. Modern browsers take the collision-detection path below.
  if (!sessionChannel) {
    return newSessionId();
  }

  copiedSessionDetected = false;
  sessionChannel.postMessage({
    type: 'session-probe',
    sessionId: activeSessionId,
    tabId,
  });

  await new Promise((resolve) => window.setTimeout(resolve, 100));

  return copiedSessionDetected ? newSessionId() : activeSessionId;
};

const gameSessionId = establishGameSessionId();

export const apiUrl = (path) => {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  return `${apiBaseUrl}${normalizedPath}`;
};

export const apiFetch = async (path, options = {}) => {
  const headers = new Headers(options.headers);
  headers.set('X-Game-Session', await gameSessionId);

  return fetch(apiUrl(path), {
    ...options,
    headers,
  });
};
