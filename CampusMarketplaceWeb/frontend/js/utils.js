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

/** Shows message in #message area if available. */
function showMessage(message, isError) {
  const el = document.getElementById('message');
  if (!el) return;
  el.textContent = message || '';
  el.style.color = isError ? 'red' : 'green';
}

/** Ensures user is logged in or redirects to login page. */
function requireLogin() {
  const userId = localStorage.getItem('userId');
  if (!userId) {
    showMessage('Please login first', true);
    window.location.href = 'login.html';
    return null;
  }
  return {
    userId: Number(userId),
    username: localStorage.getItem('username') || ''
  };
}
