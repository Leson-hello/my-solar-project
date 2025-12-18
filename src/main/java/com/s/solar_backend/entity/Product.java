package com.s.solar_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(length = 200)
    private String brand;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Double power;

    @Column(length = 50)
    private String powerUnit;

    @Column(length = 100)
    private String type;

    @Column(length = 500)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String galleryImages; // Comma-separated URLs for multiple images

    @Column
    private Double price;

    @Column(columnDefinition = "TEXT")
    private String specifications;

    @Column(columnDefinition = "LONGTEXT")
    private String detailContent;

    @Column
    private Integer warranty;

    @Column(length = 100)
    private String category;

    @Column(length = 500)
    private String documentUrl;

    @Column(columnDefinition = "TEXT")
    private String documents; // JSON format: [{"name":"...","url":"..."}]

    @Column
    private Boolean isActive = true;

    @Column
    private Boolean isFeatured = false;

    @Column
    private Integer displayOrder = 0;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
