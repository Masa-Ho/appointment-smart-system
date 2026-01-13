package com.masa.appointment.Notifications;

import com.masa.appointment.appointment.entity.AppointmentEntity;
import com.masa.appointment.appointment.entity.AppointmentStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyAdmin(String message) {
        messagingTemplate.convertAndSend("/topic/admin", message);
    }

 public void notifyCustomer(String customerEmail, String message) {
    String safeEmail = customerEmail.replace(".", "_").replace("@", "_");
    messagingTemplate.convertAndSend("/topic/customer/" + safeEmail, message);
}


    

    public void notifyStaff(Long staffId, String message) {
        messagingTemplate.convertAndSend("/topic/staff/" + staffId, message);
    }

    public void notifyAppointmentChanged(AppointmentEntity appointment, String extraMessage) {

        AppointmentStatus status = appointment.getStatus();

        String messageText = buildMessageText(appointment, extraMessage);

        NotificationPayload payload = new NotificationPayload(
                appointment.getId(),
                appointment.getClientName(),
                status,
                messageText
        );

        notifyAdmin(messageText);

        if (appointment.getStaff() != null) {
            notifyStaff(appointment.getStaff().getId(), messageText);
        }
    }

    private String buildMessageText(AppointmentEntity appointment, String extraMessage) {
        String base = "Appointment #" + appointment.getId()
                + " for " + appointment.getClientName()
                + " is now " + appointment.getStatus();

        if (extraMessage != null && !extraMessage.isBlank()) {
            return base + " - " + extraMessage;
        }
        return base;
    }
}
