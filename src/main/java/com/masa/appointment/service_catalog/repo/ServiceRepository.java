package com.masa.appointment.service_catalog.repo;

import com.masa.appointment.service_catalog.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}
