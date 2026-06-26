/**
 * COMP5590-A2
 * G16 - Group B (GP perspective)
 * GP booking system - Welcome Page
 * @version 1.2 (20/03/2025)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A simple welcome Page
 */
public class WelcomePage implements ActionListener {

    JFrame frame = new JFrame();
    private String LOGIN;
    private JButton doctorBookingSystem;
    private JButton doctorVisitEntry;
    private JButton patientInfoPage;
    private JButton visitDetails;
    private JButton DoctorReassignment;

    /**
     * Constructor of the welcome page
     * @param LOGIN The user's login
     */
    public WelcomePage(String LOGIN){
        this.LOGIN = LOGIN;

        JPanel welcomePage = new JPanel();
        welcomePage.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        welcomePage.setPreferredSize(new Dimension(600, 350));
        welcomePage.setLayout(new GridLayout(0,1));

        JLabel titleLabel = new JLabel(LOGIN + ", Welcome!!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Thoma", Font.BOLD, 32));
        welcomePage.add(titleLabel);

        //Panel for functions
        JPanel buttonLinkPanel = new JPanel(new GridLayout(2,3));

        doctorBookingSystem = new JButton("View Booking System");
        doctorBookingSystem.addActionListener(this);
        buttonLinkPanel.add(doctorBookingSystem);

        doctorVisitEntry = new JButton("Visit Entry");
        doctorVisitEntry.addActionListener(this);
        buttonLinkPanel.add(doctorVisitEntry);

        patientInfoPage = new JButton("Patient Info");
        patientInfoPage.addActionListener(this);
        buttonLinkPanel.add(patientInfoPage);

        visitDetails = new JButton("Patient Visit Details");
        visitDetails.addActionListener(this);
        buttonLinkPanel.add(visitDetails);

        DoctorReassignment = new JButton("Doctor Reassignment");
        DoctorReassignment.addActionListener(this);
        buttonLinkPanel.add(DoctorReassignment);

        welcomePage.add(buttonLinkPanel);


        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        buttonPanel.add(logoutButton);
        welcomePage.add(buttonPanel);

        frame.add(welcomePage, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Welcome Page");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "View Booking System":
                new DoctorBookingSystem(LOGIN);
                break;
            case "Visit Entry":
                new DoctorVisitEntry(LOGIN);
                break;
            case "Patient Info":
                new patientsInformation(LOGIN);
                break;
            case "Patient Visit Details":
                new VisitDetailsSystem(LOGIN);
                break;
            case "Doctor Reassignment":
                new DoctorReassignment(LOGIN);
                break;
            case "Logout":
                frame.setVisible(false);
                frame.dispose();
                break;
        }
    }

    /**
     * For Testing
     */
    public WelcomePage() {
        new WelcomePage("");
    }

    /**
     * For Testing
     * @param args
     */
    public static void main(String[] args) {
        new WelcomePage();
    }
}
