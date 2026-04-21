package com.cartracker.model.servicerecord;

import com.cartracker.model.common.BaseEntity;

/**
 * MaintenanceTask – a single repair or maintenance item within a ServiceRecord.
 *
 * Examples: "Oil Change", "Tyre Rotation", "Brake Pad Replacement"
 *
 * Fields:
 *   - serviceRecordId : parent service record
 *   - taskName        : short name   (e.g. "Oil Change")
 *   - taskDetails     : longer description / steps performed
 *   - partsCost       : cost of parts used
 *   - labourCost      : cost of labour
 *   - completed       : whether this specific task is done
 *
 * Team member: assign to the team member responsible for Service and Maintenance module.
 */
public class MaintenanceTask extends BaseEntity {

    private String  serviceRecordId;
    private String  taskName;
    private String  taskDetails;
    private double  partsCost;
    private double  labourCost;
    private boolean completed;

    // ── Constructors ──────────────────────────────────────────────────────────

    public MaintenanceTask() {
        super();
        this.completed = false;
    }

    public MaintenanceTask(String id, String serviceRecordId,
                           String taskName, String taskDetails,
                           double partsCost, double labourCost) {
        super(id);
        this.serviceRecordId = serviceRecordId;
        this.taskName        = taskName;
        this.taskDetails     = taskDetails;
        this.partsCost       = partsCost;
        this.labourCost      = labourCost;
        this.completed       = false;
    }

    // ── Computed helper ───────────────────────────────────────────────────────

    /** Returns total cost of this task (parts + labour). */
    public double getTotalCost() {
        return partsCost + labourCost;
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        // Format: id|serviceRecordId|taskName|taskDetails|partsCost|labourCost|completed|createdAt
        return String.join("|", id, serviceRecordId, taskName, taskDetails,
                String.valueOf(partsCost), String.valueOf(labourCost),
                String.valueOf(completed), createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String  getServiceRecordId()                          { return serviceRecordId; }
    public void    setServiceRecordId(String serviceRecordId)    { this.serviceRecordId = serviceRecordId; }

    public String  getTaskName()                                 { return taskName; }
    public void    setTaskName(String taskName)                   { this.taskName = taskName; }

    public String  getTaskDetails()                              { return taskDetails; }
    public void    setTaskDetails(String taskDetails)            { this.taskDetails = taskDetails; }

    public double  getPartsCost()                                { return partsCost; }
    public void    setPartsCost(double partsCost)                { this.partsCost = partsCost; }

    public double  getLabourCost()                               { return labourCost; }
    public void    setLabourCost(double labourCost)              { this.labourCost = labourCost; }

    public boolean isCompleted()                                 { return completed; }
    public void    setCompleted(boolean completed)               { this.completed = completed; }

    @Override
    public String toString() {
        return "MaintenanceTask{id='" + id + "', taskName='" + taskName +
               "', total=" + getTotalCost() + ", completed=" + completed + '}';
    }
}
