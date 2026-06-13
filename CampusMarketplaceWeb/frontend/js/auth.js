/** Handles login action. */
async function handleLogin(event) {
  if (event) event.preventDefault();
  clearMessage();
  if (window._loginSubmitting) return;
  window._loginSubmitting = true;
  const loginBtn = document.getElementById('loginBtn');
  const username = document.getElementById('username')?.value?.trim();
  const password = document.getElementById('password')?.value || '';
  if (!username || !password) {
    window._loginSubmitting = false;
    return showMessage('Username and password are required.', true);
  }
  if (loginBtn) {
    loginBtn.disabled = true;
    loginBtn.dataset.orig = loginBtn.textContent;
    loginBtn.textContent = 'Signing in...';
  }
  const result = await apiPost('/auth/login', { username, password });
  if (result.success) {
    // Store username. Only store userId if backend returns it.
    localStorage.setItem('username', username);
    // backend may return userId either at top-level or inside data
    const returnedUserId = result.userId ?? (result.data && result.data.userId) ?? null;
    if (returnedUserId != null) {
      localStorage.setItem('userId', String(returnedUserId));
    }
    showMessage('Login success. Redirecting...', false);
    setTimeout(() => (window.location.href = 'home.html'), 450);
  } else {
    showMessage(result.message || 'Login failed.', true);
  }
  if (loginBtn) {
    loginBtn.disabled = false;
    loginBtn.textContent = loginBtn.dataset.orig || 'Login';
  }
  window._loginSubmitting = false;
}

/** Handles register action. */
async function handleRegister(event) {
  if (event) event.preventDefault();
  clearMessage();
  if (window._registerSubmitting) return;
  window._registerSubmitting = true;
  const registerBtn = document.getElementById('registerBtn');
  const username = document.getElementById('username')?.value?.trim();
  const email = document.getElementById('email')?.value?.trim();
  const password = document.getElementById('password')?.value || '';
  const confirm = document.getElementById('confirmPassword')?.value || '';
  if (!username || !email || !password) {
    window._registerSubmitting = false;
    return showMessage('All fields are required.', true);
  }
  // simple email format check
  if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
    window._registerSubmitting = false;
    return showMessage('Please enter a valid email address.', true);
  }
  if (password !== confirm) {
    window._registerSubmitting = false;
    return showMessage('Password and confirmation do not match.', true);
  }
  if (registerBtn) {
    registerBtn.disabled = true;
    registerBtn.dataset.orig = registerBtn.textContent;
    registerBtn.textContent = 'Creating...';
  }
  const result = await apiPost('/auth/register', { username, email, password });
  if (result.success) {
    showMessage('Register success. Please login.', false);
    setTimeout(() => (window.location.href = 'login.html'), 700);
  } else {
    showMessage(result.message || 'Register failed.', true);
  }
  if (registerBtn) {
    registerBtn.disabled = false;
    registerBtn.textContent = registerBtn.dataset.orig || 'Register';
  }
  window._registerSubmitting = false;
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
    if (navUser) navUser.textContent = `Hi, ${username || 'student'}`;
    if (navAuth) {
      navAuth.innerHTML = '';
      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'btn btn-danger logout-btn';
      btn.textContent = 'Logout';
      navAuth.appendChild(btn);
    }
  } else {
    if (navUser) navUser.textContent = 'Guest';
    if (navAuth) {
      navAuth.innerHTML = '';
      const a1 = document.createElement('a'); a1.href = 'login.html'; a1.textContent = 'Login';
      const sep = document.createTextNode(' / ');
      const a2 = document.createElement('a'); a2.href = 'register.html'; a2.textContent = 'Register';
      navAuth.appendChild(a1); navAuth.appendChild(sep); navAuth.appendChild(a2);
    }
  }

  document.querySelectorAll('.logout-btn').forEach((btn) => {
    btn.onclick = handleLogout;
  });
}

(function bindAuthPage() {
  document.getElementById('loginForm')?.addEventListener('submit', handleLogin);
  document.getElementById('registerForm')?.addEventListener('submit', handleRegister);
  document.getElementById('loginBtn')?.addEventListener('click', (event) => {
    if (!document.getElementById('loginForm')) handleLogin(event);
  });
  document.getElementById('registerBtn')?.addEventListener('click', (event) => {
    if (!document.getElementById('registerForm')) handleRegister(event);
  });
  renderNavState();
  // If already logged in, visiting login/register should go to home
  const userId = localStorage.getItem('userId');
  const isAuthPage = window.location.pathname.endsWith('/login.html') || window.location.pathname.endsWith('/register.html');
  if (userId && isAuthPage) {
    window.location.href = 'home.html';
  }
})();
