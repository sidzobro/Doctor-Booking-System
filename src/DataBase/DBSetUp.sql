-- Drop tables if they already exist
DROP TABLE IF EXISTS login;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS UserActionLog;

-- =========================
-- Drop all OG table and create a new one
-- =========================
CREATE TABLE doctors (
    Doctor_ID     VARCHAR(50)  NOT NULL,
    First_Name    VARCHAR(50)  NOT NULL,
    Last_Name     VARCHAR(50)  NOT NULL,
    Phone_Number  VARCHAR(20),
    DOB           DATE,
    Gender        CHAR(1),
    Email         VARCHAR(50),
    PRIMARY KEY (Doctor_ID)
) ENGINE=InnoDB;

-- Insert sample doctors
INSERT INTO doctors (Doctor_ID, First_Name, Last_Name, Phone_Number, DOB, Gender, Email)
VALUES
('cw743', 'Edan',    'Wong',     '07463000000', '2005-05-10', 'M', 'cw743@kent.ac.uk'),
('ss2765', 'Siddhant', 'Shrestha', '07313000001', '2003-06-10', 'M', 'ss2765@kent.ac.uk'),
('ar841', 'Alfred', 'Rich', '07222000002', '2005-07-11', 'M', 'ar841@kent.ac.uk'),
('bn221', 'Benjamin', 'Nneji', '07223000000', '2005-08-20', 'M', 'bn221@kent.ac.uk'),
('DOC001', 'John',    'Smith',     '555-123-4567', '1970-05-10', 'M', 'john.smith@clinic.com'),
('DOC002', 'Emily',   'Johnson',   '555-234-5678', '1980-03-15', 'F', 'emily.johnson@clinic.com'),
('DOC003', 'Michael', 'Brown',     '555-345-6789', '1975-12-01', 'M', 'michael.brown@clinic.com'),
('DOC004', 'Sarah',   'Miller',    '555-456-7890', '1985-07-20', 'F', 'sarah.miller@clinic.com'),
('DOC005', 'David',   'Wilson',    '555-567-8901', '1968-11-09', 'M', 'david.wilson@clinic.com'),
('DOC006', 'Karen',   'Davis',     '555-678-9012', '1972-08-25', 'F', 'karen.davis@clinic.com'),
('DOC007', 'Daniel',  'Garcia',    '555-789-0123', '1978-09-14', 'M', 'daniel.garcia@clinic.com'),
('DOC008', 'Jessica', 'Rodriguez', '555-890-1234', '1990-01-30', 'F', 'jessica.rodriguez@clinic.com'),
('DOC009', 'Robert',  'Martinez',  '555-901-2345', '1965-04-18', 'M', 'robert.martinez@clinic.com'),
('DOC010', 'Patricia','Anderson',  '555-012-3456', '1977-06-02', 'F', 'patricia.anderson@clinic.com');


-- ==========================
-- 2) Create 'patients' Table
-- ==========================
CREATE TABLE patients (
    Patient_ID    VARCHAR(50) NOT NULL,
    First_Name    VARCHAR(50) NOT NULL,
    Last_Name     VARCHAR(50) NOT NULL,
    DOB           DATE,
    Gender        CHAR(1),
    Phone_Number  VARCHAR(20),
    Notes         VARCHAR(100),
    Email         VARCHAR(50),
    PRIMARY KEY (Patient_ID)
) ENGINE=InnoDB;

-- Insert sample patients
INSERT INTO patients (Patient_ID, First_Name, Last_Name, DOB, Gender, Phone_Number, Notes, Email)
VALUES
('PAT001', 'John',    'Doe',     '1985-01-15', 'M', '07123456789',  'N/A',                     'john.doe@example.com'),
('PAT002', 'Jane',    'Smith',   '1990-05-30', 'F', '07345678901',  'Allergic to penicillin',  'jane.smith@example.com'),
('PAT003', 'David',   'Johnson', '1978-11-02', 'M', '07456789123',  'Diabetic',                'david.johnson@example.com'),
('PAT004', 'Emily',   'Clark',   '1988-03-10', 'F', '07456789124',  'N/A',                     'emily.clark@example.com'),
('PAT005', 'Michael', 'Brown',   '2000-09-10', 'M', '07567891234',  'Asthmatic',               'michael.brown@example.com');

-- ==========================
-- 3) Create 'bookings' Table
-- ==========================
CREATE TABLE bookings (
    Booking_ID      INT          NOT NULL AUTO_INCREMENT,
    Booking_Date    DATE         NOT NULL,
    Booking_Time    TIME         NOT NULL,
    Patient_Name    VARCHAR(100),
    Patient_ID      VARCHAR(50),
    prescriptions   VARCHAR(100),
    visit_details   VARCHAR(100),
    Doctor_ID       VARCHAR(50),
    PRIMARY KEY (Booking_ID),
    CONSTRAINT fk_patient
        FOREIGN KEY (Patient_ID) REFERENCES patients(Patient_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_doctor
        FOREIGN KEY (Doctor_ID) REFERENCES doctors(Doctor_ID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Insert sample bookings
INSERT INTO bookings (
    Booking_Date, 
    Booking_Time, 
    Patient_Name, 
    Patient_ID, 
    prescriptions, 
    visit_details, 
    Doctor_ID
)
VALUES
('2025-04-05', '10:00:00', 'John Doe',    'PAT001', 'Tylenol',  'Mild headache',    'DOC001'),
('2025-04-05', '11:30:00', 'Jane Smith',  'PAT002', 'Penicillin','Routine checkup',  'DOC002'),
('2025-04-06', '14:15:00', 'David Johnson','PAT003', 'Insulin',  'Diabetic followup','DOC001'),
('2025-04-07', '09:00:00', 'Emily Clark', 'PAT004', 'Ventolin', 'Asthma check',     'DOC005'),
('2025-04-07', '13:30:00', 'Michael Brown','PAT005', 'N/A',     'General checkup',  'DOC010');

-- Create table login
CREATE TABLE login (
    userLogin VARCHAR(10) NOT NULL,
    password  VARCHAR(100) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName  VARCHAR(50) NOT NULL,
    PRIMARY KEY (userLogin),
    CONSTRAINT FK_doctor_id FOREIGN KEY (userLogin) REFERENCES doctors(Doctor_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO login (userLogin, password, firstName, lastName)
VALUES
('cw743', 'cw743', 'Edan', 'Wong'),
('ss2765', 'ss2765', 'Siddhant', 'Shrestha'),
('ar841', 'ar841', 'Alfred', 'Rich'),
('bn221', 'bn221','Benjamin', 'Nneji');


-- Create table UserActionLog
CREATE TABLE UserActionLog (
    UserID VARCHAR(50) NOT NULL,
    UsageDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FunctionUsed VARCHAR(255) NOT NULL,
    PRIMARY KEY (UserID, UsageDateTime)
);


