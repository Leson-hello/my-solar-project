
const I18n = {
    locale: localStorage.getItem('hbmp_lang') || 'vi',
    translations: {},

    init: async function () {
        console.log('I18n: Initializing...');
        // Load translations for current locale
        await this.loadTranslations(this.locale);
        this.updateUI();
        this.bindEvents();
        console.log('I18n: Initialization complete. Current locale:', this.locale);
    },

    loadTranslations: async function (lang) {
        try {
            console.log(`I18n: Loading translations for ${lang}...`);
            const response = await fetch(`/i18n/${lang}.json`);
            if (response.ok) {
                this.translations = await response.json();
                console.log('I18n: Translations loaded successfully');
            } else {
                console.error(`I18n: Failed to load translations for ${lang}. Status: ${response.status}`);
            }
        } catch (e) {
            console.error('I18n: Error loading translations:', e);
        }
    },

    setLocale: async function (lang) {
        if (this.locale === lang) return;

        console.log(`I18n: Switching locale to ${lang}`);
        this.locale = lang;
        localStorage.setItem('hbmp_lang', lang);

        await this.loadTranslations(lang);
        this.updateUI();

        // Update active class on flags
        document.querySelectorAll('.language-selector a').forEach(link => {
            if (link.dataset.lang === lang) {
                link.classList.add('active-lang');
            } else {
                link.classList.remove('active-lang');
            }
        });
    },

    updateUI: function () {
        document.querySelectorAll('[data-i18n]').forEach(el => {
            const key = el.getAttribute('data-i18n');
            const value = this.getNestedValue(this.translations, key);
            if (value) {
                if (el.tagName === 'INPUT' && el.getAttribute('placeholder')) {
                    el.placeholder = value;
                } else {
                    el.innerHTML = value;
                }
            }
        });

        // Update html lang attribute
        document.documentElement.lang = this.locale;

        // Update flags initial state
        document.querySelectorAll('.language-selector a').forEach(link => {
            if (link.dataset.lang === this.locale) {
                link.classList.add('active-lang');
            } else {
                link.classList.remove('active-lang');
            }
        });
    },

    getNestedValue: function (obj, path) {
        return path.split('.').reduce((prev, curr) => {
            return prev ? prev[curr] : null;
        }, obj);
    },

    bindEvents: function () {
        const langLinks = document.querySelectorAll('.language-selector a');
        langLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                // Get lang from data attribute, handling clicks on children (img)
                const targetLink = e.target.closest('a');
                if (targetLink && targetLink.dataset.lang) {
                    const lang = targetLink.dataset.lang;
                    this.setLocale(lang);
                }
            });
        });
    }
};

document.addEventListener('DOMContentLoaded', () => {
    I18n.init();
});
