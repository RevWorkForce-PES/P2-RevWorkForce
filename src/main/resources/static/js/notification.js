document.addEventListener("DOMContentLoaded", function () {

    loadUnreadCount();

    const dropdown = document.getElementById("notificationDropdown");

    if (dropdown) {
        dropdown.addEventListener("show.bs.dropdown", function () {
            loadRecentNotifications();
        });
    }
});


// ======================
// UNREAD COUNT
// ======================
function loadUnreadCount() {

    fetch("/api/notifications/unread-count")
        .then(response => {
            if (!response.ok) throw new Error("Failed to fetch unread count");
            return response.text();
        })
        .then(count => {

            const number = parseInt(count);

            // 🔔 Update navbar badge
            const badge = document.getElementById("notificationBadge");
            if (badge) {
                if (number > 0) {
                    badge.textContent = number;
                    badge.style.display = "inline-block";
                } else {
                    badge.style.display = "none";
                }
            }

            // 📦 Update dashboard card count
            const dashboardCount = document.getElementById("dashboardNotificationCount");
            if (dashboardCount) {
                dashboardCount.textContent = number;
            }

        })
        .catch(error => console.error("Error loading unread count:", error));
}


// ======================
// LOAD RECENT
// ======================
function loadRecentNotifications() {

    fetch("/api/notifications/recent?limit=5")
        .then(response => {
            if (!response.ok) throw new Error("Failed to fetch recent notifications");
            return response.json();
        })
        .then(notifications => {

            const container = document.getElementById("notificationDropdownList");
            if (!container) return;

            container.innerHTML = "";

            if (!notifications || notifications.length === 0) {
                container.innerHTML =
                    `<li class="text-center p-3 text-muted">No notifications</li>`;
                return;
            }

            notifications.forEach(notification => {

                const li = document.createElement("li");

                li.innerHTML = `
                    <div class="dropdown-item ${notification.isRead === 'N' ? 'bg-light fw-bold' : ''}"
                        style="cursor:pointer;">
                        <strong>${notification.title}</strong><br>
                        <small>${notification.message}</small>
                    </div>
                `;

                li.addEventListener("click", function () {
                    markAsRead(notification.notificationId, notification.notificationType);
                });

                container.appendChild(li);
            });
        })
        .catch(error => console.error("Error loading recent notifications:", error));
}


// ======================
// MARK SINGLE
// ======================
function markAsRead(notificationId, type) {

    fetch("/api/notifications/mark-read/" + notificationId, {
        method: "POST"
    })
    .then(response => {
        if (!response.ok) throw new Error("Failed to mark as read");
        redirectBasedOnType(type);
    })
    .catch(error => console.error("Error marking notification:", error));
}


// ======================
function redirectBasedOnType(type) {

    switch(type) {
        case "LEAVE":
            window.location.href = "/leave/employee/history";
            break;
        case "PERFORMANCE":
            window.location.href = "/performance/my-reviews";
            break;
        case "GOAL":
            window.location.href = "/goal/my-goals";
            break;
        case "ANNOUNCEMENT":
            window.location.href = "/announcements";
            break;
        default:
            window.location.href = "/notifications";
    }
}

function viewAllNotifications(event) {

    event.preventDefault();

    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch("/api/notifications/mark-all-read", {
        method: "POST",
        headers: {
            [header]: token
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Failed to mark all read");

        window.location.href = "/notifications";
    })
    .catch(error => console.error("Error marking all read:", error));
}