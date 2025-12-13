package com.s.solar_backend.controller;

import com.s.solar_backend.dto.ProductDTO;
import com.s.solar_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

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
        List<ProductDTO> relatedProducts = category != null ?
                productService.getActiveProductsByCategory(category, 0, 4).getContent() :
                productService.getFeaturedProducts();

        relatedProducts.removeIf(p -> p.getId().equals(id));

        model.addAttribute("product", product.get());
        model.addAttribute("relatedProducts", relatedProducts);

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
