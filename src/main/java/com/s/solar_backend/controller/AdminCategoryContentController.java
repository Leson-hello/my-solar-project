package com.s.solar_backend.controller;

import com.s.solar_backend.dto.CategoryContentDTO;
import com.s.solar_backend.service.CategoryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/category-contents")
@RequiredArgsConstructor
public class AdminCategoryContentController {

    private final CategoryContentService categoryService;

    // Define the list of valid categories manually for the management UI
    private static final Map<String, String> CATEGORIES = new LinkedHashMap<>();
    static {
        CATEGORIES.put("tam-pin", "Tấm pin năng lượng mặt trời");
        CATEGORIES.put("inverter", "Inverter / Bộ hòa lưới");
        CATEGORIES.put("pin-luu-tru", "Pin lưu trữ");
        CATEGORIES.put("den-nang-luong", "Đèn năng lượng mặt trời");
        CATEGORIES.put("phu-kien", "Phụ kiện");
    }

    @GetMapping
    public String list(Model model) {
        List<CategoryContentDTO> existingContents = categoryService.getAllCategoryContents();

        // Match existing content with our defined categories
        List<Map<String, Object>> displayList = new ArrayList<>();

        for (Map.Entry<String, String> entry : CATEGORIES.entrySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", entry.getKey());
            item.put("name", entry.getValue());

            // Find if content exists for this category
            CategoryContentDTO content = existingContents.stream()
                    .filter(c -> c.getCategoryCode().equals(entry.getKey()))
                    .findFirst()
                    .orElse(null);

            item.put("exists", content != null);
            item.put("id", content != null ? content.getId() : null);
            item.put("updatedAt", content != null ? content.getUpdatedAt() : null);

            displayList.add(item);
        }

        model.addAttribute("categories", displayList);
        return "admin/category-content/list";
    }

    @GetMapping("/edit/{code}")
    public String editForm(@PathVariable String code, Model model, RedirectAttributes redirectAttributes) {
        if (!CATEGORIES.containsKey(code)) {
            redirectAttributes.addFlashAttribute("error", "Danh mục không hợp lệ!");
            return "redirect:/admin/category-contents";
        }

        CategoryContentDTO dto = categoryService.getByCategoryCode(code)
                .orElse(new CategoryContentDTO(null, code, CATEGORIES.get(code), "", "", null));

        model.addAttribute("content", dto);
        model.addAttribute("categoryName", CATEGORIES.get(code));
        return "admin/category-content/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CategoryContentDTO dto, RedirectAttributes redirectAttributes) {
        try {
            // Ensure the name matches the code even if changed in form (consistency)
            if (CATEGORIES.containsKey(dto.getCategoryCode())) {
                dto.setCategoryName(CATEGORIES.get(dto.getCategoryCode()));
            }

            categoryService.saveOrUpdate(dto);
            redirectAttributes.addFlashAttribute("success", "Nội dung danh mục đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/category-contents/edit/" + dto.getCategoryCode();
    }
}
