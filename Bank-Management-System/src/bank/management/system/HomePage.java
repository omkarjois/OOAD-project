package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {

    public HomePage() {
        super("Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 300);
        setLayout(null);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(140,100,250,30);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(400,100,250,30);
        signUpButton.setBackground(Color.BLACK);
        signUpButton.setForeground(Color.WHITE);

        add(loginButton);
        add(signUpButton);

        JLabel labelHeading = new JLabel("Welcome to the Online Bank:");
        labelHeading.setFont(new Font("Raleway", Font.BOLD, 25));
        labelHeading.setBounds(220,50,500,30);
        add(labelHeading);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the login page
                Login loginPage = new Login();
                loginPage.setVisible(true);
                dispose(); // Close the current frame
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the sign-up page
                Signup signUp = new Signup();
                signUp.setVisible(true);
                dispose(); // Close the current frame
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HomePage homePage = new HomePage();
                homePage.setVisible(true);
            }
        });
    }
}
