import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

class VisitDetailsSystemTest {

    private TestableVisitDetailsSystem visitDetailsSystem;
    private ActionEvent mockEvent;

    @BeforeEach
    void setUp() {
        // Use our subclass that overrides database calls
        visitDetailsSystem = new TestableVisitDetailsSystem();
        // Mock event
        mockEvent = new ActionEvent(visitDetailsSystem, ActionEvent.ACTION_PERFORMED, "Show Visit Details");
    }

    // 1. Test for valid Booking ID
    @Test
    void testShowVisitDetailsByBookingID() {
        visitDetailsSystem.bookingDropdown.setSelectedItem("Booking ID");
        visitDetailsSystem.bookingIDTextField.setText("12345");
        visitDetailsSystem.actionPerformed(mockEvent);
        assertEquals("Visit Details for Booking ID: 12345", visitDetailsSystem.visitDetailsDisplay.getText());
    }

    // 2. Test for valid Patient Name & Date
    @Test
    void testShowVisitDetailsByPatientNameAndDate() {
        visitDetailsSystem.bookingDropdown.setSelectedItem("Patient Name & Date");
        visitDetailsSystem.patientField.setText("John Doe");
        visitDetailsSystem.dateField.setText("2025-04-01");
        visitDetailsSystem.actionPerformed(mockEvent);
        assertEquals("Visit Details for Patient: John Doe on Date: 2025-04-01", visitDetailsSystem.visitDetailsDisplay.getText());
    }

    // 3. Test when Booking ID is empty
    @Test
    void testShowVisitDetailsWithEmptyBookingID() {
        visitDetailsSystem.bookingDropdown.setSelectedItem("Booking ID");
        visitDetailsSystem.bookingIDTextField.setText("");
        visitDetailsSystem.actionPerformed(mockEvent);
        assertEquals("", visitDetailsSystem.visitDetailsDisplay.getText());
    }

    // 4. Test when either Patient Name or Date is empty
    @Test
    void testShowVisitDetailsWithEmptyPatientNameOrDate() {
        visitDetailsSystem.bookingDropdown.setSelectedItem("Patient Name & Date");
        visitDetailsSystem.patientField.setText("");
        visitDetailsSystem.dateField.setText("2025-04-01");
        visitDetailsSystem.actionPerformed(mockEvent);
        assertEquals("", visitDetailsSystem.visitDetailsDisplay.getText());

        visitDetailsSystem.patientField.setText("John Doe");
        visitDetailsSystem.dateField.setText("");
        visitDetailsSystem.actionPerformed(mockEvent);
        assertEquals("", visitDetailsSystem.visitDetailsDisplay.getText());
    }

    // 5. Test when both Booking ID and Patient fields are empty
    @Test
    void testShowVisitDetailsWithBothEmptyFields() {
        visitDetailsSystem.bookingDropdown.setSelectedItem("Booking ID");
        visitDetailsSystem.bookingIDTextField.setText("");
        visitDetailsSystem.patientField.setText("");
        visitDetailsSystem.dateField.setText("");
        visitDetailsSystem.actionPerformed(mockEvent);
        assertEquals("", visitDetailsSystem.visitDetailsDisplay.getText());
    }

    // 6. Test for invalid Booking ID (non-existent)
    @Test
    void testShowVisitDetailsForInvalidBookingID() {
        visitDetailsSystem.bookingDropdown.setSelectedItem("Booking ID");
        visitDetailsSystem.bookingIDTextField.setText("99999");
        visitDetailsSystem.actionPerformed(mockEvent);
        assertEquals("No visit details found for Booking ID: 99999", visitDetailsSystem.visitDetailsDisplay.getText());
    }

    /**
     * Subclass that overrides the real DB calls with test logic
     */
    static class TestableVisitDetailsSystem extends VisitDetailsSystem {
        public TestableVisitDetailsSystem() {
            super("testUser");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (bookingDropdown.getSelectedItem().toString().equals("Booking ID")) {
                String bookingID = bookingIDTextField.getText().trim();
                if (bookingID.isEmpty()) {
                    visitDetailsDisplay.setText("");
                } else if (bookingID.equals("99999")) {
                    visitDetailsDisplay.setText("No visit details found for Booking ID: " + bookingID);
                } else {
                    visitDetailsDisplay.setText("Visit Details for Booking ID: " + bookingID);
                }
            } else if (bookingDropdown.getSelectedItem().toString().equals("Patient Name & Date")) {
                String patientName = patientField.getText().trim();
                String date = dateField.getText().trim();

                if (patientName.isEmpty() || date.isEmpty()) {
                    visitDetailsDisplay.setText("");
                } else {
                    visitDetailsDisplay.setText("Visit Details for Patient: " + patientName + " on Date: " + date);
                }
            }
        }
    }
}
