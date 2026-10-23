package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.request.PartAdjustStockRequest;
import com.ibigboss.crm.dto.request.PartCreateRequest;
import com.ibigboss.crm.dto.request.PartUpdateRequest;
import com.ibigboss.crm.dto.response.PartResponse;
import com.ibigboss.crm.service.PartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<PartResponse> create(@Valid @RequestBody PartCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partService.create(request, 1L));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<PartResponse> update(@PathVariable Long id, @Valid @RequestBody PartUpdateRequest request) {
        return ResponseEntity.ok(partService.update(id, request, 1L));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<PartResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<PartResponse>> listAll() {
        return ResponseEntity.ok(partService.listAll());
    }

    @PostMapping("/{id}/adjust-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<PartResponse> adjustStock(@PathVariable Long id, @Valid @RequestBody PartAdjustStockRequest request) {
        return ResponseEntity.ok(partService.adjustStock(id, request));
    }
}
