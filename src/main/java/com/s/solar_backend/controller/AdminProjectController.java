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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController {

    private final ProjectService projectService;

    @GetMapping
    public String listProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Project> projectsPage = projectService.getAllProjects(page, size);

        model.addAttribute("projectsPage", projectsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", projectsPage.getTotalPages());

        return "admin/projects/project-list";
    }

    @GetMapping("/create")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("isEdit", false);
        model.addAttribute("applications", ProjectApplication.values());
        model.addAttribute("solutions", ProjectSolution.values());
        return "admin/projects/project-form";
    }

    @PostMapping("/create")
    public String createProject(
            @ModelAttribute Project project,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) MultipartFile[] galleryFiles,
            RedirectAttributes redirectAttributes) {

        try {
            Project savedProject = projectService.createProject(project, imageFile, galleryFiles);
            redirectAttributes.addFlashAttribute("success", "Dự án đã được tạo thành công!");
            return "redirect:/admin/projects/" + savedProject.getId() + "/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo dự án: " + e.getMessage());
            return "redirect:/admin/projects/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Project> projectOpt = projectService.getProjectById(id);

        if (projectOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy dự án!");
            return "redirect:/admin/projects";
        }

        model.addAttribute("project", projectOpt.get());
        model.addAttribute("isEdit", true);
        model.addAttribute("applications", ProjectApplication.values());
        model.addAttribute("solutions", ProjectSolution.values());
        return "admin/projects/project-form";
    }

    @PostMapping("/{id}/update")
    public String updateProject(
            @PathVariable Long id,
            @ModelAttribute Project project,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) MultipartFile[] galleryFiles,
            RedirectAttributes redirectAttributes) {

        try {
            projectService.updateProject(id, project, imageFile, galleryFiles);
            redirectAttributes.addFlashAttribute("success", "Dự án đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật dự án: " + e.getMessage());
        }

        return "redirect:/admin/projects/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.deleteProject(id);
            redirectAttributes.addFlashAttribute("success", "Dự án đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa dự án: " + e.getMessage());
        }

        return "redirect:/admin/projects";
    }

    @PostMapping("/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.toggleActiveStatus(id);
            redirectAttributes.addFlashAttribute("success", "Trạng thái dự án đã được cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/projects/" + id + "/edit";
    }

    @PostMapping("/{id}/toggle-featured")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.toggleFeaturedStatus(id);
            redirectAttributes.addFlashAttribute("success", "Dự án nổi bật đã được cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/projects/" + id + "/edit";
    }
}
