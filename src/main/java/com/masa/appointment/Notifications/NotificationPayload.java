package com.masa.appointment.Notifications;

import com.masa.appointment.appointment.entity.AppointmentStatus;

import java.time.LocalDateTime;

public class NotificationPayload {

    private Long appointmentId;
    private String clientName;
    private AppointmentStatus status;
    private String message;
    private LocalDateTime createdAt;

    public NotificationPayload(Long appointmentId, String clientName,
                               AppointmentStatus status, String message) {
        this.appointmentId = appointmentId;
        this.clientName = clientName;
        this.status = status;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getClientName() {
        return clientName;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
