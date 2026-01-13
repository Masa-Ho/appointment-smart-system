package com.masa.appointment.appointment.repo;

import com.masa.appointment.appointment.entity.AppointmentEntity;
import com.masa.appointment.appointment.entity.AppointmentStatus;
import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.user.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByDate(LocalDate date);
    List<AppointmentEntity> findByStaff(UserEntity staff);

    // create overlap check (ignore CANCELLED)
    @Query("""
        SELECT COUNT(a) > 0
        FROM AppointmentEntity a
        WHERE a.date = :date
          AND a.status <> :cancelled
          AND a.startTime < :newEnd
          AND a.endTime > :newStart
    """)
    boolean existsOverlap(
            @Param("date") LocalDate date,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd,
            @Param("cancelled") AppointmentStatus cancelled
    );

    // update overlap check (exclude same appointment id) (ignore CANCELLED)
    @Query("""
        SELECT COUNT(a) > 0
        FROM AppointmentEntity a
        WHERE a.date = :date
          AND a.id <> :excludeId
          AND a.status <> :cancelled
          AND a.startTime < :newEnd
          AND a.endTime > :newStart
    """)
    boolean existsOverlapExcludingId(
            @Param("date") LocalDate date,
            @Param("excludeId") Long excludeId,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd,
            @Param("cancelled") AppointmentStatus cancelled
    );
}
