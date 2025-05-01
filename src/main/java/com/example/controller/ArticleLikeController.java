package com.example.controller;

import com.example.service.ArticleLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articleLike")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/like/{articleId}")
    public ResponseEntity<Boolean>like(@PathVariable String articleId) {
        return ResponseEntity.ok(articleLikeService.like(articleId));
    }
    @PostMapping("/dislike/{articleId}")
    public ResponseEntity<Boolean>dislike(@PathVariable String articleId) {
        return ResponseEntity.ok(articleLikeService.dislike(articleId));
    }

}
