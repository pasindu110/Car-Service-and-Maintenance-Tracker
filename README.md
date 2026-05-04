# 🚗 Car Service and Maintenance Tracker

> A Java 17 console application for tracking vehicle services, maintenance, appointments, and billing.
> Built as a student team project to demonstrate core OOP principles.

---

## 📋 Project Overview

| Item         | Detail                                                        |
|--------------|---------------------------------------------------------------|
| Language     | Java 17                                                       |
| Build Tool   | Maven 3                                                       |
| UI           | Console (CLI menu-driven)                                     |
| Storage      | Flat-file `.txt` (pipe-delimited) — upgradeable to JDBC       |
| Architecture | Layered: Controller → Service → Repository → Model            |
| Testing      | JUnit Jupiter 5.10.2                                          |
| IDE          | IntelliJ IDEA                                                 |

---

## 🏗️ Architecture

```
MainApplication
    └── Controller   — reads user input, calls service methods
            └── Service      — business logic (interface + impl)
                    └── Repository   — data access (interface + File impl)
                            └── Model        — domain entities (extend BaseEntity)
```

Every layer depends only on the layer below it through **interfaces**, making it easy
to swap implementations (e.g. file → database) without touching other layers.

---

## 🧱 OOP Pillars Demonstrated

| Pillar          | Where in code                                                                 |
|-----------------|-------------------------------------------------------------------------------|
| **Encapsulation** | Every model field is `private`; exposed only via getters/setters             |
| **Inheritance**   | `BaseEntity` → `User` → `Admin` / `Customer` / `Mechanic`                   |
| **Polymorphism**  | `getPermissionsDescription()` returns different text for each user subclass  |
|                   | `toFileString()` overridden in every entity for serialisation                |
| **Abstraction**   | `BillingService`, `FeedbackService`, all `*Repository` interfaces            |

---

## 📦 Package Structure

```
com.cartracker
├── MainApplication.java              ← Entry point
├── config/
│   └── AppConfig.java                ← App-wide constants (tax rate, etc.)
├── model/
│   ├── common/
│   │   ├── BaseEntity.java           ← Abstract root (id, createdAt, updatedAt, toFileString())
│   │   ├── Status.java               ← Enum: PENDING | CONFIRMED | IN_PROGRESS | COMPLETED | CANCELLED
│   │   └── UserRole.java             ← Enum: ADMIN | CUSTOMER | MECHANIC
│   ├── user/
│   │   ├── User.java                 ← Abstract (username, email, fullName, phone, role, active)
│   │   ├── Admin.java                ← + adminLevel ("SUPER_ADMIN", "MODERATOR")
│   │   ├── Customer.java             ← + address, vehicleIds[]
│   │   └── Mechanic.java             ← + specialisation, experienceYears, available
│   ├── vehicle/
│   │   └── Vehicle.java              ← ownerId, licensePlate, make, model, year, color, mileage, fuelType
│   ├── appointment/
│   │   └── Appointment.java          ← customerId, vehicleId, mechanicId, scheduledAt, serviceType, status
│   ├── servicerecord/
│   │   ├── ServiceRecord.java        ← vehicleId, mechanicId, serviceDate, taskIds[], totalCost, status
│   │   └── MaintenanceTask.java      ← taskName, taskDetails, partsCost, labourCost, completed
│   ├── billing/
│   │   ├── Invoice.java              ← serviceRecordId, customerId, lineItems[], subTotal, taxRate, totalAmount, paid
│   │   └── Payment.java              ← invoiceId, customerId, amountPaid, paymentMethod, paidAt, referenceNumber
│   └── feedback/
│       └── Feedback.java             ← customerId, serviceRecordId, rating (1-5), comment
├── controller/
│   ├── user/         UserController
│   ├── vehicle/      VehicleController
│   ├── servicerecord/ ServiceRecordController
│   ├── appointment/  AppointmentController
│   ├── billing/      BillingController
│   └── feedback/     FeedbackController
├── service/
│   ├── billing/      BillingService (interface) + BillingServiceImpl
│   ├── feedback/     FeedbackService (interface) + FeedbackServiceImpl  ← most complete
│   └── ...           (user, vehicle, servicerecord, appointment)
├── repository/
│   ├── billing/      BillingRepository (interface) + FileBillingRepository
│   ├── feedback/     FeedbackRepository (interface) + FileFeedbackRepository
│   └── ...           (user, vehicle, servicerecord, appointment)
├── dto/
│   ├── RegisterUserRequest.java
│   ├── BookAppointmentRequest.java
│   ├── SubmitFeedbackRequest.java
│   └── InvoiceResponse.java
├── exception/
│   ├── CarTrackerException.java      ← Base unchecked exception
│   ├── EntityNotFoundException.java
│   ├── DuplicateEntryException.java
│   └── ValidationException.java
└── util/
    ├── FileUtil.java                 ← readAllLines / writeAllLines / appendLine
    ├── IdGenerator.java              ← generateUUID() or generateSequential("PREFIX")
    └── DateUtil.java
```

---

## 🗂️ Module → Team Member Assignment

| Module                   | Key Classes                                                               | Assign to |
|--------------------------|---------------------------------------------------------------------------|-----------|
| User Management          | `User`, `Admin`, `Customer`, `Mechanic`, `UserController`, `UserServiceImpl`, `FileUserRepository` | Member 1 |
| Vehicle Management       | `Vehicle`, `VehicleController`, `VehicleServiceImpl`, `FileVehicleRepository` | Member 2 |
| Service & Maintenance    | `ServiceRecord`, `MaintenanceTask`, `ServiceRecordController`             | Member 3 |
| Appointment & Scheduling | `Appointment`, `AppointmentController`, `AppointmentServiceImpl`          | Member 4 |
| Billing & Reports        | `Invoice`, `Payment`, `BillingController`, `BillingServiceImpl`           | Member 5 |

---

## 💾 Flat-File Persistence

All data lives in `src/main/resources/data/`. Each entity is stored as one pipe-delimited (`|`) line.

| File               | Format (columns in order)                                                                   |
|--------------------|----------------------------------------------------------------------------------------------|
| `users.txt`        | `id\|username\|email\|fullName\|phone\|role\|active\|createdAt[...subclass fields]`         |
| `vehicles.txt`     | `id\|ownerId\|licensePlate\|make\|model\|year\|color\|mileage\|fuelType\|createdAt`         |
| `appointments.txt` | `id\|customerId\|vehicleId\|mechanicId\|scheduledAt\|serviceType\|notes\|status\|serviceRecordId\|createdAt` |
| `servicerecords.txt` | `id\|vehicleId\|mechanicId\|serviceDate\|description\|status\|taskIds\|totalCost\|createdAt` |
| `tasks.txt`        | `id\|serviceRecordId\|taskName\|taskDetails\|partsCost\|labourCost\|completed\|createdAt`  |
| `invoices.txt`     | `id\|serviceRecordId\|customerId\|invoiceDate\|subTotal\|taxRate\|totalAmount\|paid\|paymentId\|createdAt` |
| `payments.txt`     | `id\|invoiceId\|customerId\|amountPaid\|paymentMethod\|paidAt\|referenceNumber\|createdAt` |
| `feedback.txt`     | `id\|customerId\|serviceRecordId\|rating\|comment\|createdAt`                               |

Lines starting with `#` are treated as comments and ignored by `FileUtil`.

---

## 🔑 Key Business Rules

- **Appointment lifecycle:** `PENDING → CONFIRMED → IN_PROGRESS → COMPLETED | CANCELLED`
- **Service record lifecycle:** same `Status` enum as appointments
- **Invoice total:** `totalAmount = subTotal + (subTotal × taxRate)` — calculated on construction
- **Feedback:** rating must be **1–5**; a customer can submit **only one** feedback per service record
- **Soft delete:** users have an `active` flag; setting it `false` deactivates without deleting the record
- **Payment methods:** `CASH | CARD | ONLINE_TRANSFER`
- **Mechanic availability:** `available` flag controls whether a mechanic can be assigned new jobs

---

## 🛠️ Utility Classes

### `FileUtil`
```java
FileUtil.readAllLines("invoices.txt");          // load all lines
FileUtil.writeAllLines("invoices.txt", lines);  // overwrite entire file
FileUtil.appendLine("invoices.txt", line);      // add one record
```

### `IdGenerator`
```java
IdGenerator.generateUUID();               // "550e8400-e29b-41d4-..."
IdGenerator.generateSequential("USR");    // "USR-1000", "USR-1001", ...
```

---

## ⚠️ Exception Hierarchy

```
RuntimeException
    └── CarTrackerException          ← catch-all for the app
            ├── EntityNotFoundException
            ├── DuplicateEntryException
            └── ValidationException
```

---

## ✅ Implementation Status

| Area                    | Status                                          |
|-------------------------|-------------------------------------------------|
| All model classes       | ✅ Complete — fields, constructors, getters/setters, `toFileString()` |
| `FeedbackServiceImpl`   | ✅ Fully implemented (validation, duplicate guard, average rating) |
| `BillingController`     | ✅ Menu + method stubs wired to service          |
| `BillingServiceImpl`    | ⚠️ Skeleton — most methods are `TODO`           |
| `MainApplication.main()`| ⚠️ Prints banner only — menu loop not yet wired |
| Other Service Impls     | ⚠️ Skeleton / TODO                              |
| Unit Tests              | ⚠️ Not yet written                              |

---

## 🚀 How to Run

1. Open IntelliJ IDEA → **File → Open** → select this `OOP` folder
2. Ensure Maven auto-import is enabled (**pom.xml** detected)
3. Run `MainApplication.java` (right-click → Run)

```bash
# Or from terminal
mvn compile exec:java -Dexec.mainClass="com.cartracker.MainApplication"
```

---

## 🌿 Git Branching Strategy

```
main                        ← stable, reviewed code only
dev                         ← integration branch
feature/user-management
feature/vehicle-management
feature/service-records
feature/appointments
feature/billing
```

**Rule:** Never push directly to `main`. Open a Pull Request from your feature branch into `dev`.

---

## 🔄 Future Database Migration

When ready to switch from flat files to a database:

1. Add a JDBC driver dependency to `pom.xml`
2. Create `JdbcUserRepository implements UserRepository` (and same for other modules)
3. Replace `File*Repository` instances in `MainApplication`
4. All service, model, and controller code stays unchanged — thanks to interfaces!
