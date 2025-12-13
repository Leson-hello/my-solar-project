package com.s.solar_backend.controller;

import com.s.solar_backend.dto.QuoteRequestDTO;
import com.s.solar_backend.service.QuoteRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteRestController {

    private final QuoteRequestService quoteRequestService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> submitQuoteRequest(
            @RequestBody QuoteRequestDTO quoteRequestDTO) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (quoteRequestDTO.getFullName() == null || quoteRequestDTO.getFullName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Vui lòng nhập họ tên");
                return ResponseEntity.badRequest().body(response);
            }

            if (quoteRequestDTO.getEmail() == null || quoteRequestDTO.getEmail().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Vui lòng nhập email");
                return ResponseEntity.badRequest().body(response);
            }

            if (quoteRequestDTO.getPhone() == null || quoteRequestDTO.getPhone().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Vui lòng nhập số điện thoại");
                return ResponseEntity.badRequest().body(response);
            }

            QuoteRequestDTO savedRequest = quoteRequestService.createQuoteRequest(quoteRequestDTO);

            response.put("success", true);
            response.put("message", "Yêu cầu báo giá đã được gửi thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.");
            response.put("quoteRequest", savedRequest);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi gửi yêu cầu: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
