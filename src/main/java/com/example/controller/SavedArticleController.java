package com.example.controller;
import java.util.List;
import com.example.dto.saved_article.SavedArticleDTO;
import com.example.service.SavedArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/savedArticle")
public class SavedArticleController {
    @Autowired
    private SavedArticleService savedArticleService;
    @PostMapping("/{articleId}")
    public ResponseEntity<Boolean>saveArticle(@PathVariable("articleId") String articleId){
        return ResponseEntity.ok(savedArticleService.saveArticle(articleId));
    }
//        3. Get Profile Saved Article List (ANY)
    @GetMapping()
    public ResponseEntity<List<SavedArticleDTO>>getByProfileId(){
        return ResponseEntity.ok(savedArticleService.getByProfileId());
    }
}
