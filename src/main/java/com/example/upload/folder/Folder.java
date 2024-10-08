package com.example.upload.folder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long folderId;

    private String folderName;

    public Folder() {
    }

    public Folder(String folderName) {
        this.folderName = folderName;

    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

}
