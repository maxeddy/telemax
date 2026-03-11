(function() {
    var container = document.getElementById('page-container');
    var contentDiv = container.querySelector('.content');
    var partIndicator = document.getElementById('part-indicator');
    var paragraphs = contentDiv.querySelectorAll('.paragraph');

    var parts = [];
    var currentPart = 0;

    function measureAndSplit() {
        // Show all paragraphs to measure
        for (var i = 0; i < paragraphs.length; i++) {
            paragraphs[i].style.display = '';
        }
        contentDiv.style.overflowY = 'auto';

        if (contentDiv.scrollHeight <= contentDiv.clientHeight) {
            parts = [];
            currentPart = 0;
            partIndicator.textContent = '';
            return;
        }

        // Disable scrolling since we're paginating
        contentDiv.style.overflowY = 'hidden';

        var availableHeight = contentDiv.clientHeight;
        parts = [];
        var currentGroup = [];
        var accumulatedHeight = 0;

        for (var i = 0; i < paragraphs.length; i++) {
            var p = paragraphs[i];
            var style = window.getComputedStyle(p);
            var marginBottom = parseFloat(style.marginBottom) || 0;
            var totalHeight = p.offsetHeight + marginBottom;

            if (accumulatedHeight + totalHeight > availableHeight && currentGroup.length > 0) {
                parts.push(currentGroup.slice());
                currentGroup = [];
                accumulatedHeight = 0;
            }
            currentGroup.push(i);
            accumulatedHeight += totalHeight;
        }
        if (currentGroup.length > 0) {
            parts.push(currentGroup.slice());
        }

        currentPart = 0;
        showPart(0);
    }

    function showPart(index) {
        currentPart = index;
        var visible = parts[index];
        for (var i = 0; i < paragraphs.length; i++) {
            paragraphs[i].style.display = visible.indexOf(i) >= 0 ? '' : 'none';
        }
        partIndicator.textContent = (index + 1) + '/' + parts.length;
    }

    // Swipe navigation
    var startX = 0;
    var startY = 0;

    container.addEventListener('touchstart', function(e) {
        startX = e.touches[0].clientX;
        startY = e.touches[0].clientY;
    }, { passive: true });

    container.addEventListener('touchend', function(e) {
        var dx = e.changedTouches[0].clientX - startX;
        var dy = e.changedTouches[0].clientY - startY;
        var absDx = Math.abs(dx);
        var absDy = Math.abs(dy);

        if (absDx >= 50 && absDx > absDy) {
            // Horizontal swipe -> navigate between pages
            if (dx < 0 && container.dataset.next) {
                window.location.href = '/' + container.dataset.next;
            } else if (dx > 0 && container.dataset.prev) {
                window.location.href = '/' + container.dataset.prev;
            }
        } else if (absDy >= 50 && absDy > absDx && parts.length > 0) {
            // Vertical swipe -> navigate between parts
            if (dy < 0 && currentPart < parts.length - 1) {
                showPart(currentPart + 1);
            } else if (dy > 0 && currentPart > 0) {
                showPart(currentPart - 1);
            }
        }
    }, { passive: true });

    measureAndSplit();
    window.addEventListener('resize', measureAndSplit);
})();
