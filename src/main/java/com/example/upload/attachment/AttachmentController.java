package com.example.upload.attachment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.upload.utils.UtilService;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentServiceInterface attachmentService;

    @Autowired
    private UtilService utilService;

    @ResponseBody
    @GetMapping(value = "/all/{folderId}")
    public List<Attachment> getAttachments(@PathVariable Long folderId) {

        return attachmentService.getAttachmentByFolderId(folderId);
    }

    @ResponseBody
    @GetMapping(value = "/get/{attachmentId}")
    public Attachment getAttachmentById(@PathVariable Long attachmentId) {

        return attachmentService.getAttachmentByAttachmentId(attachmentId);
    }

    @ResponseBody
    @PostMapping(value = "/add")
    public String addNewAttachment(MultipartHttpServletRequest request) {

        MultipartFile file = utilService.convertToMultipartFile(request);

        Long folderId = utilService.checkLong(request.getParameter("folder_id"));
        String uuid = utilService.checkString(request.getParameter("uuid"));

        return attachmentService.uploadAttachment(folderId, file, uuid);
    }

    @ResponseBody
    @PostMapping(value = "/move/{attachmentId}/{folderId}")
    public ResponseEntity<String> moveAttachment(@PathVariable Long attachmentId, @PathVariable Long folderId) {

        return attachmentService.moveAttachmentToFolder(attachmentId, folderId);
    }

    @ResponseBody
    @DeleteMapping(value = "/delete/{attachmentId}")
    public ResponseEntity<String> deleteAttachment(@PathVariable Long attachmentId) {

        return attachmentService.deleteAttachment(attachmentId);
    }

    @ResponseBody
    @PostMapping(value = "/order/{attachmentId}/{order}")
    public void setAttachmentOrder(@PathVariable Long attachmentId, @PathVariable Integer order) {

        attachmentService.setOrderByAttachmentId(attachmentId, order);
    }

}
