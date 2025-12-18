package com.s.solar_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quote_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String fullName;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String productType;

    @Column
    private Double estimatedPower;

    @Column(length = 100)
    private String installationType;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(length = 50)
    private String status = "PENDING";

    @Column(length = 20)
    private String quoteType = "PRODUCT"; // PRODUCT or HOUSEHOLD

    @Column(length = 100)
    private String monthlyBill; // For household: electricity bill range

    @Column(length = 50)
    private String recommendedPackage; // Calculated package recommendation

    @Column(columnDefinition = "TEXT")
    private String adminNotes;

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
