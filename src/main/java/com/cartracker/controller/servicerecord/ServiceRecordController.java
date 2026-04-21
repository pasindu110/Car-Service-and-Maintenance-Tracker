package com.cartracker.controller.servicerecord;

import com.cartracker.service.servicerecord.ServiceRecordService;

/**
 * ServiceRecordController – handles user-facing interactions for Service and Maintenance.
 *
 * Team member: assign to the Service and Maintenance module owner.
 */
public class ServiceRecordController {

    private final ServiceRecordService serviceRecordService;

    public ServiceRecordController(ServiceRecordService serviceRecordService) {
        this.serviceRecordService = serviceRecordService;
    }

    public void showMenu()            { /* TODO */ }
    public void createRecord()        { /* TODO: Start a new service record for a vehicle */ }
    public void addTaskToRecord()     { /* TODO: Add a MaintenanceTask to an existing record */ }
    public void viewRecord()          { /* TODO: View a service record and its tasks */ }
    public void updateRecordStatus()  { /* TODO: Mechanic updates status (IN_PROGRESS → COMPLETED) */ }
    public void viewServiceHistory()  { /* TODO: Show all records for a given vehicle */ }
}
