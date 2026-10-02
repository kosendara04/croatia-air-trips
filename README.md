# Croatia Air Trips

Java Swing desktop application for managing panoramic flights, users and flight reservations. The project was developed as an academic final-year project and demonstrates desktop GUI development, relational database integration, role-based application flows and report export.

![Croatia Air Trips welcome screen](docs/screenshots/home.png)

## Highlights

- Separate user and administrator interfaces
- Flight browsing and seat reservations
- Reservation history for logged-in users
- Administration of flights, aircraft, users and reservations
- Reports for reservations, users, flights and aircraft
- Export of reports to CSV, TXT and Excel (XLSX)
- MySQL/MariaDB integration through JDBC
- Prepared statements for database operations
- Database credentials supplied through environment variables rather than hardcoded in source code

## Technologies

- Java
- Swing
- JDBC
- MySQL / MariaDB
- Apache POI
- Eclipse IDE

## Application structure

The project separates application responsibilities across packages:

- `model` — database access, domain models and password hashing utilities
- `panorama` — Swing windows, panels and user interface logic
- `kontroller` — controllers used for selected application views
- `database` — sanitized database schema and fictional demo data
- `docs/screenshots` — application screenshots

The application uses the `KORISNIK.uloga` field to distinguish `user` and `admin` accounts.

## Screenshots

The application interface is in Croatian.

<details>
<summary>User interface</summary>

### User login

![User login](docs/screenshots/user-login.png)

### Flight browsing

![Flight browsing](docs/screenshots/flight-list.png)

### Flight reservation

![Flight reservation](docs/screenshots/flight-reservation.png)

### Reservation history

![Reservation history](docs/screenshots/reservation-history.png)

</details>

<details>
<summary>Administrator interface</summary>

### Flight management

![Flight management](docs/screenshots/admin-flights.png)

### Aircraft management

![Aircraft management](docs/screenshots/admin-aircraft.png)

### Reservation management

![Reservation management](docs/screenshots/admin-reservations.png)

### Reports

![Reports](docs/screenshots/admin-reports.png)

</details>

## Running the project

### Requirements

- JDK 24 (the source was compiled with JDK 24 during repository preparation)
- Eclipse IDE for Java Developers
- MySQL or MariaDB

### Setup

1. Clone or download this repository.
2. In Eclipse, select **File > Import > General > Existing Projects into Workspace** and select the project folder.
3. Create a new empty database named `panoramski_letovi` using `utf8mb4`.
4. Import `database/schema.sql`.
5. Optionally import `database/demo-data.sql` once if you want ready-made fictional demo accounts and sample records.
6. In **Run > Run Configurations > Java Application**, choose `panorama.GlavnaStr` as the main class.
7. Under **Environment**, define your own database connection settings:

| Variable | Example |
| --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/panoramski_letovi` |
| `DB_USER` | `your_database_user` |
| `DB_PASSWORD` | `your_database_password` |

8. Use the project root as the working directory so the application can load the icons.
9. Run the application.

Database credentials are read from environment variables and are not stored in the source code.

## Database

The included SQL files are intentionally sanitized:

- `database/schema.sql` contains the database structure only.
- `database/demo-data.sql` contains fictional local demo data.
- Original database contents and database connection credentials are not included.

The schema was exported from MariaDB 11.8 using HeidiSQL. The application connects through MySQL Connector/J with a `jdbc:mysql:` connection URL.

### Demo application accounts

| Login screen | Email | Password |
| --- | --- | --- |
| Administrator | `admin@example.com` | `DemoFlights2026!` |
| User | `putnik@example.com` | `DemoFlights2026!` |

These credentials are intentionally public and are only for the fictional local demo dataset.

Both login roles use the `KORISNIK` table. The exported `ADMIN` table is retained to reflect the original database schema, but the current login implementation does not query it.

## Security notes

- Database credentials are supplied through `DB_URL`, `DB_USER` and `DB_PASSWORD` environment variables.
- SQL values are passed with prepared statements in the application database operations.
- Application passwords are stored as SHA-256 hashes in this academic implementation.
- For a production application, password hashing should be migrated to a password-specific algorithm such as Argon2 or bcrypt with per-password salts.

## Validation

During repository preparation:

- all 27 Java source files compiled successfully;
- the included Apache POI libraries passed a workbook creation and reopening check;
- the Excel export date formatting was corrected and reviewed.

A clean MySQL/MariaDB import and full end-to-end database workflow should be verified on the target machine before treating the project as production-ready.

## Project context

Academic final-year project by Marko Kos.

The repository is intended to demonstrate practical work with Java desktop development, Swing interfaces, SQL/JDBC database access, role-based application flows and report generation.
