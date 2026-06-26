import DataBase.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Visit Details and Prescriptions (Doctor's View)
 * @version 1.3 (20/03/2025)
 */
public class VisitDetailsSystem implements ActionListener{
    protected JFrame frame;
    protected JTextArea visitDetailsDisplay;
    protected JComboBox<String> bookingDropdown;
    protected JTextField bookingIDTextField;

    protected JTextField patientField;

    protected JTextField dateField;

    protected JTextArea visitDetailsArea;
    protected JTextArea prescriptionArea;

    private DBManager dbManager;
    private String userID;


    public VisitDetailsSystem(String login) {
        userID = login;
        dbManager = new DBManager(userID);
        initializeUI();
    }

    /**
     * Create the UI for this page.
     */
    private void initializeUI() {
        frame = new JFrame("Visit Details System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        visitDetailsDisplay = new JTextArea();
        visitDetailsDisplay.setEditable(false);
        frame.add(new JScrollPane(visitDetailsDisplay), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel bookingIDLabel = new JLabel("Booking ID:");
        JLabel dateLabel = new JLabel("Booking Date:");
        JLabel patientLabel = new JLabel("Patient Name:");
        JLabel visitDetailsLabel = new JLabel("Visit Details:");  // New label for visit details
        JLabel prescriptionLabel = new JLabel("Prescription:");

        bookingIDTextField = new JTextField();
        dateField = new JTextField("2025-04-01");
        patientField = new JTextField();
        visitDetailsArea = new JTextArea();
        prescriptionArea = new JTextArea();

        String[] bookingOptions = {"Booking ID", "Patient Name & Date"};
        bookingDropdown = new JComboBox<>(bookingOptions);

        inputPanel.add(new JLabel("Select options:"));
        inputPanel.add(bookingDropdown);
        inputPanel.add(bookingIDLabel);
        inputPanel.add(bookingIDTextField);

        inputPanel.add(visitDetailsLabel);
        inputPanel.add(new JScrollPane(visitDetailsArea)); // **Scrollable text area for visit details**

        inputPanel.add(prescriptionLabel);
        inputPanel.add(new JScrollPane(prescriptionArea));

        JPanel buttonPanel = new JPanel();
        JButton showVisitDetailsButton = new JButton("Show Visit Details");
        JButton updateVisitDetailsButton = new JButton("Update Visit Details"); // **New button to update visit details**

        showVisitDetailsButton.addActionListener(this);
        updateVisitDetailsButton.addActionListener(e -> updateVisitDetails());  // **Action listener for the new button**

        buttonPanel.add(showVisitDetailsButton);
        buttonPanel.add(updateVisitDetailsButton);

        bookingDropdown.addActionListener(e -> {
            String selected = bookingDropdown.getSelectedItem().toString();
            if(selected.equals("Booking ID")){
                inputPanel.add(bookingIDLabel);
                inputPanel.add(bookingIDTextField);

                inputPanel.remove(patientLabel);
                inputPanel.remove(patientField);
                inputPanel.remove(dateLabel);
                inputPanel.remove(dateField);

                visitDetailsDisplay.setText("");
            }
            else if(selected.equals("Patient Name & Date")){
                inputPanel.remove(bookingIDLabel);
                inputPanel.remove(bookingIDTextField);

                inputPanel.add(patientLabel);
                inputPanel.add(patientField);
                inputPanel.add(dateLabel);
                inputPanel.add(dateField);

                visitDetailsDisplay.setText("");
            }
            inputPanel.revalidate();
            inputPanel.repaint();
        });

        showVisitDetailsButton.addActionListener(this);
        buttonPanel.add(showVisitDetailsButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updateVisitDetails() {
        String bookingID = bookingIDTextField.getText().trim();
        String visitDetails = visitDetailsArea.getText().trim();
        String prescription = prescriptionArea.getText().trim();

        boolean success = dbManager.saveVisitDetails(Integer.parseInt(bookingID), visitDetails, prescription);
        if (success) {
            dbManager.sendConfirmationToPatient(bookingID);
            dbManager.sendConfirmationToDoctor(bookingID);
            JOptionPane.showMessageDialog(frame, "Visit details updated successfully. Confirmations sent.");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to update visit details.");
        }
    }


    public void actionPerformed(ActionEvent e) {
        if(bookingDropdown.getSelectedItem().toString().equals("Booking ID")){
            System.out.printf("O1");
            String bookingID = bookingIDTextField.getText();
            System.out.println(bookingID);
            visitDetailsDisplay.setText(dbManager.getVisitDetails(bookingID));
        } else if(bookingDropdown.getSelectedItem().toString().equals("Patient Name & Date")){
            System.out.println("O2");
            String patientName = patientField.getText().trim();
            String date = dateField.getText().trim();

            System.out.println(patientName);
            System.out.println(date);

            visitDetailsDisplay.setText(dbManager.getVisitDetails(patientName, date));
        }
    }

    /**
     * Test, to open this page directly
     * @param args []
     */
    public static void main(String[] args) {
        new VisitDetailsSystem("0");
    }
}

