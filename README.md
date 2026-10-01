# Croatia Air Trips

Java Swing desktop application for managing panoramic flights and reservations, developed as a final-year project.

![Croatia Air Trips welcome screen](docs/screenshots/home.png)

## Features

- Separate user and administrator interfaces
- Flight browsing and seat reservations
- User reservation history
- Administration of flights, aircraft, users and reservations
- Reports exported to CSV, TXT and Excel (XLSX)
- MySQL database access through JDBC and SQL queries

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

## Technologies

Java, Swing, JDBC, MySQL/MariaDB, Apache POI and Eclipse.

## Running the project

1. Install a JDK and Eclipse IDE for Java Developers. Compilation was checked with JDK 24.
2. Download or clone this repository.
3. In Eclipse, select **File > Import > General > Existing Projects into Workspace** and select the project folder.
4. Create an empty database and import `database/schema.sql`, then optionally `database/demo-data.sql` (see Database setup below).
5. In **Run > Run Configurations > Java Application**, choose `panorama.GlavnaStr` as the main class.
6. Under **Environment**, add your own connection settings:

| Variable | Example |
| --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/panoramski_letovi` |
| `DB_USER` | `your_database_user` |
| `DB_PASSWORD` | `your_database_password` |

7. Use the project root as the working directory so that icons can be loaded, then run the application.

Database credentials are read from environment variables and are not included in the source code.

## Database setup

The schema was exported from MariaDB 11.8 using HeidiSQL. The application uses MySQL Connector/J and a `jdbc:mysql:` connection URL. Import and end-to-end compatibility have not yet been tested in a local database.

1. Connect to your own local MySQL/MariaDB server in HeidiSQL.
2. Create a **new, empty** database named `panoramski_letovi`, using `utf8mb4`.
3. Select that database in HeidiSQL.
4. Open `database/schema.sql` with **File > Load SQL file**, then execute the script in the selected database.
5. Optionally load and execute `database/demo-data.sql` **once**, after the schema. It supplies fictional accounts, aircraft, two future flights and one reservation.
6. Set `DB_URL`, `DB_USER` and `DB_PASSWORD` in Eclipse as described above. These are your own database connection credentials, separate from the application accounts below.

### Demo application accounts

| Login screen | Email | Password |
| --- | --- | --- |
| Administrator | `admin@example.com` | `DemoFlights2026!` |
| User | `putnik@example.com` | `DemoFlights2026!` |

These are intentionally public, fictional accounts for a local demo. Their stored password hashes match the current application's SHA-256 login implementation. Without the optional demo data the tables are empty and these accounts do not exist.

Both login roles use the `KORISNIK` table. The exported `ADMIN` table is retained for fidelity to the original schema, but the current login implementation does not query it.

The scripts do not include original database contents or database connection credentials. `schema.sql` creates tables in the database you select; it does not select, create or delete a database. The demo script is intended for a fresh schema, not repeated imports into an existing working database.

## Known limitations

- Excel export currently writes date values without a display format, so Excel may show serial numbers until the cells are formatted as dates.

## Validation

- All 27 Java source files compiled during preparation.
- The included Excel libraries passed a workbook creation and reopening check.
- Database operations and end-to-end user workflows were not tested during this preparation.

## Project context

Academic final-year project by Marko Kos. This repository demonstrates desktop application development, database integration and reporting.
