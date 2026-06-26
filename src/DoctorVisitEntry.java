import DataBase.DBManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Doctor Visit Entry for pass bookings
 * @version 1.2 (21/03/2025)
 */
public class DoctorVisitEntry implements ActionListener {

    public final JFrame frame;
    public final JTextField bookingIdText;
    public final JTextArea visitDetailsText;
    public final JTextArea prescriptionText;
    private String bookingId;
    private String userID;

    /**
     * Constructor for the interface
     */
    public DoctorVisitEntry(String userID) {
        this.userID = userID;

        frame = new JFrame("Doctor Visit Entry");

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        bookingPanel.add(new JLabel("Booking ID:"));
        bookingIdText = new JTextField(bookingId, 10);
        bookingPanel.add(bookingIdText);
        panel.add(bookingPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel visitPanel = new JPanel(new BorderLayout());
        visitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        visitPanel.add(new JLabel("Visit Details:"), BorderLayout.NORTH);
        visitDetailsText = new JTextArea();
        visitDetailsText.setLineWrap(true);
        visitDetailsText.setWrapStyleWord(true);
        JScrollPane scrollVisit = new JScrollPane(visitDetailsText);
        visitPanel.add(scrollVisit, BorderLayout.CENTER);
        centerPanel.add(visitPanel);

        JPanel prescriptionPanel = new JPanel(new BorderLayout());
        prescriptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        prescriptionPanel.add(new JLabel("Prescription:"), BorderLayout.NORTH);
        prescriptionText = new JTextArea();
        prescriptionText.setLineWrap(true);
        prescriptionText.setWrapStyleWord(true);
        JScrollPane scrollPrescription = new JScrollPane(prescriptionText);
        prescriptionPanel.add(scrollPrescription, BorderLayout.CENTER);
        centerPanel.add(prescriptionPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        buttonPanel.add(submitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Action after submit button was pressed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String visitDetails = visitDetailsText.getText().trim();
        String prescription = prescriptionText.getText().trim();
        String bookingID = bookingIdText.getText().trim();
        this.bookingId = bookingID;

        if (visitDetails.isEmpty() || prescription.isEmpty() || bookingID.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DBManager dbManager = new DBManager(userID);
        boolean success = dbManager.saveVisitDetails(Integer.parseInt(bookingId), visitDetails, prescription);

        if (success) {
            JOptionPane.showMessageDialog(frame, "Visit details saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            sendConfirmationMessages();
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to save visit details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ask the server to send confirmation messages
     */
    protected void sendConfirmationMessages() {
        DBManager dbManager = new DBManager(userID);
        dbManager.sendConfirmationToPatient(bookingId);
        dbManager.sendConfirmationToDoctor(bookingId);
    }

    /**
     * For testing
     * @param args
     */
    public static void main(String[] args) {
        new DoctorVisitEntry("0");
    }
}
