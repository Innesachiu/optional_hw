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
    container.innerHTML = '<div class="empty-state">Loading your products...</div>';
    const res = await apiGet(`/products/my?sellerId=${encodeURIComponent(userId)}`);
    if (!res) return showMessage('Failed to load products.', true);
    if (!res.success) {
      showMessage(res.message || 'Failed to load products.', true);
      container.innerHTML = '';
      return;
    }
    const list = res.data || [];
    container.innerHTML = '';
    if (list.length === 0) {
      container.innerHTML = '<div class="empty-state">你目前還沒有上架任何商品<br><a class="btn btn-primary" href="add-product.html" style="margin-top:12px; display:inline-block;">立即上架商品</a></div>';
      return;
    }
    list.forEach((p) => {
      const card = document.createElement('article');
      card.className = 'card product-card';
      const title = escapeHtml(p.title || 'Untitled');
      const desc = escapeHtml(p.description || '');
      card.innerHTML = `
        <div class="product-meta">#${escapeHtml(p.productId)}</div>
        <h3 class="product-title">${title}</h3>
        <div class="product-price">${formatPrice(p.price)}</div>
        <div class="product-desc">${desc}</div>
        <div class="actions">${getStatusBadge(p.status)} <a class="btn btn-secondary" href="product-detail.html?id=${encodeURIComponent(p.productId)}">View</a></div>
      `;
      card.addEventListener('click', (ev)=>{
        // avoid double navigate when clicking inner button
        if (ev.target.tagName.toLowerCase() === 'a' || ev.target.closest('a')) return;
        window.location.href = `product-detail.html?id=${encodeURIComponent(p.productId)}`;
      });
      container.appendChild(card);
    });
  }

  // reuse product status badge from product.js; ensure function exists
  if (typeof getStatusBadge !== 'function') {
    window.getStatusBadge = function(status) {
      const safeStatus = escapeHtml(status || 'UNKNOWN');
      const cls = status === 'ACTIVE' ? 'badge badge-success' : 'badge badge-muted';
      return `<span class="${cls}">${safeStatus}</span>`;
    };
  }

  document.addEventListener('DOMContentLoaded', loadMyProducts);
})();
