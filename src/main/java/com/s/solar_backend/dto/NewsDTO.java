package com.s.solar_backend.dto;

import com.s.solar_backend.entity.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Long id;
    private String title;
    private String shortDescription;
    private String content;
    private String imageUrl;
    private String author;
    private News.NewsCategory category;
    private String categoryDisplayName;
    private Boolean isFeatured;
    private Boolean isPublished;
    private Long views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    // Formatted dates for display
    private String createdDateFormatted;
    private String publishedDateFormatted;

    public NewsDTO(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.shortDescription = news.getShortDescription();
        this.content = news.getContent();
        this.imageUrl = news.getImageUrl();
        this.author = news.getAuthor();
        this.category = news.getCategory();
        this.categoryDisplayName = news.getCategory().getDisplayName();
        this.isFeatured = news.getIsFeatured();
        this.isPublished = news.getIsPublished();
        this.views = news.getViews();
        this.createdAt = news.getCreatedAt();
        this.updatedAt = news.getUpdatedAt();
        this.publishedAt = news.getPublishedAt();

        // Format dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (this.createdAt != null) {
            this.createdDateFormatted = this.createdAt.format(formatter);
        }
        if (this.publishedAt != null) {
            this.publishedDateFormatted = this.publishedAt.format(formatter);
        }
    }
}