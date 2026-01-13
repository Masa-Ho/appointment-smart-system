package com.masa.appointment.appointment.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.masa.appointment.Notifications.NotificationService;
import com.masa.appointment.appointment.entity.AppointmentEntity;
import com.masa.appointment.appointment.entity.AppointmentStatus;
import com.masa.appointment.appointment.repo.AppointmentRepository;
import com.masa.appointment.service_catalog.entity.ServiceEntity;
import com.masa.appointment.service_catalog.repo.ServiceRepository;
import com.masa.appointment.user.entity.Role;
import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.user.repo.UserRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
private final NotificationService notificationService;
private final UserRepository userRepository;


    public AppointmentService(AppointmentRepository appointmentRepository, 
        ServiceRepository serviceRepository,
         NotificationService notificationService,
           UserRepository userRepository) {
        

        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    public AppointmentEntity create(String clientName, Long serviceId, LocalDate date, LocalTime startTime, LocalTime endTime,UserEntity customer) {

        if (clientName == null || clientName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "clientName is required");
        }
        if (serviceId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "serviceId is required");
        }
        if (date == null || startTime == null || endTime == null) {
            throw new ResponseStatusException(BAD_REQUEST, "date/startTime/endTime are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(BAD_REQUEST, "endTime must be after startTime");
        }

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Service not found with id: " + serviceId));

        boolean overlap = appointmentRepository.existsOverlap(date, startTime, endTime, AppointmentStatus.CANCELLED);
        if (overlap) {
            throw new ResponseStatusException(BAD_REQUEST, "Appointment overlaps with an existing appointment");
        }



        AppointmentEntity a = new AppointmentEntity();
        a.setClientName(clientName);
        a.setDate(date);
        a.setStartTime(startTime);
        a.setEndTime(endTime);
        a.setService(service);
        a.setStatus(AppointmentStatus.PENDING);
      a.setCustomer(customer);

AppointmentEntity saved = appointmentRepository.save(a);
// WebSocket notification
notificationService.notifyAppointmentChanged(saved,
    "New appointment created and waiting for confirmation"
 );


       return saved;
    }

    public List<AppointmentEntity> findAll() {
        return appointmentRepository.findAll();
    }
   public List<AppointmentEntity> findByStaff(UserEntity staff) {
       return appointmentRepository.findByStaff(staff);
    }

    public AppointmentEntity findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Appointment not found with id: " + id));
    }

    public AppointmentEntity update(Long id, String clientName, Long serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        AppointmentEntity existing = findById(id);

        if (clientName == null || clientName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "clientName is required");
        }
        if (serviceId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "serviceId is required");
        }
        if (date == null || startTime == null || endTime == null) {
            throw new ResponseStatusException(BAD_REQUEST, "date/startTime/endTime are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(BAD_REQUEST, "endTime must be after startTime");
        }

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Service not found with id: " + serviceId));

        boolean overlap = appointmentRepository.existsOverlapExcludingId(date, id, startTime, endTime, AppointmentStatus.CANCELLED);
        if (overlap) {
            throw new ResponseStatusException(BAD_REQUEST, "Appointment overlaps with an existing appointment");
        }

        existing.setClientName(clientName);
        existing.setDate(date);
        existing.setStartTime(startTime);
        existing.setEndTime(endTime);
        existing.setService(service);
AppointmentEntity saved = appointmentRepository.save(existing);
notificationService.notifyAppointmentChanged(
    saved, 
    "Appointment details have been updated"
);
return saved;
    }

      public AppointmentEntity changeStatus(Long id, AppointmentStatus status) {
        AppointmentEntity existing = findById(id);
        if (status == null) {
            throw new ResponseStatusException(BAD_REQUEST, "status is required");
        }
        existing.setStatus(status);
        AppointmentEntity saved = appointmentRepository.save(existing);

        String extraMsg = switch (status) {
            case CONFIRMED -> "Appointment has been confirmed by admin";
            case CANCELLED -> "Appointment has been cancelled";
            case PENDING -> "Appointment moved back to pending status";
        };
// WebSocket notification
        notificationService.notifyAppointmentChanged(saved, extraMsg);

        return saved;
    }


    public void delete(Long id) {
        AppointmentEntity existing = findById(id);
        appointmentRepository.delete(existing);
    }

   public AppointmentEntity assignStaff(Long appointmentId, Long staffId) {
    AppointmentEntity appointment = findById(appointmentId);

    UserEntity staff = userRepository.findById(staffId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Staff not found with id: " + staffId));

    if (staff.getRole() != Role.STAFF) {
        throw new ResponseStatusException(BAD_REQUEST, "User is not a staff member");
    }

    appointment.setStaff(staff);
    AppointmentEntity saved = appointmentRepository.save(appointment);

    // WebSocket notification
    notificationService.notifyAppointmentChanged(saved, "Staff assigned to appointment");

   

    return saved;
}





}
