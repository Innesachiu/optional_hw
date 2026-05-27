/**
 * Sends create-order API request for current product.
 */
async function placeOrder() {
  const loginUser = requireLogin();
  if (!loginUser) return;
  const productId = Number(getQueryParam('id'));
  if (!productId) return showMessage('Missing product id', true);

  const result = await apiPost('/orders', { buyerId: loginUser.userId, productId });
  if (result.success) {
    showMessage('Order success', false);
    setTimeout(() => (window.location.href = 'home.html'), 600);
  } else {
    showMessage(result.message || 'Order failed', true);
  }
}

/**
 * Loads order list for the current login user.
 */
async function loadMyOrders() {
  const user = requireLogin();
  if (!user) return;

  const container = document.getElementById('orderList');
  if (!container) return;

  const result = await apiGet(`/orders/my?buyerId=${encodeURIComponent(user.userId)}`);
  if (!result.success) {
    showMessage(result.message || 'Load orders failed', true);
    container.innerHTML = '';
    return;
  }

  const list = result.data || [];
  container.innerHTML = '';
  if (list.length === 0) {
    container.textContent = '目前沒有訂單';
    return;
  }

  list.forEach((item) => {
    const row = document.createElement('div');
    row.style.border = '1px solid #ccc';
    row.style.padding = '8px';
    row.style.margin = '6px 0';
    row.innerHTML = `
      <div>Order #${item.orderId} | ${item.status}</div>
      <div>Product: ${item.productTitle} (ID: ${item.productId})</div>
      <div>Price: ${formatPrice(item.price)}</div>
      <div>Seller: ${item.sellerId} | Buyer: ${item.buyerId}</div>
      <div>Created: ${item.createdAt || '-'}</div>
    `;
    container.appendChild(row);
  });
}

(function bindOrderPage() {
  const btn = document.getElementById('orderBtn');
  if (btn) btn.addEventListener('click', placeOrder);

  if (document.getElementById('orderList')) {
    loadMyOrders();
  }
})();
