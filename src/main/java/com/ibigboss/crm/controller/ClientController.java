package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.request.ClientCreateRequest;
import com.ibigboss.crm.dto.request.ClientUpdateRequest;
import com.ibigboss.crm.dto.response.ClientResponse;
import com.ibigboss.crm.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientCreateRequest request) {
        ClientResponse response = clientService.create(request, 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<ClientResponse> update(@PathVariable Long id, @Valid @RequestBody ClientUpdateRequest request) {
        ClientResponse response = clientService.update(id, request, 1L);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEIVER', 'MASTER', 'ADMIN', 'DIRECTOR')")
    public ResponseEntity<ClientResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getById(id, 1L));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<ClientResponse>> listAll() {
        return ResponseEntity.ok(clientService.listAll(1L));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id, 1L);
        return ResponseEntity.noContent().build();
    }
}
