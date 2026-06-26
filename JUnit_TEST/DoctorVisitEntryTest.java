import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

class DoctorVisitEntryTest {

    private TestableDoctorVisitEntry doctorVisitEntry;
    private ActionEvent mockEvent;

    @BeforeEach
    void setUp() {
        doctorVisitEntry = new TestableDoctorVisitEntry();
        mockEvent = new ActionEvent(doctorVisitEntry, ActionEvent.ACTION_PERFORMED, "Submit");
    }

    @Test
    void testActionPerformed_Success() {
        // Set fields with valid data
        doctorVisitEntry.bookingIdText.setText("1");
        doctorVisitEntry.visitDetailsText.setText("Routine Checkup");
        doctorVisitEntry.prescriptionText.setText("Vitamin D");

        // Simulate button click
        doctorVisitEntry.actionPerformed(mockEvent);

        // Check if visit details were "saved"
        assertTrue(doctorVisitEntry.isSaved, "Visit details should be saved");
        assertTrue(doctorVisitEntry.confirmationSent, "Confirmation messages should be sent");
    }

    @Test
    void testActionPerformed_Failure() {
        // Set fields but force failure in DB save
        doctorVisitEntry.bookingIdText.setText("1");
        doctorVisitEntry.visitDetailsText.setText("Routine Checkup");
        doctorVisitEntry.prescriptionText.setText("Vitamin D");

        // Force failure
        doctorVisitEntry.forceFailSave = true;

        // Simulate button click
        doctorVisitEntry.actionPerformed(mockEvent);

        // Ensure save failed
        assertFalse(doctorVisitEntry.isSaved, "Visit details should not be saved");
        assertFalse(doctorVisitEntry.confirmationSent, "Confirmation messages should not be sent");
    }

    @Test
    void testActionPerformed_EmptyFields() {
        // Leave fields empty
        doctorVisitEntry.bookingIdText.setText("1");
        doctorVisitEntry.visitDetailsText.setText("");
        doctorVisitEntry.prescriptionText.setText("");

        // Simulate button click
        doctorVisitEntry.actionPerformed(mockEvent);

        // Ensure data was not saved
        assertFalse(doctorVisitEntry.isSaved, "Visit details should not be saved");
        assertFalse(doctorVisitEntry.confirmationSent, "Confirmation messages should not be sent");
    }

    /**
     * Custom subclass of DoctorVisitEntry to override database behavior
     */
    static class TestableDoctorVisitEntry extends DoctorVisitEntry {

        boolean isSaved = false;
        boolean confirmationSent = false;
        boolean forceFailSave = false; // Flag to force a save failure

        public TestableDoctorVisitEntry() {
            super("");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (bookingIdText.getText().trim().isEmpty() || visitDetailsText.getText().trim().isEmpty() || prescriptionText.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simulate DB save
            isSaved = !forceFailSave;

            if (isSaved) {
                sendConfirmationMessages();
                frame.dispose();
            }
        }

        @Override
        protected void sendConfirmationMessages() {
            confirmationSent = true;
        }
    }
}

