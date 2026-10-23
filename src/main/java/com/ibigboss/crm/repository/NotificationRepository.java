package com.ibigboss.crm.repository;

import entity.notification.Notification;
import entity.notification.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    List<Notification> findByOrderId(Long orderId);
    List<Notification> findByClientId(Long clientId);
    List<Notification> findByStatus(NotificationStatus status);
}
