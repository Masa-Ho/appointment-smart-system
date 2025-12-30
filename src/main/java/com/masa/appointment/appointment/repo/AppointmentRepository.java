package com.masa.appointment.appointment.repo;

import com.masa.appointment.appointment.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    @Query("""
        SELECT COUNT(a) > 0
        FROM AppointmentEntity a
        WHERE a.service.id = :serviceId
          AND a.date = :date
          AND :newStart < a.endTime
          AND :newEnd   > a.startTime
    """)
    boolean existsOverlap(
            @Param("serviceId") Long serviceId,
            @Param("date") LocalDate date,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd
    );
}
