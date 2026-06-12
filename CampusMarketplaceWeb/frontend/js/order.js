/**
 * Sends create-order API request for current product.
 */
async function placeOrder() {
  clearMessage();
  const loginUser = requireLogin();
  if (!loginUser) return;
  const productId = Number(getQueryParam('id'));
  if (!productId) return showMessage('Missing product id.', true);

  const result = await apiPost('/orders', { buyerId: loginUser.userId, productId });
  if (result.success) {
    showMessage('Order success. Redirecting to My Orders...', false);
    setTimeout(() => (window.location.href = 'my-orders.html'), 700);
  } else {
    showMessage(result.message || 'Order failed.', true);
  }
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
    showMessage(result.message || 'Load orders failed.', true);
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
      <h3>${escapeHtml(item.productTitle || 'Untitled product')}</h3>
      <div class="product-price">${formatPrice(item.price)}</div>
      <dl class="detail-list">
        <div class="detail-row"><dt>Order</dt><dd>#${escapeHtml(item.orderId)}</dd></div>
        <div class="detail-row"><dt>Status</dt><dd>${getOrderStatusBadge(item.status)}</dd></div>
        <div class="detail-row"><dt>Product ID</dt><dd>#${escapeHtml(item.productId)}</dd></div>
        <div class="detail-row"><dt>Created</dt><dd>${escapeHtml(item.createdAt || '-')}</dd></div>
      </dl>
    `;
    container.appendChild(row);
  });
}

function getOrderStatusBadge(status) {
  const cls = status === 'COMPLETED' ? 'badge badge-success' : 'badge badge-muted';
  return `<span class="${cls}">${escapeHtml(status || 'UNKNOWN')}</span>`;
}

(function bindOrderPage() {
  const btn = document.getElementById('orderBtn');
  if (btn) btn.addEventListener('click', placeOrder);

  if (document.getElementById('orderList')) {
    loadMyOrders();
  }
})();
