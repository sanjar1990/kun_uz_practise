package com.example.controller;

import com.example.dto.comment.CommentDTO;
import com.example.dto.comment.CreateCommentDTO;
import com.example.dto.comment.FilterCommentDTO;
import com.example.dto.comment.UpdateCommentDTO;
import com.example.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    //1. CREATE (ANY)/
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CreateCommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.createComment(commentDTO));
    }

    //        2. UPDATE (ANY and owner)(content,article_id)
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateCommentDTO dto) {
        return ResponseEntity.ok(commentService.updateComment(dto, id));
    }

    //3. DELETE (ADMIN,ANY(only owner))
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable("id") String id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }

    // 4. Get Article Comment List By Article Id
    //        id,created_date,update_date,profile(id,name,surname)
    @GetMapping("/public/getByArticleId/{articleId}")
    public ResponseEntity<PageImpl<CommentDTO>> getByArticleId(@PathVariable("articleId") String articleId,
                                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(commentService.getByArticleId(articleId, page - 1, size));
    }
    // 5. Comment List (pagination) (ADMIN)

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<PageImpl<CommentDTO>> getAllComment(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(commentService.getAllPagination(page - 1, size));
    }

    //     6. Comment Filter(id,created_date_from,created_date_to,profile_id,article_id) with Pagination (ADMIN)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filtterPagination")
    public ResponseEntity<PageImpl<CommentDTO>> filtterPagination(@RequestBody FilterCommentDTO dto,
                                                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(commentService.getFilterPagination(page-1,size,dto));
    }

    //  7. Get Replied Comment List by Comment Id
    @GetMapping("/public/getReply/{id}")
    public ResponseEntity<List<CommentDTO>> getReply(@PathVariable("id") String commentId) {
        return ResponseEntity.ok(commentService.getReply(commentId));
    }
}
