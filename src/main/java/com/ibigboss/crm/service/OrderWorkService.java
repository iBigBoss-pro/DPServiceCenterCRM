package com.ibigboss.crm.service;

import com.ibigboss.crm.dto.request.OrderWorkCreateRequest;
import com.ibigboss.crm.dto.request.OrderWorkPayoutRequest;
import com.ibigboss.crm.dto.response.OrderWorkResponse;
import entity.order.OrderWork;
import entity.order.WorkExecutorType;
import entity.common.Money;
import com.ibigboss.crm.repository.OrderWorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderWorkService {

    private final OrderWorkRepository orderWorkRepository;

    public OrderWorkService(OrderWorkRepository orderWorkRepository) {
        this.orderWorkRepository = orderWorkRepository;
    }

    @Transactional
    public OrderWorkResponse create(Long orderId, OrderWorkCreateRequest request) {
        OrderWork work = new OrderWork(
            orderId,
            request.getName(),
            request.getClientPrice(),
            request.getQuantity(),
            WorkExecutorType.valueOf(request.getExecutorType()),
            request.getMasterId(),
            request.getExternalMasterName(),
            request.getExternalMasterContact(),
            request.getCostPrice()
        );
        work.setComment(request.getComment());
        work = orderWorkRepository.save(work);
        return toResponse(work);
    }

    public OrderWorkResponse getById(Long id) {
        OrderWork work = orderWorkRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Работа не найдена: " + id));
        return toResponse(work);
    }

    public List<OrderWorkResponse> findByOrderId(Long orderId) {
        return orderWorkRepository.findByOrderId(orderId).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public OrderWorkResponse registerPayout(Long workId, OrderWorkPayoutRequest request) {
        OrderWork work = orderWorkRepository.findById(workId)
            .orElseThrow(() -> new IllegalArgumentException("Работа не найдена: " + workId));
        work.registerPayout(request.getAmount());
        work = orderWorkRepository.save(work);
        return toResponse(work);
    }

    private OrderWorkResponse toResponse(OrderWork work) {
        OrderWorkResponse response = new OrderWorkResponse();
        response.setId(work.getId());
        response.setOrderId(work.getOrderId());
        response.setName(work.getName());
        response.setClientPrice(work.getClientPrice());
        response.setQuantity(work.getQuantity());
        response.setExecutorType(work.getExecutorType() != null ? work.getExecutorType().name() : null);
        response.setMasterId(work.getMasterId());
        response.setExternalMasterName(work.getExternalMasterName());
        response.setExternalMasterContact(work.getExternalMasterContact());
        response.setCostPrice(work.getCostPrice());
        response.setPaidToExecutor(work.getPaidToExecutor());
        response.setTotalClientPrice(work.getTotalClientPrice());
        response.setProfit(work.getProfit());
        response.setDebtToExecutor(work.getDebtToExecutor());
        response.setFullyPaidToExecutor(work.isFullyPaidToExecutor());
        response.setComment(work.getComment());
        return response;
    }
}
