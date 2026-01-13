package com.masa.appointment.service_catalog.service;

import com.masa.appointment.service_catalog.entity.ServiceEntity;
import com.masa.appointment.service_catalog.repo.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServiceEntity create(ServiceEntity serviceEntity) {
        serviceEntity.setId(null); 
        return serviceRepository.save(serviceEntity);
    }

    public List<ServiceEntity> findAll() {
        return serviceRepository.findAll();
    }

    public ServiceEntity findById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    public ServiceEntity update(Long id, ServiceEntity newData) {
        ServiceEntity existing = findById(id);
        existing.setName(newData.getName());
        existing.setDurationMinutes(newData.getDurationMinutes());
        existing.setPrice(newData.getPrice());
        return serviceRepository.save(existing);
    }

    public void delete(Long id) {
        ServiceEntity existing = findById(id);
        serviceRepository.delete(existing);
    }
}
