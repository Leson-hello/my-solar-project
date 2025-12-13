package com.s.solar_backend.repository;

import com.s.solar_backend.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    // Find published news
    Page<News> findByIsPublishedTrueOrderByPublishedAtDesc(Pageable pageable);

    // Find by category
    Page<News> findByIsPublishedTrueAndCategoryOrderByPublishedAtDesc(
            News.NewsCategory category, Pageable pageable);

    // Find featured news
    List<News> findByIsPublishedTrueAndIsFeaturedTrueOrderByPublishedAtDesc();

    // Find recent news for homepage
    List<News> findTop6ByIsPublishedTrueOrderByPublishedAtDesc();

    // Search by title or content (derived query, ignore case)
    Page<News> findByIsPublishedTrueAndTitleContainingIgnoreCaseOrIsPublishedTrueAndContentContainingIgnoreCase(
            String titleKeyword, String contentKeyword, Pageable pageable);

    // Find published news with ID
    Optional<News> findByIdAndIsPublishedTrue(Long id);

    // Search published news
    @Query("SELECT n FROM News n WHERE " +
            "n.isPublished = true AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY n.publishedAt DESC")
    Page<News> searchPublishedNews(@Param("keyword") String keyword, Pageable pageable);

    // Increment views
    @Modifying
    @Query("UPDATE News n SET n.views = n.views + 1 WHERE n.id = :id")
    void incrementViews(@Param("id") Long id);

    // Admin: Find all (published and unpublished)
    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Admin: Find by category
    Page<News> findByCategoryOrderByCreatedAtDesc(News.NewsCategory category, Pageable pageable);

    // Admin: Search all news - Fixed query
    @Query("SELECT n FROM News n WHERE " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY n.createdAt DESC")
    Page<News> searchAllNews(@Param("keyword") String keyword, Pageable pageable);
}