package com.ibigboss.crm.repository;

import entity.warranty.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WarrantyRepository extends JpaRepository<Warranty, Long>, JpaSpecificationExecutor<Warranty> {
    Optional<Warranty> findByOrderId(Long orderId);
}
