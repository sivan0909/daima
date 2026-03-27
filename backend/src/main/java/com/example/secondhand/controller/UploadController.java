package com.example.secondhand.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "请选择要上传的图片"));
        }

        String original = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(original);
        ext = ext != null && !ext.isBlank() ? ("." + ext.toLowerCase()) : "";

        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target);

        String url = "/uploads/" + filename;
        Map<String, Object> res = new HashMap<>();
        res.put("url", url);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}

