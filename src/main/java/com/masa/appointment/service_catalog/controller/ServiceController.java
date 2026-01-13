package com.masa.appointment.service_catalog.controller;

import com.masa.appointment.service_catalog.entity.ServiceEntity;
import com.masa.appointment.service_catalog.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceEntity create(@Valid @RequestBody ServiceEntity serviceEntity) {
        return serviceService.create(serviceEntity);
    }

    @GetMapping
    public List<ServiceEntity> list() {
        return serviceService.findAll();
    }

    @GetMapping("/{id}")
    public ServiceEntity get(@PathVariable Long id) {
        return serviceService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ServiceEntity update(@PathVariable Long id, @Valid @RequestBody ServiceEntity serviceEntity) {
        return serviceService.update(id, serviceEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        serviceService.delete(id);
    }
}
