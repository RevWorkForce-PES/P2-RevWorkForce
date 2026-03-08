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
                    <a href="${basePath}/admin/dashboard" class="nav-link">
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

        // Initial attempt to populate user name/role from injected meta tags
        this.populateFromMeta();

        // If meta tags are missing (as on many pages), try to load from API
        const nameEl = this.querySelector('#sidebarUserName');
        if (nameEl && nameEl.textContent === 'Loading...') {
            this.loadUserInfo();
        }
    }

    populateFromMeta() {
        const nameEl = this.querySelector('#sidebarUserName');
        const roleEl = this.querySelector('#sidebarUserRole');
        const metaName = document.querySelector('meta[name="user-fullname"]');
        const metaRole = document.querySelector('meta[name="user-role"]');

        if (nameEl && metaName && metaName.content !== 'User') {
            nameEl.textContent = metaName.content;
        }
        if (roleEl && metaRole && metaRole.content !== 'User') {
            roleEl.textContent = metaRole.content;
        }
    }

    async loadUserInfo() {
        const basePath = window.location.pathname.startsWith('/RevWorkForce') ? '/RevWorkForce' : '';
        try {
            const response = await fetch(`${basePath}/api/user/info`);
            if (response.ok) {
                const data = await response.json();
                const nameEl = this.querySelector('#sidebarUserName');
                const roleEl = this.querySelector('#sidebarUserRole');

                if (nameEl && data.fullName) nameEl.textContent = data.fullName;
                if (roleEl && data.role) roleEl.textContent = data.role;
            }
        } catch (error) {
            console.error('Failed to load user info:', error);
            const nameEl = this.querySelector('#sidebarUserName');
            if (nameEl) nameEl.textContent = 'User';
        }
    }

    highlightActiveLink() {
        // Simple highlighting based on current URL path
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
                .icon-btn:hover {
                    color: var(--primary);
                }
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
                .notification-dropdown.show {
                    display: flex;
                }
                .notification-header {
                    padding: 1rem;
                    border-bottom: 1px solid var(--border-color);
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    background: #f8fafc;
                }
                .notification-header h3 {
                    margin: 0;
                    font-size: 1rem;
                }
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
                .notification-item:hover {
                    background: #f1f5f9;
                }
                .notification-item:last-child {
                    border-bottom: none;
                }
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
                .notify-content p {
                    margin: 0 0 0.25rem 0;
                    font-size: 0.9rem;
                    color: var(--text-dark);
                }
                .notify-content span {
                    font-size: 0.75rem;
                    color: var(--text-muted);
                }
                .notification-footer {
                    padding: 0.75rem;
                    text-align: center;
                    border-top: 1px solid var(--border-color);
                    background: #f8fafc;
                }
                .notification-footer a {
                    color: var(--primary);
                    text-decoration: none;
                    font-size: 0.85rem;
                    font-weight: 500;
                }
            </style>
            <header>
                <h1 class="page-title">${title}</h1>
                <div class="header-actions">
                    <div style="position: relative;">
                        <button class="icon-btn" title="System Notifications" id="notificationBtn">
                            <i class="fas fa-bell"></i>
                            <span class="notification-badge" id="notificationBadge">0</span>
                        </button>
                        
                        <div class="notification-dropdown" id="notificationDropdown">
                            <div class="notification-header">
                                <h3>Notifications</h3>
								<button id="markAllReadBtn"
								        style="background:none;border:none;color:var(--primary);cursor:pointer;font-size:0.8rem;">
								    Mark all as read
								</button>
								  </div>
                            <ul class="notification-list" id="notificationList">
                                <!-- Populated dynamically -->
                            </ul>
                            <div class="notification-footer">
                               <a href="/notifications">View All Activity</a>
                            </div>
                        </div>
                    </div>
                    
                    <button class="icon-btn" title="Change Password" onclick="window.location.href = '${basePath}/change-password'">
                        <i class="fas fa-key"></i>
                    </button>
                    <button class="icon-btn" title="Settings" onclick="window.location.href = '${basePath}/employee/profile'">
                        <i class="fas fa-cog"></i>
                    </button>
                </div>
            </header>
        `;

        this.setupDropdownLogic();
    }

    setupDropdownLogic() {
        const dropdown = this.querySelector('#notificationDropdown');
        const btn = this.querySelector('#notificationBtn');
        const list = this.querySelector('#notificationList');
        const badge = this.querySelector('#notificationBadge');
        const markAllBtn = this.querySelector('#markAllReadBtn');

        const loadUnreadCount = () => {
            fetch("/api/notifications/unread-count")
                .then(res => res.text())
                .then(count => {
                    const num = parseInt(count);
                    badge.innerText = num;
                    badge.style.display = num > 0 ? "flex" : "none";
                });
        };

        const markAsRead = (id) => {
            const token = document.querySelector('meta[name="_csrf"]').content;
            const header = document.querySelector('meta[name="_csrf_header"]').content;

            fetch(`/api/notifications/mark-read/${id}`, {
                method: "POST",
                headers: {
                    [header]: token
                }
            })
                .then(res => {
                    if (res.ok) {
                        loadUnreadCount();
                        loadNavbarNotifications();
                    }
                })
                .catch(err => console.error(err));
        };

        const loadNavbarNotifications = () => {
            fetch("/api/notifications/recent?limit=5")
                .then(res => res.json())
                .then(data => {
                    list.innerHTML = "";
                    if (!data || data.length === 0) {
                        list.innerHTML = `<li class="notification-item">No notifications</li>`;
                        return;
                    }

                    data.forEach(n => {
                        const li = document.createElement("li");
                        li.className = "notification-item";
                        if (n.isRead === 'N') li.style.background = "#f8fafc";

                        li.innerHTML = `
                            <div class="notify-icon" style="background: ${n.isRead === 'N' ? 'rgba(59,130,246,0.1)' : 'rgba(100,116,139,0.1)'}; color: ${n.isRead === 'N' ? '#3b82f6' : '#64748b'}">
                                <i class="fas fa-bell"></i>
                            </div>
                            <div class="notify-content" style="flex-grow: 1;">
                                <p style="font-weight: ${n.isRead === 'N' ? '600' : '400'}">${n.message}</p>
                                <span style="font-size: 0.7rem; color: #94a3b8;">${new Date(n.createdAt).toLocaleString()}</span>
                            </div>
                            ${n.isRead === 'N' ? `
                            <button class="mark-single-read" data-id="${n.notificationId}" title="Mark as read" 
                                    style="background:none; border:none; color:#10b981; cursor:pointer; padding: 4px;">
                                <i class="fas fa-check-circle"></i>
                            </button>` : ''}
                        `;

                        li.addEventListener("click", (e) => {
                            if (e.target.closest('.mark-single-read')) {
                                e.stopPropagation();
                                const id = e.target.closest('.mark-single-read').dataset.id;
                                markAsRead(id);
                                return;
                            }
                            window.location.href = "/notifications";
                        });

                        list.appendChild(li);
                    });
                });
        };

        if (markAllBtn) {
            markAllBtn.addEventListener("click", () => {
                const token = document.querySelector('meta[name="_csrf"]').content;
                const header = document.querySelector('meta[name="_csrf_header"]').content;

                fetch("/api/notifications/mark-all-read", {
                    method: "POST",
                    headers: {
                        [header]: token
                    }
                })
                    .then(res => {
                        if (!res.ok) throw new Error("Failed");
                        badge.innerText = 0;
                        badge.style.display = "none";
                        list.innerHTML = "<li class='notification-item'>No new notifications</li>";
                    })
                    .catch(err => console.error(err));
            });
        }

        // Toggle dropdown
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            dropdown.classList.toggle('show');
            loadNavbarNotifications();
        });

        document.addEventListener('click', (e) => {
            if (!dropdown.contains(e.target) && !btn.contains(e.target)) {
                dropdown.classList.remove('show');
            }
        });

        loadUnreadCount();
    }
}

customElements.define('app-navbar', AppNavbar);
