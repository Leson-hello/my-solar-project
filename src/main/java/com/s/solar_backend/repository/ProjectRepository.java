package com.s.solar_backend.repository;

import com.s.solar_backend.entity.Project;
import com.s.solar_backend.entity.Project.ProjectApplication;
import com.s.solar_backend.entity.Project.ProjectSolution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // For public pages - only active projects
    Page<Project> findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(Pageable pageable);

    // Filter by application only
    Page<Project> findByApplicationAndIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(
            ProjectApplication application, Pageable pageable);

    // Filter by solution only
    Page<Project> findBySolutionAndIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(
            ProjectSolution solution, Pageable pageable);

    // Filter by both application and solution
    Page<Project> findByApplicationAndSolutionAndIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(
            ProjectApplication application, ProjectSolution solution, Pageable pageable);

    // For admin - all projects
    Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Related projects - same solution, excluding current project
    List<Project> findTop4BySolutionAndIsActiveTrueAndIdNotOrderByCreatedAtDesc(
            ProjectSolution solution, Long excludeId);

    // Related projects - same application, excluding current project (fallback)
    List<Project> findTop4ByApplicationAndIsActiveTrueAndIdNotOrderByCreatedAtDesc(
            ProjectApplication application, Long excludeId);

    // Featured projects
    List<Project> findByIsFeaturedTrueAndIsActiveTrueOrderByDisplayOrderAsc();

    // Search by name or location
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Project> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
