package com.example.upload.attachment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByOrderBySeqOrder();

    List<Attachment> findAllByFolderIdOrderBySeqOrder(Long folderId);

    List<Attachment> findAllByFolderId(Long folderId);

}
