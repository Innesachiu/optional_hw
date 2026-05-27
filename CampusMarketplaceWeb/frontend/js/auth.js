/** Handles login action. */
async function handleLogin() {
  const username = document.getElementById('username')?.value?.trim();
  const password = document.getElementById('password')?.value || '';
  if (!username || !password) return showMessage('Username and password are required', true);

  const result = await apiPost('/auth/login', { username, password });
  if (result.success) {
    localStorage.setItem('username', username);
    localStorage.setItem('userId', String(result.userId || 1));
    showMessage('Login success', false);
    window.location.href = 'home.html';
  } else {
    showMessage(result.message || 'Login failed', true);
  }
}

/** Handles register action. */
async function handleRegister() {
  const username = document.getElementById('username')?.value?.trim();
  const email = document.getElementById('email')?.value?.trim();
  const password = document.getElementById('password')?.value || '';
  if (!username || !email || !password) return showMessage('All fields are required', true);

  const result = await apiPost('/auth/register', { username, email, password });
  if (result.success) {
    showMessage('Register success, please login', false);
    setTimeout(() => (window.location.href = 'login.html'), 500);
  } else {
    showMessage(result.message || 'Register failed', true);
  }
}

/** Clears local login data and redirects to login page. */
function handleLogout() {
  localStorage.removeItem('userId');
  localStorage.removeItem('username');
  window.location.href = 'login.html';
}

/**
 * Updates nav state based on login status.
 * Expects optional elements: #navAuth, #navUser, .logout-btn
 */
function renderNavState() {
  const userId = localStorage.getItem('userId');
  const username = localStorage.getItem('username') || '';
  const navAuth = document.getElementById('navAuth');
  const navUser = document.getElementById('navUser');

  if (userId) {
    if (navUser) navUser.textContent = `Hi, ${username}`;
    if (navAuth) navAuth.innerHTML = '<button class="logout-btn">Logout</button>';
  } else {
    if (navUser) navUser.textContent = 'Guest';
    if (navAuth) navAuth.innerHTML = '<a href="login.html">Login</a> / <a href="register.html">Register</a>';
  }

  document.querySelectorAll('.logout-btn').forEach((btn) => {
    btn.onclick = handleLogout;
  });
}

(function bindAuthPage() {
  document.getElementById('loginBtn')?.addEventListener('click', handleLogin);
  document.getElementById('registerBtn')?.addEventListener('click', handleRegister);
  renderNavState();
})();
