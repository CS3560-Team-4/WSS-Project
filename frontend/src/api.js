const defaultApiBaseUrl = import.meta.env.DEV
  ? 'http://localhost:8080'
  : 'https://netricsports.us/game-api';

const apiBaseUrl = (import.meta.env.VITE_API_BASE_URL || defaultApiBaseUrl)
  .replace(/\/$/, '');

export const apiUrl = (path) => {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  return `${apiBaseUrl}${normalizedPath}`;
};
