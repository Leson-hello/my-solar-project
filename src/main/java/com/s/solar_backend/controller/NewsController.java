package com.s.solar_backend.controller;

import com.s.solar_backend.dto.NewsDTO;
import com.s.solar_backend.entity.News;
import com.s.solar_backend.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public String newsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Model model) {

        Page<NewsDTO> newsPage;

        if (search != null && !search.trim().isEmpty()) {
            // Search functionality
            newsPage = newsService.searchPublishedNews(search.trim(), page, size);
            model.addAttribute("search", search);
            model.addAttribute("pageTitle", "Kết quả tìm kiếm: " + search);
        } else if (category != null && !category.trim().isEmpty()) {
            // Filter by category
            try {
                News.NewsCategory newsCategory = News.NewsCategory.valueOf(category);
                newsPage = newsService.getPublishedNewsByCategory(newsCategory, page, size);
                model.addAttribute("selectedCategory", category);
                model.addAttribute("pageTitle", newsCategory.getDisplayName());
            } catch (IllegalArgumentException e) {
                // Invalid category, show all news
                newsPage = newsService.getPublishedNews(page, size);
                model.addAttribute("pageTitle", "Tất cả tin tức");
            }
        } else {
            // Show all news
            newsPage = newsService.getPublishedNews(page, size);
            model.addAttribute("pageTitle", "Tất cả tin tức");
        }

        model.addAttribute("newsPage", newsPage);
        model.addAttribute("categories", News.NewsCategory.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", newsPage.getTotalPages());
        model.addAttribute("hasNext", newsPage.hasNext());
        model.addAttribute("hasPrevious", newsPage.hasPrevious());

        return "news/news-list";
    }

    @GetMapping("/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        Optional<NewsDTO> newsOpt = newsService.getPublishedNewsById(id);

        if (newsOpt.isEmpty()) {
            return "redirect:/news";
        }

        NewsDTO news = newsOpt.get();

        // Increment view count
        newsService.incrementViews(id);

        // Get related news (same category, exclude current news)
        Page<NewsDTO> relatedNewsPage = newsService.getPublishedNewsByCategory(news.getCategory(), 0, 4);

        model.addAttribute("news", news);
        model.addAttribute("relatedNews", relatedNewsPage.getContent());
        model.addAttribute("categories", News.NewsCategory.values());

        return "news/news-detail";
    }
}