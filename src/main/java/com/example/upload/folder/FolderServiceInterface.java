package com.example.upload.folder;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface FolderServiceInterface {

    List<Folder> getAllFolders();

    ResponseEntity<String> addFolder(String folderName);

    ResponseEntity<String> editFolder(Long folderId, String folderName);

    ResponseEntity<String> deleteFolder(Long folderId);

}
