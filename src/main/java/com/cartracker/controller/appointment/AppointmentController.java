package com.cartracker.controller.appointment;

import com.cartracker.service.appointment.AppointmentService;

/**
 * AppointmentController – handles user-facing interactions for Appointment Scheduling.
 *
 * Team member: assign to the Appointment and Scheduling module owner.
 */
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void showMenu()             { /* TODO */ }
    public void bookAppointment()      { /* TODO: Customer selects vehicle, date, service type */ }
    public void viewAppointment()      { /* TODO: Show appointment details */ }
    public void confirmAppointment()   { /* TODO: Admin/Mechanic assigns mechanic, confirms */ }
    public void cancelAppointment()    { /* TODO: Customer or admin cancels */ }
    public void listMyAppointments()   { /* TODO: Show customer's upcoming appointments */ }
    public void viewSchedule()         { /* TODO: Show all appointments for a given date */ }
}
