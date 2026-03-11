document.addEventListener('DOMContentLoaded', function () {
    const addBtn = document.getElementById('add-paragraph');
    const container = document.getElementById('paragraphs-container');

    if (addBtn && container) {
        addBtn.addEventListener('click', function () {
            const index = container.querySelectorAll('.paragraph-group').length;
            const group = document.createElement('div');
            group.className = 'paragraph-group';
            group.innerHTML =
                '<div class="paragraph-header">' +
                    '<span>Paragraph ' + (index + 1) + '</span>' +
                    '<button type="button" class="btn-small btn-delete remove-paragraph">Remove</button>' +
                '</div>' +
                '<div class="form-group">' +
                    '<label for="paragraphs' + index + '.text">Text</label>' +
                    '<textarea id="paragraphs' + index + '.text" name="paragraphs[' + index + '].text"></textarea>' +
                '</div>' +
                '<div class="checkbox-group">' +
                    '<input type="hidden" name="paragraphs[' + index + '].hasBullet" value="false">' +
                    '<input type="checkbox" id="paragraphs' + index + '.hasBullet" name="paragraphs[' + index + '].hasBullet" value="true">' +
                    '<label for="paragraphs' + index + '.hasBullet">Show bullet</label>' +
                '</div>';
            container.appendChild(group);
        });

        container.addEventListener('click', function (e) {
            if (e.target.classList.contains('remove-paragraph')) {
                e.target.closest('.paragraph-group').remove();
                reindex();
            }
        });
    }

    function reindex() {
        var groups = container.querySelectorAll('.paragraph-group');
        groups.forEach(function (group, i) {
            group.querySelector('.paragraph-header span').textContent = 'Paragraph ' + (i + 1);
            var textarea = group.querySelector('textarea');
            textarea.id = 'paragraphs' + i + '.text';
            textarea.name = 'paragraphs[' + i + '].text';
            var hidden = group.querySelector('input[type="hidden"]');
            hidden.name = 'paragraphs[' + i + '].hasBullet';
            var checkbox = group.querySelector('input[type="checkbox"]');
            checkbox.id = 'paragraphs' + i + '.hasBullet';
            checkbox.name = 'paragraphs[' + i + '].hasBullet';
            var label = group.querySelector('.checkbox-group label');
            label.setAttribute('for', 'paragraphs' + i + '.hasBullet');
            var textLabel = group.querySelector('.form-group label');
            textLabel.setAttribute('for', 'paragraphs' + i + '.text');
        });
    }
});
