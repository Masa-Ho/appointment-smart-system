package com.masa.appointment.appointment.controller;

import com.masa.appointment.appointment.entity.AppointmentEntity;
import com.masa.appointment.appointment.entity.AppointmentStatus;
import com.masa.appointment.appointment.service.AppointmentService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // DTO للـ create/update
    public static class AppointmentRequest {
        @NotBlank public String clientName;
        @NotNull public Long serviceId;
        @NotNull public LocalDate date;       // "2025-12-30"
        @NotNull public LocalTime startTime;  // "10:00"
        @NotNull public LocalTime endTime;    // "10:30"
    }

    public static class ChangeStatusRequest {
        @NotNull public AppointmentStatus status; // "APPROVED" ...
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentEntity create(@RequestBody AppointmentRequest req) {
        return appointmentService.create(req.clientName, req.serviceId, req.date, req.startTime, req.endTime);
    }

    @GetMapping
    public List<AppointmentEntity> list() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public AppointmentEntity get(@PathVariable Long id) {
        return appointmentService.findById(id);
    }

    @PutMapping("/{id}")
    public AppointmentEntity update(@PathVariable Long id, @RequestBody AppointmentRequest req) {
        return appointmentService.update(id, req.clientName, req.serviceId, req.date, req.startTime, req.endTime);
    }

    @PatchMapping("/{id}/status")
    public AppointmentEntity changeStatus(@PathVariable Long id, @RequestBody ChangeStatusRequest req) {
        return appointmentService.changeStatus(id, req.status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }
}
