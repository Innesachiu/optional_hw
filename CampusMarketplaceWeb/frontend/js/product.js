function renderProductList(products) {
  const list = document.getElementById('productList');
  if (!list) return;
  list.innerHTML = '';
  if (!products || products.length === 0) {
    list.textContent = 'No products found';
    return;
  }
  products.forEach((p) => {
    const item = document.createElement('div');
    item.style.cursor = 'pointer';
    item.style.border = '1px solid #ccc';
    item.style.margin = '6px 0';
    item.style.padding = '6px';
    item.textContent = `#${p.productId} ${p.title} | ${formatPrice(p.price)} | ${p.status}`;
    item.addEventListener('click', () => {
      window.location.href = `product-detail.html?id=${p.productId}`;
    });
    list.appendChild(item);
  });
}

async function loadActiveProducts() {
  const result = await apiGet('/products');
  if (!result.success) return showMessage(result.message || 'Load products failed', true);
  renderProductList(result.data || []);
}

async function loadProductDetail() {
  const detailArea = document.getElementById('productDetail');
  if (!detailArea) return;
  const id = getQueryParam('id');
  if (!id) return showMessage('Missing product id', true);

  const result = await apiGet(`/products/${id}`);
  if (!result.success) return showMessage(result.message || 'Load detail failed', true);

  const p = result.data;
  detailArea.innerHTML = `
    <div>Title: ${p.title}</div>
    <div>Price: ${formatPrice(p.price)}</div>
    <div>Status: ${p.status}</div>
    <div>Seller: ${p.sellerId}</div>
    <div>Category: ${p.categoryId ?? '-'}</div>
    <div>Description: ${p.description || ''}</div>
  `;
}

async function handleAddProduct() {
  const loginUser = requireLogin();
  if (!loginUser) return;
  const title = document.getElementById('title')?.value?.trim();
  const price = Number(document.getElementById('price')?.value || 0);
  const categoryIdRaw = document.getElementById('categoryId')?.value;
  const categoryId = categoryIdRaw ? Number(categoryIdRaw) : null;
  const description = document.getElementById('description')?.value || '';
  if (!title || price <= 0) return showMessage('Title and valid price are required', true);

  const result = await apiPost('/products', {
    sellerId: loginUser.userId,
    categoryId,
    title,
    price,
    description
  });
  if (result.success) {
    showMessage('Product added', false);
    setTimeout(() => (window.location.href = 'home.html'), 500);
  } else {
    showMessage(result.message || 'Add product failed', true);
  }
}

(function bindProductPage() {
  if (document.getElementById('productList')) {
    loadActiveProducts();
    document.getElementById('reloadBtn')?.addEventListener('click', loadActiveProducts);
  }
  if (document.getElementById('productDetail')) {
    loadProductDetail();
  }
  if (document.getElementById('addProductBtn')) {
    document.getElementById('addProductBtn').addEventListener('click', handleAddProduct);
  }
})();
