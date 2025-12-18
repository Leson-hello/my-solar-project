package com.s.solar_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HouseholdController {

    @GetMapping("/ho-gia-dinh")
    public String household(Model model) {
        // Add model attributes for statistics
        model.addAttribute("yearsExperience", "20+");
        model.addAttribute("totalStaff", "350+");
        model.addAttribute("totalCapacity", "900");
        model.addAttribute("totalSystems", "10.000+");

        return "household";
    }
}
