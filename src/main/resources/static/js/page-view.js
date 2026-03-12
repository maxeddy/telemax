(function() {
    var container = document.getElementById('page-container');
    var content = container.querySelector('.content');
    var indicator = document.getElementById('part-indicator');
    var CONTENT_ROWS = 19;
    var TOTAL_ROWS = 24;
    var LINE_HEIGHT_RATIO = 1.4;

    var parts = []; // each entry: { scrollY, visibleHeight }
    var currentPart = 0;
    var totalParts = 1;

    function computeFontSize() {
        var h = container.clientHeight;
        var fontSize = h / TOTAL_ROWS / LINE_HEIGHT_RATIO;
        container.style.fontSize = fontSize + 'px';
        return fontSize;
    }

    function measure() {
        var fontSize = computeFontSize();
        var lineHeight = fontSize * LINE_HEIGHT_RATIO;
        var maxHeight = lineHeight * CONTENT_ROWS;

        content.style.clipPath = '';
        content.style.paddingBottom = '';
        content.scrollTop = 0;

        // Check if content overflows
        if (content.scrollHeight <= maxHeight + 1) {
            parts = [{ scrollY: 0, visibleHeight: 0 }];
            totalParts = 1;
            currentPart = 0;
            updateIndicator();
            return;
        }

        // Build line map: each entry is cumulative Y position from top of content
        var paragraphs = content.querySelectorAll('.paragraph');
        var lineTops = [];
        var y = 0;

        for (var i = 0; i < paragraphs.length; i++) {
            var p = paragraphs[i];
            var pLines = Math.round(p.offsetHeight / lineHeight);
            for (var j = 0; j < pLines; j++) {
                lineTops.push(y + j * lineHeight);
            }
            y += p.offsetHeight;
            // Add blank line for margin-bottom (1.4em = one grid row) unless last paragraph
            if (i < paragraphs.length - 1) {
                lineTops.push(y);
                y += lineHeight;
            }
        }

        // Compute subpages: each shows lines that fit within maxHeight
        parts = [];
        var startLine = 0;

        while (startLine < lineTops.length) {
            var startY = lineTops[startLine];

            // Find last line that fits
            var lastFit = startLine;
            for (var k = startLine; k < lineTops.length; k++) {
                if (lineTops[k] + lineHeight <= startY + maxHeight + 1) {
                    lastFit = k;
                } else {
                    break;
                }
            }

            var visibleHeight = lineTops[lastFit] + lineHeight - startY;
            parts.push({ scrollY: startY, visibleHeight: visibleHeight });

            // Next subpage starts at the line after lastFit
            var nextStart = lastFit + 1;
            if (nextStart >= lineTops.length) break;
            startLine = nextStart;
        }

        totalParts = parts.length;

        // Ensure content can scroll to all breakpoints.
        // scrollTop is capped at scrollHeight - clientHeight, so the last
        // subpage may be unreachable without extra padding.
        if (totalParts > 1) {
            var lastScrollY = parts[totalParts - 1].scrollY;
            var maxScrollTop = content.scrollHeight - content.clientHeight;
            if (lastScrollY > maxScrollTop) {
                content.style.paddingBottom = (lastScrollY - maxScrollTop) + 'px';
            }
        }

        currentPart = 0;
        showPart(0);
    }

    function showPart(index) {
        if (index < 0 || index >= totalParts) return;
        currentPart = index;

        content.scrollTop = parts[index].scrollY;

        if (totalParts > 1) {
            // clipPath inset is relative to the element's visible box (clientHeight)
            var clipFromBottom = content.clientHeight - parts[index].visibleHeight;
            if (clipFromBottom > 1) {
                content.style.clipPath = 'inset(0 0 ' + clipFromBottom + 'px 0)';
            } else {
                content.style.clipPath = '';
            }
        } else {
            content.style.clipPath = '';
        }

        updateIndicator();
    }

    function updateIndicator() {
        if (totalParts <= 1) {
            indicator.textContent = '';
        } else {
            indicator.textContent = (currentPart + 1) + '/' + totalParts;
        }
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

        // Vertical swipe: navigate subpages
        if (Math.abs(dy) >= 50 && Math.abs(dy) > Math.abs(dx)) {
            if (dy < 0 && currentPart < totalParts - 1) {
                showPart(currentPart + 1);
            } else if (dy > 0 && currentPart > 0) {
                showPart(currentPart - 1);
            }
            return;
        }

        // Horizontal swipe: navigate pages
        if (Math.abs(dx) >= 50 && Math.abs(dx) > Math.abs(dy)) {
            if (dx < 0 && container.dataset.next) {
                window.location.href = '/' + container.dataset.next;
            } else if (dx > 0 && container.dataset.prev) {
                window.location.href = '/' + container.dataset.prev;
            }
        }
    }, { passive: true });

    // Keyboard navigation
    document.addEventListener('keydown', function(e) {
        if (e.key === 'ArrowRight' && container.dataset.next) {
            window.location.href = '/' + container.dataset.next;
        } else if (e.key === 'ArrowLeft' && container.dataset.prev) {
            window.location.href = '/' + container.dataset.prev;
        } else if (e.key === 'ArrowDown' && currentPart < totalParts - 1) {
            e.preventDefault();
            showPart(currentPart + 1);
        } else if (e.key === 'ArrowUp' && currentPart > 0) {
            e.preventDefault();
            showPart(currentPart - 1);
        }
    });

    window.addEventListener('resize', function() {
        measure();
    });

    measure();
})();
