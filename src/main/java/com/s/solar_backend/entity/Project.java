package com.s.solar_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String galleryImages; // Comma-separated URLs for multiple images

    @Column(columnDefinition = "LONGTEXT")
    private String detailContent; // HTML content from Summernote

    @Column
    private Double power; // Công suất (kWp)

    @Column(length = 200)
    private String location; // Vị trí/địa điểm

    @Column(length = 200)
    private String investor; // Chủ đầu tư

    @Column(length = 200)
    private String contractor; // Tổng thầu EPC

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ProjectApplication application;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ProjectSolution solution;

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

    // Enum for Application types
    public enum ProjectApplication {
        HO_GIA_DINH("Hộ gia đình"),
        DOANH_NGHIEP("Doanh nghiệp"),
        NONG_NGHIEP("Nông nghiệp"),
        CONG_TRINH_CONG("Công trình công cộng");

        private final String displayName;

        ProjectApplication(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Enum for Solution types
    public enum ProjectSolution {
        HOA_LUOI("Hệ hòa lưới"),
        LUU_TRU("Hệ lưu trữ"),
        HYBRID("Hybrid"),
        DOC_LAP("Độc lập");

        private final String displayName;

        ProjectSolution(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Helper methods
    public String getApplicationDisplayName() {
        return application != null ? application.getDisplayName() : "";
    }

    public String getSolutionDisplayName() {
        return solution != null ? solution.getDisplayName() : "";
    }
}
