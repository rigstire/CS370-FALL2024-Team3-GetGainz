package app.getgainz.controller.crud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create upload directory.");
            }

            Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("Photo uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Photo upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir, filename);
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            byte[] photo = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.IMAGE_JPEG)
                    .body(photo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listPhotos() {
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            String[] files = dir.list();
            if (files != null) {
                return ResponseEntity.ok(Arrays.stream(files)
                        .filter(name -> name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$"))
                        .collect(Collectors.toList()));
            }
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
