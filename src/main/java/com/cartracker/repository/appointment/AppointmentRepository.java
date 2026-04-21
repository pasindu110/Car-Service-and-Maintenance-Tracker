package com.cartracker.repository.appointment;

import com.cartracker.model.appointment.Appointment;

import java.util.List;
import java.util.Optional;

/**
 * AppointmentRepository – data access interface for Appointment entities.
 *
 * Team member: assign to the Appointment and Scheduling module owner.
 */
public interface AppointmentRepository {

    Appointment          save(Appointment appointment);
    Optional<Appointment> findById(String id);
    List<Appointment>    findAll();
    Appointment          update(Appointment appointment);
    boolean              deleteById(String id);
}
