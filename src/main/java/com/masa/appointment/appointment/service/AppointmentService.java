package com.masa.appointment.appointment.service;

import com.masa.appointment.appointment.entity.AppointmentEntity;
import com.masa.appointment.appointment.repo.AppointmentRepository;
import com.masa.appointment.service_catalog.entity.ServiceEntity;
import com.masa.appointment.service_catalog.repo.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, ServiceRepository serviceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
    }

    public AppointmentEntity create(String clientName, Long serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {

        if (clientName == null || clientName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "clientName is required");
        }
        if (date == null || startTime == null || endTime == null) {
            throw new ResponseStatusException(BAD_REQUEST, "date/startTime/endTime are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(BAD_REQUEST, "endTime must be after startTime");
        }

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Service not found with id: " + serviceId));

        boolean overlap = appointmentRepository.existsOverlap(date, startTime, endTime);
        if (overlap) {
            throw new ResponseStatusException(BAD_REQUEST, "Appointment overlaps with an existing appointment");
        }

        AppointmentEntity a = new AppointmentEntity();
        a.setClientName(clientName);
        a.setDate(date);
        a.setStartTime(startTime);
        a.setEndTime(endTime);
        a.setService(service);

        return appointmentRepository.save(a);
    }

    public List<AppointmentEntity> findAll() {
        return appointmentRepository.findAll();
    }

    public AppointmentEntity findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Appointment not found with id: " + id));
    }

    public void delete(Long id) {
        AppointmentEntity existing = findById(id);
        appointmentRepository.delete(existing);
    }
}
