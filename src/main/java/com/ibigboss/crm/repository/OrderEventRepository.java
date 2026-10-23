package com.ibigboss.crm.repository;

import entity.order.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, Long>, JpaSpecificationExecutor<OrderEvent> {
    List<OrderEvent> findByOrderIdOrderByTimestampDesc(Long orderId);
}
