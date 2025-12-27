package com.s.solar_backend.repository;

import com.s.solar_backend.entity.CategoryContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryContentRepository extends JpaRepository<CategoryContent, Long> {
    Optional<CategoryContent> findByCategoryCode(String categoryCode);
}
