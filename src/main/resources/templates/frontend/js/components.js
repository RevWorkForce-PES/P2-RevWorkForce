class AppSidebar extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        // Always fetch the freshest role when connecting to DOM, in case app.js updated it
        this.role = localStorage.getItem('userRole') || 'employee';
        this.render();
        this.setupEventListeners();
    }

    render() {
        let linksHtml = '';

        const path = window.location.pathname;
        let prefix = '../'; // default for 1 level deep
        if (path.includes('/pages/admin/') || path.includes('/pages/manager/') || path.includes('/pages/employee/')) {
            prefix = '../../';
        } else if (path.includes('/pages/auth/')) {
            prefix = '../../';
        } else if (path.endsWith('index.html') || path === '/' || path.endsWith('/frontend/')) {
            prefix = './';
        }

        if (this.role === 'admin') {
            linksHtml = `
                <li class="nav-item">
                    <a href="${prefix}pages/admin/dashboard.html" class="nav-link">
                        <i class="fas fa-home"></i> <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/admin/employee-management.html" class="nav-link">
                        <i class="fas fa-users"></i> <span>Employee Mgt</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/admin/system-config.html" class="nav-link">
                        <i class="fas fa-cogs"></i> <span>System Config</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/admin/audit-reports.html" class="nav-link">
                        <i class="fas fa-file-alt"></i> <span>Audit & Reports</span>
                    </a>
                </li>
            `;
        } else if (this.role === 'manager') {
            linksHtml = `
                <li class="nav-item">
                    <a href="${prefix}pages/manager/dashboard.html" class="nav-link">
                        <i class="fas fa-home"></i> <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/manager/team-management.html" class="nav-link">
                        <i class="fas fa-users-cog"></i> <span>Team Mgt</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/manager/leave-approvals.html" class="nav-link">
                        <i class="fas fa-calendar-check"></i> <span>Leave Approvals</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/manager/performance-review.html" class="nav-link">
                        <i class="fas fa-chart-line"></i> <span>Performance</span>
                    </a>
                </li>
            `;
        } else {
            // Employee
            linksHtml = `
                <li class="nav-item">
                    <a href="${prefix}pages/employee/dashboard.html" class="nav-link">
                        <i class="fas fa-home"></i> <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/employee/profile-directory.html" class="nav-link">
                        <i class="fas fa-id-card"></i> <span>Profile & Directory</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/employee/leave-management.html" class="nav-link">
                        <i class="fas fa-calendar-alt"></i> <span>Leave Management</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${prefix}pages/employee/performance-goals.html" class="nav-link">
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
                        <span class="user-name">John Doe</span>
                        <span class="user-role">${this.role}</span>
                    </div>
                    <button class="logout-btn" id="logoutBtn" title="Logout">
                        <i class="fas fa-sign-out-alt"></i>
                    </button>
                </div>
            </aside>
        `;

        this.highlightActiveLink();
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
                const path = window.location.pathname;
                let prefix = '../'; // default for 1 level deep
                if (path.includes('/pages/admin/') || path.includes('/pages/manager/') || path.includes('/pages/employee/')) {
                    prefix = '../../';
                } else if (path.includes('/pages/auth/')) {
                    prefix = '../../';
                }
                window.location.href = prefix + 'pages/auth/index.html';
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
        const path = window.location.pathname;
        let prefix = '../'; // default for 1 level deep
        if (path.includes('/pages/admin/') || path.includes('/pages/manager/') || path.includes('/pages/employee/')) {
            prefix = '../../';
        } else if (path.includes('/pages/auth/')) {
            prefix = '../../';
        } else if (path.endsWith('index.html') || path === '/' || path.endsWith('/frontend/')) {
            prefix = './';
        }

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
                                <button style="background:none;border:none;color:var(--primary);cursor:pointer;font-size:0.8rem;">Mark all as read</button>
                            </div>
                            <ul class="notification-list" id="notificationList">
                                <!-- Populated dynamically -->
                            </ul>
                            <div class="notification-footer">
                                <a href="#">View All Activity</a>
                            </div>
                        </div>
                    </div>
                    
                    <button class="icon-btn" title="Change Password" onclick="window.location.href = '${prefix}pages/auth/reset-password.html'">
                        <i class="fas fa-key"></i>
                    </button>
                    <button class="icon-btn" title="Settings" onclick="window.location.href = '${prefix}pages/auth/settings.html'">
                        <i class="fas fa-cog"></i>
                    </button>
                </div>
            </header>
        `;

        this.setupDropdownLogic();
    }

    setupDropdownLogic() {
        const role = localStorage.getItem('userRole') || 'employee';
        const dropdown = this.querySelector('#notificationDropdown');
        const btn = this.querySelector('#notificationBtn');
        const list = this.querySelector('#notificationList');
        const badge = this.querySelector('#notificationBadge');

        // Role-based mock data
        let notifications = [];
        if (role === 'admin') {
            notifications = [
                { icon: 'fa-shield-alt', text: 'System backup completed successfully.', time: '10 mins ago' },
                { icon: 'fa-exclamation-triangle', text: 'High CPU usage detected on server 2.', time: '1 hour ago' },
                { icon: 'fa-user-plus', text: '5 new employees pending onboarding.', time: '2 hours ago' }
            ];
        } else if (role === 'manager') {
            notifications = [
                { icon: 'fa-calendar-check', text: 'Alice placed a leave request (Sick Leave).', time: '5 mins ago' },
                { icon: 'fa-bullseye', text: 'Charlie updated his Q1 OKRs.', time: '1 hour ago' },
                { icon: 'fa-envelope', text: 'HR sent a reminder for performance reviews.', time: 'Yesterday' }
            ];
        } else {
            // Employee
            notifications = [
                { icon: 'fa-check-circle', text: 'Your Casual Leave request was approved.', time: 'Just now' },
                { icon: 'fa-bullhorn', text: 'Company Townhall meeting tomorrow.', time: '2 hours ago' },
                { icon: 'fa-birthday-cake', text: 'It is Diana\'s birthday today!', time: '4 hours ago' }
            ];
        }

        // Populate HTML
        badge.innerText = notifications.length;
        list.innerHTML = notifications.map(n => `
            <li class="notification-item" onclick="alert('Navigating to notification context...')">
                <div class="notify-icon"><i class="fas ${n.icon}"></i></div>
                <div class="notify-content">
                    <p>${n.text}</p>
                    <span>${n.time}</span>
                </div>
            </li>
        `).join('');

        // Toggle logic
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            dropdown.classList.toggle('show');
        });

        // Close when clicking outside
        document.addEventListener('click', (e) => {
            if (!dropdown.contains(e.target) && e.target !== btn) {
                dropdown.classList.remove('show');
            }
        });
    }
}

customElements.define('app-navbar', AppNavbar);
