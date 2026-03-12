(function() {
    var container = document.getElementById('page-container');

    // Swipe navigation between pages
    var startX = 0;
    var startY = 0;

    container.addEventListener('touchstart', function(e) {
        startX = e.touches[0].clientX;
        startY = e.touches[0].clientY;
    }, { passive: true });

    container.addEventListener('touchend', function(e) {
        var dx = e.changedTouches[0].clientX - startX;
        var dy = e.changedTouches[0].clientY - startY;

        if (Math.abs(dx) >= 50 && Math.abs(dx) > Math.abs(dy)) {
            if (dx < 0 && container.dataset.next) {
                window.location.href = '/' + container.dataset.next;
            } else if (dx > 0 && container.dataset.prev) {
                window.location.href = '/' + container.dataset.prev;
            }
        }
    }, { passive: true });
})();
