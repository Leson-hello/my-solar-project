package com.s.solar_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NongNghiepController {

    private final com.s.solar_backend.service.ProjectService projectService;

    @GetMapping("/nong-nghiep")
    public String nongNghiepPage(Model model) {
        // Fetch Agriculture Projects
        List<com.s.solar_backend.entity.Project> projectEntities = projectService.filterProjects(
                com.s.solar_backend.entity.Project.ProjectApplication.NONG_NGHIEP,
                null,
                0, 20).getContent();

        List<java.util.Map<String, Object>> projects = projectEntities.stream().map(p -> {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("imageUrl", p.getImageUrl());
            map.put("application", p.getApplication() != null ? p.getApplication().name() : "NONG_NGHIEP");
            map.put("power", p.getPower());
            map.put("createdAt", p.getCreatedAt() != null ? p.getCreatedAt().toString() : null);
            return map;
        }).collect(java.util.stream.Collectors.toList());

        model.addAttribute("projects", projects);

        return "nong-nghiep";
    }
}
