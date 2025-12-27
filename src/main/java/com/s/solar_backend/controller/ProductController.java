package com.s.solar_backend.controller;

import com.s.solar_backend.dto.ProductDTO;
import com.s.solar_backend.service.ProductService;
import com.s.solar_backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProjectService projectService;

    @GetMapping("/products")
    public String productsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Model model) {

        Page<ProductDTO> productsPage;

        if (search != null && !search.trim().isEmpty()) {
            productsPage = productService.searchActiveProducts(search, page, size);
            model.addAttribute("search", search);
        } else if (category != null && !category.trim().isEmpty()) {
            productsPage = productService.getActiveProductsByCategory(category, page, size);
            model.addAttribute("category", category);
        } else {
            productsPage = productService.getActiveProducts(page, size);
        }

        List<ProductDTO> featuredProducts = productService.getFeaturedProducts();

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());
        model.addAttribute("featuredProducts", featuredProducts);

        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetailPage(@PathVariable Long id, Model model) {
        Optional<ProductDTO> product = productService.getActiveProductById(id);

        if (product.isEmpty()) {
            return "redirect:/products";
        }

        String category = product.get().getCategory();
        List<ProductDTO> relatedProducts = new ArrayList<>();
        if (category != null && !category.trim().isEmpty()) {
            relatedProducts.addAll(productService.getActiveProductsByCategory(category, 0, 7).getContent());
        }

        relatedProducts.removeIf(p -> p.getId().equals(id));

        // Fallback 1: Featured Products
        if (relatedProducts.size() < 6) {
            List<ProductDTO> featured = productService.getFeaturedProducts();
            for (ProductDTO f : featured) {
                if (relatedProducts.size() >= 6)
                    break;
                if (!f.getId().equals(id) && relatedProducts.stream().noneMatch(p -> p.getId().equals(f.getId()))) {
                    relatedProducts.add(f);
                }
            }
        }

        // Fallback 2: Any Active Products (if still not enough)
        if (relatedProducts.size() < 6) {
            List<ProductDTO> allActive = productService.getActiveProducts(0, 10).getContent();
            for (ProductDTO a : allActive) {
                if (relatedProducts.size() >= 6)
                    break;
                if (!a.getId().equals(id) && relatedProducts.stream().noneMatch(p -> p.getId().equals(a.getId()))) {
                    relatedProducts.add(a);
                }
            }
        }

        if (relatedProducts.size() > 6) {
            relatedProducts = relatedProducts.subList(0, 6);
        }

        // Fetch recent projects
        List<?> recentProjects = projectService.getActiveProjects(0, 6).getContent();

        model.addAttribute("product", product.get());
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("recentProjects", recentProjects);

        return "product-detail";
    }

    @GetMapping("/quote-request")
    public String quoteRequestPage(
            @RequestParam(required = false) Long productId,
            Model model) {

        if (productId != null) {
            Optional<ProductDTO> product = productService.getActiveProductById(productId);
            product.ifPresent(p -> model.addAttribute("selectedProduct", p));
        }

        List<ProductDTO> products = productService.getActiveProducts(0, 100).getContent();
        model.addAttribute("products", products);

        return "quote-request";
    }
}
