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

(function bindOrderPage() {
  const btn = document.getElementById('orderBtn');
  if (btn) btn.addEventListener('click', placeOrder);
})();
