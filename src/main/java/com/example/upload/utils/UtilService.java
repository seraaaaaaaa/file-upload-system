package com.example.upload.utils;

import java.util.Iterator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UtilService {

    public String checkString(String str) {

        return str == null || str.trim().isEmpty() ? "" : str.trim();
    }

    public Long checkLong(String str) {
        try {
            return str == null || str.trim().isEmpty() ? 0
                    : Long.valueOf(str.replaceAll(",", "").replaceAll("\\s+", ""));
        } catch (NumberFormatException e) {
            return Long.valueOf(0);
        }
    }

    public MultipartFile convertToMultipartFile(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = null;
        int i = 0;
        while (itr.hasNext() && i == 0) {
            i++;
            String uploadedFile = itr.next();
            file = request.getFile(uploadedFile);
        }
        return file;
    }

}
