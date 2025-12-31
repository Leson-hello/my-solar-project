// HBMP Solar Website JavaScript

document.addEventListener('DOMContentLoaded', function () {

    // Smooth scrolling for navigation links
    const navLinks = document.querySelectorAll('a[href^="#"]');
    navLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            if (targetSection) {
                targetSection.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    // Fade in animation for elements
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function (entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, observerOptions);

    // Add fade-in class to sections
    const sections = document.querySelectorAll('section');
    sections.forEach(section => {
        section.classList.add('fade-in');
        observer.observe(section);
    });

    // Navbar background change on scroll
    const navbar = document.querySelector('.main-nav');
    window.addEventListener('scroll', function () {
        if (window.scrollY > 100) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    });

    // Counter animation for statistics
    const counters = document.querySelectorAll('.stat-number');
    const animateCounter = (counter) => {
        const target = counter.textContent.replace(/[^0-9.]/g, '');
        const numTarget = parseFloat(target);
        let count = 0;
        const increment = numTarget / 100;

        const updateCounter = () => {
            if (count < numTarget) {
                count += increment;
                if (counter.textContent.includes('+')) {
                    counter.textContent = Math.ceil(count) + '+';
                } else {
                    counter.textContent = Math.ceil(count);
                }
                setTimeout(updateCounter, 20);
            } else {
                counter.textContent = counter.textContent; // Reset to original
            }
        };

        updateCounter();
    };

    // Trigger counter animation when statistics section is visible
    const statsSection = document.querySelector('.statistics-section');
    if (statsSection) {
        const statsObserver = new IntersectionObserver(function (entries) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    counters.forEach(counter => {
                        animateCounter(counter);
                    });
                    statsObserver.unobserve(entry.target);
                }
            });
        }, { threshold: 0.5 });

        statsObserver.observe(statsSection);
    }

    // Project category filter
    const projectButtons = document.querySelectorAll('.project-buttons .btn');
    projectButtons.forEach(button => {
        button.addEventListener('click', function () {
            // Remove active class from all buttons
            projectButtons.forEach(btn => btn.classList.remove('active'));
            // Add active class to clicked button
            this.classList.add('active');

            // Here you can add logic to filter projects
            console.log('Filter projects by:', this.textContent);
        });
    });

    // Mobile menu toggle
    const mobileMenuToggle = document.querySelector('.navbar-toggler');
    const mobileMenu = document.querySelector('#mainNav');
    const menuOverlay = document.querySelector('.menu-overlay');
    const mobileCloseBtn = document.querySelector('.btn-close-menu'); // If you add a specific class to the close button

    if (mobileMenu) {
        // Bootstrap handles the toggle of 'show' class mostly, but we can hook into it
        // Or manually handle it if we want custom behavior. 
        // Since we rely on Bootstrap's data-bs-toggle, let's listen for changes

        const observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                if (mutation.attributeName === "class") {
                    const isOpened = mobileMenu.classList.contains('show');
                    if (isOpened) {
                        if (menuOverlay) menuOverlay.classList.add('active');
                        document.body.style.overflow = 'hidden'; // Prevent scrolling
                    } else {
                        if (menuOverlay) menuOverlay.classList.remove('active');
                        document.body.style.overflow = '';
                    }
                }
            });
        });

        observer.observe(mobileMenu, {
            attributes: true
        });

        // click on overlay to close
        if (menuOverlay) {
            menuOverlay.addEventListener('click', function () {
                const bsCollapse = new bootstrap.Collapse(mobileMenu, {
                    toggle: false
                });
                bsCollapse.hide();
            });
        }
    }

    // Newsletter form submission
    const newsletterForm = document.querySelector('.newsletter-form');
    if (newsletterForm) {
        newsletterForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const emailInput = this.querySelector('input[type="email"]');
            const email = emailInput.value;
            const submitBtn = this.querySelector('button[type="submit"]');

            if (email) {
                submitBtn.disabled = true;
                const originalBtnText = submitBtn.textContent;
                submitBtn.textContent = 'ĐANG GỬI...';

                fetch('/api/newsletter/subscribe', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ email: email })
                })
                    .then(response => response.json())
                    .then(data => {
                        alert(data.message || 'Cảm ơn bạn đã đăng ký nhận tin tức từ HBMP Solar!');
                        emailInput.value = '';
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Có lỗi xảy ra, vui lòng thử lại sau.');
                    })
                    .finally(() => {
                        submitBtn.disabled = false;
                        submitBtn.textContent = originalBtnText;
                    });
            }
        });
    }

    // Search functionality
    const searchButton = document.querySelector('.btn-link');
    if (searchButton) {
        searchButton.addEventListener('click', function () {
            // Toggle search input or show search modal
            console.log('Search functionality');
        });
    }

    // Lazy loading for images
    const images = document.querySelectorAll('img');
    const imageObserver = new IntersectionObserver(function (entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                if (img.dataset.src) {
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            }
        });
    });

    images.forEach(img => {
        if (img.dataset.src) {
            imageObserver.observe(img);
        }
    });

    // Parallax effect for hero section
    const heroSection = document.querySelector('.hero-section');
    if (heroSection) {
        window.addEventListener('scroll', function () {
            const scrolled = window.pageYOffset;
            const rate = scrolled * -0.5;

            if (scrolled < heroSection.offsetHeight) {
                heroSection.style.transform = `translateY(${rate}px)`;
            }
        });
    }

    // Product hover effects
    const productCards = document.querySelectorAll('.product-card');
    productCards.forEach(card => {
        card.addEventListener('mouseenter', function () {
            this.style.transform = 'translateY(-10px)';
            this.style.boxShadow = '0 15px 35px rgba(0, 0, 0, 0.2)';
        });

        card.addEventListener('mouseleave', function () {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.1)';
        });
    });

    // Back to top button
    const backToTopButton = document.createElement('button');
    backToTopButton.innerHTML = '<i class="fas fa-arrow-up"></i>';
    backToTopButton.className = 'back-to-top';
    backToTopButton.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        width: 50px;
        height: 50px;
        background: var(--primary-color);
        color: white;
        border: none;
        border-radius: 50%;
        cursor: pointer;
        opacity: 0;
        visibility: hidden;
        transition: all 0.3s;
        z-index: 999;
    `;

    document.body.appendChild(backToTopButton);

    window.addEventListener('scroll', function () {
        if (window.pageYOffset > 300) {
            backToTopButton.style.opacity = '1';
            backToTopButton.style.visibility = 'visible';
        } else {
            backToTopButton.style.opacity = '0';
            backToTopButton.style.visibility = 'hidden';
        }
    });

    backToTopButton.addEventListener('click', function () {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });



    // Contact form validation (if exists)
    const contactForms = document.querySelectorAll('form');
    contactForms.forEach(form => {
        form.addEventListener('submit', function (e) {
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;

            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('error');
                } else {
                    field.classList.remove('error');
                }
            });

            if (!isValid) {
                e.preventDefault();
                alert('Vui lòng điền đầy đủ thông tin bắt buộc');
            }
        });
    });

    // Social sharing functionality
    const socialLinks = document.querySelectorAll('.social-links a');
    socialLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            const href = this.getAttribute('href');
            if (!href || href === '#' || href.includes('zalo.me')) return; // Allow normal links

            const url = encodeURIComponent(window.location.href);
            const title = encodeURIComponent(document.title);

            let shareUrl = '';

            if (href.includes('facebook') && !href.includes('sharer')) {
                e.preventDefault();
                shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${url}`;
            } else if (href.includes('twitter')) {
                e.preventDefault();
                shareUrl = `https://twitter.com/intent/tweet?url=${url}&text=${title}`;
            } else if (href.includes('linkedin')) {
                e.preventDefault();
                shareUrl = `https://www.linkedin.com/sharing/share-offsite/?url=${url}`;
            }

            if (shareUrl) {
                window.open(shareUrl, '_blank', 'width=600,height=400');
            }
        });
    });

    // Loading animation
    window.addEventListener('load', function () {
        document.body.classList.add('loaded');
    });

    console.log('HBMP Solar website initialized successfully');
});