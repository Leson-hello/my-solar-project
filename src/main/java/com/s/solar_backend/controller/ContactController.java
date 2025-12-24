package com.s.solar_backend.controller;

import com.s.solar_backend.model.ContactMessage;
import com.s.solar_backend.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageService contactMessageService;

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

        ContactMessage contactMessage = ContactMessage.builder()
                .fullName(fullName)
                .phone(phone)
                .email(email)
                .address(address)
                .message(message)
                .status("PENDING")
                .build();

        contactMessageService.save(contactMessage);

        redirectAttributes.addFlashAttribute("success",
                "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm nhất có thể.");
        return "redirect:/contact";
    }
}
