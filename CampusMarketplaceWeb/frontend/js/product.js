let categoryNameById = {};

function getStatusBadge(status) {
  const safeStatus = escapeHtml(status || 'UNKNOWN');
  const cls = status === 'ACTIVE' ? 'badge badge-success' : 'badge badge-muted';
  return `<span class="${cls}">${safeStatus}</span>`;
}

function getCategoryLabel(categoryId) {
  if (categoryId === null || categoryId === undefined || categoryId === '') return '-';
  return categoryNameById[categoryId] || `Category #${categoryId}`;
}

function renderProductList(products) {
  const list = document.getElementById('productList');
  if (!list) return;
  list.innerHTML = '';
  if (!products || products.length === 0) {
    list.innerHTML = '<div class="empty-state">No active products found.</div>';
    return;
  }
  products.forEach((p) => {
    const item = document.createElement('article');
    item.className = 'card product-card';
    item.tabIndex = 0;
    item.setAttribute('role', 'button');
    item.setAttribute('aria-label', `Open product ${p.title}`);
    item.innerHTML = `
      <div class="product-meta">#${escapeHtml(p.productId)}</div>
      <h3 class="product-title">${escapeHtml(p.title)}</h3>
      <div class="product-price">${formatPrice(p.price)}</div>
      <div class="actions">${getStatusBadge(p.status)}</div>
    `;
    const openDetail = () => {
      window.location.href = `product-detail.html?id=${encodeURIComponent(p.productId)}`;
    };
    item.addEventListener('click', openDetail);
    item.addEventListener('keydown', (event) => {
      if (event.key === 'Enter' || event.key === ' ') openDetail();
    });
    list.appendChild(item);
  });
}

async function loadActiveProducts() {
  clearMessage();
  const result = await apiGet('/products');
  if (!result.success) return showMessage(result.message || 'Load products failed.', true);
  renderProductList(result.data || []);
}

async function loadCategories() {
  const result = await apiGet('/categories');
  if (!result.success) return [];
  const categories = result.data || [];
  categoryNameById = {};
  categories.forEach((category) => {
    categoryNameById[category.categoryId] = category.name;
  });
  return categories;
}

async function populateCategorySelect() {
  const select = document.getElementById('categoryId');
  if (!select) return;
  const categories = await loadCategories();
  select.innerHTML = '<option value="">Select category</option>';
  categories.forEach((category) => {
    const option = document.createElement('option');
    option.value = category.categoryId;
    option.textContent = category.name;
    select.appendChild(option);
  });
  if (categories.length === 0) {
    select.innerHTML = '<option value="">No categories available</option>';
  }
}

async function loadProductDetail() {
  const detailArea = document.getElementById('productDetail');
  if (!detailArea) return;
  clearMessage();
  await loadCategories();
  const id = getQueryParam('id');
  if (!id) return showMessage('Missing product id.', true);

  const result = await apiGet(`/products/${encodeURIComponent(id)}`);
  if (!result.success) return showMessage(result.message || 'Load detail failed.', true);

  const p = result.data;
  detailArea.innerHTML = `
    <div class="detail-card">
      <div>
        <div class="actions">${getStatusBadge(p.status)}</div>
        <h2 class="detail-title">${escapeHtml(p.title)}</h2>
        <div class="detail-price">${formatPrice(p.price)}</div>
      </div>
      <dl class="detail-list">
        <div class="detail-row"><dt>Description</dt><dd>${escapeHtml(p.description || 'No description')}</dd></div>
        <div class="detail-row"><dt>Category</dt><dd>${escapeHtml(getCategoryLabel(p.categoryId))}</dd></div>
        <div class="detail-row"><dt>Seller</dt><dd>#${escapeHtml(p.sellerId)}</dd></div>
        <div class="detail-row"><dt>Status</dt><dd>${escapeHtml(p.status)}</dd></div>
        <div class="detail-row"><dt>Search Hits</dt><dd>${escapeHtml(p.searchHitCount || 0)}</dd></div>
      </dl>
    </div>
  `;

  const orderBtn = document.getElementById('orderBtn');
  if (orderBtn && p.status !== 'ACTIVE') {
    orderBtn.disabled = true;
    orderBtn.textContent = 'Unavailable';
    orderBtn.classList.add('btn-secondary');
  }
}

async function handleAddProduct(event) {
  if (event) event.preventDefault();
  clearMessage();
  const loginUser = requireLogin();
  if (!loginUser) return;
  const title = document.getElementById('title')?.value?.trim();
  const price = Number(document.getElementById('price')?.value || 0);
  const categoryIdRaw = document.getElementById('categoryId')?.value;
  const categoryId = categoryIdRaw ? Number(categoryIdRaw) : null;
  const description = document.getElementById('description')?.value || '';

  if (!title) return showMessage('Product title is required.', true);
  if (!Number.isInteger(price) || price <= 0) return showMessage('Price must be a positive whole number.', true);
  if (!categoryId) return showMessage('Please select a category.', true);

  const result = await apiPost('/products', {
    sellerId: loginUser.userId,
    categoryId,
    title,
    price,
    description
  });
  if (result.success) {
    showMessage('Product added successfully. Redirecting...', false);
    setTimeout(() => (window.location.href = 'home.html'), 700);
  } else {
    showMessage(result.message || 'Add product failed.', true);
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
  if (document.getElementById('categoryId')) {
    requireLogin();
    populateCategorySelect();
  }
  document.getElementById('addProductForm')?.addEventListener('submit', handleAddProduct);
})();
