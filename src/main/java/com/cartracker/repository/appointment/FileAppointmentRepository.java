package com.cartracker.repository.appointment;

import com.cartracker.model.appointment.Appointment;
import com.cartracker.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FileAppointmentRepository – flat-file implementation.
 * Reads and writes appointments.txt.
 *
 * Team member: assign to the Appointment and Scheduling module owner.
 */
public class FileAppointmentRepository implements AppointmentRepository {

    private static final String         FILE_NAME = "appointments.txt";
    private final        List<Appointment> cache  = new ArrayList<>();

    public FileAppointmentRepository() { /* TODO: loadFromFile() */ }

    private void persistToFile() {
        List<String> lines = new ArrayList<>();
        cache.forEach(a -> lines.add(a.toFileString()));
        FileUtil.writeAllLines(FILE_NAME, lines);
    }

    @Override public Appointment          save(Appointment a)       { cache.add(a); FileUtil.appendLine(FILE_NAME, a.toFileString()); return a; }
    @Override public Optional<Appointment> findById(String id)     { return cache.stream().filter(a -> a.getId().equals(id)).findFirst(); }
    @Override public List<Appointment>    findAll()                 { return List.copyOf(cache); }
    @Override public Appointment          update(Appointment a)     { deleteById(a.getId()); cache.add(a); persistToFile(); return a; }
    @Override public boolean              deleteById(String id)     { boolean r = cache.removeIf(a -> a.getId().equals(id)); if(r) persistToFile(); return r; }
}
