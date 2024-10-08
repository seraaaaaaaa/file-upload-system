package com.example.upload.folder;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.upload.attachment.AttachmentServiceInterface;

@Service
@Transactional
public class FolderService implements FolderServiceInterface {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private AttachmentServiceInterface attachmentService;

    public List<Folder> getAllFolders() {

        return folderRepository.findAllByOrderByFolderNameAsc();
    }

    public ResponseEntity<String> addFolder(String folderName) {
        Folder folder = new Folder(folderName);
        folderRepository.save(folder);

        return ResponseEntity.ok("OK");
    }

    public ResponseEntity<String> editFolder(Long folderId, String folderName) {
        Folder folder = folderRepository.findById(folderId).orElse(null);

        if (folder != null) {
            folder.setFolderName(folderName);

            folderRepository.save(folder);

            return ResponseEntity.ok("OK");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

    public ResponseEntity<String> deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElse(null);

        if (folder != null) {

            attachmentService.deleteAllAttachmentByFolder(folderId);

            folderRepository.deleteById(folderId);

            return ResponseEntity.ok("OK");

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

}
