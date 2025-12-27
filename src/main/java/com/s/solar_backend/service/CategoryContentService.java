package com.s.solar_backend.service;

import com.s.solar_backend.dto.CategoryContentDTO;
import com.s.solar_backend.entity.CategoryContent;
import com.s.solar_backend.repository.CategoryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryContentService {

    private final CategoryContentRepository repository;

    public List<CategoryContentDTO> getAllCategoryContents() {
        return repository.findAll().stream()
                .map(CategoryContentDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<CategoryContentDTO> getByCategoryCode(String code) {
        return repository.findByCategoryCode(code).map(CategoryContentDTO::new);
    }

    public Optional<CategoryContentDTO> getById(Long id) {
        if (id == null)
            return Optional.empty();
        return repository.findById(id).map(CategoryContentDTO::new);
    }

    @Transactional
    public CategoryContentDTO saveOrUpdate(CategoryContentDTO dto) {
        CategoryContent entity = repository.findByCategoryCode(dto.getCategoryCode())
                .orElse(new CategoryContent());

        entity.setCategoryCode(dto.getCategoryCode());
        entity.setCategoryName(dto.getCategoryName());
        entity.setTopContent(dto.getTopContent());
        entity.setBottomContent(dto.getBottomContent());
        entity.setUpdatedAt(LocalDateTime.now());

        CategoryContent saved = repository.save(entity);
        return new CategoryContentDTO(saved);
    }
}
