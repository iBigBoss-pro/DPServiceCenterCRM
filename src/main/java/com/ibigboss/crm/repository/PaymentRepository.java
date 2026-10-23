package com.ibigboss.crm.repository;

import entity.payment.Payment;
import entity.payment.PaymentDirection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    List<Payment> findByOrderIdAndServiceCenterId(Long orderId, Long serviceCenterId);
    List<Payment> findByDirectionAndServiceCenterId(PaymentDirection direction, Long serviceCenterId);
}
