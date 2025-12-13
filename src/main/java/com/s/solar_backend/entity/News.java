package com.s.solar_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "short_description", length = 1000)
    private String shortDescription;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "author", length = 100)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private NewsCategory category = NewsCategory.COMPANY_NEWS;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Column(name = "views")
    private Long views = 0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    public enum NewsCategory {
        COMPANY_NEWS("Tin công ty"),
        INDUSTRY_NEWS("Tin ngành"),
        TECHNOLOGY("Tin công nghệ"),
        PROJECT_NEWS("Tin dự án"),
        MARKET_NEWS("Tin thị trường");

        private final String displayName;

        NewsCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}