/** Returns query parameter value from current URL. */
function getQueryParam(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}

/** Formats number to currency-like text. */
function formatPrice(value) {
  const num = Number(value || 0);
  // NT$ currency formatting
  return `NT$ ${num.toLocaleString('en-US')}`;
}

/** Formats an ISO/local datetime string to readable local format. */
function formatDate(value) {
  if (!value) return '-';
  try {
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return value;
    return d.toLocaleString();
  } catch (e) {
    return value;
  }
}

/** Escapes dynamic text before injecting into HTML. */
function escapeHtml(value) {
  return String(value ?? '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

/** Shows message in #message area if available. */
function showMessage(message, isError) {
  const el = document.getElementById('message');
  if (!el) return;
  el.textContent = message || '';
  el.className = 'message is-visible ' + (isError ? 'is-error' : 'is-success');
  el.setAttribute('aria-live', isError ? 'assertive' : 'polite');
  el.setAttribute('role', 'status');
}

/** Clears message in #message area if available. */
function clearMessage() {
  const el = document.getElementById('message');
  if (!el) return;
  el.textContent = '';
  el.className = 'message';
}

/** Ensures user is logged in or redirects to login page. */
function requireLogin() {
  const userId = localStorage.getItem('userId');
  if (!userId) {
    showMessage('請先登入', true);
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 300);
    return null;
  }
  return {
    userId: Number(userId),
    username: localStorage.getItem('username') || ''
  };
}
