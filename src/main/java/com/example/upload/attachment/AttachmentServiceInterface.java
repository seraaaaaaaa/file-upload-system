package com.example.upload.attachment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentServiceInterface {

    String uploadAttachment(Long folderId, MultipartFile file, String uuid);

    List<Attachment> getAttachmentByFolderId(Long folderId);

    Attachment getAttachmentByAttachmentId(Long attachmentId);

    ResponseEntity<String> moveAttachmentToFolder(Long attachmentId, Long folderId);

    ResponseEntity<String> deleteAttachment(Long attachmentId);

    void deleteAllAttachmentByFolder(Long folderId);

    void setOrderByAttachmentId(Long attachmentId, Integer order);

}
