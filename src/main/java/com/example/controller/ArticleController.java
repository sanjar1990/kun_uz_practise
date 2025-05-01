package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.article.*;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    @PostMapping("")
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody CreateArticleDTO dto) {
        return ResponseEntity.ok(articleService.createArticle(dto));
    }

    @PreAuthorize("hasAnyRole('ROLE_MODERATOR','ROLE_PUBLISHER')")
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateArticle(
            @PathVariable("id") String id,
            @RequestBody CreateArticleDTO dto) {
        return ResponseEntity.ok(articleService.updateArticle(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteArticle(@PathVariable("id") String id) {
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_PUBLISHER')")
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Boolean> updateStatus(@PathVariable("id") String id) {
        return ResponseEntity.ok(articleService.updateStatus(id));
    }

    @GetMapping("/public/getByType")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLastArticle(@RequestParam("typeId") Integer typeId,
                                                                    @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(articleService.getArticleByType(typeId, limit));
    }

    @GetMapping("/public/lastEightArticle")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLastEightArticle(@RequestBody List<String> listId) {
        return ResponseEntity.ok(articleService.getLastEightArticle(listId));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ArticleFullInfoDTO> getArticleById(@PathVariable("id") String id,
                                                             @RequestHeader(value = "Accept-Language",
                                                                     defaultValue = "uz") Language lang) {
        return ResponseEntity.ok(articleService.getByIdAndLang(id, lang));
    }

    //    9. Get Last 4 Article By Types and except given article id.
    @GetMapping("/public/getLastFour")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLastFourArticle(@RequestParam("articleId") String articleId,
                                                                        @RequestParam("typeId") Integer typeId) {
        return ResponseEntity.ok(articleService.getLastFourArticle(articleId, typeId));
    }

    //    10. Get 4 most read articles ArticleShortInfo
    @GetMapping("/public/mostViewed")
    public ResponseEntity<List<ArticleShortInfoDTO>> getMostViewed() {
        return ResponseEntity.ok(articleService.getMostView());
    }

    //11. Get Last 4 Article By TagName (Bitta article ni eng ohirida chiqib turadi.) ArticleShortInfo
    @GetMapping("/public/byTag")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLastFourByTag(@RequestParam("tagId") String tagId) {
        return ResponseEntity.ok(articleService.getByTag(tagId));
    }

    //    12. Get Last 5 Article By Types  And By Region Key ArticleShortInfo
    @GetMapping("/public/byTypeAndRegion")
    public ResponseEntity<List<ArticleShortInfoDTO>> getByTypeAndRegion(@RequestParam("typeId") Integer typeId,
                                                                        @RequestParam("regId") Integer regId) {
        return ResponseEntity.ok(articleService.getByTypeAndRegion(typeId, regId));
    }

    // 13. Get Article list by Region Key (Pagination) ArticleShortInfo
    @GetMapping("/public/getByReg/{id}")
    public ResponseEntity<PageImpl<ArticleShortInfoDTO>> getByReg(@PathVariable("id") Integer regId,
                                                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(articleService.getByRegId(page - 1, size, regId));
    }

    //14. Get Last 5 Article Category Key ArticleShortInfo
    @GetMapping("/public/getByCategory/{id}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLastFiveArticle(@PathVariable("id") Integer categoryId) {
        return ResponseEntity.ok(articleService.getByCategory(categoryId));
    }

    //    15. Get Article By Category Key (Pagination)ArticleShortInfo
    @GetMapping("/public/categoryPagination/{id}")
    public ResponseEntity<PageImpl<ArticleShortInfoDTO>> getByCategoryPagination(@PathVariable("id") Integer id,
                                                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(articleService.getByCategoryPagination(id, page - 1, size));
    }

    //17. Increase Share View Count by Article Id
    @PutMapping("/public/increaseShareCount/{id}")
    public ResponseEntity<Boolean> increaseShareCount(@PathVariable("id") String articleId) {
        return ResponseEntity.ok(articleService.increaseShareCount(articleId));
    }

    //      18. Filter Article (id,title,region_id,category_id,crated_date_from,created_date_to
//            published_date_from,published_date_to,moderator_id,publisher_id,status) with Pagination (PUBLISHER)
//    ArticleShortInfo
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/filterArticle")
    public ResponseEntity<PageImpl<ArticleShortInfoDTO>> filterArticlePagination
    (@RequestBody ArticleFilterPaginationDTO dto,
     @RequestParam(value = "page", defaultValue = "1") Integer page,
     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(articleService.filterPagination(dto, page - 1, size));
    }

}
