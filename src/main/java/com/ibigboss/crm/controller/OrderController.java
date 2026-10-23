package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.request.*;
import com.ibigboss.crm.dto.response.OrderResponse;
import com.ibigboss.crm.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEIVER', 'ADMIN')")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.create(request, 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<OrderResponse>> listAll() {
        return ResponseEntity.ok(orderService.listAll());
    }

    @GetMapping(params = "clientId")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<OrderResponse>> findByClientId(@RequestParam Long clientId) {
        return ResponseEntity.ok(orderService.findByClientId(clientId));
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN')")
    public ResponseEntity<OrderResponse> changeStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusChangeRequest request) {
        return ResponseEntity.ok(orderService.changeStatus(id, request));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<OrderResponse> approve(@PathVariable Long id, @Valid @RequestBody OrderApproveRequest request) {
        return ResponseEntity.ok(orderService.approve(id, request));
    }

    @PostMapping("/{id}/finalize-pricing")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<OrderResponse> finalizePricing(@PathVariable Long id, @Valid @RequestBody OrderFinalizePricingRequest request) {
        return ResponseEntity.ok(orderService.finalizePricing(id, request));
    }
}
