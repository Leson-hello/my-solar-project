package com.s.solar_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequestDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String productType;
    private Double estimatedPower;
    private String installationType;
    private String message;
    private String status;
    private String adminNotes;
    private String quoteType;
    private String monthlyBill;
    private String recommendedPackage;
    private String solution;
    private String company;
    private String province;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
