package com.s.solar_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double power;
    private String powerUnit;
    private String type;
    private String imageUrl;
    private String galleryImages;
    private Double price;
    private String specifications;
    private String detailContent;
    private Integer warranty;
    private String category;
    private String documentUrl;
    private String documents;
    private boolean active;
    private boolean featured;
    private Integer displayOrder;
}