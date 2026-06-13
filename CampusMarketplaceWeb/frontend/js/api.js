// Centralized API helper
const API_BASE_URL = `${window.location.protocol}//${window.location.hostname}:2026/api`;
const API_BASE = API_BASE_URL; // keep backward compatibility

async function safeParseJson(response) {
  try {
    return await response.json();
  } catch (e) {
    return null;
  }
}

async function apiGet(path) {
  try {
    const res = await fetch(`${API_BASE}${path}`);
    const payload = await safeParseJson(res);
    if (!res.ok) {
      if (payload && typeof payload.message === 'string') return { success: false, message: payload.message };
      return { success: false, message: `API error: ${res.status}` };
    }
    return payload ?? { success: false, message: 'Invalid JSON from server.' };
  } catch (error) {
    console.error('apiGet error', error);
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
    const data = await safeParseJson(res);
    if (!res.ok) {
      if (data && typeof data.message === 'string') return { success: false, message: data.message };
      return { success: false, message: `API error: ${res.status}` };
    }
    return data ?? { success: false, message: 'Invalid JSON from server.' };
  } catch (error) {
    console.error('apiPost error', error);
    return { success: false, message: 'Cannot connect to API server.' };
  }
}
