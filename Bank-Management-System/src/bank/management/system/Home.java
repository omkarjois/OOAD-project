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
        setSize(850, 600); // Adjusted height for better layout

        // Create a panel for the top section with customer ID label
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Left-aligned layout

        JLabel customerIDLabel = new JLabel("Customer ID: " + customerID);
        topPanel.add(customerIDLabel);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10)); // Grid layout with spacing

        JButton accountsButton = createButton("Accounts Overview");
        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AccountsOverview(Integer.parseInt(customerID));
                setVisible(false);
            }
        });

        JButton loansButton = createButton("Loans");
        loansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoansDisplayPage(new Connn(), Integer.parseInt(customerID));
                setVisible(false);
            }
        });

        JButton paymentButton = createButton("Payment");
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Payment(customerID);
                setVisible(false);
            }
        });

        JButton profileButton = createButton("Profile");
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Profile.ProfileBuilder(Integer.parseInt(customerID)).build();
                setVisible(false);
            }
        });

        buttonPanel.add(accountsButton);
        buttonPanel.add(loansButton);
        buttonPanel.add(paymentButton);
        buttonPanel.add(profileButton);

        // Add both panels to the main frame with BorderLayout
        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50)); // Set preferred size for buttons
        return button;
    }
}
