package com.s.solar_backend.controller;

import com.s.solar_backend.dto.ProductDTO;
import com.s.solar_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Model model) {

        Page<ProductDTO> productsPage;

        if (search != null && !search.trim().isEmpty()) {
            productsPage = productService.searchActiveProducts(search, page, size);
            model.addAttribute("search", search);
        } else if (category != null && !category.trim().isEmpty()) {
            productsPage = productService.getProductsByCategory(category, page, size);
            model.addAttribute("selectedCategory", category);
        } else {
            productsPage = productService.getAllProducts(page, size);
        }

        model.addAttribute("productsPage", productsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());

        return "admin/products/product-list";
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("isEdit", false);
        return "admin/products/product-form";
    }

    @PostMapping("/create")
    public String createProduct(
            @ModelAttribute ProductDTO productDTO,
            @RequestParam(required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = saveImage(imageFile);
                productDTO.setImageUrl(imageUrl);
            }

            ProductDTO savedProduct = productService.createProduct(productDTO);

            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được tạo thành công!");
            return "redirect:/admin/products/" + savedProduct.getId() + "/edit";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo sản phẩm: " + e.getMessage());
            return "redirect:/admin/products/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProductDTO> productOpt = productService.getProductById(id);

        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/products";
        }

        model.addAttribute("product", productOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/products/product-form";
    }

    @PostMapping("/{id}/update")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute ProductDTO productDTO,
            @RequestParam(required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = saveImage(imageFile);
                productDTO.setImageUrl(imageUrl);
            }

            productService.updateProduct(id, productDTO);

            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được cập nhật thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }

        return "redirect:/admin/products/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.toggleActiveStatus(id);
            redirectAttributes.addFlashAttribute("success", "Trạng thái sản phẩm đã được cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/products/" + id + "/edit";
    }

    @PostMapping("/{id}/toggle-featured")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.toggleFeaturedStatus(id);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm nổi bật đã được cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/products/" + id + "/edit";
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadsDir = "src/main/resources/static/photo/";

        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "product.jpg";
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
