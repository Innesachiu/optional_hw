async function searchProducts() {
  const kw = document.getElementById('keyword')?.value?.trim();
  if (!kw) return showMessage('Please input keyword', true);
  const result = await apiGet(`/products/search?keyword=${encodeURIComponent(kw)}`);
  if (!result.success) return showMessage(result.message || 'Search failed', true);
  renderProductList(result.data || []);
}

async function loadHotKeywords() {
  const target = document.getElementById('hotKeywords');
  if (!target) return;
  const result = await apiGet('/search/hot-keywords');
  if (!result.success) {
    target.textContent = 'Failed to load hot keywords';
    return;
  }
  const list = result.data || [];
  target.innerHTML = '';
  list.forEach((item) => {
    const keyword = String(item).replace(/\s*\(\d+\)\s*$/, '');
    const btn = document.createElement('button');
    btn.textContent = item;
    btn.addEventListener('click', () => {
      const input = document.getElementById('keyword');
      if (input) input.value = keyword;
      searchProducts();
    });
    target.appendChild(btn);
  });
}

(function bindSearchPage() {
  const btn = document.getElementById('searchBtn');
  if (btn) btn.addEventListener('click', searchProducts);
  if (document.getElementById('hotKeywords')) loadHotKeywords();
})();
