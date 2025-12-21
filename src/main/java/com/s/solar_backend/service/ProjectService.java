package com.s.solar_backend.service;

import com.s.solar_backend.entity.Project;
import com.s.solar_backend.entity.Project.ProjectApplication;
import com.s.solar_backend.entity.Project.ProjectSolution;
import com.s.solar_backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    // ==================== Public Methods ====================

    public Page<Project> getActiveProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectRepository.findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(pageable);
    }

    public Page<Project> filterProjects(ProjectApplication application, ProjectSolution solution, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (application != null && solution != null) {
            return projectRepository.findByApplicationAndSolutionAndIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(
                    application, solution, pageable);
        } else if (application != null) {
            return projectRepository.findByApplicationAndIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(
                    application, pageable);
        } else if (solution != null) {
            return projectRepository.findBySolutionAndIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(
                    solution, pageable);
        }

        return projectRepository.findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc(pageable);
    }

    public Page<Project> searchProjects(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectRepository.searchByKeyword(keyword, pageable);
    }

    public Optional<Project> getActiveProjectById(Long id) {
        return projectRepository.findById(id)
                .filter(Project::getIsActive);
    }

    public List<Project> getRelatedProjects(Project project) {
        // First try to get projects with the same solution
        List<Project> related = projectRepository.findTop4BySolutionAndIsActiveTrueAndIdNotOrderByCreatedAtDesc(
                project.getSolution(), project.getId());

        // If not enough, try to get projects with the same application
        if (related.size() < 4 && project.getApplication() != null) {
            List<Project> byApplication = projectRepository
                    .findTop4ByApplicationAndIsActiveTrueAndIdNotOrderByCreatedAtDesc(
                            project.getApplication(), project.getId());
            for (Project p : byApplication) {
                if (!related.contains(p) && related.size() < 4) {
                    related.add(p);
                }
            }
        }

        return related;
    }

    public List<Project> getFeaturedProjects() {
        return projectRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByDisplayOrderAsc();
    }

    // ==================== Admin Methods ====================

    public Page<Project> getAllProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project createProject(Project project, MultipartFile imageFile, MultipartFile[] galleryFiles)
            throws IOException {
        // Handle main image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            project.setImageUrl(imageUrl);
        }

        // Handle gallery images upload
        if (galleryFiles != null && galleryFiles.length > 0) {
            List<String> galleryUrls = new ArrayList<>();
            for (MultipartFile file : galleryFiles) {
                if (file != null && !file.isEmpty()) {
                    String url = saveImage(file);
                    galleryUrls.add(url);
                }
            }
            if (!galleryUrls.isEmpty()) {
                project.setGalleryImages(String.join(",", galleryUrls));
            }
        }

        // Set defaults
        if (project.getIsActive() == null) {
            project.setIsActive(true);
        }
        if (project.getIsFeatured() == null) {
            project.setIsFeatured(false);
        }
        if (project.getDisplayOrder() == null) {
            project.setDisplayOrder(0);
        }

        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long id, Project updatedProject, MultipartFile imageFile, MultipartFile[] galleryFiles)
            throws IOException {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // Update basic fields
        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setDetailContent(updatedProject.getDetailContent());
        project.setPower(updatedProject.getPower());
        project.setLocation(updatedProject.getLocation());
        project.setInvestor(updatedProject.getInvestor());
        project.setContractor(updatedProject.getContractor());
        project.setApplication(updatedProject.getApplication());
        project.setSolution(updatedProject.getSolution());
        project.setIsActive(updatedProject.getIsActive());
        project.setIsFeatured(updatedProject.getIsFeatured());
        project.setDisplayOrder(updatedProject.getDisplayOrder());

        // Handle main image upload (only if new image provided)
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            project.setImageUrl(imageUrl);
        }

        // Handle gallery images upload (append to existing or replace)
        if (galleryFiles != null && galleryFiles.length > 0) {
            List<String> newGalleryUrls = new ArrayList<>();
            for (MultipartFile file : galleryFiles) {
                if (file != null && !file.isEmpty()) {
                    String url = saveImage(file);
                    newGalleryUrls.add(url);
                }
            }
            if (!newGalleryUrls.isEmpty()) {
                // Append to existing gallery
                String existingGallery = project.getGalleryImages();
                if (existingGallery != null && !existingGallery.isEmpty()) {
                    project.setGalleryImages(existingGallery + "," + String.join(",", newGalleryUrls));
                } else {
                    project.setGalleryImages(String.join(",", newGalleryUrls));
                }
            }
        }

        // Handle gallery images from hidden field (for preserving existing images)
        if (updatedProject.getGalleryImages() != null) {
            project.setGalleryImages(updatedProject.getGalleryImages());
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public Project toggleActiveStatus(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        project.setIsActive(!project.getIsActive());
        return projectRepository.save(project);
    }

    @Transactional
    public Project toggleFeaturedStatus(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        project.setIsFeatured(!project.getIsFeatured());
        return projectRepository.save(project);
    }

    // ==================== Helper Methods ====================

    private String saveImage(MultipartFile imageFile) throws IOException {
        // Use the same path as ProductService for consistency
        String uploadsDir = "src/main/resources/static/photo/";

        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "project.jpg";
        }

        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        String fileName = UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get(uploadsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/photo/" + fileName;
    }
}
