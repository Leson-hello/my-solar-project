package com.s.solar_backend.controller;

import com.s.solar_backend.dto.QuoteRequestDTO;
import com.s.solar_backend.service.QuoteRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/quotes")
@RequiredArgsConstructor
public class AdminQuoteController {

    private final QuoteRequestService quoteRequestService;

    @GetMapping
    public String listQuotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            Model model) {

        Page<QuoteRequestDTO> quotesPage;

        if (type != null && !type.isEmpty()) {
            if (status != null && !status.isEmpty()) {
                quotesPage = quoteRequestService.getQuoteRequestsByTypeAndStatus(type, status, page, size);
                model.addAttribute("selectedStatus", status);
            } else {
                quotesPage = quoteRequestService.getQuoteRequestsByType(type, page, size);
            }
            model.addAttribute("selectedType", type);
            model.addAttribute("pendingCount", quoteRequestService.countByTypeAndStatus(type, "PENDING"));
            model.addAttribute("contactedCount", quoteRequestService.countByTypeAndStatus(type, "CONTACTED"));
            model.addAttribute("quotedCount", quoteRequestService.countByTypeAndStatus(type, "QUOTED"));
            model.addAttribute("completedCount", quoteRequestService.countByTypeAndStatus(type, "COMPLETED"));
        } else {
            if (status != null && !status.isEmpty()) {
                quotesPage = quoteRequestService.getQuoteRequestsByStatus(status, page, size);
                model.addAttribute("selectedStatus", status);
            } else {
                quotesPage = quoteRequestService.getAllQuoteRequests(page, size);
            }
            model.addAttribute("pendingCount", quoteRequestService.countByStatus("PENDING"));
            model.addAttribute("contactedCount", quoteRequestService.countByStatus("CONTACTED"));
            model.addAttribute("quotedCount", quoteRequestService.countByStatus("QUOTED"));
            model.addAttribute("completedCount", quoteRequestService.countByStatus("COMPLETED"));
        }

        model.addAttribute("quotesPage", quotesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", quotesPage.getTotalPages());

        return "admin/quotes/quote-list";
    }

    @GetMapping("/{id}")
    public String viewQuote(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<QuoteRequestDTO> quoteOpt = quoteRequestService.getQuoteRequestById(id);

        if (quoteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy yêu cầu báo giá!");
            return "redirect:/admin/quotes";
        }

        model.addAttribute("quote", quoteOpt.get());
        return "admin/quotes/quote-detail";
    }

    @PostMapping("/{id}/update-status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String adminNotes,
            RedirectAttributes redirectAttributes) {

        try {
            QuoteRequestDTO dto = new QuoteRequestDTO();
            dto.setStatus(status);
            dto.setAdminNotes(adminNotes);

            quoteRequestService.updateQuoteRequest(id, dto);

            redirectAttributes.addFlashAttribute("success", "Trạng thái đã được cập nhật!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/quotes/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteQuote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            quoteRequestService.deleteQuoteRequest(id);
            redirectAttributes.addFlashAttribute("success", "Yêu cầu báo giá đã được xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/quotes";
    }
}
