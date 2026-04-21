# 🚗 Car Service and Maintenance Tracker

> A Java OOP student team project for tracking vehicle services, maintenance, appointments, and billing.

---

## 📋 Project Overview

| Item            | Detail                          |
|-----------------|---------------------------------|
| Language        | Java 17                         |
| Build Tool      | Maven                           |
| Storage         | Flat-file (txt) → upgradeable to JDBC |
| Architecture    | Layered (Controller → Service → Repository → Model) |
| IDE             | IntelliJ IDEA                   |

---

## 🏗️ Architecture

```
MainApplication
    └── Controller (user input / menu)
            └── Service (business logic)
                    └── Repository (data access)
                            └── Model (domain entities)
```

**OOP pillars used:**
- **Encapsulation** – all fields are private; access via getters/setters
- **Inheritance** – `BaseEntity` → `User` → `Admin / Customer / Mechanic`
- **Polymorphism** – `getPermissionsDescription()` differs per user type; `toFileString()` defined per entity
- **Abstraction** – `UserService`, `VehicleService`, `*Repository` are interfaces

---

## 🗂️ Module → Team Member Assignment

| Module                     | Key Classes                                      | Assign to     |
|----------------------------|--------------------------------------------------|---------------|
| User Management            | `User`, `Admin`, `Customer`, `Mechanic`, `UserController`, `UserServiceImpl`, `FileUserRepository` | Member 1 |
| Vehicle Management         | `Vehicle`, `VehicleController`, `VehicleServiceImpl`, `FileVehicleRepository` | Member 2 |
| Service & Maintenance      | `ServiceRecord`, `MaintenanceTask`, `ServiceRecordController` | Member 3 |
| Appointment & Scheduling   | `Appointment`, `AppointmentController`, `AppointmentServiceImpl` | Member 4 |
| Billing & Reports          | `Invoice`, `Payment`, `BillingController`, `BillingServiceImpl` | Member 5 |

---

## 🚀 How to Run

1. Open IntelliJ IDEA → **File → Open** → select this `OOP` folder
2. Ensure Maven auto-import is enabled (**pom.xml** detected)
3. Run `MainApplication.java` (right-click → Run)

```bash
# or from terminal
mvn compile exec:java -Dexec.mainClass="com.cartracker.MainApplication"
```

---

## 📁 Key Files

| File | Purpose |
|------|---------|
| `src/main/java/com/cartracker/MainApplication.java` | Entry point |
| `src/main/java/com/cartracker/model/common/BaseEntity.java` | Root of entity hierarchy |
| `src/main/java/com/cartracker/util/FileUtil.java` | All file read/write operations |
| `src/main/java/com/cartracker/util/IdGenerator.java` | ID generation (UUID or sequential) |
| `src/main/java/com/cartracker/config/AppConfig.java` | App-wide constants |
| `src/main/resources/data/*.txt` | Flat-file data storage |
| `src/main/resources/application.properties` | Configuration values |

---

## 🌿 Git Branching Strategy

```
main          ← stable, reviewed code only
dev           ← integration branch
feature/user-management
feature/vehicle-management
feature/service-records
feature/appointments
feature/billing
```

**Rule:** Never push directly to `main`. Open a Pull Request from your feature branch.

---

## 📦 Adding Dependencies

Edit `pom.xml` and add a `<dependency>` block inside `<dependencies>`.
Run **Maven → Reload Project** in IntelliJ after adding.

---

## 🔄 Future Database Migration

When ready to switch from flat files to a database:

1. Add JDBC driver dependency to `pom.xml`
2. Create `JdbcUserRepository implements UserRepository`
3. Replace `FileUserRepository` in `MainApplication` (or Spring context)
4. Keep all service/model/controller code unchanged ← thanks to interfaces!
