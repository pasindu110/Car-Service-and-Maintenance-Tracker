package com.cartracker.repository.user;

import com.cartracker.model.user.User;
import com.cartracker.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FileUserRepository – flat-file implementation of UserRepository.
 *
 * Reads and writes users.txt in src/main/resources/data/.
 *
 * TEAM NOTES:
 *   - Each line in users.txt is one user record (pipe-delimited via toFileString()).
 *   - Implement fromFileLine() to parse a line back into a User object.
 *   - Replace this class with a JdbcUserRepository when switching to a DB.
 *
 * Team member: assign to the User Management module owner.
 */
public class FileUserRepository implements UserRepository {

    private static final String FILE_NAME = "users.txt";

    // In-memory store – populated once at startup from users.txt
    private final List<User> cache = new ArrayList<>();

    public FileUserRepository() {
        // TODO: Call loadFromFile() here to populate cache at startup
    }

    // ── Load / Save ───────────────────────────────────────────────────────────

    private void loadFromFile() {
        // TODO: Read all lines with FileUtil.readAllLines(FILE_NAME)
        // TODO: Parse each line into the appropriate User subclass (Admin/Customer/Mechanic)
        // TODO: Add parsed objects to cache
    }

    private void persistToFile() {
        List<String> lines = new ArrayList<>();
        for (User u : cache) {
            lines.add(u.toFileString());
        }
        FileUtil.writeAllLines(FILE_NAME, lines);
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @Override
    public User save(User user) {
        cache.add(user);
        FileUtil.appendLine(FILE_NAME, user.toFileString());
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return cache.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return cache.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(cache);
    }

    @Override
    public User update(User updated) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(updated.getId())) {
                cache.set(i, updated);
                persistToFile();
                return updated;
            }
        }
        return null; // not found
    }

    @Override
    public boolean deleteById(String id) {
        boolean removed = cache.removeIf(u -> u.getId().equals(id));
        if (removed) persistToFile();
        return removed;
    }
}
