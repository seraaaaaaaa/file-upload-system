package com.example.upload.attachment;

import java.util.List;

import jakarta.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.upload.UploadService;

@Service
@Transactional
public class AttachmentService implements AttachmentServiceInterface {

    @Autowired
    private AttachmentRepository attachmentRepo;

    @Autowired
    private UploadService uploadService;

    public String uploadAttachment(Long folderId, MultipartFile file, String uuid) {
        JSONObject response = new JSONObject();
        response.put("uuid", uuid);

        String result = "";
        String fileId = file != null ? uploadService.uploadFile(file) : "";

        if (!fileId.isEmpty() && !fileId.equals("Failed")) {
            Attachment attachment = new Attachment(folderId, fileId, 0);
            attachmentRepo.save(attachment);

            result = "OK";
        } else {
            result = fileId;
        }

        response.put("result", result);

        return response.toString();
    }

    public List<Attachment> getAttachmentByFolderId(Long folderId) {
        if (folderId == 0) {
            List<Attachment> attachments = attachmentRepo.findAllByOrderBySeqOrder();
            attachments.removeIf(a -> a.getFolderId() != null && a.getFolderId() != 0);
            return attachments;
        } else {
            return attachmentRepo.findAllByFolderIdOrderBySeqOrder(folderId);
        }
    }

    public Attachment getAttachmentByAttachmentId(Long attachmentId) {

        return attachmentRepo.findById(attachmentId).orElse(null);
    }

    public ResponseEntity<String> moveAttachmentToFolder(Long attachmentId, Long folderId) {
        Attachment attachment = attachmentRepo.findById(attachmentId).orElse(null);

        if (attachment != null) {
            attachment.setFolderId(folderId);
            attachment.setSeqOrder(0);
            attachmentRepo.save(attachment);

            return ResponseEntity.ok("OK");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

    public ResponseEntity<String> deleteAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepo.findById(attachmentId).orElse(null);

        if (attachment != null) {

            String fileId = attachment.getFileId();
            attachmentRepo.delete(attachment);

            uploadService.deleteFile(fileId);

            return ResponseEntity.ok("OK");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

    public void deleteAllAttachmentByFolder(Long folderId) {
        List<Attachment> attachments = attachmentRepo.findAllByFolderId(folderId);

        for (Attachment attachment : attachments) {
            uploadService.deleteFile(attachment.getFileId());
        }

        attachmentRepo.deleteAllInBatch(attachments);
    }

    public void setOrderByAttachmentId(Long attachmentId, Integer order) {
        Attachment attachment = attachmentRepo.findById(attachmentId).orElse(null);
        if (attachment != null) {
            attachment.setSeqOrder(order);
            attachmentRepo.save(attachment);
        }
    }

}
