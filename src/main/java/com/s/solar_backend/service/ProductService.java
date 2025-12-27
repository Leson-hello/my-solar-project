package com.s.solar_backend.service;

import com.s.solar_backend.dto.ProductDTO;
import com.s.solar_backend.entity.Product;
import com.s.solar_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductDTO> getActiveProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(pageable)
                .map(this::convertToDTO);
    }

    public Page<ProductDTO> getActiveProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByIsActiveTrueAndCategoryOrderByDisplayOrderAscCreatedAtDesc(
                category, pageable)
                .map(this::convertToDTO);
    }

    public List<ProductDTO> getFeaturedProducts() {
        return productRepository.findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getActiveProductById(Long id) {
        return productRepository.findById(id)
                .filter(Product::getIsActive)
                .map(this::convertToDTO);
    }

    public Page<ProductDTO> searchActiveProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.searchActiveProducts(keyword, pageable)
                .map(this::convertToDTO);
    }

    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToDTO);
    }

    public Page<ProductDTO> getProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryOrderByCreatedAtDesc(category, pageable)
                .map(this::convertToDTO);
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        updateProductFromDTO(product, productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        updateProductFromDTO(product, productDTO);
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductDTO toggleActiveStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setIsActive(!product.getIsActive());
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public ProductDTO toggleFeaturedStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setIsFeatured(!product.getIsFeatured());
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    private void updateProductFromDTO(Product product, ProductDTO dto) {
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setDescription(dto.getDescription());
        product.setPower(dto.getPower());
        product.setPowerUnit(dto.getPowerUnit());
        product.setType(dto.getType());
        // Only update imageUrl if new one provided (preserve existing)
        if (dto.getImageUrl() != null) {
            product.setImageUrl(dto.getImageUrl());
        }
        // Only update galleryImages if new ones provided (preserve existing)
        if (dto.getGalleryImages() != null) {
            product.setGalleryImages(dto.getGalleryImages());
        }
        product.setPrice(dto.getPrice());
        product.setSpecifications(dto.getSpecifications());
        product.setDetailContent(dto.getDetailContent());
        product.setWarranty(dto.getWarranty());
        product.setCategory(dto.getCategory());
        product.setDocumentUrl(dto.getDocumentUrl());
        // Only update documents if new ones provided (preserve existing)
        if (dto.getDocuments() != null) {
            product.setDocuments(dto.getDocuments());
        }
        if (dto.getCertificates() != null) {
            product.setCertificates(dto.getCertificates());
        }
        if (dto.getPolicies() != null) {
            product.setPolicies(dto.getPolicies());
        }
        product.setIsActive(dto.isActive());
        product.setIsFeatured(dto.isFeatured());
        product.setDisplayOrder(dto.getDisplayOrder());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setDescription(product.getDescription());
        dto.setPower(product.getPower());
        dto.setPowerUnit(product.getPowerUnit());
        dto.setType(product.getType());
        dto.setImageUrl(product.getImageUrl());
        dto.setGalleryImages(product.getGalleryImages());
        dto.setPrice(product.getPrice());
        dto.setSpecifications(product.getSpecifications());
        dto.setDetailContent(product.getDetailContent());
        dto.setWarranty(product.getWarranty());
        dto.setCategory(product.getCategory());
        dto.setDocumentUrl(product.getDocumentUrl());
        dto.setDocuments(product.getDocuments());
        dto.setCertificates(product.getCertificates());
        dto.setPolicies(product.getPolicies());
        dto.setActive(product.getIsActive() != null ? product.getIsActive() : true);
        dto.setFeatured(product.getIsFeatured() != null ? product.getIsFeatured() : false);
        dto.setDisplayOrder(product.getDisplayOrder());
        return dto;
    }
}
