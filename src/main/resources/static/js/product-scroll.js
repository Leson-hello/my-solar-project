\u003cscript\u003e
document.addEventListener('DOMContentLoaded', function () {
    const scrollContainer = document.getElementById('productGridContainer');
    if (scrollContainer) {
        let isDown = false;
        let startX;
        let scrollLeft;

        scrollContainer.addEventListener('mousedown', (e) =\u003e {
            isDown = true;
            scrollContainer.style.cursor = 'grabbing';
            startX = e.pageX - scrollContainer.offsetLeft;
            scrollLeft = scrollContainer.scrollLeft;
        });

        scrollContainer.addEventListener('mousele', () =\u003e {
            isDown = false;
            scrollContainer.style.cursor = 'grab';
        });

        scrollContainer.addEventListener('mouseup', () =\u003e {
            isDown = false;
            scrollContainer.style.cursor = 'grab';
        });

        scrollContainer.addEventListener('mousemove', (e) =\u003e {
            if(!isDown) return;
            e.preventDefault();
            const x = e.pageX - scrollContainer.offsetLeft;
            const walk = (x - startX) * 2; // Scroll speed multiplier
            scrollContainer.scrollLeft = scrollLeft - walk;
        });
    }
});
\u003c / script\u003e
