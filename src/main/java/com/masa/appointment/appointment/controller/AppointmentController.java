// src/main/java/com/masa/appointment/appointment/controller/AppointmentController.java
package com.masa.appointment.appointment.controller;

import com.masa.appointment.appointment.entity.AppointmentEntity;
import com.masa.appointment.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
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

    public static class UpsertAppointmentRequest {
        @NotBlank public String clientName;
        @NotNull public Long serviceId;

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        public LocalDate date;       // "2025-12-30"

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        public LocalTime startTime;  // "10:00"

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        public LocalTime endTime;    // "10:30"
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentEntity create(@Valid @RequestBody UpsertAppointmentRequest req) {
        return appointmentService.create(
                req.clientName,
                req.serviceId,
                req.date,
                req.startTime,
                req.endTime
        );
    }

    // GET /api/appointments  OR  GET /api/appointments?date=2025-12-30
    @GetMapping
    public List<AppointmentEntity> list(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        if (date != null) {
            return appointmentService.findByDate(date);
        }
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public AppointmentEntity get(@PathVariable Long id) {
        return appointmentService.findById(id);
    }

    @PutMapping("/{id}")
    public AppointmentEntity update(@PathVariable Long id, @Valid @RequestBody UpsertAppointmentRequest req) {
        return appointmentService.update(
                id,
                req.clientName,
                req.serviceId,
                req.date,
                req.startTime,
                req.endTime
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }
}
