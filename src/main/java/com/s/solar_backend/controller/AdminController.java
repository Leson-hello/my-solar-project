package com.s.solar_backend.controller;

import com.s.solar_backend.dto.NewsDTO;
import com.s.solar_backend.entity.News;
import com.s.solar_backend.service.NewsService;
import com.s.solar_backend.repository.NewsletterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final NewsService newsService;
    private final NewsletterRepository newsletterRepository;

    // Dashboard
    @GetMapping
    public String adminDashboard(Model model) {
        // Get recent news for dashboard
        Page<NewsDTO> recentNews = newsService.getAllNews(0, 10);
        model.addAttribute("recentNews", recentNews.getContent());
        model.addAttribute("totalNews", recentNews.getTotalElements());

        return "admin/dashboard";
    }

    // News Management
    @GetMapping("/news")
    public String adminNewsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Model model) {

        Page<NewsDTO> newsPage;

        if (search != null && !search.trim().isEmpty()) {
            newsPage = newsService.searchAllNews(search.trim(), page, size);
            model.addAttribute("search", search);
        } else if (category != null && !category.trim().isEmpty()) {
            try {
                News.NewsCategory newsCategory = News.NewsCategory.valueOf(category);
                newsPage = newsService.getNewsByCategory(newsCategory, page, size);
                model.addAttribute("selectedCategory", category);
            } catch (IllegalArgumentException e) {
                newsPage = newsService.getAllNews(page, size);
            }
        } else {
            newsPage = newsService.getAllNews(page, size);
        }

        model.addAttribute("newsPage", newsPage);
        model.addAttribute("categories", News.NewsCategory.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", newsPage.getTotalPages());

        return "admin/news/news-list";
    }

    // Create News Form
    @GetMapping("/news/create")
    public String createNewsForm(Model model) {
        model.addAttribute("news", new NewsDTO());
        model.addAttribute("categories", News.NewsCategory.values());
        model.addAttribute("isEdit", false);

        return "admin/news/news-form";
    }

    // Create News
    @PostMapping("/news/create")
    public String createNews(
            @ModelAttribute NewsDTO newsDTO,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(value = "action", required = false, defaultValue = "save") String action,
            RedirectAttributes redirectAttributes) {

        try {
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = saveImage(imageFile);
                newsDTO.setImageUrl(imageUrl);
            }

            // Set default values
            if (newsDTO.getAuthor() == null || newsDTO.getAuthor().trim().isEmpty()) {
                newsDTO.setAuthor("Admin HBMP");
            }

            // Handle different actions
            switch (action) {
                case "save_and_publish":
                    newsDTO.setIsPublished(true);
                    break;
                case "save_draft":
                    newsDTO.setIsPublished(false);
                    break;
                case "save":
                default:
                    // Keep the current isPublished status from form
                    break;
            }

            NewsDTO createdNews = newsService.createNews(newsDTO);

            String message = getSuccessMessage(action, "tạo", createdNews.getIsPublished());
            redirectAttributes.addFlashAttribute("success", message);

            return "redirect:/admin/news/" + createdNews.getId() + "/edit";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo tin tức: " + e.getMessage());
            return "redirect:/admin/news/create";
        }
    }

    // Edit News Form
    @GetMapping("/news/{id}/edit")
    public String editNewsForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<NewsDTO> newsOpt = newsService.getNewsById(id);

        if (newsOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tin tức!");
            return "redirect:/admin/news";
        }

        model.addAttribute("news", newsOpt.get());
        model.addAttribute("categories", News.NewsCategory.values());
        model.addAttribute("isEdit", true);

        return "admin/news/news-form";
    }

    // Update News
    @PostMapping("/news/{id}/update")
    public String updateNews(
            @PathVariable Long id,
            @ModelAttribute NewsDTO newsDTO,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(value = "action", required = false, defaultValue = "save") String action,
            RedirectAttributes redirectAttributes) {

        try {
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = saveImage(imageFile);
                newsDTO.setImageUrl(imageUrl);
            }

            // Handle different actions
            switch (action) {
                case "save_and_publish":
                    newsDTO.setIsPublished(true);
                    break;
                case "save_draft":
                    newsDTO.setIsPublished(false);
                    break;
                case "save":
                default:
                    // Keep the current isPublished status from form
                    break;
            }

            NewsDTO updatedNews = newsService.updateNews(id, newsDTO);

            String message = getSuccessMessage(action, "cập nhật", updatedNews.getIsPublished());
            redirectAttributes.addFlashAttribute("success", message);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật tin tức: " + e.getMessage());
        }

        return "redirect:/admin/news/" + id + "/edit";
    }

    // Delete News
    @PostMapping("/news/{id}/delete")
    public String deleteNews(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            newsService.deleteNews(id);
            redirectAttributes.addFlashAttribute("success", "Tin tức đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa tin tức: " + e.getMessage());
        }

        return "redirect:/admin/news";
    }

    // Publish News
    @PostMapping("/news/{id}/publish")
    public String publishNews(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            newsService.publishNews(id);
            redirectAttributes.addFlashAttribute("success", "Tin tức đã được phát hành thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi phát hành tin tức: " + e.getMessage());
        }

        return "redirect:/admin/news/" + id + "/edit";
    }

    // Unpublish News
    @PostMapping("/news/{id}/unpublish")
    public String unpublishNews(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            newsService.unpublishNews(id);
            redirectAttributes.addFlashAttribute("success", "Tin tức đã được gỡ xuống thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi gỡ tin tức: " + e.getMessage());
        }

        return "redirect:/admin/news/" + id + "/edit";
    }

    // Newsletter Management
    @GetMapping("/newsletter")
    public String adminNewsletterPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        Page<?> newsletterPage = newsletterRepository.findAll(PageRequest.of(page, size));

        model.addAttribute("newsletterPage", newsletterPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", newsletterPage.getTotalPages());

        return "admin/newsletter/list";
    }

    @PostMapping("/newsletter/{id}/delete")
    public String deleteNewsletter(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "ID không hợp lệ!");
            return "redirect:/admin/newsletter";
        }
        try {
            newsletterRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Email đã được xóa khỏi danh sách nhận tin!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa email: " + e.getMessage());
        }

        return "redirect:/admin/newsletter";
    }

    // Helper method to get success message based on action
    private String getSuccessMessage(String action, String operation, Boolean isPublished) {
        switch (action) {
            case "save_and_publish":
                return "Tin tức đã được " + operation + " và phát hành thành công!";
            case "save_draft":
                return "Tin tức đã được " + operation + " và lưu dưới dạng bản nháp!";
            case "save":
            default:
                if (isPublished) {
                    return "Tin tức đã được " + operation + " và phát hành thành công!";
                } else {
                    return "Tin tức đã được " + operation + " dưới dạng bản nháp!";
                }
        }
    }

    // Helper method to save uploaded images
    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadsDir = "src/main/resources/static/photo/";

        // Create safe filename
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "image.jpg";
        }

        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        String fileName = UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get(uploadsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath);

        return "/photo/" + fileName;
    }
}