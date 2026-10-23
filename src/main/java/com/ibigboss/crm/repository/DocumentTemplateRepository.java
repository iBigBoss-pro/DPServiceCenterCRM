package com.ibigboss.crm.repository;

import entity.document.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long>, JpaSpecificationExecutor<DocumentTemplate> {
    List<DocumentTemplate> findByCode(String code);
    Optional<DocumentTemplate> findByCodeAndActiveTrue(String code);
}
