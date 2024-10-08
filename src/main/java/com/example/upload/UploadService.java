package com.example.upload;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.upload.file.File;
import com.example.upload.file.FileRepository;

@Service
public class UploadService {

    @Autowired
    private FileRepository fileRepository;

    private File upload(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File FileDB = new File(fileName, file.getContentType(), file.getBytes());
        return fileRepository.save(FileDB);
    }

    public String uploadFile(MultipartFile file) {
        String result = "";
        try {
            File uploadedFile = upload(file);

            result = uploadedFile.getId();
        } catch (Exception e) {
            result = "Failed";
        }
        return result;
    }

    public File getFile(String id) {
        return id.isEmpty() ? null : fileRepository.findById(id).orElse(null);
    }

    public String deleteFile(String id) {
        String result = "";

        if (id != null && !id.isEmpty()) {
            try {
                fileRepository.deleteById(id);
                result = "OK";
            } catch (Exception e) {
                result = e.toString();
            }
        } else {
            result = "OK";
        }

        return result;
    }

}
