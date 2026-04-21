package com.cartracker.service.servicerecord;

import com.cartracker.model.servicerecord.MaintenanceTask;
import com.cartracker.model.servicerecord.ServiceRecord;

import java.util.List;
import java.util.Optional;

/**
 * ServiceRecordService – service interface for the Service and Maintenance module.
 */
public interface ServiceRecordService {

    ServiceRecord          createRecord(ServiceRecord record);
    Optional<ServiceRecord> findById(String id);
    List<ServiceRecord>    findByVehicle(String vehicleId);
    List<ServiceRecord>    findByMechanic(String mechanicId);
    ServiceRecord          updateStatus(String recordId, String newStatus);
    MaintenanceTask        addTask(String recordId, MaintenanceTask task);
    List<MaintenanceTask>  getTasksForRecord(String recordId);
    boolean                deleteRecord(String recordId);
}
