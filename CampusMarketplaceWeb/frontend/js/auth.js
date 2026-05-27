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

function handleLogout() {
  localStorage.removeItem('userId');
  localStorage.removeItem('username');
  window.location.href = 'login.html';
}

(function bindAuthPage() {
  document.getElementById('loginBtn')?.addEventListener('click', handleLogin);
  document.getElementById('registerBtn')?.addEventListener('click', handleRegister);
  document.getElementById('logoutBtn')?.addEventListener('click', handleLogout);
  const welcome = document.getElementById('welcome');
  if (welcome) {
    const name = localStorage.getItem('username');
    welcome.textContent = name ? `Hello, ${name}` : 'Not logged in';
  }
})();
