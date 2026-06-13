let categoryNameById = {};
// currentProducts stores the latest product array returned from the API
// and is the source for client-side sorting and re-rendering.
let currentProducts = [];
// hot keywords will be stored on window.currentHotKeywords by the search module
// but provide a default empty array accessor for safety
window.currentHotKeywords = window.currentHotKeywords || [];

function normalizeText(value) {
  return String(value || '')
    .trim()
    .toLowerCase()
    .replace(/\s+/g, ' ');
}

function parseHotKeywordItem(item) {
  if (typeof item === 'string') {
    const text = item.trim();

    const match = text.match(/^(.*?)\s*\((\d+)\)\s*$/);

    if (match) {
      return {
        keyword: match[1].trim(),
        count: Number(match[2])
      };
    }

    return {
      keyword: text,
      count: 0
    };
  }

  return {
    keyword:
      item?.keyword ??
      item?.query ??
      item?.searchKeyword ??
      item?.search_query ??
      item?.term ??
      item?.name ??
      '',
    count: Number(
      item?.count ??
      item?.searchCount ??
      item?.search_count ??
      item?.frequency ??
      item?.total ??
      item?.hits ??
      0
    )
  };
}

window.parseHotKeywordItem = parseHotKeywordItem;

function getProductPopularityScore(product) {
  const productText = normalizeText(product?.title);

  return (window.currentHotKeywords || []).reduce((total, rawItem) => {
    const item = parseHotKeywordItem(rawItem);
    const keyword = normalizeText(item.keyword);
    const count = Number(item.count || 0);

    if (!keyword || count <= 0) return total;

    if (
      productText === keyword ||
      productText.includes(keyword) ||
      keyword.includes(productText)
    ) {
      return total + count;
    }

    return total;
  }, 0);
}

// expose for other modules and debugging
window.normalizeText = normalizeText;
window.getProductPopularityScore = getProductPopularityScore;

function mapStatusLabel(status) {
  const s = (status || '').toString();
  return s === 'ACTIVE' ? '販售中' : (s === 'SOLD' ? '已售出' : (s || '未知'));
}

function getStatusBadge(status) {
  const s = (status || '').toString();
  const label = mapStatusLabel(s);
  const cls = s === 'ACTIVE' ? 'badge badge-success' : 'badge badge-muted';
  return `<span class="${cls}">${escapeHtml(label)}</span>`;
}

function getCategoryLabel(categoryId) {
  if (categoryId === null || categoryId === undefined || categoryId === '') return '-';
  return categoryNameById[categoryId] || `分類 #${categoryId}`;
}

function renderProductList(products) {
  const list = document.getElementById('productList');
  if (!list) return;
  list.innerHTML = '';
  if (!products) {
    list.innerHTML = '<div class="empty-state">載入商品失敗。</div>';
    return;
  }
  if (products.length === 0) {
    list.innerHTML = '<div class="empty-state">目前沒有商品。</div>';
    return;
  }
  products.forEach((p) => {
    const item = document.createElement('article');
    item.className = 'card product-card';
    item.tabIndex = 0;
    item.setAttribute('role', 'button');
    item.setAttribute('aria-label', `查看商品 ${p.title}`);
    const descHtml = p.description ? `<div class="product-desc">${escapeHtml(p.description)}</div>` : '';
    const catHtml = p.categoryName ? `<div class="product-category">${escapeHtml(p.categoryName)}</div>` : '';
    item.innerHTML = `
      <div class="product-meta">#${escapeHtml(p.productId)}</div>
      <h3 class="product-title">${escapeHtml(p.title)}</h3>
      ${catHtml}
      ${descHtml}
      <div class="product-row"><div class="product-price">${formatPrice(p.price)}</div><div class="actions">${getStatusBadge(p.status)}</div></div>
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

function getSortedProducts(products, sortType) {
  const sortedProducts = Array.isArray(products) ? [...products] : [];
  if (sortType === 'popular') {
    return sortedProducts.sort((a, b) => {
      const scoreDifference = getProductPopularityScore(b) - getProductPopularityScore(a);
      if (scoreDifference !== 0) return scoreDifference;
      return Number(b.productId || b.id || 0) - Number(a.productId || a.id || 0);
    });
  }

  // newest (default)
  return sortedProducts.sort((a, b) => {
    const aDate = a.createdAt || a.created_at || a.createdTime || a.created_time;
    const bDate = b.createdAt || b.created_at || b.createdTime || b.created_time;
    if (aDate && bDate) {
      return new Date(bDate) - new Date(aDate);
    }
    return Number(b.productId || b.id || 0) - Number(a.productId || a.id || 0);
  });
}

function updateProductResults(products) {
  currentProducts = Array.isArray(products) ? [...products] : [];
  renderSortedProducts();
}

function renderSortedProducts() {
  const sortType = document.getElementById('product-sort')?.value || 'newest';
  const sorted = getSortedProducts(currentProducts, sortType);
  renderProductList(sorted);
}

async function loadActiveProducts() {
  clearMessage();
  const list = document.getElementById('productList');
  if (list) list.innerHTML = '<div class="empty-state">載入商品中…</div>';
  const result = await apiGet('/products');
  if (!result.success) return showMessage(result.message || '載入商品失敗。', true);
  updateProductResults(result.data || []);
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
  select.disabled = true;
  select.innerHTML = '<option value="">載入分類中…</option>';
  const categories = await loadCategories();
  select.disabled = false;
  select.innerHTML = '<option value="">請選擇分類</option>';
  categories.forEach((category) => {
    const option = document.createElement('option');
    option.value = category.categoryId;
    option.textContent = category.name;
    select.appendChild(option);
  });
  if (!categories || categories.length === 0) {
    select.innerHTML = '<option value="">暫無分類</option>';
    select.disabled = true;
  }
}

async function loadProductDetail() {
  const detailArea = document.getElementById('productDetail');
  if (!detailArea) return;
  clearMessage();
  await loadCategories();
  const id = getQueryParam('id');
  if (!id) return showMessage('找不到商品 ID。', true);

  const result = await apiGet(`/products/${encodeURIComponent(id)}`);
  if (!result.success) return showMessage(result.message || '載入商品詳細失敗。', true);

  const p = result.data;
  detailArea.innerHTML = `
    <div class="detail-layout">
      <div class="detail-image" aria-hidden="true"></div>
      <div class="detail-info">
        <div class="actions">${getStatusBadge(p.status)}</div>
        <h2 class="detail-title">${escapeHtml(p.title)}</h2>
        <div class="detail-price">${formatPrice(p.price)}</div>
        <dl class="detail-list">
          <div class="detail-row"><dt>商品描述</dt><dd>${escapeHtml(p.description || '無描述')}</dd></div>
          <div class="detail-row"><dt>分類</dt><dd>${escapeHtml(getCategoryLabel(p.categoryId))}</dd></div>
          <div class="detail-row"><dt>賣家</dt><dd>#${escapeHtml(p.sellerId)}</dd></div>
          <div class="detail-row"><dt>狀態</dt><dd>${escapeHtml(mapStatusLabel(p.status))}</dd></div>
          <div class="detail-row"><dt>搜尋次數</dt><dd>${escapeHtml(p.searchHitCount || 0)}</dd></div>
        </dl>
      </div>
    </div>
  `;

  // notify other modules that product detail has loaded
  try {
    document.dispatchEvent(new CustomEvent('productLoaded', { detail: { productId: p.productId } }));
  } catch (e) {
    // ignore if CustomEvent unsupported
  }

  const orderBtn = document.getElementById('orderBtn');
  if (orderBtn && p.status !== 'ACTIVE') {
    orderBtn.disabled = true;
    orderBtn.textContent = '無法下單';
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

  if (!title) return showMessage('請輸入商品標題。', true);
  if (!Number.isInteger(price) || price <= 0) return showMessage('價格必須為正整數。', true);
  if (!categoryId) return showMessage('請選擇分類。', true);

  const result = await apiPost('/products', {
    sellerId: loginUser.userId,
    categoryId,
    title,
    price,
    description
  });
  if (result.success) {
    showMessage('商品已新增，正在導向…', false);
    setTimeout(() => (window.location.href = 'home.html'), 700);
  } else {
    showMessage(result.message || '新增商品失敗。', true);
  }
}

(function bindProductPage() {
  if (document.getElementById('productList')) {
    loadActiveProducts();
    document.getElementById('reloadBtn')?.addEventListener('click', loadActiveProducts);
    const sortSelect = document.getElementById('product-sort');
    sortSelect?.addEventListener('change', () => {
      renderSortedProducts();
    });
  }
  if (document.getElementById('productDetail')) {
    loadProductDetail();
  }
  if (document.getElementById('categoryId')) {
    const user = requireLogin();
    if (!user) {
      // requireLogin will redirect, but ensure immediate redirect for add-product page
      window.location.href = 'login.html';
      return;
    }
    populateCategorySelect();
  }
  document.getElementById('addProductForm')?.addEventListener('submit', handleAddProduct);
})();

// expose for other modules (search, etc.) to update and render with current sort
window.updateProductResults = updateProductResults;
window.renderSortedProducts = renderSortedProducts;
