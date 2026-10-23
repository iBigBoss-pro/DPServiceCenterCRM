package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.request.LocationCreateRequest;
import com.ibigboss.crm.dto.response.LocationResponse;
import com.ibigboss.crm.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LocationResponse> create(@Valid @RequestBody LocationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.create(request, 1L));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<LocationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<LocationResponse>> listAll() {
        return ResponseEntity.ok(locationService.listAll());
    }
}
