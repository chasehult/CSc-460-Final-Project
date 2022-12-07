-- Run these lines to reset things
DROP TRIGGER passflight_overlap;
DROP TRIGGER passenger_student_bags;
DROP TRIGGER passflight_student_bags;
DROP TABLE StaffFlight;
DROP TABLE PassengerFlight;
DROP TABLE PassengerAirline;
DROP TABLE Staff;
DROP TABLE Passenger;
DROP TABLE Flight;
DROP TABLE BoardingGate;
DROP TABLE Airport;
DROP TABLE Airline;

CREATE TABLE Airline (name VARCHAR(9) NOT NULL PRIMARY KEY);
INSERT INTO Airline VALUES ('Alaska');
INSERT INTO Airline VALUES ('Delta');
INSERT INTO Airline VALUES ('Southwest');
INSERT INTO Airline VALUES ('United');

CREATE TABLE Airport (
    airport_id INTEGER NOT NULL PRIMARY KEY,
    airport_name VARCHAR(100) NOT NULL,
    airport_code CHAR(3) NOT NULL,
    location VARCHAR(100) NOT NULL
);

CREATE TABLE BoardingGate (
    boarding_gate VARCHAR(3) NOT NULL
        CHECK (REGEXP_LIKE(boarding_gate, '[A-D]\d{1,2}', 'c')),
    airport_id INTEGER NOT NULL REFERENCES Airport (airport_id) ON DELETE CASCADE,
    CONSTRAINT boardgate_pk PRIMARY KEY (boarding_gate, airport_id)
);

CREATE TABLE Flight (
    flight_id INTEGER NOT NULL PRIMARY KEY,
    airline VARCHAR(9) NOT NULL REFERENCES Airline (name) ON DELETE CASCADE,
    boarding_gate VARCHAR(3) NOT NULL,
    flight_date DATE NOT NULL
        CHECK (flight_date BETWEEN DATE '2021-01-01' AND DATE '2021-12-31'),
    boarding_time INTEGER NOT NULL
        CHECK (0 <= boarding_time AND boarding_time < 2400 AND MOD(boarding_time, 100) < 60),
    departing_time INTEGER NOT NULL
        CHECK (0 <= departing_time AND departing_time < 2400 AND MOD(departing_time, 100) < 60),
    duration INTEGER NOT NULL
        CHECK (100 <= duration AND duration <= 500 AND MOD(duration, 100) < 60),
    dest_from INTEGER NOT NULL,
    dest_to INTEGER NOT NULL REFERENCES Airport (airport_id) ON DELETE CASCADE,
    CONSTRAINT flight_fk_boardgate FOREIGN KEY (boarding_gate, dest_from) REFERENCES BoardingGate (boarding_gate, airport_id) ON DELETE CASCADE,
    CONSTRAINT flight_uniq_bddt UNIQUE (boarding_gate, dest_from, flight_date, departing_time),
    CONSTRAINT flight_valid_board_depart CHECK (boarding_time <= departing_time),
    -- Assert that "departing_time" + "duration" < 2400, but added as 24 hour times instead of integers
    --   I don't have permission to create a function to add 24 hour times together, so I do it here :(
    CONSTRAINT flight_valid_duration CHECK (
        -- Hours
        ((TRUNC(departing_time / 100) + TRUNC(duration / 100) + TRUNC((MOD(departing_time, 100) + MOD(duration, 100)) / 60)) * 100
        -- Minutes
        + MOD((MOD(departing_time, 100) + MOD(duration, 100)), 60)) < 2400)
);
-- This is as close as I can get to a function. 
CREATE OR REPLACE VIEW ArrivalTimes AS 
SELECT flight_id, 
        -- Hours
        ((TRUNC(departing_time / 100) + TRUNC(duration / 100) + TRUNC((MOD(departing_time, 100) + MOD(duration, 100)) / 60)) * 100
        -- Minutes
        + MOD((MOD(departing_time, 100) + MOD(duration, 100)), 60))
        AS arrival_time
  FROM Flight;

CREATE TABLE Passenger (
    passenger_id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    -- This 100% should be an attribute of (airline, passenger), but it's not!  for some reason!
    frequent_flier NUMBER(1) NOT NULL CHECK (frequent_flier in (0, 1)),
    student NUMBER(1) NOT NULL CHECK (student in (0, 1)),
    minor NUMBER(1) NOT NULL CHECK (minor in (0, 1))
);

CREATE TABLE Staff (
    employee_id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    job VARCHAR(30) NOT NULL,
    employed_by VARCHAR(9) NOT NULL REFERENCES Airline (name) ON DELETE CASCADE
);

-- This isn't needed atm, but frequent flier definitely *should* be here
CREATE TABLE PassengerAirline (
    passenger_id INTEGER NOT NULL
        REFERENCES Passenger (passenger_id) ON DELETE CASCADE,
    airline VARCHAR(9) NOT NULL REFERENCES Airline (name) ON DELETE CASCADE,
    CONSTRAINT passair_pk PRIMARY KEY (passenger_id, airline)
);

CREATE TABLE PassengerFlight (
    passenger_id INTEGER NOT NULL REFERENCES Passenger (passenger_id) ON DELETE CASCADE,
    flight_id INTEGER NOT NULL REFERENCES Flight (flight_id) ON DELETE CASCADE,
    bags_checked INTEGER NOT NULL CHECK (bags_checked >= 0),
    ordered_snacks NUMBER(1) NOT NULL CHECK (ordered_snacks in (0, 1)),
    CONSTRAINT passflight_pk PRIMARY KEY (passenger_id, flight_id)
);
-- "bags_checked" cannot be more than 2 UNLESS passenger is a student
CREATE OR REPLACE TRIGGER passflight_student_bags
  BEFORE INSERT OR UPDATE OF passenger_id, bags_checked ON PassengerFlight FOR EACH ROW 
  WHEN (NEW.bags_checked > 2) 
DECLARE
   "count" NUMBER;
BEGIN
  SELECT COUNT(*) INTO "count" FROM Passenger 
    WHERE passenger_id = :NEW.passenger_id AND student = 0;
   IF "count" > 0 THEN
    Raise_application_error(-20000, 'Too many bags :(');
  END IF;
END;
/ 
CREATE OR REPLACE TRIGGER passenger_student_bags
  BEFORE UPDATE OF student ON Passenger FOR EACH ROW
  WHEN (NEW.student = 0) 
DECLARE
  "count" NUMBER;
BEGIN
  SELECT COUNT(*) INTO "count" FROM PassengerFlight 
    WHERE passenger_id = :NEW.passenger_id AND bags_checked > 1;
   IF "count" > 0 THEN
    Raise_application_error(-20000, 'This passenger has flights with more than 2 checked bags.');
  END IF;
END;
/ 
-- A passenger may not be on two overlapping flights.
CREATE OR REPLACE TRIGGER passflight_overlap
  AFTER INSERT OR UPDATE OF passenger_id, flight_id ON PassengerFlight 
DECLARE
  "count" NUMBER;
BEGIN
  SELECT COUNT(*) INTO "count" 
    FROM PassengerFlight pf1
      JOIN Flight f1 ON (pf1.flight_id = f1.flight_id)
      JOIN ArrivalTimes a1 ON (f1.flight_id = a1.flight_id)
      JOIN PassengerFlight pf2 ON (pf1.passenger_id = pf2.passenger_id)
      JOIN Flight f2 ON (pf2.flight_id = f2.flight_id)
    WHERE f1.flight_id <> f2.flight_id
      AND f1.flight_date = f2.flight_date
      AND f1.departing_time <= f2.departing_time
      AND a1.arrival_time >= f2.departing_time;
  IF "count" > 0 THEN
    Raise_application_error(-20001, 'Overlapping flights :(');
  END IF;
END;
/
CREATE OR REPLACE TRIGGER flight_overlap
  AFTER UPDATE OF departing_time, duration ON Flight 
DECLARE
  "count" NUMBER;
BEGIN
  SELECT COUNT(*) INTO "count" 
    FROM PassengerFlight pf1
      JOIN Flight f1 ON (pf1.flight_id = f1.flight_id)
      JOIN ArrivalTimes a1 ON (f1.flight_id = a1.flight_id)
      JOIN PassengerFlight pf2 ON (pf1.passenger_id = pf2.passenger_id)
      JOIN Flight f2 ON (pf2.flight_id = f2.flight_id)
    WHERE f1.flight_id <> f2.flight_id
      AND f1.flight_date = f2.flight_date
      AND f1.departing_time <= f2.departing_time
      AND a1.arrival_time >= f2.departing_time;
  IF "count" > 0 THEN
    Raise_application_error(-20001, 'Changing this would cause overlapping flights.');
  END IF;
END;
/
-- Notes:
--   - Snacks are cheaper if the passenger is a frequent flier.
--       I have no idea how to represent this.

CREATE TABLE StaffFlight (
    employee_id INTEGER NOT NULL REFERENCES Staff (employee_id) ON DELETE CASCADE,
    flight_id INTEGER NOT NULL REFERENCES Flight (flight_id) ON DELETE CASCADE,
    CONSTRAINT sflight_pk PRIMARY KEY (employee_id, flight_id)
);

GRANT SELECT ON Airline TO PUBLIC; 
GRANT SELECT ON Flight TO PUBLIC; 
GRANT SELECT ON Passenger TO PUBLIC; 
GRANT SELECT ON Staff TO PUBLIC; 
GRANT SELECT ON PassengerAirline TO PUBLIC; 
GRANT SELECT ON PassengerFlight TO PUBLIC; 
GRANT SELECT ON StaffFlight TO PUBLIC; 

-- Test Passengers
INSERT INTO Passenger VALUES (0, 'Avia Agreste', 0, 0, 0);
INSERT INTO Passenger VALUES (1, 'Brad Kristen', 0, 0, 1);
INSERT INTO Passenger VALUES (2, 'Cody Arasmus', 0, 1, 0);
INSERT INTO Passenger VALUES (3, 'Dana Kelso',   0, 1, 1);
INSERT INTO Passenger VALUES (4, 'Enra Jacobs',  1, 0, 0);
INSERT INTO Passenger VALUES (5, 'Fred Smith',   1, 0, 1);
INSERT INTO Passenger VALUES (6, 'Gina Cortez',  1, 1, 0);
INSERT INTO Passenger VALUES (7, 'Haru Takashi', 1, 1, 1);

-- Test Staff.  These are not used in anything.  Why do we have to do these.
INSERT INTO Staff VALUES (0, 'Tyler Donovan', 'Pilot', 'Delta');
INSERT INTO Staff VALUES (1, 'Isaac King', 'Pilot', 'Delta');
INSERT INTO Staff VALUES (2, 'Milly Peterson', 'Ground Crew', 'Delta');
INSERT INTO Staff VALUES (3, 'River Medina', 'Ground Crew', 'United');
INSERT INTO Staff VALUES (4, 'Felix Pearson', 'Flight Attendant', 'United');
INSERT INTO Staff VALUES (5, 'Billy Walter', 'Flight Attendant', 'United');
INSERT INTO Staff VALUES (6, 'Doris Morales', 'Cabin Crew', 'United');
INSERT INTO Staff VALUES (7, 'Moira Barbos', 'Cabin Crew', 'United');

-- Test Airports
INSERT INTO Airport VALUES (0, 'Los Angeles International Airport', 'LAX', 'Los Angeles, CA');
INSERT INTO Airport VALUES (1, 'Phoenix Sky Harbor International Airport', 'PHX', 'Phoenix, AZ');
INSERT INTO BoardingGate VALUES ('A1', 0);
INSERT INTO BoardingGate VALUES ('A2', 0);
INSERT INTO BoardingGate VALUES ('A3', 0);
INSERT INTO BoardingGate VALUES ('B1', 0);
INSERT INTO BoardingGate VALUES ('B2', 0);
INSERT INTO BoardingGate VALUES ('A1', 1);

-- Test Flights
-- Data for Query 1
INSERT INTO Flight VALUES (0, 'Delta', 'A1', DATE '2021-01-01', 0000, 0100, 500, 0, 0);
INSERT INTO Flight VALUES (1, 'Southwest', 'A2', DATE '2021-04-12', 1230, 1300, 100, 0, 0);
INSERT INTO Flight VALUES (2, 'United', 'A3', DATE '2021-02-15', 1500, 1530, 200, 0, 0);
INSERT INTO Flight VALUES (3, 'Alaska', 'B1', DATE '2021-11-21', 1815, 1900, 130, 0, 0);
INSERT INTO PassengerFlight VALUES (0, 0, 1, 0);
INSERT INTO PassengerFlight VALUES (0, 1, 1, 0);
INSERT INTO PassengerFlight VALUES (0, 2, 1, 0);
INSERT INTO PassengerFlight VALUES (0, 3, 1, 0);
INSERT INTO PassengerFlight VALUES (1, 0, 1, 0);
INSERT INTO PassengerFlight VALUES (1, 1, 1, 0);
INSERT INTO PassengerFlight VALUES (1, 2, 1, 0);
INSERT INTO PassengerFlight VALUES (2, 2, 1, 0);
INSERT INTO PassengerFlight VALUES (2, 3, 1, 0);
INSERT INTO PassengerFlight VALUES (3, 2, 1, 0);
-- SELECT DISTINCT name
--   FROM Passenger 
--     JOIN PassengerFlight USING (passenger_id)
--     JOIN Flight USING (flight_id)
--   GROUP BY passenger_id, name
--   HAVING COUNT(DISTINCT airline) = 4;
-- -- Should give only Avia

-- Data for Query 2
INSERT INTO Flight VALUES (4, 'Southwest', 'B2', DATE '2021-03-14', 0314, 0345, 314, 0, 0);
INSERT INTO Flight VALUES (5, 'Southwest', 'A1', DATE '2021-03-17', 0615, 0630, 500, 0, 0);
INSERT INTO Flight VALUES (6, 'Alaska', 'A2', DATE '2021-03-14', 0700, 1730, 500, 0, 0);
INSERT INTO PassengerFlight VALUES (4, 4, 2, 0);
INSERT INTO PassengerFlight VALUES (5, 5, 2, 1);
INSERT INTO PassengerFlight VALUES (6, 6, 3, 1);
INSERT INTO PassengerFlight VALUES (7, 6, 1, 0);
SELECT name, bags_checked
  FROM Passenger 
    NATURAL JOIN PassengerFlight 
    NATURAL JOIN Flight
  WHERE flight_date = TO_DATE(14 || '-MAR-21')  -- Replace 14 with input date
  ORDER BY flight_id, bags_checked;


-- Data for Query 3
INSERT INTO Flight VALUES (7, 'Alaska', 'A3', DATE '2021-06-12', 0612, 0700, 100, 0, 0);
INSERT INTO Flight VALUES (8, 'Southwest', 'B1', DATE '2021-06-12', 0800, 0830, 200, 0, 0);
INSERT INTO Flight VALUES (9, 'Southwest', 'B2', DATE '2021-06-12', 1830, 1900, 200, 0, 0);
INSERT INTO Flight VALUES (10, 'United', 'A1', DATE '2021-06-12', 0700, 0730, 130, 0, 0);
INSERT INTO Flight VALUES (11, 'Alaska', 'A2', DATE '2021-06-01', 0000, 0010, 100, 0, 0);
INSERT INTO Flight VALUES (12, 'Alaska', 'A3', DATE '2021-06-01', 0110, 0120, 100, 0, 0);
INSERT INTO Flight VALUES (13, 'Southwest', 'B1', DATE '2021-06-01', 0220, 0230, 100, 0, 0);
-- SELECT DISTINCT flight_id, boarding_gate, airline, flight_date, boarding_time, departing_time, duration, route
--   FROM Flight
--   WHERE flight_date = TO_DATE(12 || '-JUN-21')  -- Replace 12 with input date
--   ORDER BY boarding_time ASC;

-- Data for Query 4
INSERT INTO Flight VALUES (14, 'Delta', 'B2', DATE '2021-03-10', 0300, 0330, 100, 0, 0);
INSERT INTO Flight VALUES (15, 'Delta', 'A1', DATE '2021-03-11', 0500, 0530, 100, 0, 0);
INSERT INTO PassengerFlight VALUES (0, 14, 1, 0);
INSERT INTO PassengerFlight VALUES (0, 15, 1, 0);
INSERT INTO PassengerFlight VALUES (1, 14, 1, 0);
INSERT INTO Flight VALUES (16, 'Delta', 'A2', DATE '2021-07-10', 0100, 0100, 100, 0, 0);
INSERT INTO Flight VALUES (17, 'Delta', 'A3', DATE '2021-06-13', 0000, 0000, 100, 0, 0);
INSERT INTO PassengerFlight VALUES (2, 16, 2, 0);
INSERT INTO PassengerFlight VALUES (2, 17, 0, 1);
INSERT INTO PassengerFlight VALUES (3, 16, 1, 0);
INSERT INTO PassengerFlight VALUES (3, 17, 3, 0);

-- -- Part 1
-- SELECT DISTINCT name
--   FROM Passenger
--     JOIN PassengerFlight USING (passenger_id)
--     JOIN Flight USING (flight_id)
--   WHERE airline LIKE '%' -- Replace with `= '<airline>'` 
--     AND 1 = 1  -- Replace first 1 with category
--     AND flight_date BETWEEN DATE '2021-03-01' AND DATE '2021-03-31'
--   GROUP BY name, passenger_id
--   HAVING COUNT(*) = 1;
-- -- Should just be Brad

-- -- Part 2
-- SELECT DISTINCT name
--   FROM Passenger
--     JOIN PassengerFlight USING (passenger_id)
--     JOIN Flight USING (flight_id)
--   WHERE airline LIKE '%' -- Replace with `= '<airline>'`
--     AND 1 = 1  -- Replace first 1 with category
--     AND flight_date BETWEEN DATE '2021-06-01' AND DATE '2021-07-31'
--     AND bags_checked = 1;
-- -- Should just be Dana

-- -- Part 3
-- SELECT DISTINCT name
--   FROM Passenger
--     JOIN PassengerFlight USING (passenger_id)
--     JOIN Flight USING (flight_id)
--   WHERE airline LIKE '%' -- Replace with `= '<airline>'`
--     AND 1 = 1  -- Replace first 1 with category
--     AND ordered_snacks = 1;
-- -- Should just be Cody

--Data For Query 5--
INSERT INTO Flight VALUES (21, 'Delta', 'A1', DATE '2021-03-01', 0200, 0400, 300, 1, 1);

-- Test Student Trigger
INSERT INTO Flight VALUES (18, 'Alaska', 'B1', DATE '2021-01-02', 0000, 0000, 100, 0, 0);
-- These two should succeed
INSERT INTO PassengerFlight VALUES (0, 18, 1, 1); 
INSERT INTO PassengerFlight VALUES (2, 18, 3, 1); 
-- -- These ones should fail because Brad isn't a student
-- INSERT INTO PassengerFlight VALUES (1, 18, 3, 1); 
-- UPDATE PassengerFlight SET passenger_id = 1 WHERE passenger_id = 2 AND flight_id = 18; 

-- Test Overlap Trigger
INSERT INTO Flight VALUES (19, 'Alaska', 'B2', DATE '2021-01-03', 0000, 0000, 100, 0, 0);
INSERT INTO Flight VALUES (20, 'Alaska', 'A1', DATE '2021-01-03', 0100, 0100, 100, 0, 0);
INSERT INTO PassengerFlight VALUES (0, 19, 1, 1);
-- -- These should fail because the flights overlap
-- INSERT INTO PassengerFlight VALUES (0, 20, 1, 1);
-- UPDATE PassengerFlight SET flight_id = 20 WHERE passenger_id = 0 AND flight_id = 18;

-- TODO:
--  - Put staff in flights or whatever.  Idk, we'll probably be graded on it or whatever.

COMMIT;