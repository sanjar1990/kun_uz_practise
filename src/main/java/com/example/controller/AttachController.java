package com.example.controller;

import com.example.dto.attach.AttachDTO;
import com.example.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;
//    @PostMapping("/upload")
//    public ResponseEntity<String>upload(@RequestParam("file") MultipartFile file) {
//        String fileName=attachService.saverToSystem(file);
//        return ResponseEntity.ok(fileName);
//    }
    @PostMapping("/upload")
    public ResponseEntity<AttachDTO>uploadTwo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.save(file));
    }
    @GetMapping(value = "/open/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("filename") String filename) {
    return attachService.loadImage(filename);
    }
    @GetMapping(value = "/open/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] openImg(@PathVariable("id") String  id) {
        return attachService.loadImageById(id);
    }
    @GetMapping(value = "/open/general/{id}", produces = MediaType.ALL_VALUE)
    public byte[] openGeneral(@PathVariable("id") String id) {
        return attachService.loadByIdGeneral(id);
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource>download(@PathVariable("id") String id) {
       return  attachService.download(id);

    }
}
