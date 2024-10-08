package com.example.upload;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.upload.file.File;

@Controller
@CrossOrigin
@RequestMapping("/file")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public String uploadFileToDatabase(MultipartHttpServletRequest request) {

        String img = "";
        Iterator<String> itr = request.getFileNames();
        while (itr.hasNext()) {
            String uploadedFile = itr.next();
            MultipartFile file = request.getFile(uploadedFile);
            img = uploadService.uploadFile(file);

        }

        return img;
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String id) {
        File file = uploadService.getFile(id);

        if (file != null) {
            try {
                // Encode the filename using UTF-8
                String encodedFilename = URLEncoder.encode(file.getName(), "UTF-8")
                        .replace("+", "%20")
                        .replace("%2F", "/");

                if (MediaType.parseMediaType(file.getType()).isCompatibleWith(MediaType.IMAGE_PNG)
                        || MediaType.parseMediaType(file.getType()).isCompatibleWith(MediaType.IMAGE_JPEG)
                        || MediaType.parseMediaType(file.getType()).isCompatibleWith(MediaType.IMAGE_GIF)
                        || MediaType.parseMediaType(file.getType()).isCompatibleWith(MediaType.APPLICATION_PDF)
                        || MediaType.parseMediaType(file.getType()).isCompatibleWith(MediaType.TEXT_PLAIN)) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "inline; filename=" + encodedFilename)
                            .contentType(MediaType.parseMediaType(file.getType()))
                            .body(file.getData());
                } else {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=" + encodedFilename)
                            .contentType(MediaType.parseMediaType(file.getType()))
                            .body(file.getData());
                }
            } catch (UnsupportedEncodingException e) {
                // Handle encoding exception
                e.printStackTrace();
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            return ResponseEntity.ok()
                    // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                    // file.getName())
                    // .contentType(MediaType.parseMediaType(file.getType()))
                    .body(null);
        }

    }

}
