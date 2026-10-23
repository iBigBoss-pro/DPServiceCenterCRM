package com.ibigboss.crm.repository;

import entity.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, JpaSpecificationExecutor<Attachment> {
    List<Attachment> findByOwnerIdAndOwnerType(Long ownerId, String ownerType);
}
