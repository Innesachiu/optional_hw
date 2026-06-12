/** Returns query parameter value from current URL. */
function getQueryParam(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}

/** Formats number to currency-like text. */
function formatPrice(value) {
  const num = Number(value || 0);
  return `$${num.toLocaleString()}`;
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
    showMessage('Please login first', true);
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
