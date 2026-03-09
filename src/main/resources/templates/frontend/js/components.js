class AppSidebar extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        const path = window.location.pathname;
        if (path.includes('/admin/')) {
            this.role = 'admin';
        } else if (path.includes('/manager/')) {
            this.role = 'manager';
        } else {
            this.role = 'employee';
        }
        localStorage.setItem('userRole', this.role);

        this.render();
        this.setupEventListeners();
    }

    render() {
        let linksHtml = '';

        const basePath = window.location.pathname.startsWith('/RevWorkForce') ? '/RevWorkForce' : '';

        if (this.role === 'admin') {
            linksHtml = `
                <li class="nav-item">
                    <a href="${basePath}/admin/dashboard" class="nav-link">
                        <i class="fas fa-home"></i> <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/admin/employees" class="nav-link">
                        <i class="fas fa-users"></i> <span>Employee Mgt</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/admin/system-config" class="nav-link">
                        <i class="fas fa-cogs"></i> <span>System Config</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/admin/audit-reports" class="nav-link">
                        <i class="fas fa-file-alt"></i> <span>Audit & Reports</span>
                    </a>
                </li>
            `;
        } else if (this.role === 'manager') {
            linksHtml = `
                <li class="nav-item">
                    <a href="${basePath}/manager/dashboard" class="nav-link">
                        <i class="fas fa-home"></i> <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/manager/team-management" class="nav-link">
                        <i class="fas fa-users-cog"></i> <span>Team Directory</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/manager/leave-approvals" class="nav-link">
                        <i class="fas fa-calendar-check"></i> <span>Leave Approvals</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/manager/performance" class="nav-link">
                        <i class="fas fa-chart-line"></i> <span>Performance</span>
                    </a>
                </li>
            `;
        } else {
            // Employee
            linksHtml = `
                <li class="nav-item">
                    <a href="${basePath}/employee/dashboard" class="nav-link">
                        <i class="fas fa-home"></i> <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/employee/directory" class="nav-link">
                        <i class="fas fa-id-card"></i> <span>Profile & Directory</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/employee/leave-management" class="nav-link">
                        <i class="fas fa-calendar-alt"></i> <span>Leave Management</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${basePath}/employee/performance" class="nav-link">
                        <i class="fas fa-bullseye"></i> <span>Performance Goals</span>
                    </a>
                </li>
            `;
        }

        this.innerHTML = `
            <style>
                aside {
                    width: 260px;
                    height: 100vh;
                    position: fixed;
                    left: 0;
                    top: 0;
                    background: var(--bg-white);
                    border-right: 1px solid var(--border-color);
                    display: flex;
                    flex-direction: column;
                    z-index: 100;
                    box-shadow: var(--shadow-sm);
                }
                .logo-container {
                    padding: 1.5rem;
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    border-bottom: 1px solid var(--border-color);
                }
                .logo-icon {
                    width: 32px;
                    height: 32px;
                    background: linear-gradient(135deg, var(--primary), var(--primary-light));
                    border-radius: 8px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    color: white;
                    font-weight: bold;
                    font-size: 1.2rem;
                }
                .logo-text {
                    font-size: 1.25rem;
                    font-weight: 700;
                    color: var(--primary-dark);
                    letter-spacing: -0.5px;
                }
                .nav-menu {
                    list-style: none;
                    padding: 1.5rem 1rem;
                    flex-grow: 1;
                    padding-left: 0;
                    margin: 0;
                }
                .nav-item {
                    margin-bottom: 0.5rem;
                }
                .nav-link {
                    display: flex;
                    align-items: center;
                    padding: 0.75rem 1rem;
                    color: var(--text-muted);
                    border-radius: var(--border-radius-sm);
                    transition: all var(--transition-speed);
                    font-weight: 500;
                    gap: 12px;
                    text-decoration: none;
                }
                .nav-link:hover {
                    background-color: rgba(255, 140, 0, 0.08);
                    color: var(--primary);
                }
                .nav-link.active {
                    background: linear-gradient(135deg, rgba(255,140,0,0.1), rgba(255,160,122,0.1));
                    color: var(--primary);
                    border-left: 4px solid var(--primary);
                }
                .nav-link i {
                    font-size: 1.1rem;
                    width: 20px;
                    text-align: center;
                }
                .user-profile-section {
                    padding: 1rem;
                    border-top: 1px solid var(--border-color);
                    display: flex;
                    align-items: center;
                    gap: 12px;
                    background: var(--bg-main);
                }
                .avatar {
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                    background-color: var(--primary-light);
                    color: white;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-weight: 600;
                }
                .user-info {
                    display: flex;
                    flex-direction: column;
                }
                .user-name {
                    font-weight: 600;
                    font-size: 0.9rem;
                    color: var(--text-dark);
                }
                .user-role {
                    font-size: 0.75rem;
                    color: var(--text-muted);
                    text-transform: capitalize;
                }
                .logout-btn {
                    margin-left: auto;
                    color: var(--text-muted);
                    cursor: pointer;
                    transition: color var(--transition-speed);
                    background: none;
                    border: none;
                    font-size: 1.1rem;
                }
                .logout-btn:hover {
                    color: var(--primary);
                }
            </style>
            <aside>
                <div class="logo-container">
                    <div class="logo-icon">R</div>
                    <div class="logo-text">RevWorkForce</div>
                </div>
                <ul class="nav-menu">
                    ${linksHtml}
                </ul>
                <div class="user-profile-section">
                    <div class="avatar">
                        <i class="fas fa-user"></i>
                    </div>
                    <div class="user-info">
                        <span class="user-name" id="sidebarUserName">Loading...</span>
                        <span class="user-role" id="sidebarUserRole">${this.role}</span>
                    </div>
                    <button class="logout-btn" id="logoutBtn" title="Logout">
                        <i class="fas fa-sign-out-alt"></i>
                    </button>
                </div>
            </aside>
        `;

        this.highlightActiveLink();

        // Populate user name/role from meta tags (local or global) or localStorage or fetch
        const nameEl = this.querySelector('#sidebarUserName');
        const roleEl = this.querySelector('#sidebarUserRole');

        const metaName = document.querySelector('meta[name="user-fullname"]') || document.querySelector('meta[name="global-user-fullname"]');
        const metaRole = document.querySelector('meta[name="user-role"]') || document.querySelector('meta[name="global-user-role"]');

        let nameValue = (metaName && metaName.content && metaName.content !== 'null') ? metaName.content : null;
        let roleValue = (metaRole && metaRole.content && metaRole.content !== 'null') ? metaRole.content : null;

        if (nameEl && nameValue) {
            nameEl.textContent = nameValue;
            localStorage.setItem('userFullName', nameValue);
            if (roleEl && roleValue) {
                roleEl.textContent = roleValue;
                localStorage.setItem('userRole', roleValue);
            }
        } else {
            // Try localStorage as fallback
            const cachedName = localStorage.getItem('userFullName');
            const cachedRole = localStorage.getItem('userRole');
            if (nameEl && cachedName && cachedName !== 'undefined' && cachedName !== 'null') {
                nameEl.textContent = cachedName;
                if (roleEl && cachedRole && cachedRole !== 'undefined' && cachedRole !== 'null') {
                    roleEl.textContent = cachedRole;
                }
            }
            this.fetchUserInfo(nameEl, roleEl);
        }
    }

    async fetchUserInfo(nameEl, roleEl) {
        try {
            const basePath = window.location.pathname.startsWith('/RevWorkForce') ? '/RevWorkForce' : '';
            const response = await fetch(`${basePath}/api/auth/me`);
            if (response.ok) {
                const data = await response.json();
                if (nameEl) nameEl.textContent = data.fullName;
                if (roleEl) {
                    roleEl.textContent = data.role;
                    this.role = data.role.toLowerCase();
                }
            } else {
                if (nameEl) nameEl.textContent = 'Guest';
            }
        } catch (error) {
            console.error('Error fetching user info:', error);
            if (nameEl) nameEl.textContent = 'User';
        }
    }

    highlightActiveLink() {
        const currentPath = window.location.pathname;
        const links = this.querySelectorAll('.nav-link');
        links.forEach(link => {
            const href = link.getAttribute('href');
            if (currentPath.includes(href) || (currentPath.endsWith('/') && href.includes('dashboard'))) {
                link.classList.add('active');
            }
        });
    }

    setupEventListeners() {
        const logoutBtn = this.querySelector('#logoutBtn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => {
                localStorage.removeItem('userRole');
                const basePath = window.location.pathname.startsWith('/RevWorkForce') ? '/RevWorkForce' : '';
                window.location.href = basePath + '/logout';
            });
        }
    }
}

customElements.define('app-sidebar', AppSidebar);

class AppNavbar extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        this.render();
    }

    render() {
        const title = this.getAttribute('page-title') || 'Dashboard';
        const basePath = window.location.pathname.startsWith('/RevWorkForce') ? '/RevWorkForce' : '';

        this.innerHTML = `
            <style>
                header {
                    background: var(--bg-glass);
                    backdrop-filter: blur(10px);
                    -webkit-backdrop-filter: blur(10px);
                    height: 70px;
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    padding: 0 2rem;
                    border-bottom: 1px solid var(--border-color);
                    position: sticky;
                    top: 0;
                    z-index: 90;
                }
                .page-title {
                    font-size: 1.25rem;
                    font-weight: 600;
                    color: var(--text-dark);
                    margin: 0;
                }
                .header-actions {
                    display: flex;
                    align-items: center;
                    gap: 1.5rem;
                }
                .icon-btn {
                    background: none;
                    border: none;
                    color: var(--text-muted);
                    font-size: 1.25rem;
                    cursor: pointer;
                    position: relative;
                    transition: color var(--transition-speed);
                }
                .icon-btn:hover { color: var(--primary); }
                .notification-badge {
                    position: absolute;
                    top: -5px;
                    right: -5px;
                    background-color: #ef4444;
                    color: white;
                    font-size: 0.65rem;
                    font-weight: bold;
                    width: 16px;
                    height: 16px;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    border: 2px solid var(--bg-white);
                }
                .notification-dropdown {
                    position: absolute;
                    top: 100%;
                    right: 0;
                    width: 350px;
                    background: var(--bg-white);
                    border: 1px solid var(--border-color);
                    border-radius: var(--border-radius-md);
                    box-shadow: var(--shadow-md);
                    margin-top: 1rem;
                    display: none;
                    flex-direction: column;
                    z-index: 100;
                    overflow: hidden;
                }
                .notification-dropdown.show { display: flex; }
                .notification-header {
                    padding: 1rem;
                    border-bottom: 1px solid var(--border-color);
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    background: #f8fafc;
                }
                .notification-header h3 { margin: 0; font-size: 1rem; }
                .notification-list {
                    max-height: 300px;
                    overflow-y: auto;
                    list-style: none;
                    margin: 0;
                    padding: 0;
                }
                .notification-item {
                    padding: 1rem;
                    border-bottom: 1px solid var(--border-color);
                    transition: background var(--transition-speed);
                    cursor: pointer;
                    display: flex;
                    gap: 1rem;
                    align-items: flex-start;
                }
                .notification-item:hover { background: #f1f5f9; }
                .notification-item:last-child { border-bottom: none; }
                .notify-icon {
                    width: 32px;
                    height: 32px;
                    border-radius: 50%;
                    background: rgba(255,140,0,0.1);
                    color: var(--primary);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    flex-shrink: 0;
                }
                .notify-content p { margin: 0 0 0.25rem 0; font-size: 0.9rem; color: var(--text-dark); }
                .notify-content span { font-size: 0.75rem; color: var(--text-muted); }
                .notification-footer {
                    padding: 0.75rem;
                    text-align: center;
                    border-top: 1px solid var(--border-color);
                    background: #f8fafc;
                }
                .notification-footer a { color: var(--primary); text-decoration: none; font-size: 0.85rem; font-weight: 500; }
            </style>
            <header>
                <h1 class="page-title">${title}</h1>
                <div class="header-actions">
                    <div style="position: relative;">
                        <button class="icon-btn" title="Notifications" id="notificationBtn">
                            <i class="fas fa-bell"></i>
                            <span class="notification-badge" id="notificationBadge">0</span>
                        </button>
                        <div class="notification-dropdown" id="notificationDropdown">
                            <div class="notification-header">
                                <h3>Notifications</h3>
                                <button id="markAllReadBtn" style="background:none;border:none;color:var(--primary);cursor:pointer;font-size:0.8rem;">
                                    Mark all as read
                                </button>
                            </div>
                            <ul class="notification-list" id="notificationList"></ul>
                            <div class="notification-footer">
                                <a href="/notifications" id="viewAllLink">View All Activity</a>
                            </div>
                        </div>
                    </div>
                    <button class="icon-btn" title="Change Password" onclick="window.location.href='${basePath}/change-password'">
                        <i class="fas fa-key"></i>
                    </button>
                    <button class="icon-btn" title="Settings" onclick="window.location.href='${basePath}/employee/profile'">
                        <i class="fas fa-cog"></i>
                    </button>
                </div>
            </header>
        `;

        this.setupDropdownLogic();
    }

    setupDropdownLogic() {
        const basePath = window.location.pathname.startsWith('/RevWorkForce') ? '/RevWorkForce' : '';
        const dropdown = this.querySelector('#notificationDropdown');
        const btn = this.querySelector('#notificationBtn');
        const list = this.querySelector('#notificationList');
        const badge = this.querySelector('#notificationBadge');
        const markAllBtn = this.querySelector('#markAllReadBtn');
        const viewAllLink = this.querySelector('#viewAllLink');

        const loadUnreadCount = () => {
            fetch(`${basePath}/api/notifications/unread-count`)
                .then(res => res.text())
                .then(count => {
                    const num = parseInt(count) || 0;
                    badge.innerText = num;
                    badge.style.display = num > 0 ? 'flex' : 'none';
                })
                .catch(() => { });
        };

        const getCSRF = () => {
            const tokenEl = document.querySelector('meta[name="_csrf"]');
            const headerEl = document.querySelector('meta[name="_csrf_header"]');
            return {
                token: tokenEl ? tokenEl.content : '',
                header: headerEl ? headerEl.content : 'X-CSRF-TOKEN'
            };
        };

        const markAsRead = (id) => {
            const { token, header } = getCSRF();
            fetch(`${basePath}/api/notifications/mark-read/${id}`, {
                method: 'POST',
                headers: { [header]: token }
            }).then(res => {
                if (res.ok) {
                    loadUnreadCount();
                    loadNavbarNotifications();
                }
            }).catch(err => console.error(err));
        };

        const loadNavbarNotifications = () => {
            fetch(`${basePath}/api/notifications/recent?limit=5`)
                .then(res => res.json())
                .then(data => {
                    list.innerHTML = '';
                    if (!data || data.length === 0) {
                        list.innerHTML = '<li class="notification-item" style="justify-content:center;color:var(--text-muted);">No new notifications</li>';
                        return;
                    }
                    data.forEach(n => {
                        const li = document.createElement('li');
                        li.className = 'notification-item';
                        if (n.isRead === 'N') li.style.background = '#f8fafc';

                        let icon = 'fa-bell';
                        if (n.notificationType === 'LEAVE_REQUEST' || n.notificationType === 'LEAVE_APPROVED' || n.notificationType === 'LEAVE_REJECTED') icon = 'fa-calendar-alt';
                        if (n.notificationType === 'SYSTEM_ALERT') icon = 'fa-exclamation-triangle';
                        if (n.notificationType === 'PERFORMANCE_REVIEW') icon = 'fa-chart-line';
                        if (n.notificationType === 'GOAL_ASSIGNED') icon = 'fa-bullseye';

                        li.innerHTML = `
                            <div class="notify-icon" style="background:${n.isRead === 'N' ? 'rgba(59,130,246,0.1)' : 'rgba(100,116,139,0.1)'};color:${n.isRead === 'N' ? '#3b82f6' : '#64748b'}">
                                <i class="fas ${icon}"></i>
                            </div>
                            <div class="notify-content" style="flex-grow:1;">
                                <p style="font-weight:${n.isRead === 'N' ? '600' : '400'}">${n.message}</p>
                                <span>${new Date(n.createdAt).toLocaleString()}</span>
                            </div>
                            ${n.isRead === 'N' ? `<button class="mark-single-read" data-id="${n.notificationId}" title="Mark as read" style="background:none;border:none;color:#10b981;cursor:pointer;padding:4px;"><i class="fas fa-check-circle"></i></button>` : ''}
                        `;
                        li.addEventListener('click', (e) => {
                            if (e.target.closest('.mark-single-read')) {
                                e.stopPropagation();
                                markAsRead(e.target.closest('.mark-single-read').dataset.id);
                                return;
                            }
                            window.location.href = `${basePath}/notifications`;
                        });
                        list.appendChild(li);
                    });
                })
                .catch(() => {
                    list.innerHTML = '<li class="notification-item" style="justify-content:center;color:var(--text-muted);">Could not load notifications</li>';
                });
        };

        if (markAllBtn) {
            markAllBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                const { token, header } = getCSRF();
                fetch(`${basePath}/api/notifications/mark-all-read`, {
                    method: 'POST',
                    headers: { [header]: token }
                }).then(res => {
                    if (res.ok) {
                        badge.innerText = '0';
                        badge.style.display = 'none';
                        list.innerHTML = '<li class="notification-item" style="justify-content:center;color:var(--text-muted);">No new notifications</li>';
                    }
                }).catch(err => console.error(err));
            });
        }

        if (viewAllLink) {
            viewAllLink.addEventListener('click', (e) => {
                e.preventDefault();
                dropdown.classList.remove('show');
                window.location.href = `${basePath}/notifications`;
            });
        }

        // Toggle dropdown

        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            dropdown.classList.toggle('show');
            loadNavbarNotifications();
        });

        document.addEventListener('click', (e) => {
            if (dropdown && btn && !dropdown.contains(e.target) && !btn.contains(e.target)) {
                dropdown.classList.remove('show');
            }
        });

    }
}

customElements.define('app-navbar', AppNavbar);

// ======================================================
// BOOTSTRAP CDN — injected dynamically like FontAwesome
// ======================================================
(function injectBootstrap() {
    // Only add Bootstrap CSS if not already present
    if (!document.querySelector('link[href*="bootstrap"]')) {
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css';
        link.crossOrigin = 'anonymous';
        // Append BEFORE custom style.css so our styles always win
        const firstLink = document.querySelector('link[rel="stylesheet"]');
        if (firstLink) {
            document.head.insertBefore(link, firstLink);
        } else {
            document.head.appendChild(link);
        }
    }
})();

// ======================================================
// MOBILE SIDEBAR — Hamburger toggle logic
// ======================================================
(function setupMobileSidebar() {
    // Create overlay element
    const overlay = document.createElement('div');
    overlay.id = 'sidebar-overlay';
    document.body.appendChild(overlay);

    function openSidebar() {
        const sidebar = document.querySelector('app-sidebar');
        if (sidebar) sidebar.classList.add('sidebar-open');
        overlay.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    function closeSidebar() {
        const sidebar = document.querySelector('app-sidebar');
        if (sidebar) sidebar.classList.remove('sidebar-open');
        overlay.classList.remove('active');
        document.body.style.overflow = '';
    }

    overlay.addEventListener('click', closeSidebar);

    // Wire toggle button — it's injected by AppNavbar so we use event delegation
    document.addEventListener('click', (e) => {
        if (e.target.closest('#sidebarToggle')) {
            const sidebar = document.querySelector('app-sidebar');
            if (sidebar && sidebar.classList.contains('sidebar-open')) {
                closeSidebar();
            } else {
                openSidebar();
            }
        }
    });

    // Close sidebar on nav-link click on mobile
    document.addEventListener('click', (e) => {
        if (e.target.closest('.nav-link') && window.innerWidth <= 768) {
            closeSidebar();
        }
    });

    // Close on resize to desktop
    window.addEventListener('resize', () => {
        if (window.innerWidth > 768) {
            closeSidebar();
        }
    });
})();

// ======================================================
// PAGE SKELETON LOADER
// Shows a shimmer skeleton while the page is loading,
// then fades it out once everything is ready.
// ======================================================
(function initSkeletonLoader() {
    // Don't show skeleton on login/auth pages
    const path = window.location.pathname;
    if (path === '/' || path.includes('/login') || path.includes('/forgot-password') ||
        path.includes('/auth/') || path.includes('/reset-password')) {
        return;
    }

    const isMobile = window.innerWidth <= 768;
    const sidebarMargin = isMobile ? '0' : '260px';

    const skeleton = document.createElement('div');
    skeleton.id = 'page-skeleton';
    skeleton.innerHTML = `
        <div class="sk-header" style="margin-left: ${sidebarMargin}">
            <div class="skeleton" style="width: 40px; height: 40px; border-radius: 8px;"></div>
            <div class="skeleton-text medium" style="height: 1.2rem;"></div>
            <div style="margin-left: auto; display: flex; gap: 1rem;">
                <div class="skeleton-circle"></div>
                <div class="skeleton-circle"></div>
            </div>
        </div>
        <div class="sk-body" style="margin-left: ${sidebarMargin}">
            <div class="sk-row">
                <div class="sk-card">
                    <div class="skeleton-text short"></div>
                    <div class="skeleton" style="height: 2.5rem; border-radius: 6px;"></div>
                </div>
                <div class="sk-card">
                    <div class="skeleton-text short"></div>
                    <div class="skeleton" style="height: 2.5rem; border-radius: 6px;"></div>
                </div>
                <div class="sk-card">
                    <div class="skeleton-text short"></div>
                    <div class="skeleton" style="height: 2.5rem; border-radius: 6px;"></div>
                </div>
            </div>
            <div class="sk-card" style="padding: 1.5rem;">
                <div class="skeleton-text medium" style="height: 1.2rem; margin-bottom: 1.5rem;"></div>
                ${[...Array(5)].map(() => `
                    <div class="sk-table-row">
                        <div class="skeleton-text" style="flex: 2;"></div>
                        <div class="skeleton-text" style="flex: 1;"></div>
                        <div class="skeleton-text" style="flex: 1;"></div>
                        <div class="skeleton-text" style="flex: 0.5;"></div>
                    </div>
                `).join('')}
            </div>
        </div>
    `;

    document.body.appendChild(skeleton);

    // Remove skeleton when page is fully loaded
    function removeSkeleton() {
        skeleton.classList.add('hidden');
        setTimeout(() => skeleton.remove(), 450);
    }

    if (document.readyState === 'complete') {
        // Already loaded — just remove quickly
        setTimeout(removeSkeleton, 200);
    } else {
        window.addEventListener('load', () => setTimeout(removeSkeleton, 100));
        // Safety timeout — always remove after 3 seconds
        setTimeout(removeSkeleton, 3000);
    }
})();

// ======================================================
// HAMBURGER BUTTON — injected into AppNavbar dynamically
// ======================================================
// We patch AppNavbar's render to include the hamburger btn.
// This runs after customElements.define so we wrap via MutationObserver.
(function patchNavbarWithHamburger() {
    const observer = new MutationObserver(() => {
        const navbar = document.querySelector('app-navbar');
        if (!navbar) return;
        const header = navbar.querySelector('header');
        if (!header || header.querySelector('#sidebarToggle')) return;

        // Prepend hamburger to the header
        const hamburger = document.createElement('button');
        hamburger.id = 'sidebarToggle';
        hamburger.setAttribute('aria-label', 'Toggle sidebar');
        hamburger.style.cssText = `
            display: none;
            background: none;
            border: none;
            cursor: pointer;
            padding: 0.5rem;
            color: var(--text-dark);
            font-size: 1.4rem;
            align-items: center;
            justify-content: center;
            border-radius: var(--border-radius-sm);
            transition: background 0.2s ease;
            margin-right: 0.5rem;
        `;
        hamburger.innerHTML = '<i class="fas fa-bars"></i>';
        hamburger.addEventListener('mouseover', () => hamburger.style.background = 'rgba(255,140,0,0.08)');
        hamburger.addEventListener('mouseout', () => hamburger.style.background = 'transparent');

        header.insertBefore(hamburger, header.firstChild);
        observer.disconnect();
    });
    observer.observe(document.body, { childList: true, subtree: true });
})();
