// Utility functions for RevWorkForce Frontend

function mockLogin(role) {
    localStorage.setItem('userRole', role);
    const basePath = window.location.pathname.startsWith('/RevWorkForce') ? '/RevWorkForce' : '';

    if (role === 'admin') {
        window.location.href = basePath + '/admin/dashboard';
    } else if (role === 'manager') {
        window.location.href = basePath + '/manager/dashboard';
    } else {
        window.location.href = basePath + '/employee/dashboard';
    }
}

function checkAuth() {
    // Spring Security handles backend authentication.
    // For pure JS components, we derive role based on the current context path.
    const path = window.location.pathname;
    if (path.includes('/admin/')) {
        localStorage.setItem('userRole', 'admin');
    } else if (path.includes('/manager/')) {
        localStorage.setItem('userRole', 'manager');
    } else if (path.includes('/employee/')) {
        localStorage.setItem('userRole', 'employee');
    }
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
