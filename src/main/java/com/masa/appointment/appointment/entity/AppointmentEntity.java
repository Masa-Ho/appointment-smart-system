package com.masa.appointment.appointment.entity;

import com.masa.appointment.service_catalog.entity.ServiceEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import com.masa.appointment.user.entity.UserEntity;


@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientName;


    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "staff_id")
private com.masa.appointment.user.entity.UserEntity staff;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "customer_id", nullable = false)
private UserEntity customer;


    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public ServiceEntity getService() { return service; }
    public void setService(ServiceEntity service) { this.service = service; }

    public AppointmentStatus getStatus() {return status;}

    public void setStatus(AppointmentStatus status) {this.status = status;}
public UserEntity getStaff() {
    return staff;
}

public void setStaff(UserEntity staff) {
    this.staff = staff;
}

public UserEntity getCustomer() {
    return customer;
}

public void setCustomer(UserEntity customer) {
    this.customer = customer;
}

}
