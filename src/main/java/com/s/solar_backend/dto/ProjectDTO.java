package com.s.solar_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private String location;
    private Double capacity;
    private String capacityUnit;
    private String status;
    private String imageUrl;
    private String completionDate;
    private String projectType;
}