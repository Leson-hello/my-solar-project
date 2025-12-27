package com.s.solar_backend.service;

import com.s.solar_backend.dto.NewsDTO;
import com.s.solar_backend.entity.News;
import com.s.solar_backend.repository.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    // Public methods for frontend

    @Cacheable(value = "news", key = "'published_' + #page + '_' + #size")
    public Page<NewsDTO> getPublishedNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByIsPublishedTrueOrderByPublishedAtDesc(pageable)
                .map(NewsDTO::new);
    }

    public Page<NewsDTO> getPublishedNewsByCategory(News.NewsCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByIsPublishedTrueAndCategoryOrderByPublishedAtDesc(category, pageable)
                .map(NewsDTO::new);
    }

    @Cacheable(value = "news", key = "'featured'")
    public List<NewsDTO> getFeaturedNews() {
        return newsRepository.findByIsPublishedTrueAndIsFeaturedTrueOrderByPublishedAtDesc()
                .stream()
                .map(NewsDTO::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "news", key = "'homepage_recent'")
    public List<NewsDTO> getRecentNewsForHomepage() {
        return newsRepository.findTop6ByIsPublishedTrueOrderByPublishedAtDesc()
                .stream()
                .map(NewsDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<NewsDTO> getPublishedNewsById(Long id) {
        return newsRepository.findByIdAndIsPublishedTrue(id)
                .map(NewsDTO::new);
    }

    @Transactional
    public void incrementViews(Long id) {
        newsRepository.incrementViews(id);
    }

    public Page<NewsDTO> searchPublishedNews(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.searchPublishedNews(keyword, pageable)
                .map(NewsDTO::new);
    }

    // Admin methods

    public Page<NewsDTO> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(NewsDTO::new);
    }

    public Page<NewsDTO> getNewsByCategory(News.NewsCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByCategoryOrderByCreatedAtDesc(category, pageable)
                .map(NewsDTO::new);
    }

    public Page<NewsDTO> searchAllNews(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.searchAllNews(keyword, pageable)
                .map(NewsDTO::new);
    }

    public Optional<NewsDTO> getNewsById(Long id) {
        return newsRepository.findById(id).map(NewsDTO::new);
    }

    @Transactional
    @CacheEvict(value = "news", allEntries = true)
    public NewsDTO createNews(NewsDTO newsDTO) {
        News news = new News();
        updateNewsFromDTO(news, newsDTO);
        news.setCreatedAt(LocalDateTime.now());

        News savedNews = newsRepository.save(news);
        return new NewsDTO(savedNews);
    }

    @Transactional
    @CacheEvict(value = "news", allEntries = true)
    public NewsDTO updateNews(Long id, NewsDTO newsDTO) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        updateNewsFromDTO(news, newsDTO);
        news.setUpdatedAt(LocalDateTime.now());

        News savedNews = newsRepository.save(news);
        return new NewsDTO(savedNews);
    }

    @Transactional
    @CacheEvict(value = "news", allEntries = true)
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new RuntimeException("News not found with id: " + id);
        }
        newsRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "news", allEntries = true)
    public NewsDTO publishNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        news.setIsPublished(true);
        news.setPublishedAt(LocalDateTime.now());

        News savedNews = newsRepository.save(news);
        return new NewsDTO(savedNews);
    }

    @Transactional
    @CacheEvict(value = "news", allEntries = true)
    public NewsDTO unpublishNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        news.setIsPublished(false);
        news.setPublishedAt(null);

        News savedNews = newsRepository.save(news);
        return new NewsDTO(savedNews);
    }

    private void updateNewsFromDTO(News news, NewsDTO newsDTO) {
        news.setTitle(newsDTO.getTitle());
        news.setShortDescription(newsDTO.getShortDescription());
        news.setContent(newsDTO.getContent());
        news.setImageUrl(newsDTO.getImageUrl());
        news.setAuthor(newsDTO.getAuthor());
        news.setCategory(newsDTO.getCategory());
        news.setIsFeatured(newsDTO.getIsFeatured());

        // Handle publishing
        if (newsDTO.getIsPublished() && !news.getIsPublished()) {
            news.setIsPublished(true);
            news.setPublishedAt(LocalDateTime.now());
        } else if (!newsDTO.getIsPublished() && news.getIsPublished()) {
            news.setIsPublished(false);
            news.setPublishedAt(null);
        }
    }
}