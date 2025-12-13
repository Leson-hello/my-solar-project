package com.s.solar_backend.controller;

import com.s.solar_backend.dto.NewsDTO;
import com.s.solar_backend.service.NewsService;
import com.s.solar_backend.service.ProductService;
import com.s.solar_backend.service.QuoteRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {
    private final NewsService newsService;
    private final ProductService productService;
    private final QuoteRequestService quoteRequestService;



    /**
     * Upload image for content editor
     */
    @PostMapping("/upload-content-image")
    public ResponseEntity<Map<String, Object>> uploadContentImage(
            @RequestParam("image") MultipartFile imageFile) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (imageFile.isEmpty()) {
                response.put("success", false);
                response.put("message", "File không được để trống");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate file type
            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("success", false);
                response.put("message", "Chỉ chấp nhận file hình ảnh");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate file size (5MB max)
            if (imageFile.getSize() > 5 * 1024 * 1024) {
                response.put("success", false);
                response.put("message", "File không được vượt quá 5MB");
                return ResponseEntity.badRequest().body(response);
            }

            String imageUrl = saveImage(imageFile);

            response.put("success", true);
            response.put("url", imageUrl);
            response.put("message", "Upload thành công");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi upload: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Auto-save news draft
     */
    @PostMapping("/news/{id}/auto-save")
    public ResponseEntity<Map<String, Object>> autoSaveNews(
            @PathVariable Long id,
            @RequestBody NewsDTO newsDTO) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Check if news exists
            Optional<NewsDTO> existingNews = newsService.getNewsById(id);
            if (existingNews.isEmpty()) {
                response.put("success", false);
                response.put("message", "Tin tức không tồn tại");
                return ResponseEntity.notFound().build();
            }

            // Update only basic fields for auto-save
            NewsDTO updatedNews = newsService.updateNews(id, newsDTO);

            response.put("success", true);
            response.put("message", "Đã tự động lưu");
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("news", updatedNews);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lưu: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Create a draft for auto-save (for new news)
     */
    @PostMapping("/news/create-draft")
    public ResponseEntity<Map<String, Object>> createDraft(@RequestBody NewsDTO newsDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Ensure it's saved as a draft
            newsDTO.setIsPublished(false);
            newsDTO.setIsFeatured(false);

            NewsDTO createdNews = newsService.createNews(newsDTO);

            response.put("success", true);
            response.put("message", "Đã tạo bản nháp");
            response.put("news", createdNews);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo bản nháp: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get news for preview
     */
    @GetMapping("/news/{id}/preview")
    public ResponseEntity<NewsDTO> previewNews(@PathVariable Long id) {
        Optional<NewsDTO> news = newsService.getNewsById(id);

        if (news.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(news.get());
    }

    /**
     * Validate news data
     */
    @PostMapping("/news/validate")
    public ResponseEntity<Map<String, Object>> validateNews(@RequestBody NewsDTO newsDTO) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        // Validate title
        if (newsDTO.getTitle() == null || newsDTO.getTitle().trim().length() < 10) {
            errors.put("title", "Tiêu đề phải có ít nhất 10 ký tự");
        } else if (newsDTO.getTitle().length() > 500) {
            errors.put("title", "Tiêu đề không được vượt quá 500 ký tự");
        }

        // Validate short description
        if (newsDTO.getShortDescription() == null || newsDTO.getShortDescription().trim().length() < 20) {
            errors.put("shortDescription", "Mô tả ngắn phải có ít nhất 20 ký tự");
        } else if (newsDTO.getShortDescription().length() > 1000) {
            errors.put("shortDescription", "Mô tả ngắn không được vượt quá 1000 ký tự");
        }

        // Validate content
        if (newsDTO.getContent() == null || newsDTO.getContent().trim().isEmpty()) {
            errors.put("content", "Nội dung không được để trống");
        }

        // Validate category
        if (newsDTO.getCategory() == null) {
            errors.put("category", "Vui lòng chọn danh mục");
        }

        response.put("valid", errors.isEmpty());
        response.put("errors", errors);

        return ResponseEntity.ok(response);
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadsDir = "src/main/resources/static/photo/";
        String fileName = UUID.randomUUID().toString() + "_" +
                imageFile.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "");

        Path uploadPath = Paths.get(uploadsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath);

        return "/photo/" + fileName;
    }

    /**
     * Upload product image
     */
    @PostMapping("/upload-product-image")
    public ResponseEntity<Map<String, Object>> uploadProductImage(
            @RequestParam("image") MultipartFile imageFile) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (imageFile.isEmpty()) {
                response.put("success", false);
                response.put("message", "File không được để trống");
                return ResponseEntity.badRequest().body(response);
            }

            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("success", false);
                response.put("message", "Chỉ chấp nhận file hình ảnh");
                return ResponseEntity.badRequest().body(response);
            }

            if (imageFile.getSize() > 5 * 1024 * 1024) {
                response.put("success", false);
                response.put("message", "File không được vượt quá 5MB");
                return ResponseEntity.badRequest().body(response);
            }

            String imageUrl = saveImage(imageFile);

            response.put("success", true);
            response.put("url", imageUrl);
            response.put("message", "Upload thành công");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi upload: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get product statistics
     */
    @GetMapping("/products/stats")
    public ResponseEntity<Map<String, Object>> getProductStats() {
        Map<String, Object> response = new HashMap<>();

        long totalProducts = productService.getAllProducts(0, Integer.MAX_VALUE).getTotalElements();
        long activeProducts = productService.getActiveProducts(0, Integer.MAX_VALUE).getTotalElements();
        long featuredProducts = productService.getFeaturedProducts().size();

        response.put("total", totalProducts);
        response.put("active", activeProducts);
        response.put("featured", featuredProducts);
        response.put("inactive", totalProducts - activeProducts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get quote statistics
     */
    @GetMapping("/quotes/stats")
    public ResponseEntity<Map<String, Object>> getQuoteStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("pending", quoteRequestService.countByStatus("PENDING"));
        response.put("contacted", quoteRequestService.countByStatus("CONTACTED"));
        response.put("quoted", quoteRequestService.countByStatus("QUOTED"));
        response.put("completed", quoteRequestService.countByStatus("COMPLETED"));
        response.put("cancelled", quoteRequestService.countByStatus("CANCELLED"));
        return ResponseEntity.ok(response);
    }

    /**
     * Toggle product active status via API
     */
    @PutMapping("/products/{id}/toggle-active")
    public ResponseEntity<Map<String, Object>> toggleProductActive(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productService.toggleActiveStatus(id);
            response.put("success", true);
            response.put("message", "Trạng thái sản phẩm đã được cập nhật");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Toggle product featured status via API
     */
    @PutMapping("/products/{id}/toggle-featured")
    public ResponseEntity<Map<String, Object>> toggleProductFeatured(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productService.toggleFeaturedStatus(id);
            response.put("success", true);
            response.put("message", "Sản phẩm nổi bật đã được cập nhật");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}