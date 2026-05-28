const API_BASE = 'http://localhost:2026/api';

async function apiGet(path) {
  const res = await fetch(`${API_BASE}${path}`);
  return await res.json();
}

async function apiPost(path, payload) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  return await res.json();
}
