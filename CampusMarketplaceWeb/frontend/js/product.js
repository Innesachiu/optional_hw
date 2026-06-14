let categoryNameById = {};

// shared category label mapping (English -> 繁體中文)
const _categoryLabelMap = {
  Books: '書籍',
  Electronics: '3C產品',
  'Daily Goods': '生活用品',
  Clothing: '服飾',
  Other: '其他',
  'Daily Supplies': '日常用品',
  Sports: '運動用品',
  Others: '其他'
};

/**
 * Get display label for a category. Accepts either a category id (number/string)
 * or a raw category name string returned from the API. Returns mapped
 * Traditional Chinese label when available, otherwise returns the original
 * name, or '未分類' when not available.
 */
function getCategoryLabel(value) {
  if (value === null || value === undefined || value === '') return '未分類';

  // If value is an id (number or numeric string), try lookup first
  const idKey = String(value);
  if (categoryNameById && categoryNameById[idKey]) {
    const raw = categoryNameById[idKey];
    return _categoryLabelMap[raw] || raw || '未分類';
  }

  // Otherwise treat value as a raw name
  const rawName = String(value);
  return _categoryLabelMap[rawName] || rawName || '未分類';
}
// currentProducts stores the latest product array returned from the API
// and is the source for client-side sorting and re-rendering.
let currentProducts = [];
// currently selected category filter (string 'all' or categoryId)
let selectedCategoryId = 'all';
// currently viewed product on product-detail page
window.currentProduct = window.currentProduct || null;
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

// Note: previous implementation (lookup by id) is now handled by the
// getCategoryLabel function above which accepts id or name.

function getFilteredProducts(products) {
  if (selectedCategoryId === 'all') return Array.isArray(products) ? [...products] : [];

  return (Array.isArray(products) ? products : []).filter((product) => {
    const productCategoryId =
      product?.categoryId ??
      product?.category_id ??
      product?.category?.categoryId ??
      product?.category?.id;

    return String(productCategoryId) === String(selectedCategoryId);
  });
}

function renderFilteredAndSortedProducts() {
  const list = document.getElementById('productList');
  if (!list) return;
  // If product objects don't include category info, disable the category filter.
  const filterSelect = document.getElementById('category-filter');
  function productsHaveCategoryField() {
    if (!Array.isArray(currentProducts) || currentProducts.length === 0) return false;
    return currentProducts.some((product) => {
      return (
        product?.categoryId !== undefined ||
        product?.category_id !== undefined ||
        product?.category?.categoryId !== undefined ||
        product?.category?.id !== undefined ||
        product?.categoryName !== undefined ||
        product?.category_name !== undefined
      );
    });
  }
  if (filterSelect) {
    if (!productsHaveCategoryField()) {
      filterSelect.disabled = true;
      filterSelect.title = '商品列表未提供分類欄位，無法篩選';
    } else {
      filterSelect.disabled = false;
      filterSelect.removeAttribute('title');
    }
  }

  const filteredProducts = getFilteredProducts(currentProducts);
  if (!Array.isArray(filteredProducts) || filteredProducts.length === 0) {
    // decide appropriate empty state message
    const hasKeyword = Boolean(document.getElementById('keyword')?.value?.trim());
    if (!Array.isArray(currentProducts) || currentProducts.length === 0) {
      list.innerHTML = '<div class="empty-state">目前沒有商品。</div>';
      return;
    }
    if (hasKeyword && selectedCategoryId !== 'all') {
      list.innerHTML = '<div class="empty-state">找不到符合搜尋與商品類型的結果</div>';
      return;
    }
    if (selectedCategoryId !== 'all') {
      list.innerHTML = '<div class="empty-state">此類型目前沒有符合條件的商品</div>';
      return;
    }
    list.innerHTML = '<div class="empty-state">目前沒有商品。</div>';
    return;
  }

  const sortType = document.getElementById('product-sort')?.value || 'newest';
  const sorted = getSortedProducts(filteredProducts, sortType);
  renderProductList(sorted);
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
    // image element
    const imgEl = document.createElement('img');
    const placeholder = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="160" height="120"><rect width="100%" height="100%" fill="%23f0f0f0"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%23888888" font-size="14">No Image</text></svg>';
    imgEl.className = 'product-thumb';
    imgEl.alt = p.title || 'product image';
    imgEl.src = p.imageUrl ? resolveImageUrl(p.imageUrl) : placeholder;
    imgEl.onerror = function() { this.onerror = null; this.src = placeholder; };
    item.insertBefore(imgEl, item.firstChild);
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
  renderFilteredAndSortedProducts();
}

function renderSortedProducts() {
  // keep compatibility: renderSortedProducts now applies current filter first
  renderFilteredAndSortedProducts();
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
    option.textContent = getCategoryLabel(category.name);
    select.appendChild(option);
  });
  if (!categories || categories.length === 0) {
    select.innerHTML = '<option value="">暫無分類</option>';
    select.disabled = true;
  }
}

async function populateCategoryFilter() {
  const select = document.getElementById('category-filter');
  if (!select) return;
  select.disabled = true;
  select.innerHTML = '<option value="all">載入分類中…</option>';
  const categories = await loadCategories();
  select.disabled = false;
  select.innerHTML = '<option value="all">全部類型</option>';
  (categories || []).forEach((category) => {
    const option = document.createElement('option');
    option.value = category.categoryId;
    option.textContent = getCategoryLabel(category.name || category.categoryName);
    select.appendChild(option);
  });
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
  // expose current product so other modules (order modal) can use it
  window.currentProduct = p;
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

  // set product image in detail view
  const detailImageEl = detailArea.querySelector('.detail-image');
  if (detailImageEl) {
    const img = document.createElement('img');
    const placeholder = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="480" height="360"><rect width="100%" height="100%" fill="%23f0f0f0"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%23888888" font-size="24">No Image</text></svg>';
    img.alt = p.title || 'product image';
    img.src = p.imageUrl ? resolveImageUrl(p.imageUrl) : placeholder;
    img.onerror = function() { this.onerror = null; this.src = placeholder; };
    detailImageEl.innerHTML = '';
    detailImageEl.appendChild(img);
  }

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

// --- Order confirmation modal functions ---
function openOrderConfirmModal(product) {
  const modal = document.getElementById('order-confirm-modal');
  if (!modal) return;

  // prefer passed product, otherwise use global currentProduct
  const prod = product || window.currentProduct;
  if (!prod) return showMessage('找不到商品資訊。', true);

  // Prevent opening modal for SOLD or non-active items
  if ((prod.status || '').toString() === 'SOLD') return;

  // Populate fields
  const titleEl = document.getElementById('order-confirm-product-title');
  const priceEl = document.getElementById('order-confirm-product-price');
  if (titleEl) titleEl.textContent = prod.title || '未命名商品';
  if (priceEl) priceEl.textContent = `NT$ ${Number(prod.price || 0).toLocaleString('zh-TW')}`;

  modal.hidden = false;
  document.body.classList.add('modal-open');

  // focus confirm button
  const submit = document.getElementById('order-confirm-submit');
  submit?.focus();

  // attach handlers
  attachOrderModalHandlers();
}

function closeOrderConfirmModal() {
  const modal = document.getElementById('order-confirm-modal');
  if (!modal) return;
  modal.hidden = true;
  document.body.classList.remove('modal-open');

  // remove handlers
  detachOrderModalHandlers();

  // return focus to order button
  const orderBtn = document.getElementById('orderBtn');
  orderBtn?.focus();
}

let _orderModalHandlers = null;
function attachOrderModalHandlers() {
  if (_orderModalHandlers) return; // already attached
  const overlay = document.getElementById('order-confirm-modal');
  const closeBtn = document.getElementById('order-confirm-close');
  const cancelBtn = document.getElementById('order-confirm-cancel');
  const submitBtn = document.getElementById('order-confirm-submit');

  const onOverlayClick = (e) => {
    if (e.target === overlay) closeOrderConfirmModal();
  };

  const onEsc = (e) => {
    if (e.key === 'Escape') closeOrderConfirmModal();
  };

  const onCancel = () => closeOrderConfirmModal();

  const onClose = () => closeOrderConfirmModal();

  const onSubmit = async () => {
    if (!submitBtn) return;
    // disable to avoid double clicks
    submitBtn.disabled = true;
    submitBtn.dataset.orig = submitBtn.textContent;
    submitBtn.textContent = '處理中…';

    // reuse existing placeOrder function from order.js
    try {
      if (typeof placeOrder === 'function') {
        const res = await placeOrder();
        if (res && res.success) {
          closeOrderConfirmModal();
        } else {
          // leave modal open; placeOrder already shows error message
          submitBtn.disabled = false;
          submitBtn.textContent = submitBtn.dataset.orig || '確認購買';
        }
      } else {
        // fallback: if placeOrder not present, close modal
        closeOrderConfirmModal();
      }
    } catch (e) {
      console.error(e);
      submitBtn.disabled = false;
      submitBtn.textContent = submitBtn.dataset.orig || '確認購買';
    }
  };

  overlay.addEventListener('click', onOverlayClick);
  document.addEventListener('keydown', onEsc);
  closeBtn?.addEventListener('click', onClose);
  cancelBtn?.addEventListener('click', onCancel);
  submitBtn?.addEventListener('click', onSubmit);

  _orderModalHandlers = { overlay, onOverlayClick, onEsc, closeBtn, onClose, cancelBtn, onCancel, submitBtn, onSubmit };
}

function detachOrderModalHandlers() {
  if (!_orderModalHandlers) return;
  const h = _orderModalHandlers;
  h.overlay.removeEventListener('click', h.onOverlayClick);
  document.removeEventListener('keydown', h.onEsc);
  h.closeBtn?.removeEventListener('click', h.onClose);
  h.cancelBtn?.removeEventListener('click', h.onCancel);
  h.submitBtn?.removeEventListener('click', h.onSubmit);
  _orderModalHandlers = null;
}

// expose modal controls globally so order.js can call openOrderConfirmModal
window.openOrderConfirmModal = openOrderConfirmModal;
window.closeOrderConfirmModal = closeOrderConfirmModal;

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

  const createResult = await apiPost('/products', {
    sellerId: loginUser.userId,
    categoryId,
    title,
    price,
    description
  });
  console.log('create product response:', createResult);
  if (!createResult || !createResult.success) return showMessage((createResult && createResult.message) || '新增商品失敗。', true);

  // robustly extract productId from possible response shapes
  const productId = createResult?.data?.productId ?? createResult?.productId ?? createResult?.data?.id ?? createResult?.id ?? null;
  if (!productId) {
    showMessage('商品已建立，但無法取得商品 ID，因此圖片未上傳。', true);
    return;
  }

  // re-get selected image (do not rely on previously cached variables)
  const imageInput = document.getElementById('product-image');
  const selectedImage = imageInput?.files?.[0] || null;
  console.log({ selectedImage, productId, mimeType: selectedImage?.type, size: selectedImage?.size });

  if (!selectedImage) {
    showMessage('商品已上架。', false);
    setTimeout(() => (window.location.href = 'home.html'), 700);
    return;
  }

  // validate type and size
  const allowed = ['image/jpeg', 'image/png', 'image/webp'];
  if (!allowed.includes(selectedImage.type)) {
    showMessage('商品已成功上架，但圖片格式不支援。', true);
    return;
  }
  if (selectedImage.size > 3 * 1024 * 1024) {
    showMessage('商品已成功上架，但圖片超過 3MB。', true);
    return;
  }

  // helper: file -> pure base64
  function fileToBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const result = String(reader.result || '');
        const commaIndex = result.indexOf(',');
        resolve(commaIndex >= 0 ? result.slice(commaIndex + 1) : result);
      };
      reader.onerror = () => reject(new Error('圖片讀取失敗。'));
      reader.readAsDataURL(file);
    });
  }

  // convert and upload
  try {
    const base64Data = await fileToBase64(selectedImage);
    console.log('upload payload preview', { productId: Number(productId), fileName: selectedImage.name, mimeType: selectedImage.type, base64Len: base64Data.length });
    const imageResult = await apiPost('/products/image', {
      productId: Number(productId),
      fileName: selectedImage.name,
      mimeType: selectedImage.type,
      base64Data
    });
    console.log('upload image response:', imageResult);
    if (!imageResult || !imageResult.success) {
      showMessage(imageResult?.message || '商品已建立，但圖片上傳失敗。', true);
      return;
    }
    showMessage('商品與圖片已上架完成。', false);
    // only clear input and navigate after successful upload
    try { imageInput.value = ''; } catch (e) {}
    setTimeout(() => (window.location.href = 'home.html'), 700);
  } catch (e) {
    console.error('image upload error', e);
    showMessage('商品已建立，但圖片上傳過程發生錯誤。', true);
  }
}

(function bindProductPage() {
  if (document.getElementById('productList')) {
    loadActiveProducts();
    document.getElementById('reloadBtn')?.addEventListener('click', loadActiveProducts);
    const sortSelect = document.getElementById('product-sort');
    sortSelect?.addEventListener('change', () => {
      renderFilteredAndSortedProducts();
    });
    // populate category filter on home page and bind change
    populateCategoryFilter();
    const filterSelect = document.getElementById('category-filter');
    filterSelect?.addEventListener('change', (event) => {
      selectedCategoryId = event.target.value;
      renderFilteredAndSortedProducts();
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
  // bind image input preview on add-product page
  const imgInput = document.getElementById('product-image');
  const imgPreview = document.getElementById('product-image-preview');
  const imgRemove = document.getElementById('product-image-remove');
  if (imgInput) {
    imgInput.addEventListener('change', (ev) => {
      const f = ev.target.files && ev.target.files[0];
      if (!f) { imgPreview.innerHTML = ''; imgRemove.hidden = true; return; }
      const allowed = ['image/jpeg','image/png','image/webp'];
      if (!allowed.includes(f.type) || f.size > 3*1024*1024) {
        imgPreview.innerHTML = '<div class="image-preview">不支援的圖片或檔案過大</div>';
        imgRemove.hidden = false;
        return;
      }
      const reader = new FileReader();
      reader.onload = function(e) {
        imgPreview.innerHTML = '';
        const im = document.createElement('img');
        im.src = e.target.result;
        imgPreview.appendChild(im);
        imgRemove.hidden = false;
      };
      reader.readAsDataURL(f);
    });
    imgRemove?.addEventListener('click', () => {
      imgInput.value = '';
      imgPreview.innerHTML = '';
      imgRemove.hidden = true;
    });
  }
  document.getElementById('addProductForm')?.addEventListener('submit', handleAddProduct);
})();

// expose for other modules (search, etc.) to update and render with current sort
window.updateProductResults = updateProductResults;
window.renderSortedProducts = renderSortedProducts;
