-- Croatia Air Trips: structure only.
-- Select a NEW empty MySQL/MariaDB database before importing this file.
-- No original server details, credentials or user records are included.

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Table: ADMIN
CREATE TABLE IF NOT EXISTS `ADMIN` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `korisnicko_ime` varchar(50) NOT NULL,
  `lozinka` varchar(255) NOT NULL,
  `ime` varchar(100) NOT NULL,
  `kreirano` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `korisnicko_ime` (`korisnicko_ime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Table: AVION
CREATE TABLE IF NOT EXISTS `AVION` (
  `id_aviona` int(11) NOT NULL AUTO_INCREMENT,
  `kapacitet_aviona` int(11) NOT NULL,
  `model_aviona` varchar(100) NOT NULL,
  `proizvodac_aviona` varchar(100) NOT NULL,
  PRIMARY KEY (`id_aviona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Table: IZVJESTAJI
CREATE TABLE IF NOT EXISTS `IZVJESTAJI` (
  `izvjestaj_id` int(11) NOT NULL AUTO_INCREMENT,
  `korisnik_id` int(11) DEFAULT NULL,
  `grad_polaska` varchar(100) DEFAULT NULL,
  `grad_dolaska` varchar(100) DEFAULT NULL,
  `vrijeme_polaska` datetime DEFAULT NULL,
  PRIMARY KEY (`izvjestaj_id`),
  KEY `korisnik_id` (`korisnik_id`),
  CONSTRAINT `IZVJESTAJI_ibfk_1` FOREIGN KEY (`korisnik_id`) REFERENCES `KORISNIK` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Table: KORISNIK
CREATE TABLE IF NOT EXISTS `KORISNIK` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ime` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `lozinka` varchar(255) NOT NULL,
  `kreirano` timestamp NOT NULL DEFAULT current_timestamp(),
  `uloga` varchar(20) NOT NULL DEFAULT 'user',
  `kontaktBroj_korisnika` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Table: LETOVI
CREATE TABLE IF NOT EXISTS `LETOVI` (
  `let_id` int(11) NOT NULL AUTO_INCREMENT,
  `broj_leta` varchar(20) NOT NULL,
  `grad_polaska` varchar(100) DEFAULT NULL,
  `grad_dolaska` varchar(100) DEFAULT NULL,
  `vrijeme_polaska` datetime DEFAULT NULL,
  `vrijeme_dolaska` datetime DEFAULT NULL,
  `dostupna_sjedista` int(11) DEFAULT NULL,
  `id_avion` int(11) DEFAULT NULL,
  PRIMARY KEY (`let_id`),
  UNIQUE KEY `broj_leta` (`broj_leta`),
  KEY `FK_Letovi_Avion` (`id_avion`),
  CONSTRAINT `FK_Letovi_Avion` FOREIGN KEY (`id_avion`) REFERENCES `AVION` (`id_aviona`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Table: REZERVACIJE_LETOVA
CREATE TABLE IF NOT EXISTS `REZERVACIJE_LETOVA` (
  `rezervacija_id` int(11) NOT NULL AUTO_INCREMENT,
  `korisnik_id` int(11) DEFAULT NULL,
  `let_id` int(11) DEFAULT NULL,
  `datum_rezervacije` timestamp NOT NULL DEFAULT current_timestamp(),
  `broj_sjedista` int(11) DEFAULT 1,
  PRIMARY KEY (`rezervacija_id`),
  KEY `korisnik_id` (`korisnik_id`),
  KEY `let_id` (`let_id`),
  CONSTRAINT `REZERVACIJE_LETOVA_ibfk_1` FOREIGN KEY (`korisnik_id`) REFERENCES `KORISNIK` (`id`) ON DELETE CASCADE,
  CONSTRAINT `REZERVACIJE_LETOVA_ibfk_2` FOREIGN KEY (`let_id`) REFERENCES `LETOVI` (`let_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
