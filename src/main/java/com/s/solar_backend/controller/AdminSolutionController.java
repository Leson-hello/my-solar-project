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
@RequestMapping("/admin/solutions")
@RequiredArgsConstructor
public class AdminSolutionController {

    private final QuoteRequestService quoteRequestService;

    @GetMapping
    public String listSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String solution,
            Model model) {

        Page<QuoteRequestDTO> solutionsPage;

        if (solution != null && !solution.isEmpty()) {
            // Filter by specific solution type
            if (status != null && !status.isEmpty()) {
                solutionsPage = quoteRequestService.getQuoteRequestsBySolutionTypeAndStatus(solution, status, page,
                        size);
                model.addAttribute("selectedStatus", status);
            } else {
                solutionsPage = quoteRequestService.getQuoteRequestsBySolutionType(solution, page, size);
            }
            model.addAttribute("selectedSolution", solution);
            model.addAttribute("pendingCount", quoteRequestService.countBySolutionAndStatus(solution, "PENDING"));
            model.addAttribute("contactedCount", quoteRequestService.countBySolutionAndStatus(solution, "CONTACTED"));
            model.addAttribute("quotedCount", quoteRequestService.countBySolutionAndStatus(solution, "QUOTED"));
            model.addAttribute("completedCount", quoteRequestService.countBySolutionAndStatus(solution, "COMPLETED"));
        } else {
            // Show all solutions (ESCO, HYBRID, ESS)
            if (status != null && !status.isEmpty()) {
                solutionsPage = quoteRequestService.getQuoteRequestsBySolution(status, page, size);
                model.addAttribute("selectedStatus", status);
            } else {
                solutionsPage = quoteRequestService.getAllSolutionQuotes(page, size);
            }
            model.addAttribute("pendingCount", quoteRequestService.countSolutionByStatus("PENDING"));
            model.addAttribute("contactedCount", quoteRequestService.countSolutionByStatus("CONTACTED"));
            model.addAttribute("quotedCount", quoteRequestService.countSolutionByStatus("QUOTED"));
            model.addAttribute("completedCount", quoteRequestService.countSolutionByStatus("COMPLETED"));
        }

        model.addAttribute("solutionsPage", solutionsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", solutionsPage.getTotalPages());

        return "admin/solutions/solution-list";
    }

    @GetMapping("/{id}")
    public String viewSolution(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<QuoteRequestDTO> solutionOpt = quoteRequestService.getQuoteRequestById(id);

        if (solutionOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đăng ký giải pháp!");
            return "redirect:/admin/solutions";
        }

        model.addAttribute("solution", solutionOpt.get());
        return "admin/solutions/solution-detail";
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

        return "redirect:/admin/solutions/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteSolution(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            quoteRequestService.deleteQuoteRequest(id);
            redirectAttributes.addFlashAttribute("success", "Đăng ký giải pháp đã được xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/solutions";
    }
}
