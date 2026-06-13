(function(){
  async function loadMyProducts() {
    clearMessage();
    const userId = localStorage.getItem('userId');
    if (!userId) {
      window.location.href = 'login.html';
      return;
    }
    const container = document.getElementById('myProductList');
    if (!container) return;
    container.innerHTML = '<div class="empty-state">載入我的商品中…</div>';
    const res = await apiGet(`/products/my?sellerId=${encodeURIComponent(userId)}`);
    if (!res) return showMessage('載入商品失敗。', true);
    if (!res.success) {
      showMessage(res.message || '載入商品失敗。', true);
      container.innerHTML = '';
      return;
    }
    const list = res.data || [];
    if (list.length === 0) {
      container.innerHTML = '<div class="empty-state">你目前還沒有上架任何商品<br><a class="btn btn-primary" href="add-product.html" style="margin-top:12px; display:inline-block;">立即上架商品</a></div>';
      return;
    }
    // store for client-side filtering
    window._myProducts = list;
    renderMyProducts(list);
  }

  function renderMyProducts(list) {
    const container = document.getElementById('myProductList');
    if (!container) return;
    container.innerHTML = '';
    list.forEach((p) => {
      const card = document.createElement('article');
      card.className = 'card product-card';
      const title = escapeHtml(p.title || '未命名商品');
      const desc = escapeHtml(p.description || '');
      card.innerHTML = `
        <div class="product-image" aria-hidden="true"></div>
        <div class="product-meta">#${escapeHtml(p.productId)}</div>
        <h3 class="product-title">${title}</h3>
        <div class="product-price">${formatPrice(p.price)}</div>
        <div class="product-desc">${desc}</div>
        <div class="actions">${getStatusBadge(p.status)} <a class="btn btn-secondary" href="product-detail.html?id=${encodeURIComponent(p.productId)}">查看詳情</a></div>
      `;
      card.addEventListener('click', (ev)=>{
        // avoid double navigate when clicking inner button
        if (ev.target.tagName.toLowerCase() === 'a' || ev.target.closest('a')) return;
        window.location.href = `product-detail.html?id=${encodeURIComponent(p.productId)}`;
      });
      container.appendChild(card);
    });
  }

  // bind tabs
  function bindMyProductTabs() {
    const tabs = document.querySelectorAll('.tabs .tab');
    if (!tabs || tabs.length === 0) return;
    tabs.forEach(t => t.addEventListener('click', () => {
      tabs.forEach(x => x.classList.remove('active'));
      t.classList.add('active');
      const filter = t.dataset.filter;
      const all = window._myProducts || [];
      const out = filter === 'ALL' ? all : all.filter(it => it.status === filter);
      renderMyProducts(out);
    }));
  }

  // reuse product status badge from product.js; ensure function exists
  if (typeof getStatusBadge !== 'function') {
    window.getStatusBadge = function(status) {
      const s = (status || '').toString();
      const label = s === 'ACTIVE' ? '販售中' : (s === 'SOLD' ? '已售出' : escapeHtml(s || '未知'));
      const cls = s === 'ACTIVE' ? 'badge badge-success' : 'badge badge-muted';
      return `<span class="${cls}">${escapeHtml(label)}</span>`;
    };
  }

  document.addEventListener('DOMContentLoaded', () => { loadMyProducts().then(()=>bindMyProductTabs()); });
})();
