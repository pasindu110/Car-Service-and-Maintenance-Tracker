package com.cartracker.repository.servicerecord;

import com.cartracker.model.servicerecord.MaintenanceTask;
import com.cartracker.model.servicerecord.ServiceRecord;

import java.util.List;
import java.util.Optional;

/**
 * ServiceRecordRepository – data access interface.
 *
 * Team member: assign to the Service and Maintenance module owner.
 */
public interface ServiceRecordRepository {

    ServiceRecord          save(ServiceRecord record);
    Optional<ServiceRecord> findById(String id);
    List<ServiceRecord>    findAll();
    ServiceRecord          update(ServiceRecord record);
    boolean                deleteById(String id);

    // MaintenanceTask sub-operations
    MaintenanceTask        saveTask(MaintenanceTask task);
    Optional<MaintenanceTask> findTaskById(String taskId);
    List<MaintenanceTask>  findTasksByRecord(String serviceRecordId);
}
