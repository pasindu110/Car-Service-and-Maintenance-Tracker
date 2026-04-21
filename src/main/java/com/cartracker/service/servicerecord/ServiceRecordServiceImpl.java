package com.cartracker.service.servicerecord;

import com.cartracker.model.servicerecord.MaintenanceTask;
import com.cartracker.model.servicerecord.ServiceRecord;
import com.cartracker.repository.servicerecord.ServiceRecordRepository;

import java.util.List;
import java.util.Optional;

/**
 * ServiceRecordServiceImpl – placeholder implementation.
 *
 * Team member: assign to the Service and Maintenance module owner.
 */
public class ServiceRecordServiceImpl implements ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;

    public ServiceRecordServiceImpl(ServiceRecordRepository serviceRecordRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
    }

    @Override
    public ServiceRecord createRecord(ServiceRecord record) {
        // TODO: Generate ID, validate vehicleId and mechanicId exist, save
        return serviceRecordRepository.save(record);
    }

    @Override
    public Optional<ServiceRecord> findById(String id) {
        // TODO: Delegate to repository
        return Optional.empty();
    }

    @Override
    public List<ServiceRecord> findByVehicle(String vehicleId) {
        // TODO: Filter records by vehicleId
        return List.of();
    }

    @Override
    public List<ServiceRecord> findByMechanic(String mechanicId) {
        // TODO: Filter records by mechanicId
        return List.of();
    }

    @Override
    public ServiceRecord updateStatus(String recordId, String newStatus) {
        // TODO: Find record, parse newStatus to Status enum, save
        return null;
    }

    @Override
    public MaintenanceTask addTask(String recordId, MaintenanceTask task) {
        // TODO: Find record, generate task ID, add task to record's taskIds list, save both
        return null;
    }

    @Override
    public List<MaintenanceTask> getTasksForRecord(String recordId) {
        // TODO: Load maintenance tasks by serviceRecordId
        return List.of();
    }

    @Override
    public boolean deleteRecord(String recordId) {
        return serviceRecordRepository.deleteById(recordId);
    }
}
