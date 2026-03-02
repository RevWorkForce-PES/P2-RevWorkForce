// Utility functions for RevWorkForce Frontend

function mockLogin(role) {
    localStorage.setItem('userRole', role);
    // Redirect logic - Handle deep paths relative to index location
    const currentUrl = window.location.href;
    const baseUrl = currentUrl.replace('auth/index.html', '').replace('auth/', '');

    if (role === 'admin') {
        window.location.href = baseUrl + 'pages/admin/dashboard.html';
    } else if (role === 'manager') {
        window.location.href = baseUrl + 'pages/manager/dashboard.html';
    } else {
        window.location.href = baseUrl + 'pages/employee/dashboard.html';
    }
}

function checkAuth() {
    // Disabled for testing mode - Allow direct access to all pages
    const role = localStorage.getItem('userRole');
    if (!role || window.location.pathname.includes('/pages/')) {
        // Infer the role from the path for testing mode so the correct sidebar renders
        const path = window.location.pathname;
        if (path.includes('/pages/admin/')) {
            localStorage.setItem('userRole', 'admin');
        } else if (path.includes('/pages/manager/')) {
            localStorage.setItem('userRole', 'manager');
        } else if (path.includes('/pages/employee/')) {
            localStorage.setItem('userRole', 'employee');
        }
    }

    // Uncomment for production (Requires auth to view /pages/ folder)
    /*
    if (!role && path.includes('/pages/') && !path.includes('/pages/auth/')) {
        let prefix = '../../'; 
        window.location.href = prefix + 'pages/auth/index.html';
    }
    */
}

// Ensure auth check runs on protected pages
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    // Add FontAwesome dynamically
    if (!document.querySelector('link[href*="font-awesome"]')) {
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css';
        document.head.appendChild(link);
    }

    // Initialize tabs if present
    initTabs();
});

// Tab switching logic for consolidated pages
function initTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    if (tabBtns.length === 0) return;

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            // Remove active class from all
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));

            // Add active class to clicked
            btn.classList.add('active');
            const targetId = btn.getAttribute('data-target');
            document.getElementById(targetId).classList.add('active');
        });
    });
}
