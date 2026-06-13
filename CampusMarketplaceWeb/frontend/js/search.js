async function searchProducts() {
  clearMessage();
  const btn = document.getElementById('searchBtn');
  const kw = document.getElementById('keyword')?.value?.trim();
  if (!kw) return showMessage('請輸入搜尋關鍵字。', true);
  if (btn) { btn.disabled = true; btn.textContent = '搜尋中...'; }
  const result = await apiGet(`/products/search?keyword=${encodeURIComponent(kw)}`);
  if (btn) { btn.disabled = false; btn.textContent = '搜尋'; }
  if (!result.success) return showMessage(result.message || '搜尋失敗。', true);
  // store search results and render according to current sort selection
  if (window.updateProductResults) {
    window.updateProductResults(result.data || []);
  } else {
    renderProductList(result.data || []);
  }
}

async function loadHotKeywords() {
  const target = document.getElementById('hotKeywords');
  if (!target) return;
  const result = await apiGet('/search/hot-keywords');
  if (!result.success) {
    target.innerHTML = '<div class="empty-state">熱門關鍵字載入失敗。</div>';
    return;
  }
  const list = result.data || [];
  target.innerHTML = '';
  // save hot keywords for client-side popularity scoring (parse strings like "kw (8)")
  window.currentHotKeywords = Array.isArray(list)
    ? list.map((item) =>
        window.parseHotKeywordItem ? window.parseHotKeywordItem(item) : item
      )
    : [];
  if (list.length === 0) {
    target.innerHTML = '<div class="empty-state">暫無熱門關鍵字。</div>';
    return;
  }
  // render parsed keywords (now objects with { keyword, count })
  (window.currentHotKeywords || []).forEach((item) => {
    const btn = document.createElement('button');
    btn.className = 'keyword-chip';
    btn.type = 'button';
    btn.textContent = `${item.keyword} (${item.count})`;
    btn.addEventListener('click', () => {
      const input = document.getElementById('keyword');
      if (input) input.value = item.keyword;
      searchProducts();
    });
    target.appendChild(btn);
  });
  // If user currently has 'popular' selected, re-render sorted products without calling API
  if (document.getElementById('product-sort')?.value === 'popular') {
    window.renderSortedProducts?.();
  }
}

(function bindSearchPage() {
  const btn = document.getElementById('searchBtn');
  if (btn) btn.addEventListener('click', searchProducts);
  document.getElementById('keyword')?.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') searchProducts();
  });
  if (document.getElementById('hotKeywords')) loadHotKeywords();
})();

