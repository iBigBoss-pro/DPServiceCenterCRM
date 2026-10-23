package com.ibigboss.crm.repository;

import entity.order.OrderWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderWorkRepository extends JpaRepository<OrderWork, Long>, JpaSpecificationExecutor<OrderWork> {
    List<OrderWork> findByOrderId(Long orderId);
}
