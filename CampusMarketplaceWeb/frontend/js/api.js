const API_BASE_URL = `${window.location.protocol}//${window.location.hostname}:2026/api`;

async function apiGet(path) {
  try {
    const res = await fetch(`${API_BASE}${path}`);
    return await res.json();
  } catch (error) {
    return { success: false, message: 'Cannot connect to API server.' };
  }
}

async function apiPost(path, payload) {
  try {
    const res = await fetch(`${API_BASE}${path}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    return await res.json();
  } catch (error) {
    return { success: false, message: 'Cannot connect to API server.' };
  }
}
