import DataBase.DBManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Doctor Reassignment System
 * @version 1.2 (04/04/2025)
 */
public class DoctorReassignment {
    private JFrame frame;

    // UI components for booking lookup
    private JTextField bookingIdField;
    private JButton searchBookingButton;

    // Labels to show original booking info
    private JLabel currentPatientLabel;
    private JLabel currentDoctorLabel;

    // UI components for new doctor selection
    private JComboBox<String> doctorDropdown;
    private JButton assignButton;

    private DBManager dbManager;
    // Map to hold display doctor name -> doctor ID (for new doctor assignment)
    private Map<String, String> doctorMap = new HashMap<>();

    // Store current booking info for update
    private String currentBookingId;
    private String currentPatientId;
    private String userID;

    public DoctorReassignment(String login) {
        userID = login;
        dbManager = new DBManager(userID);
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Doctor Reassignment");
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // --- Top Panel: Booking ID Entry ---
        JPanel bookingPanel = new JPanel(new FlowLayout());
        bookingPanel.add(new JLabel("Enter Booking ID:"));
        bookingIdField = new JTextField(10);
        bookingPanel.add(bookingIdField);
        searchBookingButton = new JButton("Search Booking");
        bookingPanel.add(searchBookingButton);
        frame.add(bookingPanel, BorderLayout.NORTH);

        // --- Center Panel: Display Booking Info & New Doctor Selection ---
        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        centerPanel.add(new JLabel("Current Patient:"));
        currentPatientLabel = new JLabel("---");
        centerPanel.add(currentPatientLabel);

        centerPanel.add(new JLabel("Current Doctor:"));
        currentDoctorLabel = new JLabel("---");
        centerPanel.add(currentDoctorLabel);

        centerPanel.add(new JLabel("New Doctor:"));
        // Populate doctor dropdown (similar to previous logic)
        List<String> rawDoctorNames = dbManager.getAllDoctors();
        List<String> displayDoctorNames = rawDoctorNames.stream()
                .map(name -> "Dr. " + name)
                .collect(Collectors.toList());
        doctorDropdown = new JComboBox<>(displayDoctorNames.toArray(new String[0]));
        // Build mapping: display name -> doctor ID (lookup by last name)
        for (String displayName : displayDoctorNames) {
            String lastName = displayName.replace("Dr. ", "");
            try {
                doctorMap.put(displayName, dbManager.getDoctorIdByName(lastName));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        centerPanel.add(doctorDropdown);

        // An empty cell for spacing
        centerPanel.add(new JLabel(""));

        // Assign button row
        assignButton = new JButton("Assign New Doctor");
        assignButton.setEnabled(false); // initially disabled until booking info is found
        centerPanel.add(assignButton);

        frame.add(centerPanel, BorderLayout.CENTER);

        // --- Listeners ---
        searchBookingButton.addActionListener(this::searchBooking);
        assignButton.addActionListener(this::assignNewDoctor);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Looks up booking information based on the entered booking ID.
     */
    private void searchBooking(ActionEvent e) {
        String bookingId = bookingIdField.getText().trim();
        if (bookingId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a booking ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Retrieve booking info (assumes DBManager.getBookingInfo returns a BookingInfo object)
            DBManager.BookingInfo bookingInfo = dbManager.getBookingInfo(bookingId);
            currentBookingId = bookingId;
            currentPatientId = bookingInfo.getPatientId();

            // Retrieve and display patient details (for simplicity, using getPatientDetails and extracting the name)
            String patientDetails = dbManager.getPatientDetails(currentPatientId);
            // Assuming patientDetails starts with "Patient's Name: FirstName LastName"
            String[] lines = patientDetails.split("\n");
            currentPatientLabel.setText(lines[0].replace("Patient's Name: ", ""));

            // Get current doctor's name using the doctor ID from the booking info.
            String currentDoctorId = bookingInfo.getDoctorId();
            String currentDoctorName = dbManager.getDoctorNameById(currentDoctorId);
            currentDoctorLabel.setText("Dr. " + currentDoctorName);

            // Enable the assign button now that booking info is available.
            assignButton.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error retrieving booking info: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Updates the booking record with the new doctor.
     */
    private void assignNewDoctor(ActionEvent e) {
        String selectedDoctorDisplay = (String) doctorDropdown.getSelectedItem();
        if (selectedDoctorDisplay == null || currentBookingId == null) {
            JOptionPane.showMessageDialog(frame, "No booking or doctor selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String newDoctorId = doctorMap.get(selectedDoctorDisplay);
        try {
            boolean success = dbManager.updateBookingDoctor(currentBookingId, newDoctorId);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Doctor reassignment successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Optionally, you could call a method to send confirmation emails here.
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to assign new doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error updating booking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorReassignment("0"));
    }
}