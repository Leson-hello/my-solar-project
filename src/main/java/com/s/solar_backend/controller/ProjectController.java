package com.s.solar_backend.controller;

import com.s.solar_backend.entity.Project;
import com.s.solar_backend.entity.Project.ProjectApplication;
import com.s.solar_backend.entity.Project.ProjectSolution;
import com.s.solar_backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public String listProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String application,
            @RequestParam(required = false) String solution,
            @RequestParam(required = false) String keyword,
            Model model) {

        Page<Project> projectsPage;

        // Parse enums from string parameters
        ProjectApplication appEnum = null;
        ProjectSolution solEnum = null;

        if (application != null && !application.isEmpty()) {
            try {
                appEnum = ProjectApplication.valueOf(application);
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (solution != null && !solution.isEmpty()) {
            try {
                solEnum = ProjectSolution.valueOf(solution);
            } catch (IllegalArgumentException ignored) {
            }
        }

        // Search or filter
        if (keyword != null && !keyword.trim().isEmpty()) {
            projectsPage = projectService.searchProjects(keyword, page, size);
            model.addAttribute("keyword", keyword);
        } else {
            projectsPage = projectService.filterProjects(appEnum, solEnum, page, size);
        }

        model.addAttribute("projectsPage", projectsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", projectsPage.getTotalPages());
        model.addAttribute("selectedApplication", application);
        model.addAttribute("selectedSolution", solution);

        // Add enum values for filter tabs
        model.addAttribute("applications", ProjectApplication.values());
        model.addAttribute("solutions", ProjectSolution.values());

        // Featured projects for sidebar
        List<Project> featuredProjects = projectService.getFeaturedProjects();
        model.addAttribute("featuredProjects", featuredProjects);

        return "projects";
    }

    @GetMapping("/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        Optional<Project> projectOpt = projectService.getActiveProjectById(id);

        if (projectOpt.isEmpty()) {
            return "redirect:/projects";
        }

        Project project = projectOpt.get();
        model.addAttribute("project", project);

        // Get related projects
        List<Project> relatedProjects = projectService.getRelatedProjects(project);
        model.addAttribute("relatedProjects", relatedProjects);

        // Add enum values for display
        model.addAttribute("applications", ProjectApplication.values());
        model.addAttribute("solutions", ProjectSolution.values());

        return "project-detail";
    }
}
