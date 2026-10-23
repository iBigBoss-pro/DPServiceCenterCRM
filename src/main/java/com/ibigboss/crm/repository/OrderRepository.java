package com.ibigboss.crm.repository;

import entity.order.Order;
import entity.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findByPublicToken(String publicToken);
    List<Order> findByClientIdAndServiceCenterId(Long clientId, Long serviceCenterId);
    List<Order> findByStatusAndServiceCenterId(OrderStatus status, Long serviceCenterId);
    List<Order> findByMasterIdAndServiceCenterId(Long masterId, Long serviceCenterId);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.serviceCenterId = :scId AND o.deletedAt IS NULL")
    List<Order> findByStatusActive(OrderStatus status, Long scId);
}
