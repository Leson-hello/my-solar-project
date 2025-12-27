package com.s.solar_backend.controller;

import com.s.solar_backend.entity.Newsletter;
import com.s.solar_backend.repository.NewsletterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterRestController {

    private final NewsletterRepository newsletterRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không được để trống"));
        }

        if (newsletterRepository.existsByEmail(email)) {
            return ResponseEntity.ok(Map.of("message", "Email này đã đăng ký nhận tin tức"));
        }

        Newsletter newsletter = new Newsletter();
        newsletter.setEmail(email);
        newsletterRepository.save(newsletter);

        return ResponseEntity.ok(Map.of("message", "Đăng ký nhận tin tức thành công!"));
    }
}
