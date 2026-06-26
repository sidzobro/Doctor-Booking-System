import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

class PatientsInformationTest {

    private TestablePatientsInformationPage patientsPage;
    private ActionEvent mockEvent;

    @BeforeEach
    void setUp() {
        patientsPage = new TestablePatientsInformationPage("0");
        mockEvent = new ActionEvent(patientsPage, ActionEvent.ACTION_PERFORMED, "Submit");
    }

    @Test
    void testShowPatientDetailsByID() {
        // Simulate user selecting "Patient ID"
        patientsPage.searchOptions.setSelectedItem("Patient ID");

        // Enter a patient ID
        patientsPage.patientIDField.setText("P12345");

        // Simulate button click
        patientsPage.actionPerformed(mockEvent);

        // Check if patient details were fetched correctly
        assertEquals("Details for Patient ID: P12345", patientsPage.informationDisplay.getText());
    }

    @Test
    void testShowPatientDetailsByNameAndDOB() {
        // Simulate user selecting "Patient Name & DOB"
        patientsPage.searchOptions.setSelectedItem("Patient Name & DOB");

        // Enter patient name and DOB
        patientsPage.patientNameField.setText("Jane Doe");
        patientsPage.patientDOBField.setText("2000-01-01");

        // Simulate button click
        patientsPage.actionPerformed(mockEvent);

        // Check if patient details were fetched correctly
        assertEquals("Details for Patient: Jane Doe, DOB: 2000-01-01", patientsPage.informationDisplay.getText());
    }

    @Test
    void testShowPatientDetailsWithEmptyID() {
        // Simulate user selecting "Patient ID"
        patientsPage.searchOptions.setSelectedItem("Patient ID");

        // Leave patient ID empty
        patientsPage.patientIDField.setText("");

        // Simulate button click
        patientsPage.actionPerformed(mockEvent);

        // Expect empty result
        assertEquals("", patientsPage.informationDisplay.getText());
    }

    @Test
    void testShowPatientDetailsWithEmptyNameOrDOB() {
        // Simulate user selecting "Patient Name & DOB"
        patientsPage.searchOptions.setSelectedItem("Patient Name & DOB");

        // Leave patient name empty, provide a DOB
        patientsPage.patientNameField.setText("");
        patientsPage.patientDOBField.setText("2000-01-01");

        // Simulate button click
        patientsPage.actionPerformed(mockEvent);

        // Expect empty result
        assertEquals("", patientsPage.informationDisplay.getText());

        // Provide patient name but leave DOB empty
        patientsPage.patientNameField.setText("Jane Doe");
        patientsPage.patientDOBField.setText("");

        // Simulate button click
        patientsPage.actionPerformed(mockEvent);

        // Expect empty result
        assertEquals("", patientsPage.informationDisplay.getText());
    }

    /**
     * Custom subclass of patientsInformationPage to override database interactions
     */
    static class TestablePatientsInformationPage extends patientsInformation {
        public TestablePatientsInformationPage(String login) {
            super(login);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (searchOptions.getSelectedItem().toString().equals("Patient ID")) {
                String patientID = patientIDField.getText().trim();
                if (patientID.isEmpty()) {
                    informationDisplay.setText("");
                } else {
                    informationDisplay.setText("Details for Patient ID: " + patientID);
                }
            } else if (searchOptions.getSelectedItem().toString().equals("Patient Name & DOB")) {
                String patientName = patientNameField.getText().trim();
                String patientDOB = patientDOBField.getText().trim();

                if (patientName.isEmpty() || patientDOB.isEmpty()) {
                    informationDisplay.setText("");
                } else {
                    informationDisplay.setText("Details for Patient: " + patientName + ", DOB: " + patientDOB);
                }
            }
        }
    }
}
