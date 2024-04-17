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
        setSize(850, 1000);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JButton accountsButton = new JButton("Accounts Overview");
        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to open Accounts Overview page
                new AccountsOverview(Integer.parseInt(customerID));
                setVisible(false);
            }
        });

        JButton loansButton = new JButton("Loans");
        loansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to open Loans page
                new LoansDisplayPage(new Connn(),Integer.parseInt(customerID));
                setVisible(false);
            }
        });

        JButton paymentButton = new JButton("Payment");
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to open Payment page
                new Payment(customerID);
                setVisible(false);
            }
        });

        JButton profileButton = new JButton("Profile");
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Profile(Integer.parseInt(customerID));
                setVisible(false);
            }
        });

        panel.add(accountsButton);
        panel.add(loansButton);
        panel.add(paymentButton);
        panel.add(profileButton);

        add(panel);
        setVisible(true);
    }
}

