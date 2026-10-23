package com.ibigboss.crm.repository;

import entity.approval.Approval;
import entity.approval.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long>, JpaSpecificationExecutor<Approval> {
    List<Approval> findByOrderId(Long orderId);
    List<Approval> findByStatus(ApprovalStatus status);
    Optional<Approval> findByIdAndStatus(Long id, ApprovalStatus status);
}
