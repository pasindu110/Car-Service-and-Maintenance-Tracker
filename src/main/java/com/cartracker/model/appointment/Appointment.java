package com.cartracker.model.appointment;

import com.cartracker.model.common.BaseEntity;
import com.cartracker.model.common.Status;

import java.time.LocalDateTime;

/**
 * Appointment – a booking made by a Customer for a vehicle service.
 *
 * Lifecycle: PENDING → CONFIRMED → IN_PROGRESS → COMPLETED | CANCELLED
 *
 * Fields:
 *   - customerId       : who booked the appointment
 *   - vehicleId        : which vehicle is being serviced
 *   - mechanicId       : assigned mechanic (may be null until confirmed)
 *   - scheduledAt      : date and time of the appointment
 *   - serviceType      : brief label  (e.g. "Oil Change", "Full Service")
 *   - notes            : any extra customer notes
 *   - status           : current lifecycle status
 *   - serviceRecordId  : linked service record (set once service starts)
 *
 * Team member: assign to the team member responsible for Appointment module.
 */
public class Appointment extends BaseEntity {

    private String        customerId;
    private String        vehicleId;
    private String        mechanicId;       // optional until confirmed
    private LocalDateTime scheduledAt;
    private String        serviceType;
    private String        notes;
    private Status        status;
    private String        serviceRecordId;  // set when service begins

    // ── Constructors ──────────────────────────────────────────────────────────

    public Appointment() {
        super();
        this.status = Status.PENDING;
    }

    public Appointment(String id, String customerId, String vehicleId,
                       LocalDateTime scheduledAt, String serviceType, String notes) {
        super(id);
        this.customerId  = customerId;
        this.vehicleId   = vehicleId;
        this.scheduledAt = scheduledAt;
        this.serviceType = serviceType;
        this.notes       = notes;
        this.status      = Status.PENDING;
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        // Format: id|customerId|vehicleId|mechanicId|scheduledAt|serviceType|notes|status|serviceRecordId|createdAt
        return String.join("|", id, customerId, vehicleId,
                (mechanicId != null ? mechanicId : ""),
                scheduledAt.toString(), serviceType, notes,
                status.name(),
                (serviceRecordId != null ? serviceRecordId : ""),
                createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String        getCustomerId()                          { return customerId; }
    public void          setCustomerId(String customerId)         { this.customerId = customerId; }

    public String        getVehicleId()                           { return vehicleId; }
    public void          setVehicleId(String vehicleId)           { this.vehicleId = vehicleId; }

    public String        getMechanicId()                          { return mechanicId; }
    public void          setMechanicId(String mechanicId)         { this.mechanicId = mechanicId; }

    public LocalDateTime getScheduledAt()                         { return scheduledAt; }
    public void          setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public String        getServiceType()                         { return serviceType; }
    public void          setServiceType(String serviceType)       { this.serviceType = serviceType; }

    public String        getNotes()                               { return notes; }
    public void          setNotes(String notes)                   { this.notes = notes; }

    public Status        getStatus()                              { return status; }
    public void          setStatus(Status status)                 { this.status = status; }

    public String        getServiceRecordId()                             { return serviceRecordId; }
    public void          setServiceRecordId(String serviceRecordId)       { this.serviceRecordId = serviceRecordId; }

    @Override
    public String toString() {
        return "Appointment{id='" + id + "', customer='" + customerId +
               "', scheduledAt=" + scheduledAt + ", status=" + status + '}';
    }
}
