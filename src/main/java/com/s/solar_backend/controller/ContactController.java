package com.s.solar_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String contactPage(Model model) {
        return "contact";
    }

    @PostMapping("/contact/submit")
    public String submitContact(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String message,
            RedirectAttributes redirectAttributes) {

        // TODO: Save contact message to database or send email
        redirectAttributes.addFlashAttribute("success",
                "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm nhất có thể.");
        return "redirect:/contact";
    }
}
