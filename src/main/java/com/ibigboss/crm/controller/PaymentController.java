package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.request.PaymentCreateRequest;
import com.ibigboss.crm.dto.response.PaymentResponse;
import com.ibigboss.crm.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest request) {
        PaymentResponse response = paymentService.create(request, 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping(params = "orderId")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<PaymentResponse>> findByOrderId(@RequestParam Long orderId) {
        return ResponseEntity.ok(paymentService.findByOrderId(orderId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<PaymentResponse>> listAll() {
        return ResponseEntity.ok(paymentService.listAll());
    }
}
