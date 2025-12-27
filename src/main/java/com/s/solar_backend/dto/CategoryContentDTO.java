package com.s.solar_backend.dto;

import com.s.solar_backend.entity.CategoryContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryContentDTO {
    private Long id;
    private String categoryCode;
    private String categoryName;
    private String topContent;
    private String bottomContent;
    private LocalDateTime updatedAt;

    public CategoryContentDTO(CategoryContent entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.categoryCode = entity.getCategoryCode();
            this.categoryName = entity.getCategoryName();
            this.topContent = entity.getTopContent();
            this.bottomContent = entity.getBottomContent();
            this.updatedAt = entity.getUpdatedAt();
        }
    }
}
