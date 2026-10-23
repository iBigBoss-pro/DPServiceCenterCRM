package com.ibigboss.crm.service;

import entity.servicecenter.ServiceCenter;
import com.ibigboss.crm.dto.response.ServiceCenterResponse;
import com.ibigboss.crm.repository.ServiceCenterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ServiceCenterService {

    private final ServiceCenterRepository serviceCenterRepository;

    public ServiceCenterService(ServiceCenterRepository serviceCenterRepository) {
        this.serviceCenterRepository = serviceCenterRepository;
    }

    public List<ServiceCenterResponse> listAll() {
        return serviceCenterRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ServiceCenterResponse getById(Long id) {
        ServiceCenter sc = serviceCenterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сервисный центр не найден: " + id));
        return toResponse(sc);
    }

    public ServiceCenter getByIdEntity(Long id) {
        return serviceCenterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сервисный центр не найден: " + id));
    }

    @Transactional
    public ServiceCenter create(ServiceCenter sc) {
        return serviceCenterRepository.save(sc);
    }

    private ServiceCenterResponse toResponse(ServiceCenter sc) {
        ServiceCenterResponse resp = new ServiceCenterResponse();
        resp.setId(sc.getId());
        resp.setName(sc.getName());
        resp.setLegalName(sc.getLegalName());
        resp.setInn(sc.getInn());
        resp.setOgrn(sc.getOgrn());
        resp.setLegalAddress(sc.getLegalAddress());
        resp.setActualAddress(sc.getActualAddress());
        resp.setPhone(sc.getPhone());
        resp.setEmail(sc.getEmail());
        resp.setDirectorName(sc.getDirectorName());
        return resp;
    }
}
