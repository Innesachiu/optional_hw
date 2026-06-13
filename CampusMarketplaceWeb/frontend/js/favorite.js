(function favoriteModule(){
  async function updateButtonState(btn, favorited) {
    if (!btn) return;
    btn.disabled = false;
    if (favorited) {
      btn.classList.add('is-favorited');
      btn.textContent = '♥ 已收藏';
    } else {
      btn.classList.remove('is-favorited');
      btn.textContent = '♡ 加入最愛';
    }
  }

  async function checkFavorited(userId, productId) {
    const res = await apiGet(`/favorites/check?userId=${encodeURIComponent(userId)}&productId=${encodeURIComponent(productId)}`);
    if (!res.success) return { success: false, favorited: false };
    try {
      const data = res.data || {};
      // data may be an object or JSON fragment string
      if (typeof data === 'string') {
        const obj = JSON.parse(data);
        return { success: true, favorited: Boolean(obj.favorited) };
      }
      return { success: true, favorited: Boolean(data.favorited) };
    } catch (e) {
      return { success: false, favorited: false };
    }
  }

  async function addFavorite(userId, productId) {
    const res = await apiPost('/favorites', { userId, productId });
    return res;
  }

  async function removeFavorite(userId, productId) {
    const res = await apiDelete(`/favorites?userId=${encodeURIComponent(userId)}&productId=${encodeURIComponent(productId)}`);
    return res;
  }

  function promptLogin() {
    showMessage('請先登入後再收藏商品。', true);
    setTimeout(() => window.location.href = 'login.html', 800);
  }

  async function initForProduct(productId) {
    const btn = document.getElementById('favorite-button');
    if (!btn) return;
    btn.hidden = false;
    const userId = Number(localStorage.getItem('userId'));
    const username = localStorage.getItem('username');
    const loggedIn = Boolean(userId && username);
    if (!loggedIn) {
      // show button but clicking will prompt login
      await updateButtonState(btn, false);
      btn.addEventListener('click', (ev) => {
        ev.preventDefault();
        promptLogin();
      });
      return;
    }

    // check current state
    btn.disabled = true;
    const state = await checkFavorited(userId, productId);
    if (!state.success) {
      btn.disabled = false;
      return showMessage('無法載入收藏狀態。', true);
    }
    await updateButtonState(btn, state.favorited);

    let busy = false;
    btn.addEventListener('click', async (ev) => {
      ev.preventDefault();
      if (busy) return;
      busy = true;
      btn.disabled = true;
      if (!state.favorited) {
        const res = await addFavorite(userId, productId);
        if (res.success) {
          state.favorited = true;
          await updateButtonState(btn, true);
          showMessage(res.message || '已加入我的最愛。', false);
        } else {
          showMessage(res.message || '加入我的最愛失敗。', true);
        }
      } else {
        const res = await removeFavorite(userId, productId);
        if (res.success) {
          state.favorited = false;
          await updateButtonState(btn, false);
          showMessage(res.message || '已從我的最愛移除。', false);
        } else {
          showMessage(res.message || '移除我的最愛失敗。', true);
        }
      }
      busy = false;
      btn.disabled = false;
    });
  }

  // Listen for productLoaded event dispatched by product.js
  document.addEventListener('productLoaded', (ev) => {
    const pid = ev?.detail?.productId;
    if (pid) initForProduct(pid);
  });

  // also attempt to initialize on DOMContentLoaded if product already present
  if (document.readyState !== 'loading') {
    const el = document.getElementById('productDetail');
    if (el && el.dataset && el.dataset.productId) {
      initForProduct(Number(el.dataset.productId));
    }
  } else {
    document.addEventListener('DOMContentLoaded', () => {
      const el = document.getElementById('productDetail');
      if (el && el.dataset && el.dataset.productId) {
        initForProduct(Number(el.dataset.productId));
      }
    });
  }
})();
