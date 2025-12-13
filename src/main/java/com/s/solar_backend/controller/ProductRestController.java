package com.s.solar_backend.controller;

import com.s.solar_backend.dto.ProductDTO;
import com.s.solar_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {

        Page<ProductDTO> productsPage;

        if (search != null && !search.trim().isEmpty()) {
            productsPage = productService.searchActiveProducts(search, page, size);
        } else if (category != null && !category.trim().isEmpty()) {
            productsPage = productService.getActiveProductsByCategory(category, page, size);
        } else {
            productsPage = productService.getActiveProducts(page, size);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", productsPage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", productsPage.getTotalPages());
        response.put("totalItems", productsPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductDTO>> getFeaturedProducts() {
        List<ProductDTO> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> product = productService.getActiveProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
