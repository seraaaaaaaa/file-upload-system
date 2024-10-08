package com.example.upload.folder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private FolderServiceInterface folderService;

    @ResponseBody
    @GetMapping(value = "/all")
    public List<Folder> showAllFolders() {

        return folderService.getAllFolders();
    }

    @ResponseBody
    @PostMapping(value = "/add")
    public ResponseEntity<String> addNewFolder(@RequestParam String folderName) {

        return folderService.addFolder(folderName);
    }

    @ResponseBody
    @PostMapping(value = "/edit/{folderId}")
    public ResponseEntity<String> editFolder(@PathVariable Long folderId, @RequestParam String folderName) {

        return folderService.editFolder(folderId, folderName);

    }

    @ResponseBody
    @DeleteMapping(value = "/delete/{folderId}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long folderId) {

        return folderService.deleteFolder(folderId);

    }
}
