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
      return { success: false, message: `API 錯誤：${res.status}` };
    }
    return payload ?? { success: false, message: '伺服器回傳無效的資料。' };
  } catch (erroㄎr) {
    console.error('apiGet error', error);
    return { success: false, message: '無法連線到 API 伺服器。' };
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
      return { success: false, message: `API 錯誤：${res.status}` };
    }
    return data ?? { success: false, message: '伺服器回傳無效的資料。' };
  } catch (error) {
    console.error('apiPost error', error);
    return { success: false, message: '無法連線到 API 伺服器。' };
  }
}

async function apiDelete(path) {
  try {
    const res = await fetch(`${API_BASE}${path}`, { method: 'DELETE' });
    const data = await safeParseJson(res);
    if (!res.ok) {
      if (data && typeof data.message === 'string') return { success: false, message: data.message };
      return { success: false, message: `API 錯誤：${res.status}` };
    }
    return data ?? { success: false, message: '伺服器回傳無效的資料。' };
  } catch (error) {
    console.error('apiDelete error', error);
    return { success: false, message: '無法連線到 API 伺服器。' };
  }
}
