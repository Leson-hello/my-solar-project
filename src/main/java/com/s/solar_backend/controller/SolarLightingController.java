package com.s.solar_backend.controller;

import com.s.solar_backend.entity.QuoteRequest;
import com.s.solar_backend.service.QuoteRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/solutions")
public class SolarLightingController {

    @Autowired
    private QuoteRequestService quoteRequestService;

    @GetMapping("/solar-lighting")
    public String solarLightingPage(Model model) {
        model.addAttribute("quoteRequest", new QuoteRequest());
        return "solar-lighting";
    }

    @PostMapping("/solar-lighting/register")
    public String registerSolarLighting(@ModelAttribute QuoteRequest quoteRequest,
            RedirectAttributes redirectAttributes) {
        try {
            quoteRequest.setSolution("CHIEU_SANG");
            quoteRequest.setQuoteType("CHIEU_SANG");
            quoteRequest.setStatus("PENDING");
            quoteRequestService.save(quoteRequest);
            redirectAttributes.addFlashAttribute("success",
                    "Đăng ký tư vấn thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại!");
        }
        return "redirect:/solutions/solar-lighting#register-form";
    }
}
