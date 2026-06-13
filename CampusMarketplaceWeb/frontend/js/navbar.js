// Navbar helper: accessibility and live-update support
(function navbarInit(){
  // Update nav whenever storage changes (e.g., login/logout in another tab)
  window.addEventListener('storage', (e)=>{
    if (e.key === 'userId' || e.key === 'username') {
      try { if (typeof renderNavState === 'function') renderNavState(); } catch(_){}
    }
  });

  // keyboard: allow focusing nav links and using enter
  document.addEventListener('keydown', (ev)=>{
    if (ev.key === 'Escape') {
      const active = document.activeElement;
      if (active && active.classList && active.classList.contains('keyword-chip')) {
        active.blur();
      }
    }
  });

  // initial render call if available
  try { if (typeof renderNavState === 'function') renderNavState(); } catch(_){}
})();
