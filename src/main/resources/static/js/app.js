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
    const params = new URLSearchParams(window.location.search);
    const tab = params.get("tab");

    if (tab) {

        const tabBtns = document.querySelectorAll('.tab-btn');
        const tabContents = document.querySelectorAll('.tab-content');

        // remove all active classes
        tabBtns.forEach(btn => btn.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));

        const targetTab = document.getElementById(tab);
        const targetButton = document.querySelector(`[data-target="${tab}"]`);

        if (targetTab && targetButton) {
            targetButton.classList.add("active");
            targetTab.classList.add("active");
        }
    }

    // Initialize tabs AFTER setting correct active tab
    initTabs();

});

// Tab switching logic for consolidated pages
function initTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    if (!tabBtns.length) return;

    tabBtns.forEach(btn => {
        btn.addEventListener('click', function () {

            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));

            this.classList.add('active');

            const targetId = this.dataset.target;
            const target = document.getElementById(targetId);

            if (target) {
                target.classList.add('active');
            }
        });
    });
}
