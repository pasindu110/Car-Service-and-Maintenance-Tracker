package com.cartracker.service.appointment;

import com.cartracker.model.appointment.Appointment;
import com.cartracker.model.common.Status;
import com.cartracker.repository.appointment.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * AppointmentServiceImpl – placeholder implementation.
 *
 * Team member: assign to the Appointment and Scheduling module owner.
 */
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment book(Appointment appointment) {
        // TODO: Check mechanic availability at scheduledAt time
        // TODO: Generate ID, set status = PENDING, save
        return appointmentRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> findById(String id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> findByCustomer(String customerId) {
        return List.of(); // TODO
    }

    @Override
    public List<Appointment> findByMechanic(String mechanicId) {
        return List.of(); // TODO
    }

    @Override
    public List<Appointment> findByDate(LocalDateTime date) {
        return List.of(); // TODO
    }

    @Override
    public Appointment confirm(String appointmentId, String mechanicId) {
        // TODO: Find appointment, assign mechanic, set status = CONFIRMED, save
        return null;
    }

    @Override
    public Appointment cancel(String appointmentId) {
        // TODO: Find appointment, set status = CANCELLED, save
        return null;
    }

    @Override
    public Appointment complete(String appointmentId, String serviceRecordId) {
        // TODO: Find appointment, set serviceRecordId, set status = COMPLETED, save
        return null;
    }
}
