package com.ibigboss.crm.repository;

import entity.servicecenter.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Long>, JpaSpecificationExecutor<ServiceCenter> {
    Optional<ServiceCenter> findByInn(String inn);
}
