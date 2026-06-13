/**
 * Sends create-order API request for current product.
 */
async function placeOrder() {
  clearMessage();
  if (window._placingOrder) return;
  const orderBtn = document.getElementById('orderBtn');
  const loginUser = requireLogin();
  if (!loginUser) return;
  const productId = Number(getQueryParam('id'));
  if (!productId) return showMessage('找不到商品 ID。', true);
  window._placingOrder = true;
  if (orderBtn) {
    orderBtn.disabled = true;
    orderBtn.dataset.orig = orderBtn.textContent;
    orderBtn.textContent = '下單中...';
  }
  const result = await apiPost('/orders', { buyerId: loginUser.userId, productId });
  if (result.success) {
    showMessage('下單成功。', false);
    // Refresh product detail to reflect updated status
    try { if (typeof loadProductDetail === 'function') loadProductDetail(); } catch (e) { console.error(e); }
  } else {
    showMessage(result.message || '下單失敗。', true);
  }
  if (orderBtn) {
    orderBtn.disabled = false;
    orderBtn.textContent = orderBtn.dataset.orig || '立即下單';
  }
  window._placingOrder = false;
  return result;
}

/**
 * Loads order list for the current login user.
 */
async function loadMyOrders() {
  clearMessage();
  const user = requireLogin();
  if (!user) return;

  const container = document.getElementById('orderList');
  if (!container) return;

  const result = await apiGet(`/orders/my?buyerId=${encodeURIComponent(user.userId)}`);
  if (!result.success) {
    showMessage(result.message || '載入訂單失敗。', true);
    container.innerHTML = '';
    return;
  }

  const list = result.data || [];
  container.innerHTML = '';
  if (list.length === 0) {
    container.innerHTML = '<div class="empty-state">目前沒有訂單</div>';
    return;
  }

  list.forEach((item) => {
    const row = document.createElement('article');
    row.className = 'card order-card';
    row.innerHTML = `
      <h3>${escapeHtml(item.productTitle || '未命名商品')}</h3>
      <div class="product-price">${formatPrice(item.price)}</div>
      <dl class="detail-list">
        <div class="detail-row"><dt>訂單編號</dt><dd>#${escapeHtml(item.orderId)}</dd></div>
        <div class="detail-row"><dt>狀態</dt><dd>${getOrderStatusBadge(item.status)}</dd></div>
        <div class="detail-row"><dt>商品編號</dt><dd>#${escapeHtml(item.productId)}</dd></div>
        <div class="detail-row"><dt>建立時間</dt><dd>${escapeHtml(formatDate(item.createdAt) || '-')}</dd></div>
      </dl>
    `;
    container.appendChild(row);
  });
}

function getOrderStatusBadge(status) {
  const s = (status || '').toString();
  const label = s === 'COMPLETED' ? '交易完成' : (s === 'PENDING' ? '待交易' : escapeHtml(s || '未知'));
  const cls = s === 'COMPLETED' ? 'badge badge-success' : 'badge badge-muted';
  return `<span class="${cls}">${escapeHtml(label)}</span>`;
}

(function bindOrderPage() {
  let btn = document.getElementById('orderBtn');
  if (btn) {
    // Remove any previously attached event listeners by replacing the node with a clean clone
    const newBtn = btn.cloneNode(true);
    btn.parentNode.replaceChild(newBtn, btn);
    btn = document.getElementById('orderBtn');

    // Attach a single onclick handler (assignment avoids accumulating listeners)
    btn.onclick = () => {
      // keep existing login redirect behavior
      const loginUser = requireLogin();
      if (!loginUser) return;

      // use the current loaded product if available
      const p = window.currentProduct;
      if (!p) return showMessage('找不到商品資訊。', true);

      // Do not open modal for SOLD or disabled button
      if (String(p.status).toUpperCase() !== 'ACTIVE' || btn.disabled) return;

      if (typeof window.openOrderConfirmModal === 'function') {
        window.openOrderConfirmModal(p);
      } else {
        // If modal unavailable, show a message but do not directly place order
        showMessage('目前無法開啟下單確認視窗，請稍後再試。', true);
      }
    };
  }

  if (document.getElementById('orderList')) {
    loadMyOrders();
  }
})();
