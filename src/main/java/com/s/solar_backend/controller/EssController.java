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
public class EssController {

    @Autowired
    private QuoteRequestService quoteRequestService;

    @GetMapping("/ess")
    public String essPage(Model model) {
        model.addAttribute("quoteRequest", new QuoteRequest());
        return "ess";
    }

    @PostMapping("/ess/register")
    public String registerEss(@ModelAttribute QuoteRequest quoteRequest,
            RedirectAttributes redirectAttributes) {
        try {
            quoteRequest.setSolution("ESS");
            quoteRequest.setQuoteType("ESS");
            quoteRequest.setStatus("PENDING");
            quoteRequestService.save(quoteRequest);
            redirectAttributes.addFlashAttribute("success",
                    "Đăng ký thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
        }
        return "redirect:/giai-phap/ess#register-form";
    }
}
