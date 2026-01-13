package com.masa.appointment.appointment.controller;

import com.masa.appointment.Notifications.NotificationService;
import com.masa.appointment.appointment.entity.AppointmentEntity;
import com.masa.appointment.appointment.entity.AppointmentStatus;
import com.masa.appointment.appointment.service.AppointmentService;
import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.user.repo.UserRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
private final NotificationService notificationService;


  public AppointmentController(AppointmentService appointmentService,
                             UserRepository userRepository,
                             NotificationService notificationService) {
    this.appointmentService = appointmentService;
    this.userRepository = userRepository;
    this.notificationService = notificationService;
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

  /*   @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentEntity create(@RequestBody AppointmentRequest req) {
        return appointmentService.create(req.clientName, req.serviceId,  req.date, req.startTime, req.endTime);
    }*/
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public AppointmentEntity create(@RequestBody AppointmentRequest req, Authentication auth) {

    String customerEmail = auth.getName(); // ← الإيميل من الـ JWT
   UserEntity customer = userRepository.findByEmail(customerEmail) 
       .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

     AppointmentEntity saved = appointmentService.create(
            req.clientName,
            req.serviceId,
            req.date,
            req.startTime,
            req.endTime,
            customer
    );

    notificationService.notifyAdmin("New appointment created by " + customerEmail);
    notificationService.notifyCustomer(customerEmail, "Your appointment has been created");

    return saved;
}

    @GetMapping
    public List<AppointmentEntity> list() {
        return appointmentService.findAll();
    }

@PreAuthorize("hasRole('STAFF')")
@GetMapping("/assigned")
public List<AppointmentEntity> getAssignedAppointments(Authentication auth) {
    String email = auth.getName();

    UserEntity staff = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));

    return appointmentService.findByStaff(staff);
}


    @GetMapping("/{id}")
    public AppointmentEntity get(@PathVariable Long id) {
        return appointmentService.findById(id);
    }

    @PutMapping("/{id}")
    public AppointmentEntity update(@PathVariable Long id, @RequestBody AppointmentRequest req) {
        return appointmentService.update(id, req.clientName, req.serviceId, req.date, req.startTime, req.endTime);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public AppointmentEntity changeStatus(@PathVariable Long id, @RequestBody ChangeStatusRequest req) {
        return appointmentService.changeStatus(id, req.status);
    }

@PreAuthorize("hasRole('ADMIN')")
@PatchMapping("/{id}/assign/{staffId}")
public AppointmentEntity assignStaff(@PathVariable Long id, @PathVariable Long staffId) {
    return appointmentService.assignStaff(id, staffId);
}


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }
}
