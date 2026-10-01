-- Fictional demo data. Import ONCE into a fresh database after schema.sql.
-- Public demo password for both accounts: DemoFlights2026!
-- These accounts are for a local demonstration only.
START TRANSACTION;

INSERT INTO KORISNIK (id, ime, email, lozinka, uloga, kontaktBroj_korisnika) VALUES
(1, 'Demo Administrator', 'admin@example.com', '3852d189ad4c0187be8007764c991b29ecd0550aa18ec46eb9be798db750f1f4', 'admin', NULL),
(2, 'Demo Putnik', 'putnik@example.com', '3852d189ad4c0187be8007764c991b29ecd0550aa18ec46eb9be798db750f1f4', 'user', NULL);

INSERT INTO AVION (id_aviona, kapacitet_aviona, model_aviona, proizvodac_aviona) VALUES
(1, 3, '172 Skyhawk', 'Cessna'),
(2, 3, 'PA-28', 'Piper');

INSERT INTO LETOVI (let_id, broj_leta, grad_polaska, grad_dolaska, vrijeme_polaska, vrijeme_dolaska, dostupna_sjedista, id_avion) VALUES
(1, 'DEMO-001', 'Zagreb', 'Zagreb', TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY), '11:00:00'), 2, 1),
(2, 'DEMO-002', 'Rijeka', 'Rijeka', TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 8 DAY), '14:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 8 DAY), '15:00:00'), 3, 2);

INSERT INTO REZERVACIJE_LETOVA (rezervacija_id, korisnik_id, let_id, broj_sjedista) VALUES (1, 2, 1, 1);
INSERT INTO IZVJESTAJI (korisnik_id, grad_polaska, grad_dolaska, vrijeme_polaska)
SELECT 2, grad_polaska, grad_dolaska, vrijeme_polaska FROM LETOVI WHERE let_id = 1;

COMMIT;
