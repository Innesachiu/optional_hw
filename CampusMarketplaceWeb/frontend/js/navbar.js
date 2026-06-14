// Navbar rendering and initialization (stable, id-based)
(function navbarInit(){
  function renderNavState() {
    const userId = localStorage.getItem('userId');
    const username = localStorage.getItem('username');
    const isLoggedIn = Boolean(userId && username);

    const addProduct = document.getElementById('navAddProduct');
    const myProducts = document.getElementById('navMyProducts');
    const favorites = document.getElementById('navFavorites');
    const myOrders = document.getElementById('navMyOrders');
    const guest = document.getElementById('navGuest');
    const usernameElement = document.getElementById('navUsername');
    const loginRegister = document.getElementById('navLoginRegister');
    const logout = document.getElementById('navLogout');

    if (addProduct) addProduct.hidden = !isLoggedIn;
    if (myProducts) myProducts.hidden = !isLoggedIn;
    if (favorites) {
      favorites.hidden = false;
      favorites.href = 'my-favorites.html';
      favorites.style.pointerEvents = 'auto';
      favorites.style.cursor = 'pointer';
    }
    if (myOrders) myOrders.hidden = !isLoggedIn;
    if (logout) logout.hidden = !isLoggedIn;

    if (guest) guest.hidden = isLoggedIn;
    if (loginRegister) loginRegister.hidden = isLoggedIn;

    if (usernameElement) {
      usernameElement.hidden = !isLoggedIn;
      usernameElement.textContent = isLoggedIn ? username : '';
    }
  }

  // Expose renderNavState for other modules if needed
  window.renderNavState = renderNavState;

  // sync across tabs
  window.addEventListener('storage', (e) => {
    if (e.key === 'userId' || e.key === 'username') renderNavState();
  });

  // bind logout safely (remove old handlers by replacing element)
  function bindLogout() {
    const logout = document.getElementById('navLogout');
    if (!logout) return;
    const parent = logout.parentNode;
    const newLogout = logout.cloneNode(true);
    parent.replaceChild(newLogout, logout);
    newLogout.addEventListener('click', () => {
      if (typeof handleLogout === 'function') {
        handleLogout();
        return;
      }
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
      renderNavState();
      window.location.href = 'login.html';
    });
  }

  function init() {
    bindLogout();
    renderNavState();
  }

  // Initialize immediately if DOM already ready, otherwise on DOMContentLoaded
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  // small keyboard helper
  document.addEventListener('keydown', (ev)=>{
    if (ev.key === 'Escape') {
      const active = document.activeElement;
      if (active && active.classList && active.classList.contains('keyword-chip')) {
        active.blur();
      }
    }
  });
})();
