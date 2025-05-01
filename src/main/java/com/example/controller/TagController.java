package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.tag.TagDTO;
import com.example.enums.ProfileRole;
import com.example.service.TagService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<TagDTO>createTag(@RequestParam("name") String name) {
        return ResponseEntity.ok(tagService.create(name));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean>deleteById(@PathVariable("id") String id) {
        return ResponseEntity.ok(tagService.deleteById(id));
    }
    @GetMapping("/public/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable("id")String id){
        return ResponseEntity.ok(tagService.getById(id));
    }
    @GetMapping("/public/getAll")
    public ResponseEntity<List<TagDTO>> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }

}
