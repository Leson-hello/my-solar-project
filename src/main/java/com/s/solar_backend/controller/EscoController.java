package com.s.solar_backend.controller;

import com.s.solar_backend.entity.QuoteRequest;
import com.s.solar_backend.service.QuoteRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/giai-phap")
public class EscoController {

    @Autowired
    private QuoteRequestService quoteRequestService;

    @GetMapping("/esco")
    public String escoPage(Model model) {
        model.addAttribute("quoteRequest", new QuoteRequest());
        return "esco";
    }

    @PostMapping("/esco/register")
    public String registerEsco(@ModelAttribute QuoteRequest quoteRequest,
            RedirectAttributes redirectAttributes) {
        try {
            quoteRequest.setSolution("ESCO");
            quoteRequest.setQuoteType("ESCO");
            quoteRequest.setStatus("PENDING");
            quoteRequestService.save(quoteRequest);
            redirectAttributes.addFlashAttribute("success",
                    "Đăng ký thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
        }
        return "redirect:/giai-phap/esco#register-form";
    }
}
