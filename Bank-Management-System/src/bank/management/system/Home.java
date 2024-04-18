package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {
    public String customerID;

    public Home(String customerID) {
        this.customerID = customerID;
        setTitle("Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Using GridBagLayout for flexible layout
        panel.setBackground(Color.WHITE); // Set background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20); // Adding insets for spacing

        JLabel titleLabel = new JLabel("Welcome to Your Bank");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK); // Set text color
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        JButton accountsButton = new JButton("Accounts Overview");
        customizeButton(accountsButton);
        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to open Accounts Overview page
                new AccountsOverview(Integer.parseInt(customerID));
                setVisible(false);
            }
        });
        panel.add(accountsButton, gbc);

        gbc.gridy++;
        JButton loansButton = new JButton("Loans");
        customizeButton(loansButton);
        loansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to open Loans page
                new LoansDisplayPage(new Connn(), Integer.parseInt(customerID));
                setVisible(false);
            }
        });
        panel.add(loansButton, gbc);

        gbc.gridy++;
        JButton paymentButton = new JButton("Payment");
        customizeButton(paymentButton);
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to open Payment page
                new Payment(customerID);
                setVisible(false);
            }
        });
        panel.add(paymentButton, gbc);

        gbc.gridy++;
        JButton profileButton = new JButton("Profile");
        customizeButton(profileButton);
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Profile.ProfileBuilder(Integer.parseInt(customerID)).build();
                setVisible(false);
            }
        });
        panel.add(profileButton, gbc);

        add(panel);
        setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setBackground(Color.BLACK); // Customizing button background color
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false); // Removing focus border
        button.setPreferredSize(new Dimension(200, 50)); // Setting preferred button size
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Sample usage
                Home home = new Home("123456"); // Replace "123456" with actual customer ID
            }
        });
    }
}