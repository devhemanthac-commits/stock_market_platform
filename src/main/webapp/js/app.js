// ===== StockSim — Core JS =====

// Check auth on app pages
(function checkAuth() {
    const page = window.location.pathname;
    const publicPages = ['/', '/index.html', '/login.html', '/register.html',
                         '/StockSim/', '/StockSim/index.html',
                         '/StockSim/login.html', '/StockSim/register.html'];
    if (!publicPages.some(p => page.endsWith(p))) {
        const user = localStorage.getItem('user');
        if (!user) {
            window.location.href = 'login.html';
        }
    }
})();

// Format currency
function formatCurrency(amount) {
    return '₹' + Number(amount || 0).toLocaleString('en-IN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

// Load user info into sidebar
async function loadUserInfo() {
    try {
        const res = await fetch('api/user');
        if (res.status === 401) {
            localStorage.removeItem('user');
            window.location.href = 'login.html';
            return;
        }
        const user = await res.json();
        const avatarEl = document.getElementById('userAvatar');
        const nameEl = document.getElementById('userName');
        const balEl = document.getElementById('userBalance');
        if (avatarEl) avatarEl.textContent = (user.fullName || 'U')[0].toUpperCase();
        if (nameEl) nameEl.textContent = user.fullName || user.username;
        if (balEl) balEl.textContent = formatCurrency(user.balance);
    } catch (e) {
        console.error('Failed to load user info:', e);
    }
}

// Logout
async function logout() {
    try {
        await fetch('api/login', { method: 'DELETE' });
    } catch(e) {}
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

// Toast notifications
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    if (!container) return;
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Toggle watchlist
async function toggleWatchlist(symbol, isInWatchlist) {
    try {
        if (isInWatchlist) {
            await fetch('api/watchlist?symbol=' + encodeURIComponent(symbol), { method: 'DELETE' });
            showToast(symbol + ' removed from watchlist');
        } else {
            await fetch('api/watchlist', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ symbol })
            });
            showToast(symbol + ' added to watchlist', 'success');
        }
    } catch(e) {
        showToast('Failed to update watchlist', 'error');
    }
}

// Dashboard loader
async function loadDashboard() {
    await loadUserInfo();
    try {
        // Load portfolio summary
        const portRes = await fetch('api/portfolio');
        const portfolio = await portRes.json();
        const statBal = document.getElementById('statBalance');
        const statInv = document.getElementById('statInvested');
        const statCur = document.getElementById('statCurrent');
        const statPnl = document.getElementById('statPnl');
        const statPnlPct = document.getElementById('statPnlPercent');

        if (statBal) statBal.textContent = formatCurrency(portfolio.balance);
        if (statInv) statInv.textContent = formatCurrency(portfolio.totalInvested);
        if (statCur) statCur.textContent = formatCurrency(portfolio.totalCurrentValue);
        if (statPnl) {
            statPnl.textContent = formatCurrency(portfolio.totalPnl);
            statPnl.className = 'stat-value ' +
                (portfolio.totalPnl >= 0 ? 'positive' : 'negative');
        }
        if (statPnlPct) {
            statPnlPct.textContent = `(${portfolio.totalPnlPercent >= 0 ? '+' : ''}${portfolio.totalPnlPercent.toFixed(2)}%)`;
            statPnlPct.className = 'stat-change ' +
                (portfolio.totalPnl >= 0 ? 'positive' : 'negative');
        }

        // Load top movers
        const mktRes = await fetch('api/market/');
        const stocks = await mktRes.json();
        stocks.sort((a, b) => Math.abs(b.changePercent) - Math.abs(a.changePercent));
        const topMovers = document.getElementById('topMovers');
        if (topMovers) {
            topMovers.innerHTML = stocks.slice(0, 8).map(s => `
                <tr>
                    <td>
                        <strong>${s.symbol}</strong>
                        <br><span style="color:var(--text-muted);font-size:0.8rem;">${s.name}</span>
                    </td>
                    <td><strong>₹${s.price.toFixed(2)}</strong></td>
                    <td class="${s.changePercent >= 0 ? 'positive' : 'negative'}">
                        ${s.changePercent >= 0 ? '▲' : '▼'} ${Math.abs(s.changePercent).toFixed(2)}%
                    </td>
                    <td><a href="trade.html?symbol=${s.symbol}" class="btn btn-primary btn-sm">Trade</a></td>
                </tr>
            `).join('');
        }

        // Load watchlist
        const wlRes = await fetch('api/watchlist');
        const watchlist = await wlRes.json();
        const wlContainer = document.getElementById('watchlistContainer');
        if (wlContainer) {
            if (!watchlist || watchlist.length === 0) {
                wlContainer.innerHTML = `
                    <div class="empty-state">
                        <div class="icon">⭐</div>
                        <p>Your watchlist is empty.<br><a href="markets.html">Browse markets</a> to add stocks.</p>
                    </div>`;
            } else {
                wlContainer.innerHTML = `<table class="data-table">
                    <tbody>
                        ${watchlist.map(s => `
                            <tr>
                                <td><strong>${s.symbol}</strong></td>
                                <td>₹${s.price.toFixed(2)}</td>
                                <td class="${s.changePercent >= 0 ? 'positive' : 'negative'}">
                                    ${s.changePercent >= 0 ? '+' : ''}${s.changePercent.toFixed(2)}%
                                </td>
                                <td><a href="trade.html?symbol=${s.symbol}" class="btn btn-primary btn-sm">Trade</a></td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>`;
            }
        }
    } catch(e) { console.error(e); }
}
