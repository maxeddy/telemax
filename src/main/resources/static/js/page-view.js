(function() {
    var container = document.getElementById('page-container');
    var contentDiv = container.querySelector('.content');
    var partIndicator = document.getElementById('part-indicator');

    var breakPoints = [];
    var totalParts = 0;
    var currentPart = 0;
    var pageHeight = 0;

    function measure() {
        contentDiv.style.overflowY = 'auto';
        contentDiv.style.clipPath = '';
        contentDiv.scrollTop = 0;

        if (contentDiv.scrollHeight <= contentDiv.clientHeight) {
            breakPoints = [];
            totalParts = 0;
            currentPart = 0;
            partIndicator.textContent = '';
            return;
        }

        contentDiv.style.overflowY = 'hidden';
        pageHeight = contentDiv.clientHeight;

        // Build line positions from paragraphs (relative to content div)
        var lineTops = [];
        var pars = contentDiv.querySelectorAll('.paragraph');
        var lineHeight = 0;
        var contentRect = contentDiv.getBoundingClientRect();

        for (var i = 0; i < pars.length; i++) {
            var p = pars[i];
            lineHeight = parseFloat(getComputedStyle(p).lineHeight);
            var top = p.getBoundingClientRect().top - contentRect.top + contentDiv.scrollTop;
            var numLines = Math.max(1, Math.round(p.offsetHeight / lineHeight));
            for (var j = 0; j < numLines; j++) {
                lineTops.push(top + j * lineHeight);
            }
        }

        // Compute line-aligned break points
        breakPoints = [0];
        var currentStart = 0;

        for (var i = 0; i < lineTops.length; i++) {
            if (lineTops[i] + lineHeight - currentStart > pageHeight) {
                currentStart = lineTops[i];
                breakPoints.push(currentStart);
            }
        }

        totalParts = breakPoints.length;
        currentPart = 0;
        showPart(0);
    }

    function showPart(index) {
        currentPart = index;
        contentDiv.scrollTop = breakPoints[index];

        // Clip partial content at the bottom so no line is half-visible
        var nextBreak = index < breakPoints.length - 1
            ? breakPoints[index + 1]
            : contentDiv.scrollHeight;
        var visibleHeight = Math.min(nextBreak - breakPoints[index], pageHeight);
        var clipBottom = pageHeight - visibleHeight;
        contentDiv.style.clipPath = clipBottom > 0
            ? 'inset(0 0 ' + clipBottom + 'px 0)'
            : '';

        partIndicator.textContent = (index + 1) + '/' + totalParts;
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
        } else if (absDy >= 50 && absDy > absDx && totalParts > 0) {
            // Vertical swipe -> navigate between parts
            if (dy < 0 && currentPart < totalParts - 1) {
                showPart(currentPart + 1);
            } else if (dy > 0 && currentPart > 0) {
                showPart(currentPart - 1);
            }
        }
    }, { passive: true });

    measure();
    window.addEventListener('resize', measure);
})();
