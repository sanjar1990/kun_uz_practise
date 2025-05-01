package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.category.CategoryDTO;
import com.example.dto.category.CreateCategoryDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CreateCategoryDTO dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO>updateCategory(@RequestBody CreateCategoryDTO dto,
                                                 @PathVariable Integer id
                                               ) {
        return ResponseEntity.ok(categoryService.updateCategory(dto, id));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean>deleteCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategorys() {
        return ResponseEntity.ok(categoryService.getAllCategorys());
    }
    @GetMapping("/public/lang")
    public ResponseEntity<List<CategoryDTO>> getAllCategorysByLanguage(
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language lang) {
        System.out.println("LANGUAGE: " + lang);
        return ResponseEntity.ok(categoryService.getByLangTwo(lang));
    }
}
