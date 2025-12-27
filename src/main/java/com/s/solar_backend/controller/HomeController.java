package com.s.solar_backend.controller;

import com.s.solar_backend.dto.NewsDTO;
import com.s.solar_backend.dto.ProductDTO;
import com.s.solar_backend.service.NewsService;
import com.s.solar_backend.service.ProductService;
import com.s.solar_backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final NewsService newsService;
    private final ProductService productService;
    private final ProjectService projectService;

    @GetMapping
    public String home(Model model) {
        // Add model attributes for homepage statistics
        model.addAttribute("totalCapacity", "1+");
        model.addAttribute("totalCapacityUnit", "GWP");
        model.addAttribute("totalProjects", "12.000+");
        model.addAttribute("projectsUnit", "HỆ THỐNG");
        model.addAttribute("totalEmployees", "2.000+");
        model.addAttribute("employeesUnit", "ĐỐI TÁC NHÂN SỰ");

        // Add recent news for homepage
        List<NewsDTO> recentNews = newsService.getRecentNewsForHomepage();
        model.addAttribute("recentNews", recentNews);

        // Add featured products for homepage
        List<ProductDTO> featuredProducts = productService.getFeaturedProducts();
        model.addAttribute("featuredProducts", featuredProducts);

        // Add recent projects for homepage slider
        List<com.s.solar_backend.entity.Project> projectEntities = projectService.getActiveProjects(0, 20).getContent();
        List<java.util.Map<String, Object>> projects = projectEntities.stream().map(p -> {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("imageUrl", p.getImageUrl());
            map.put("application", p.getApplication() != null ? p.getApplication().name() : "DOANH_NGHIEP");
            map.put("power", p.getPower());
            map.put("createdAt", p.getCreatedAt() != null ? p.getCreatedAt().toString() : null);
            return map;
        }).collect(java.util.stream.Collectors.toList());

        model.addAttribute("projects", projects);

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/services")
    public String services(Model model) {
        return "services";
    }

    // Removed: @GetMapping("/projects") - now handled by ProjectController

    // Removed: @GetMapping("/contact") - now handled by ContactController

    @GetMapping("/test-images")
    public String testImages(Model model) {
        return "test-images";
    }
}