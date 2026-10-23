package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.response.ServiceCenterResponse;
import com.ibigboss.crm.service.ServiceCenterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-centers")
public class ServiceCenterController {

    private final ServiceCenterService serviceCenterService;

    public ServiceCenterController(ServiceCenterService serviceCenterService) {
        this.serviceCenterService = serviceCenterService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<ServiceCenterResponse>> listAll() {
        return ResponseEntity.ok(serviceCenterService.listAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<ServiceCenterResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceCenterService.getById(id));
    }
}
