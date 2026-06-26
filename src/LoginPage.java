/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Login UI
 * @version 1.4 (20/03/2025)
 */

import DataBase.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Create the UI of the login interface.
 */
public class LoginPage implements ActionListener {

    JFrame frame = new JFrame();
    private JPanel loginPanel;
    private final JTextField LOGIN_TEXT;
    private final JTextField PASSWORD_TEXT;
    private final JLabel ERROR_LABEL;


    public LoginPage(){
        loginPanel = new JPanel();
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        loginPanel.setPreferredSize(new Dimension(600, 350));
        loginPanel.setLayout(new GridLayout(0,1));

        JLabel titleLabel = new JLabel("Login Page");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text horizontally
        titleLabel.setFont(new Font("Thoma", Font.BOLD, 28));
        loginPanel.add(titleLabel);

        JLabel login = new JLabel("Login:");
        loginPanel.add(login);
        LOGIN_TEXT = new JTextField();
        loginPanel.add(LOGIN_TEXT);

        JLabel password = new JLabel("Password:");
        loginPanel.add(password);
        PASSWORD_TEXT = new JTextField();
        loginPanel.add(PASSWORD_TEXT);

        JPanel errorMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ERROR_LABEL = new JLabel("");
        titleLabel.setFont(new Font("Thoma", Font.BOLD, 12));

        errorMessagePanel.add(ERROR_LABEL);
        loginPanel.add(errorMessagePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);
        loginPanel.add(buttonPanel);

        frame.add(loginPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Login");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Action after pressed login.
     * Pass the login and password to DBManager to check is it in the database.
     * If so, move to other page.
     * If not, remove the text and ask the user to try again.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Login: " + LOGIN_TEXT.getText());
        System.out.println("Password: " + PASSWORD_TEXT.getText());

        DBManager dbManager = new DBManager("Root");
        boolean result = dbManager.loginCheck(LOGIN_TEXT.getText(), PASSWORD_TEXT.getText());
        if (result) {
            System.out.println("Login Successful");
            new WelcomePage(LOGIN_TEXT.getText());
            frame.dispose();
        }else{
            ERROR_LABEL.setText("Wrong Password, please try again.");
            System.out.println("Wrong Password, please try again.");
        }

        LOGIN_TEXT.setText("");
        PASSWORD_TEXT.setText("");
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}