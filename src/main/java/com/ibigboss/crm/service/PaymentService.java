package com.ibigboss.crm.service;

import com.ibigboss.crm.dto.request.PaymentCreateRequest;
import com.ibigboss.crm.dto.response.PaymentResponse;
import entity.payment.*;
import com.ibigboss.crm.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentResponse create(PaymentCreateRequest request, Long serviceCenterId) {
        Payment payment;
        if (PaymentDirection.INCOME.name().equals(request.getDirection())) {
            if (request.getOrderId() == null) {
                throw new IllegalArgumentException("Для INCOME обязателен orderId");
            }
            payment = Payment.income(
                request.getOrderId(),
                request.getAmount(),
                PaymentMethod.valueOf(request.getMethod()),
                request.getAcceptedByUserId(),
                request.getComment()
            );
        } else {
            if (ExpenseCategory.OUTSOURCE.name().equals(request.getExpenseCategory())) {
                payment = Payment.outsource(
                    request.getOrderWorkId(),
                    request.getOrderId(),
                    request.getRecipientName(),
                    request.getRecipientContact(),
                    request.getAmount(),
                    PaymentMethod.valueOf(request.getMethod()),
                    request.getAcceptedByUserId(),
                    request.getComment()
                );
            } else {
                payment = Payment.expense(
                    ExpenseCategory.valueOf(request.getExpenseCategory()),
                    request.getRecipientName(),
                    request.getAmount(),
                    PaymentMethod.valueOf(request.getMethod()),
                    request.getAcceptedByUserId(),
                    request.getComment()
                );
            }
        }
        payment.setServiceCenterIdForTest(serviceCenterId);
        if (request.getSupplierId() != null) payment.setSupplierId(request.getSupplierId());
        payment = paymentRepository.save(payment);
        return toResponse(payment);
    }

    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Платёж не найден: " + id));
        return toResponse(payment);
    }

    public List<PaymentResponse> findByOrderId(Long orderId) {
        return paymentRepository.findByOrderIdAndServiceCenterId(orderId, 1L).stream()
            .map(this::toResponse)
            .toList();
    }

    public List<PaymentResponse> listAll() {
        return paymentRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setDirection(payment.getDirection() != null ? payment.getDirection().name() : null);
        response.setOrderId(payment.getOrderId());
        response.setExpenseCategory(payment.getExpenseCategory() != null ? payment.getExpenseCategory().name() : null);
        response.setOrderWorkId(payment.getOrderWorkId());
        response.setRecipientName(payment.getRecipientName());
        response.setRecipientContact(payment.getRecipientContact());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod() != null ? payment.getMethod().name() : null);
        response.setPaidAt(payment.getPaidAt());
        response.setAcceptedByUserId(payment.getAcceptedByUserId());
        response.setComment(payment.getComment());
        return response;
    }
}
