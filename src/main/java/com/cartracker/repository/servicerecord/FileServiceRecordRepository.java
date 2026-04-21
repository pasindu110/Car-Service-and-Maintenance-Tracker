package com.cartracker.repository.servicerecord;

import com.cartracker.model.servicerecord.MaintenanceTask;
import com.cartracker.model.servicerecord.ServiceRecord;
import com.cartracker.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FileServiceRecordRepository – flat-file implementation.
 * Uses services.txt for ServiceRecords.
 * (MaintenanceTasks can be stored in the same file or a separate tasks.txt)
 *
 * Team member: assign to the Service and Maintenance module owner.
 */
public class FileServiceRecordRepository implements ServiceRecordRepository {

    private static final String REC_FILE  = "services.txt";
    private final List<ServiceRecord>   recordCache = new ArrayList<>();
    private final List<MaintenanceTask> taskCache   = new ArrayList<>();

    public FileServiceRecordRepository() {
        // TODO: loadFromFile()
    }

    private void loadFromFile() { /* TODO */ }

    private void persistToFile() {
        List<String> lines = new ArrayList<>();
        recordCache.forEach(r -> lines.add(r.toFileString()));
        FileUtil.writeAllLines(REC_FILE, lines);
    }

    @Override public ServiceRecord           save(ServiceRecord r)               { recordCache.add(r); FileUtil.appendLine(REC_FILE, r.toFileString()); return r; }
    @Override public Optional<ServiceRecord> findById(String id)                { return recordCache.stream().filter(r -> r.getId().equals(id)).findFirst(); }
    @Override public List<ServiceRecord>     findAll()                          { return List.copyOf(recordCache); }
    @Override public ServiceRecord           update(ServiceRecord r)             { deleteById(r.getId()); recordCache.add(r); persistToFile(); return r; }
    @Override public boolean                 deleteById(String id)              { boolean res = recordCache.removeIf(r -> r.getId().equals(id)); if(res) persistToFile(); return res; }

    @Override public MaintenanceTask        saveTask(MaintenanceTask t)          { taskCache.add(t); return t; }
    @Override public Optional<MaintenanceTask> findTaskById(String id)          { return taskCache.stream().filter(t -> t.getId().equals(id)).findFirst(); }
    @Override public List<MaintenanceTask>  findTasksByRecord(String recId)     { return taskCache.stream().filter(t -> t.getServiceRecordId().equals(recId)).toList(); }
}
