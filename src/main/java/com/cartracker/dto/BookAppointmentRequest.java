package com.cartracker.dto;

/**
 * BookAppointmentRequest – DTO for booking an appointment.
 *
 * Team member: used by AppointmentController when collecting booking input.
 */
public class BookAppointmentRequest {

    private String customerId;
    private String vehicleId;
    private String scheduledAt;  // raw string, parsed in controller/service
    private String serviceType;
    private String notes;

    public BookAppointmentRequest() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getCustomerId()                      { return customerId; }
    public void   setCustomerId(String customerId)     { this.customerId = customerId; }

    public String getVehicleId()                       { return vehicleId; }
    public void   setVehicleId(String vehicleId)       { this.vehicleId = vehicleId; }

    public String getScheduledAt()                     { return scheduledAt; }
    public void   setScheduledAt(String scheduledAt)   { this.scheduledAt = scheduledAt; }

    public String getServiceType()                     { return serviceType; }
    public void   setServiceType(String serviceType)   { this.serviceType = serviceType; }

    public String getNotes()                           { return notes; }
    public void   setNotes(String notes)               { this.notes = notes; }
}
