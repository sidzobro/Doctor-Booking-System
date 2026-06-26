import DataBase.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Patients Information Page
 * @version 1.2 (17/03/2025)
 */
public class patientsInformation implements ActionListener {
    protected JFrame frame;
    protected JTextArea informationDisplay;
    protected JTextField patientIDField;
    protected JButton submitButton;
    protected JComboBox <String> searchOptions;
    protected JTextField patientNameField;
    protected JTextField patientDOBField;
    protected String userID;

    public patientsInformation(String login) {
        userID = login;

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Patient Information");

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(3,2));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(30,100,30,100));

        JLabel searchOptionLabel  = new JLabel("Search By:");
        searchOptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        searchOptions = new JComboBox<>( new String[]{"Patient ID", "Patient Name & DOB"});
        selectionPanel.add(searchOptionLabel);
        selectionPanel.add(searchOptions);

        JLabel patientIDLabel = new JLabel("Patient ID:");
        patientIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
        patientIDField = new JTextField();
        selectionPanel.add(patientIDLabel);
        selectionPanel.add(patientIDField);

        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        patientNameField = new JTextField();

        JLabel patientDOBLabel = new JLabel("Patient DOB:");
        patientDOBLabel.setHorizontalAlignment(SwingConstants.CENTER);
        patientDOBField = new JTextField();

        searchOptions.addActionListener(e -> {
            String selected = searchOptions.getSelectedItem().toString();
            if(selected.equals("Patient ID")){
                selectionPanel.add(patientIDLabel);
                selectionPanel.add(patientIDField);

                selectionPanel.remove(patientNameLabel);
                selectionPanel.remove(patientNameField);
                selectionPanel.remove(patientDOBLabel);
                selectionPanel.remove(patientDOBField);
            }
            else if(selected.equals("Patient Name & DOB")){
                selectionPanel.remove(patientIDLabel);
                selectionPanel.remove(patientIDField);

                selectionPanel.add(patientNameLabel);
                selectionPanel.add(patientNameField);
                selectionPanel.add(patientDOBLabel);
                selectionPanel.add(patientDOBField);
            }
            selectionPanel.revalidate();
            selectionPanel.repaint();
        });


        informationDisplay = new JTextArea();
        informationDisplay.setEditable(false);
        informationDisplay.setLineWrap(true);
        informationDisplay.setWrapStyleWord(true);

        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        buttonPanel.add(submitButton);

        frame.setSize(600, 500);
        frame.add(selectionPanel, BorderLayout.NORTH);
        frame.add(informationDisplay, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Submit the ID or Name + DOB to the DBManger and get the patient's info.
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e) {
        DBManager dbManager = new DBManager(userID);
        if(searchOptions.getSelectedItem().toString().equals("Patient ID")){
            String patientID = patientIDField.getText();
            System.out.println(patientID);
            informationDisplay.setText(dbManager.getPatientDetails(patientID));
        }else if(searchOptions.getSelectedItem().toString().equals("Patient Name & DOB")){
            String patientName = patientNameField.getText().trim();
            String patientDOB = patientDOBField.getText().trim();

            System.out.println(patientName);
            System.out.println(patientDOB);

            informationDisplay.setText(dbManager.getPatientDetails(patientName, patientDOB));
        }

    }

    public static void main(String[] args) {
        new patientsInformation("0");
    }

}
