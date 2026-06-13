(async function(){
  function requireLoginRedirect() {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      window.location.href = 'login.html';
      return null;
    }
    return Number(userId);
  }

  function createProductCard(p) {
    const article = document.createElement('article');
    article.className = 'card product-card';

    const meta = document.createElement('div');
    meta.className = 'product-meta';
    meta.textContent = `#${p.productId}`;

    const h3 = document.createElement('h3');
    h3.className = 'product-title';
    h3.textContent = p.title;

    const row = document.createElement('div');
    row.className = 'product-row';

    const price = document.createElement('div');
    price.className = 'product-price';
    price.textContent = formatPrice(p.price);

    const actions = document.createElement('div');
    actions.className = 'actions';

    // Local status-to-badge helper to avoid depending on external script load order
    function makeStatusBadge(statusValue) {
      const s = (statusValue || '').toString();
      const span = document.createElement('span');
      span.className = 'badge';
      let label = '未知';
      if (s === 'ACTIVE') {
        label = '販售中';
        span.classList.add('status-active');
      } else if (s === 'SOLD') {
        label = '已售出';
        span.classList.add('status-sold');
      } else {
        span.classList.add('status-unknown');
      }
      span.textContent = label;
      const wrapper = document.createElement('div');
      wrapper.appendChild(span);
      return wrapper;
    }

    const status = makeStatusBadge(p && p.status);

    const viewBtn = document.createElement('a');
    viewBtn.className = 'btn btn-secondary';
    viewBtn.href = `product-detail.html?id=${encodeURIComponent(p.productId)}`;
    viewBtn.textContent = '查看詳情';

    const unfav = document.createElement('button');
    unfav.className = 'btn btn-secondary';
    unfav.type = 'button';
    unfav.textContent = '取消收藏';
    unfav.addEventListener('click', async (ev) => {
      ev.preventDefault();
      unfav.disabled = true;
      const userId = Number(localStorage.getItem('userId'));
      const res = await apiDelete(`/favorites?userId=${encodeURIComponent(userId)}&productId=${encodeURIComponent(p.productId)}`);
      if (res.success) {
        article.remove();
        showMessage(res.message || '已從我的最愛移除。', false);
      } else {
        showMessage(res.message || '移除失敗。', true);
        unfav.disabled = false;
      }
    });

    actions.appendChild(status);
    actions.appendChild(viewBtn);
    actions.appendChild(unfav);

    article.appendChild(meta);
    article.appendChild(h3);
    article.appendChild(row);
    article.appendChild(actions);
    return article;
  }

  async function loadFavorites() {
    const userId = requireLoginRedirect();
    if (!userId) return;
    const container = document.getElementById('favoriteList');
    container.innerHTML = '<div class="empty-state">載入我的最愛中…</div>';
    const res = await apiGet(`/favorites?userId=${encodeURIComponent(userId)}`);
    if (!res.success) return showMessage(res.message || '載入我的最愛失敗。', true);
    const list = res.data || [];
    container.innerHTML = '';
    if (!Array.isArray(list) || list.length === 0) {
      container.innerHTML = '<div class="empty-state">你目前還沒有收藏任何商品</div>';
      return;
    }
    const frag = document.createDocumentFragment();
    list.forEach((p) => {
      const card = createProductCard(p);
      frag.appendChild(card);
    });
    container.appendChild(frag);
  }

  // initialize
  document.addEventListener('DOMContentLoaded', () => {
    loadFavorites();
  });
})();
