package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.articleTypeDTO.ArticleTypeDTO;
import com.example.dto.articleTypeDTO.CreateArticleTypeDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<ArticleTypeDTO>createArticleType(@RequestBody CreateArticleTypeDTO dto) {

        return ResponseEntity.ok(articleTypeService.create(dto));
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleTypeDTO>update(@RequestBody CreateArticleTypeDTO dto,
                                                @PathVariable("id") Integer id) {
        return ResponseEntity.ok(articleTypeService.updateArticleType(dto, id));
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(articleTypeService.deleteArticleType(id));
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<PageImpl<ArticleTypeDTO>> getAllPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                     @RequestParam(value = "size",defaultValue = "30") Integer size) {
        return ResponseEntity.ok(articleTypeService.getAllPagination(page-1, size));
    }
    @GetMapping("/public/getByLang")
    public ResponseEntity<List<ArticleTypeDTO>> getByLang(@RequestHeader(value = "Accept-Language", defaultValue = "uz")Language lang){
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }
}
