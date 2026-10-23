package com.ibigboss.crm.repository;

import entity.notification.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long>, JpaSpecificationExecutor<NotificationTemplate> {
    List<NotificationTemplate> findByCode(String code);
    Optional<NotificationTemplate> findByCodeAndActiveTrue(String code);
}
