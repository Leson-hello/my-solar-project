package com.s.solar_backend.controller;

import com.s.solar_backend.model.ContactMessage;
import com.s.solar_backend.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/contacts")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactMessageService contactMessageService;

    @GetMapping
    public String listContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            Model model) {

        Page<ContactMessage> messagesPage;
        if (status != null && !status.isEmpty()) {
            messagesPage = contactMessageService.getMessagesByStatus(status, PageRequest.of(page, size));
        } else {
            messagesPage = contactMessageService.getAllMessages(PageRequest.of(page, size));
        }

        model.addAttribute("messagesPage", messagesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentStatus", status);
        model.addAttribute("pendingCount", contactMessageService.countPending());
        model.addAttribute("totalCount", contactMessageService.countAll());

        return "admin/contacts/contact-list";
    }

    @GetMapping("/{id}")
    public String viewContact(@PathVariable Long id, Model model) {
        ContactMessage message = contactMessageService.getById(id);
        if (message != null && "PENDING".equals(message.getStatus())) {
            contactMessageService.markAsRead(id);
            message = contactMessageService.getById(id);
        }
        model.addAttribute("message", message);
        return "admin/contacts/contact-detail";
    }

    @PostMapping("/{id}/mark-replied")
    public String markAsReplied(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactMessageService.markAsReplied(id);
        redirectAttributes.addFlashAttribute("success", "Đã đánh dấu đã phản hồi!");
        return "redirect:/admin/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactMessageService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa tin nhắn!");
        return "redirect:/admin/contacts";
    }
}
