import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest {

    @Test
    /**
     * A test to validate that the login page can be accessed with the correct credentials.
     */
    public void testLoginPageValidCredentials() {
        String testUser = "cw743";
        String testPassword = "cw743";

        LoginPage loginPage = new LoginPage();


        loginPage.frame.setVisible(true);


        JTextField loginTextField = findTextFieldByPosition(loginPage.frame, 0);
        JTextField passwordTextField = findTextFieldByPosition(loginPage.frame, 1);


        assertNotNull(loginTextField, "Login text field should be found");
        assertNotNull(passwordTextField, "Password text field should be found");


        loginTextField.setText(testUser);
        passwordTextField.setText(testPassword);


        JButton loginButton = findButtonByText(loginPage.frame, "Login");
        assertNotNull(loginButton, "Login button should be found");

        loginButton.doClick();


        JLabel errorLabel = findLabelByText(loginPage.frame, "");

        assertNotNull(errorLabel, "Error label should be found");
        assertEquals("", errorLabel.getText(), "Error message should be empty for valid login");
    }

    @Test
    /**
     * A test to validate that the login page can not be accessed with invalid credentials.
     */
    public void testLoginPageInvalidCredentials() {

        String testUser = "invalidUser";
        String testPassword = "invalidPass";


        LoginPage loginPage = new LoginPage();


        loginPage.frame.setVisible(true);


        JTextField loginTextField = findTextFieldByPosition(loginPage.frame, 0);
        JTextField passwordTextField = findTextFieldByPosition(loginPage.frame, 1);


        assertNotNull(loginTextField, "Login text field should be found");
        assertNotNull(passwordTextField, "Password text field should be found");


        loginTextField.setText(testUser);
        passwordTextField.setText(testPassword);


        JButton loginButton = findButtonByText(loginPage.frame, "Login");
        assertNotNull(loginButton, "Login button should be found");

        loginButton.doClick();


        JLabel errorLabel = findLabelContainingText(loginPage.frame, "Wrong Password, please try again.");
        assertNotNull(errorLabel, "Error label should be found");
        assertEquals("Wrong Password, please try again.", errorLabel.getText(), "Error message should appear for invalid login");
    }

    /**
     * Recursively collects all components of a given type.
     */
    private <T> void collectComponents(Container container, Class<T> clazz, List<T> list) {
        for (Component comp : container.getComponents()) {
            if (clazz.isInstance(comp)) {
                list.add(clazz.cast(comp));
            }
            if (comp instanceof Container) {
                collectComponents((Container) comp, clazz, list);
            }
        }
    }

    /**
     * Helper method to find a JTextField by its position in the recursive search.
     */
    private JTextField findTextFieldByPosition(Container container, int position) {
        List<JTextField> textFields = new ArrayList<>();
        collectComponents(container, JTextField.class, textFields);
        if (position >= 0 && position < textFields.size()) {
            return textFields.get(position);
        }
        return null;
    }

    /**
     * Helper method to find a JButton by its text content recursively.
     */
    private JButton findButtonByText(Container container, String buttonText) {
        List<JButton> buttons = new ArrayList<>();
        collectComponents(container, JButton.class, buttons);
        for (JButton button : buttons) {
            if (buttonText.equals(button.getText())) {
                return button;
            }
        }
        return null;
    }

    /**
     * Helper method to find a JLabel that exactly matches the given text.
     */
    private JLabel findLabelByText(Container container, String labelText) {
        List<JLabel> labels = new ArrayList<>();
        collectComponents(container, JLabel.class, labels);
        for (JLabel label : labels) {
            if (labelText.equals(label.getText())) {
                return label;
            }
        }
        return null;
    }

    /**
     * Helper method to find a JLabel that contains the given text.
     */
    private JLabel findLabelContainingText(Container container, String text) {
        List<JLabel> labels = new ArrayList<>();
        collectComponents(container, JLabel.class, labels);
        for (JLabel label : labels) {
            if (label.getText() != null && label.getText().contains(text)) {
                return label;
            }
        }
        return null;
    }
}