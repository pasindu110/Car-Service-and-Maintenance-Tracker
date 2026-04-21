package com.cartracker.model.servicerecord;

import com.cartracker.model.common.BaseEntity;
import com.cartracker.model.common.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceRecord – tracks a single service visit for a vehicle.
 *
 * A ServiceRecord can contain multiple MaintenanceTasks (composition).
 *
 * Fields:
 *   - vehicleId      : which vehicle was serviced
 *   - mechanicId     : which mechanic performed the service
 *   - serviceDate    : date service was performed
 *   - description    : brief summary of the service
 *   - status         : current status of the service
 *   - taskIds        : list of associated MaintenanceTask IDs
 *   - totalCost      : computed or manually entered total cost
 *
 * Team member: assign to the team member responsible for Service and Maintenance module.
 */
public class ServiceRecord extends BaseEntity {

    private String        vehicleId;
    private String        mechanicId;
    private LocalDate     serviceDate;
    private String        description;
    private Status        status;
    private List<String>  taskIds;    // IDs of MaintenanceTasks in this record
    private double        totalCost;

    // ── Constructors ──────────────────────────────────────────────────────────

    public ServiceRecord() {
        super();
        this.status  = Status.PENDING;
        this.taskIds = new ArrayList<>();
    }

    public ServiceRecord(String id, String vehicleId, String mechanicId,
                         LocalDate serviceDate, String description) {
        super(id);
        this.vehicleId   = vehicleId;
        this.mechanicId  = mechanicId;
        this.serviceDate = serviceDate;
        this.description = description;
        this.status      = Status.PENDING;
        this.taskIds     = new ArrayList<>();
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        // Format: id|vehicleId|mechanicId|serviceDate|description|status|taskIds|totalCost|createdAt
        return String.join("|", id, vehicleId, mechanicId,
                serviceDate.toString(), description, status.name(),
                String.join(",", taskIds),
                String.valueOf(totalCost), createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String       getVehicleId()                    { return vehicleId; }
    public void         setVehicleId(String vehicleId)    { this.vehicleId = vehicleId; }

    public String       getMechanicId()                   { return mechanicId; }
    public void         setMechanicId(String mechanicId)  { this.mechanicId = mechanicId; }

    public LocalDate    getServiceDate()                  { return serviceDate; }
    public void         setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public String       getDescription()                  { return description; }
    public void         setDescription(String description) { this.description = description; }

    public Status       getStatus()                       { return status; }
    public void         setStatus(Status status)          { this.status = status; }

    public List<String> getTaskIds()                      { return taskIds; }
    public void         setTaskIds(List<String> taskIds)  { this.taskIds = taskIds; }
    public void         addTaskId(String taskId)          { taskIds.add(taskId); }

    public double       getTotalCost()                    { return totalCost; }
    public void         setTotalCost(double totalCost)    { this.totalCost = totalCost; }

    @Override
    public String toString() {
        return "ServiceRecord{id='" + id + "', vehicle='" + vehicleId +
               "', date=" + serviceDate + ", status=" + status + '}';
    }
}
