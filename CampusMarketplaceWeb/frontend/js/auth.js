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
    return showMessage('帳號與密碼皆為必填。', true);
  }
  if (loginBtn) {
    loginBtn.disabled = true;
    loginBtn.dataset.orig = loginBtn.textContent;
    loginBtn.textContent = '登入中...';
  }
  const result = await apiPost('/auth/login', { username, password });
  if (result.success) {
    const user = result.data;
    if (user && user.userId && user.username) {
      localStorage.setItem('userId', String(user.userId));
      localStorage.setItem('username', user.username);
      if (typeof renderNavState === 'function') renderNavState();
      showMessage('登入成功，正在轉跳...', false);
      setTimeout(() => (window.location.href = 'home.html'), 450);
    } else {
      showMessage('登入成功，但伺服器沒有回傳使用者資料。', true);
    }
  } else {
    showMessage(result.message || '登入失敗', true);
  }
  if (loginBtn) {
    loginBtn.disabled = false;
    loginBtn.textContent = loginBtn.dataset.orig || '登入';
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
    return showMessage('所有欄位皆為必填。', true);
  }
  // simple email format check
  if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
    window._registerSubmitting = false;
    return showMessage('請輸入有效的電子郵件地址。', true);
  }
  if (password !== confirm) {
    window._registerSubmitting = false;
    return showMessage('密碼與確認密碼不相符。', true);
  }
  if (registerBtn) {
    registerBtn.disabled = true;
    registerBtn.dataset.orig = registerBtn.textContent;
    registerBtn.textContent = '建立中...';
  }
  const result = await apiPost('/auth/register', { username, email, password });
  if (result.success) {
    showMessage('註冊成功，請登入。', false);
    setTimeout(() => (window.location.href = 'login.html'), 700);
  } else {
    showMessage(result.message || '註冊失敗', true);
  }
  if (registerBtn) {
    registerBtn.disabled = false;
    registerBtn.textContent = registerBtn.dataset.orig || '註冊';
  }
  window._registerSubmitting = false;
}

/** Clears local login data and redirects to login page. */
function handleLogout() {
  localStorage.removeItem('userId');
  localStorage.removeItem('username');
  if (typeof renderNavState === 'function') renderNavState();
  window.location.href = 'login.html';
}

// Note: navbar rendering is handled centrally in `navbar.js` to avoid
// duplicative DOM insertion. This file keeps auth actions only.

(function bindAuthPage() {
  document.getElementById('loginForm')?.addEventListener('submit', handleLogin);
  document.getElementById('registerForm')?.addEventListener('submit', handleRegister);
  document.getElementById('loginBtn')?.addEventListener('click', (event) => {
    if (!document.getElementById('loginForm')) handleLogin(event);
  });
  document.getElementById('registerBtn')?.addEventListener('click', (event) => {
    if (!document.getElementById('registerForm')) handleRegister(event);
  });
  // If already logged in, visiting login/register should go to home
  const userId = localStorage.getItem('userId');
  const isAuthPage = window.location.pathname.endsWith('/login.html') || window.location.pathname.endsWith('/register.html');
  if (userId && isAuthPage) {
    window.location.href = 'home.html';
  }
})();
