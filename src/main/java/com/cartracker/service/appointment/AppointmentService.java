package com.cartracker.service.appointment;

import com.cartracker.model.appointment.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * AppointmentService – service interface for the Appointment and Scheduling module.
 */
public interface AppointmentService {

    Appointment          book(Appointment appointment);
    Optional<Appointment> findById(String id);
    List<Appointment>    findAll();
    List<Appointment>    findByCustomer(String customerId);
    List<Appointment>    findByMechanic(String mechanicId);
    List<Appointment>    findByDate(LocalDateTime date);
    Appointment          confirm(String appointmentId, String mechanicId);
    Appointment          cancel(String appointmentId);
    Appointment          complete(String appointmentId, String serviceRecordId);
}
