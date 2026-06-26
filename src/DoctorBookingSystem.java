import DataBase.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Doctor Booking system (now using DBManager)
 * @version 1.3 (10/03/2025)
 */
public class DoctorBookingSystem {
    private JFrame frame;
    private JTextArea bookingDisplay;
    private JComboBox<String> dayDropdown;
    private JTextField dayField;

    // Use DBManager instead of a local bookings map.
    private DBManager dbManager;
    private String userID;

    public DoctorBookingSystem(String userID) {
        // Instantiate the database manager
        this.userID = userID;
        dbManager = new DBManager(userID);
        initializeUI();
    }

    public JTextArea getBookingDisplay(){
        return bookingDisplay;
    }

    public void setDbManager(DBManager dbManager){
        this.dbManager = dbManager;
    }


    /**
     * Create the UI for this page.
     */
    private void initializeUI() {
        frame = new JFrame("Doctor Booking System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        bookingDisplay = new JTextArea();
        bookingDisplay.setEditable(false);
        frame.add(new JScrollPane(bookingDisplay), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        JLabel yearLabel = new JLabel("Year:");
        JLabel monthLabel = new JLabel("Month:");
        JLabel dayLabel = new JLabel("Day:");

        JTextField yearField = new JTextField("2025");
        JTextField monthField = new JTextField("04");

        String[] days = new String[30];
        for (int i = 1; i <= 30; i++) {
            days[i - 1] = String.valueOf(i); // No leading zero
        }

        dayDropdown = new JComboBox<>(days);
        dayField = new JTextField(); // Manual input option

        inputPanel.add(yearLabel);
        inputPanel.add(yearField);
        inputPanel.add(monthLabel);
        inputPanel.add(monthField);
        inputPanel.add(dayLabel);
        inputPanel.add(dayDropdown);
        inputPanel.add(new JLabel("Or enter manually:"));
        inputPanel.add(dayField);

        JPanel buttonPanel = new JPanel();
        JButton showBookingsButton = new JButton("Show Bookings");

        showBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String year = yearField.getText().trim();
                String month = monthField.getText().trim();
                String selectedDay = (String) dayDropdown.getSelectedItem();
                String manualDay = dayField.getText().trim();

                String day = manualDay.isEmpty() ? selectedDay : manualDay;
                if (!day.isEmpty()) {
                    day = String.format("%02d", Integer.parseInt(day)); // Ensures format "01", "02", ..., "30"
                    String date = String.format("%s-%s-%s", year, month, day);
                    showBookings(date);
                }
            }
        });

        buttonPanel.add(showBookingsButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Fetch bookings from the database using DBManager and display
     * @param date date
     */
    public void showBookings(String date) {
        if (isValidDate(date)) {
            List<String> dailyBookings = dbManager.getBookings(date);
            StringBuilder sb = new StringBuilder();
            if (dailyBookings != null && !dailyBookings.isEmpty()) {
                for (String booking : dailyBookings) {
                    sb.append(booking).append("\n");
                }
            } else {
                sb.append("No bookings for this date.");
            }
            // Update the booking display
            bookingDisplay.setText(sb.toString());
        } else {
            bookingDisplay.setText("There are no bookings available for the selected Date.");
        }
    }

    /**
     * Check if the date is valid
     * @param date Date to be checked
     * @return Valid or not
     */
    public boolean isValidDate(String date) {
        return date.matches("2025-04-(0[1-9]|[12][0-9]|30)");
    }

    /**
     * Show the message.
     * @param message Message to be shown
     */
    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Booking Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Test, to open this page directly
     * @param args []
     */
    public static void main(String[] args) {
        new DoctorBookingSystem ("0");
    }
}