package com.s.solar_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnterpriseController {

    @GetMapping("/doanh-nghiep")
    public String enterprise(Model model) {
        // Add model attributes for statistics
        model.addAttribute("yearsExperience", "18+");
        model.addAttribute("totalStaff", "300+");
        model.addAttribute("totalCapacity", "800");
        model.addAttribute("totalSystems", "10.000+");

        return "enterprise";
    }
}
