package com.s.solar_backend.controller;

import com.s.solar_backend.dto.NewsDTO;
import com.s.solar_backend.dto.ProductDTO;
import com.s.solar_backend.service.NewsService;
import com.s.solar_backend.service.ProductService;
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

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }

    @GetMapping("/test-images")
    public String testImages(Model model) {
        return "test-images";
    }
}