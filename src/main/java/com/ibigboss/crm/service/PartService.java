package com.ibigboss.crm.service;

import com.ibigboss.crm.dto.request.PartAdjustStockRequest;
import com.ibigboss.crm.dto.request.PartCreateRequest;
import com.ibigboss.crm.dto.request.PartUpdateRequest;
import com.ibigboss.crm.dto.response.PartResponse;
import entity.inventory.Part;
import entity.common.Money;
import com.ibigboss.crm.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Transactional
    public PartResponse create(PartCreateRequest request, Long serviceCenterId) {
        Part part = new Part(request.getName(), request.getSku());
        part.setServiceCenterIdForTest(serviceCenterId);
        part.setPurchasePrice(request.getPurchasePrice() != null ? request.getPurchasePrice() : Money.zero());
        part.setSalePrice(request.getSalePrice() != null ? request.getSalePrice() : Money.zero());
        part.setMinStock(request.getMinStock());
        part.setLocationId(request.getLocationId());
        part = partRepository.save(part);
        return toResponse(part);
    }

    @Transactional
    public PartResponse update(Long id, PartUpdateRequest request, Long serviceCenterId) {
        Part part = partRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Запчасть не найдена: " + id));
        if (request.getName() != null) part.setName(request.getName());
        if (request.getSku() != null) part.setSku(request.getSku());
        if (request.getPurchasePrice() != null) part.setPurchasePrice(request.getPurchasePrice());
        if (request.getSalePrice() != null) part.setSalePrice(request.getSalePrice());
        if (request.getMinStock() != null) part.setMinStock(request.getMinStock());
        if (request.getLocationId() != null) part.setLocationId(request.getLocationId());
        part = partRepository.save(part);
        return toResponse(part);
    }

    public PartResponse getById(Long id) {
        Part part = partRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Запчасть не найдена: " + id));
        return toResponse(part);
    }

    public List<PartResponse> listAll() {
        return partRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public PartResponse adjustStock(Long id, PartAdjustStockRequest request) {
        Part part = partRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Запчасть не найдена: " + id));
        part.adjustStock(request.getDelta());
        part = partRepository.save(part);
        return toResponse(part);
    }

    private PartResponse toResponse(Part part) {
        PartResponse response = new PartResponse();
        response.setId(part.getId());
        response.setName(part.getName());
        response.setSku(part.getSku());
        response.setPurchasePrice(part.getPurchasePrice());
        response.setSalePrice(part.getSalePrice());
        response.setQuantityInStock(part.getQuantityInStock());
        response.setMinStock(part.getMinStock());
        response.setLocationId(part.getLocationId());
        response.setActive(part.isActive());
        response.setLowStock(part.isLowStock());
        return response;
    }
}
