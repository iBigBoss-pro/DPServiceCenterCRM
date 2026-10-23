package com.ibigboss.crm.service;

import com.ibigboss.crm.dto.request.LocationCreateRequest;
import com.ibigboss.crm.dto.response.LocationResponse;
import entity.servicecenter.Location;
import com.ibigboss.crm.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public LocationResponse create(LocationCreateRequest request, Long serviceCenterId) {
        Location location = new Location(request.getName(), request.getAddress());
        location.setServiceCenterIdForTest(serviceCenterId);
        location.setPhone(request.getPhone());
        location.setWorkingHours(request.getWorkingHours());
        location.setDefaultLocation(request.isDefaultLocation());
        location = locationRepository.save(location);
        return toResponse(location);
    }

    public LocationResponse getById(Long id) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Точка не найдена: " + id));
        return toResponse(location);
    }

    public List<LocationResponse> listAll() {
        return locationRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    private LocationResponse toResponse(Location location) {
        LocationResponse response = new LocationResponse();
        response.setId(location.getId());
        response.setName(location.getName());
        response.setAddress(location.getAddress());
        response.setPhone(location.getPhone());
        response.setWorkingHours(location.getWorkingHours());
        response.setActive(location.isActive());
        response.setDefaultLocation(location.isDefaultLocation());
        return response;
    }
}
