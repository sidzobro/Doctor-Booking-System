package DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Database manager (Local, may not work on other computers)
 * @version 1.8 (04/04/2025)
 */

public class DBManager {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "Password123456";
    private final String DB_URL = "jdbc:mysql://localhost:3306/COMP5590-G16B";

    private String UserID; // Replace with actual user id when available

    public DBManager(String UserID) {
        this.UserID = UserID;
    }
    /**
     * Run test
     * @param args []
     */
    public static void main(String[] args) {
        DBManager dbManager = new DBManager("0");
        dbManager.TestConnection();
//        System.out.println(dbManager.loginCheck("cw743", "cw743"));
    }

    /**
     * Test connection and show data store in the table.
     */
    private void TestConnection() {
        try {
            System.out.println("Connecting to database...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/COMP5590-G16B", DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `COMP5590-G16B`.`login`;");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("userLogin") + " - "
                        + resultSet.getString("password") + " - "
                        + resultSet.getString("firstName") + " - "
                        + resultSet.getString("lastName"));
            }
            statement.close();
            connection.close();
            System.out.println("Connection closed \n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the action to the UserActionLog table.
     * @param functionName the name of the function being called.
     */
    private void logUserAction(String functionName) {
        String logQuery = "INSERT INTO UserActionLog (UserID, FunctionUsed) VALUES (?, ?)";

        try (Connection logConn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement logStmt = logConn.prepareStatement(logQuery)) {

            // If you haven't set UserID, default to "0" or some placeholder
            String userToLog = (UserID == null || UserID.isEmpty()) ? "0" : UserID;

            logStmt.setString(1, userToLog);
            logStmt.setString(2, functionName);
            logStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class BookingInfo {
        private String bookingId;
        private String patientId;
        private String doctorId;

        public BookingInfo(String bookingId, String patientId, String doctorId) {
            this.bookingId = bookingId;
            this.patientId = patientId;
            this.doctorId = doctorId;
        }

        public String getBookingId() {
            return bookingId;
        }

        public String getPatientId() {
            return patientId;
        }

        public String getDoctorId() {
            return doctorId;
        }
    }

    public BookingInfo getBookingInfo(String bookingId) throws SQLException {
        logUserAction("getBookingInfo");
        String sql = "SELECT Booking_ID, Patient_ID, Doctor_ID FROM bookings WHERE Booking_ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String bookingIdFound = rs.getString("Booking_ID");
                    String patientId = rs.getString("Patient_ID");
                    String doctorId = rs.getString("Doctor_ID");
                    return new BookingInfo(bookingIdFound, patientId, doctorId);
                } else {
                    throw new SQLException("Booking not found for ID: " + bookingId);
                }
            }
        }
    }

    public boolean updateBookingDoctor(String bookingId, String newDoctorId) throws SQLException {
        logUserAction("updateBookingDoctor");
        String sql = "UPDATE bookings SET Doctor_ID = ? WHERE Booking_ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newDoctorId);
            ps.setString(2, bookingId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public String getDoctorNameById(String doctorId) throws SQLException {
        logUserAction("getDoctorNameById");
        String sql = "SELECT Last_Name FROM doctors WHERE Doctor_ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Last_Name");
                } else {
                    throw new SQLException("Doctor not found for ID: " + doctorId);
                }
            }
        }
    }

    public List<String> getAllDoctors() {
        logUserAction("getAllDoctors");
        List<String> doctors = new ArrayList<>();
        String sql = "SELECT Doctor_ID, First_Name, Last_Name FROM doctors";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Store just the last name for lookup
                String lastName = rs.getString("Last_Name");
                doctors.add(lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Method to fetch doctor ID based on doctor's name
    public String getDoctorIdByName(String doctorLastName) throws SQLException {
        logUserAction("getDoctorIdByName");
        // Query by last name instead of first name
        String query = "SELECT Doctor_ID FROM doctors WHERE Last_Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorLastName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Doctor_ID");
                } else {
                    throw new SQLException("Doctor not found");
                }
            }
        }
    }

    /**
     * Checks if the login details are correct.
     * @param userName userLogin provided.
     * @param password password provided.
     * @return True if the login details are correct.
     */
    public boolean loginCheck(String userName, String password) {
        logUserAction("loginCheck");
        String sql = "SELECT EXISTS (SELECT 1 FROM login WHERE userLogin = ? AND password = ?) AS user_exists";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next() && resultSet.getInt("user_exists") == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get bookings from the database for a given date.
     * Table "bookings" with columns:
     * - bookingDate (in format "YYYY-MM-DD")
     * - patientName
     * - bookingTime
     *
     * @param date the date for which bookings are requested.
     * @return a List of booking detail strings.
     */
    public List<String> getBookings(String date) {
        logUserAction("getBookings");
        List<String> dailyBookings = new ArrayList<>();
        String sql = "SELECT Patient_Name, Booking_Time FROM bookings WHERE Booking_Date = ?";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, date);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String bookingDetails = "Booking for " + resultSet.getString("Patient_Name") +
                            " - " + date + " at " + resultSet.getString("Booking_Time");
                    dailyBookings.add(bookingDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dailyBookings;
    }

    /**
     * Fetch visit details and prescriptions from the database for a given booking using the bookingID.
     * @param bookingID The booking ID
     * @return Visit details and prescriptions for the booking.
     */
    public String getVisitDetails(String bookingID) {
        logUserAction("getVisitDetails");
        String sql = "SELECT * FROM bookings WHERE Booking_ID = ?";
        String result = "Booking not found.";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, bookingID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    result = "Booking Date: " + resultSet.getString("Booking_Date") +
                            "Booking Time: " + resultSet.getString("Booking_Time");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "Database error.";
        }
        return result;
    }

    /**
     * Fetch visit details and prescriptions from the database for a given booking using the patient's name and booking date.
     * @param booking_name patient's name
     * @param booking_date booking date
     * @return Visit details and prescriptions for the booking.
     */
    public String getVisitDetails(String booking_name, String booking_date) {
        logUserAction("getVisitDetails");
        String sql = "SELECT * FROM bookings WHERE Patient_Name = ? AND Booking_Date = ?";
        String result = "Booking not found.";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, booking_name);
            stmt.setString(2, booking_date);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    result = "Booking Date: " + resultSet.getString("Booking_Date") +
                            "Booking Time: " + resultSet.getString("Booking_Time");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "Database error.";
        }
        return result;
    }

    /**
     * Get the patient's info using their ID
     * @param patientID patient's ID
     * @return Patient's info
     */
    public String getPatientDetails(String patientID) {
        logUserAction("getPatientDetails");
        String sql = "SELECT * FROM patients WHERE Patient_ID = ?";
        String result = "Patient not found.";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, patientID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    result = "Patient's Name: " + resultSet.getString("First_Name") + " " + resultSet.getString("Last_Name") +
                            "\nDate of Birth: " + resultSet.getString("DOB") +
                            "\nGender: " + resultSet.getString("Gender") +
                            "\nNote: " + resultSet.getString("Notes");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "Database error.";
        }
        return result;
    }

    /**
     * Get the patient's info using their name and DOB
     * @param patientName Name of the patient
     * @param patientDOB DOB of the patient
     * @return Patient's info
     */
    public String getPatientDetails(String patientName, String patientDOB) {
        logUserAction("getPatientDetails");
        String sql = "SELECT * FROM patients WHERE First_Name = ? AND Last_Name = ? AND DOB = ?";
        String result = "Patient not found.";
        String[] nameParts = patientName.split(" ");
        if (nameParts.length < 2) return "Invalid patient name format.";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nameParts[0]);
            stmt.setString(2, nameParts[1]);
            stmt.setString(3, patientDOB);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    result = "Patient ID: " + resultSet.getString("Patient_ID") +
                            "\nPatient's Name: " + resultSet.getString("First_Name") + " " + resultSet.getString("Last_Name") +
                            "\nDate of Birth: " + resultSet.getString("DOB") +
                            "\nGender: " + resultSet.getString("Gender") +
                            "\nNote: " + resultSet.getString("Notes");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "Database error.";
        }
        return result;
    }

    /**
     * Use to send confirmation to patient
     * @param bookingId Booking ID for the booking that was affected
     */
    public void sendConfirmationToPatient(String bookingId) {
        logUserAction("sendConfirmationToPatient");
        String fetchQuery = "SELECT Patient_ID FROM bookings WHERE Booking_ID = ?";
        String insertQuery = "INSERT INTO messages (Recipient_ID, Message) VALUES (?, ?)";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement fetchStmt = connection.prepareStatement(fetchQuery);
            fetchStmt.setString(1, bookingId);
            try (ResultSet rs = fetchStmt.executeQuery()) {
                if (rs.next()) {
                    String patientId = rs.getString("Patient_ID");

                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, patientId);
                        insertStmt.setString(2, "Your appointment is confirmed. Booking ID: " + bookingId);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Store the visit details in the database.
     * @param bookingId booking ID.
     * @param visitDetails visitDetails for that booking.
     * @param prescription prescription for that booking.
     * @return True if the visit details store successfully.
     */
    public boolean saveVisitDetails(int bookingId, String visitDetails, String prescription) {
        logUserAction("saveVisitDetails");
        String sql = "UPDATE bookings SET visit_details = ?, prescription = ? WHERE Booking_ID = ?";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, visitDetails);
            stmt.setString(2, prescription);
            stmt.setInt(3, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Send confirmation to the doctor.
     * @param bookingId Booking ID for the booking that was affected
     */
    public void sendConfirmationToDoctor(String bookingId) {
        logUserAction("sendConfirmationToPatient");
        String fetchQuery = "SELECT Doctor_ID FROM bookings WHERE Booking_ID = ?";
        String insertQuery = "INSERT INTO messages (Recipient_ID, Message) VALUES (?, ?)";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement fetchStmt = connection.prepareStatement(fetchQuery);
            fetchStmt.setString(1, bookingId);
            try (ResultSet rs = fetchStmt.executeQuery()) {
                if (rs.next()) {
                    String doctorId = rs.getString("Doctor_ID");

                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, doctorId);
                        insertStmt.setString(2, "A new appointment has been booked. Booking ID: " + bookingId);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}