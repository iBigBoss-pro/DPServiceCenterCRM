package com.ibigboss.crm.service;

import com.ibigboss.crm.dto.request.OrderApproveRequest;
import com.ibigboss.crm.dto.request.OrderCreateRequest;
import com.ibigboss.crm.dto.request.OrderFinalizePricingRequest;
import com.ibigboss.crm.dto.request.OrderStatusChangeRequest;
import com.ibigboss.crm.dto.response.OrderResponse;
import entity.client.Client;
import entity.order.*;
import entity.common.Money;
import com.ibigboss.crm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final OrderWorkRepository orderWorkRepository;
    private final OrderEventRepository orderEventRepository;
    private final WarrantyRepository warrantyRepository;
    private final ApprovalRepository approvalRepository;
    private final DocumentRepository documentRepository;
    private final NotificationRepository notificationRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository,
                        OrderWorkRepository orderWorkRepository, OrderEventRepository orderEventRepository,
                        WarrantyRepository warrantyRepository, ApprovalRepository approvalRepository,
                        DocumentRepository documentRepository, NotificationRepository notificationRepository,
                        PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.orderWorkRepository = orderWorkRepository;
        this.orderEventRepository = orderEventRepository;
        this.warrantyRepository = warrantyRepository;
        this.approvalRepository = approvalRepository;
        this.documentRepository = documentRepository;
        this.notificationRepository = notificationRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request, Long serviceCenterId) {
        Client client = clientRepository.findByIdAndServiceCenterId(request.getClientId(), serviceCenterId)
            .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + request.getClientId()));

        Order order = new Order(
            request.getOrderNumber(),
            request.getPublicToken(),
            client,
            request.getReceiverId(),
            request.getLocationId(),
            request.getMustBeFinishedAt(),
            DeviceType.valueOf(request.getDeviceType()),
            request.getManufacturer(),
            request.getModel(),
            request.getSerialNumber(),
            request.getMalfunction(),
            request.getMalfunctionDescription(),
            request.getAccessories(),
            request.getConditionOnAcceptance(),
            request.isLocked(),
            request.isPasswordProvided(),
            request.getPriceEstimate() != null ? request.getPriceEstimate() : Money.zero(),
            request.getStorageDeadlineDays()
        );
        order.setServiceCenterIdForTest(serviceCenterId);
        if (request.getMasterId() != null) order.setMasterId(request.getMasterId());
        order.setClientAgreedToDiagnostics(request.isClientAgreedToDiagnostics());
        order.setClientAgreedToPasswordUse(request.isClientAgreedToPasswordUse());

        order = orderRepository.save(order);
        return toResponse(order);
    }

    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + id));
        return toResponse(order);
    }

    public List<OrderResponse> listAll() {
        return orderRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public List<OrderResponse> findByClientId(Long clientId) {
        return orderRepository.findByClientIdAndServiceCenterId(clientId, 1L).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public OrderResponse changeStatus(Long orderId, OrderStatusChangeRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + orderId));
        OrderStatus newStatus = OrderStatus.valueOf(request.getNewStatus());
        Long userId = request.getUserId() != null ? request.getUserId() : 1L;
        order.changeStatus(newStatus, userId, request.getComment());

        // Persist order events
        List<OrderEvent> events = order.getHistory();
        if (!events.isEmpty()) {
            events.forEach(event -> {
                event.setOrderId(orderId);
                orderEventRepository.save(event);
            });
            // Clear history after persisting
            // Note: In a real implementation, you'd need to clear the history list
        }

        return toResponse(order);
    }

    @Transactional
    public OrderResponse approve(Long orderId, OrderApproveRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + orderId));
        order.approveConditions(request.getApprovedPrice(), request.getApprovedDeadline());
        return toResponse(order);
    }

    @Transactional
    public OrderResponse finalizePricing(Long orderId, OrderFinalizePricingRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + orderId));
        order.finalizePricing(
            request.getFinalPrice(),
            request.getPartsCost(),
            request.getOutsourcedCost()
        );
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setPublicToken(order.getPublicToken());
        response.setClientId(order.getClient() != null ? order.getClient().getId() : null);
        response.setClientName(order.getClient() != null ? order.getClient().getFullName() : null);
        response.setMasterId(order.getMasterId());
        response.setReceiverId(order.getReceiverId());
        response.setLocationId(order.getLocationId());
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        response.setAcceptedAt(order.getAcceptedAt());
        response.setMustBeFinishedAt(order.getMustBeFinishedAt());
        response.setFinishedAt(order.getFinishedAt());
        response.setPickUp(order.getPickUp());
        response.setDeviceType(order.getDeviceType() != null ? order.getDeviceType().name() : null);
        response.setManufacturer(order.getManufacturer());
        response.setModel(order.getModel());
        response.setSerialNumber(order.getSerialNumber());
        response.setMalfunction(order.getMalfunction());
        response.setMalfunctionDescription(order.getMalfunctionDescription());
        response.setAccessories(order.getAccessories());
        response.setConditionOnAcceptance(order.getConditionOnAcceptance());
        response.setLocked(order.isLocked());
        response.setPasswordProvided(order.isPasswordProvided());
        response.setPriceEstimate(order.getPriceEstimate());
        response.setFinalPrice(order.getFinalPrice());
        response.setDiagnosticsPrice(order.getDiagnosticsPrice());
        response.setCostPrice(order.getCostPrice());
        response.setProfit(order.getProfit());
        response.setApprovedPrice(order.getApprovedPrice());
        response.setApprovedDeadline(order.getApprovedDeadline());
        response.setApprovedAt(order.getApprovedAt());
        response.setStorageDeadlineDays(order.getStorageDeadlineDays());

        List<String> history = new ArrayList<>();
        for (OrderEvent event : order.getHistory()) {
            history.add(event.getOldStatus() + " -> " + event.getNewStatus());
        }
        response.setStatusHistory(history);
        return response;
    }
}
