package com.ibigboss.crm.repository;

import entity.inventory.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {
    Optional<Part> findBySku(String sku);
}
