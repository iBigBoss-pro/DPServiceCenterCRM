package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.request.OrderWorkCreateRequest;
import com.ibigboss.crm.dto.request.OrderWorkPayoutRequest;
import com.ibigboss.crm.dto.response.OrderWorkResponse;
import com.ibigboss.crm.service.OrderWorkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders/{orderId}/works")
public class OrderWorkController {

    private final OrderWorkService orderWorkService;

    public OrderWorkController(OrderWorkService orderWorkService) {
        this.orderWorkService = orderWorkService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<OrderWorkResponse> create(@PathVariable Long orderId, @Valid @RequestBody OrderWorkCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderWorkService.create(orderId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<List<OrderWorkResponse>> list(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderWorkService.findByOrderId(orderId));
    }

    @PutMapping("/{workId}/payout")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<OrderWorkResponse> registerPayout(@PathVariable Long workId, @Valid @RequestBody OrderWorkPayoutRequest request) {
        return ResponseEntity.ok(orderWorkService.registerPayout(workId, request));
    }
}
