package com.example.upload.attachment;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.example.upload.file.File;

@Entity
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long attachmentId;

    private Long folderId;

    @Column(name = "file_id")
    private String fileId;

    private Integer seqOrder;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    private File file;

    public Attachment() {
    }

    public Attachment(Long folderId, String fileId, Integer seqOrder) {
        this.folderId = folderId;
        this.fileId = fileId;
        this.seqOrder = seqOrder;
    }

    public Long getAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getFolderId() {
        return this.folderId == null ? 0 : this.folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getSeqOrder() {
        return this.seqOrder == null ? 0 : this.seqOrder;
    }

    public void setSeqOrder(Integer seqOrder) {
        this.seqOrder = seqOrder;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
