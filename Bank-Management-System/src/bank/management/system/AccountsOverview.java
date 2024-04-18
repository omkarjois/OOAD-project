package bank.management.system;

import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountsOverview extends JFrame {

    private Connn connectionManager;
    private int customerId;

    public AccountsOverview(int customerId) {
        this.customerId = customerId;
        this.connectionManager = new Connn();
        setTitle("Accounts Overview");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        // Create a panel for the accounts
        JPanel accountsPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Dynamic rows, with horizontal and vertical gaps
        accountsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        try {
            displayAccounts(accountsPanel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Create a scroll pane for the accounts panel
        JScrollPane scrollPane = new JScrollPane(accountsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create a panel for the back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30)); // Set button size
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Home(String.valueOf(customerId));
            }
        });
        buttonPanel.add(backButton);

        // Add the components to the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void displayAccounts(JPanel panel) throws SQLException {
        String query = "SELECT AccountNumber, IFSC, AccountType FROM Account WHERE customerId = " + customerId;

        try (ResultSet rs = connectionManager.statement.executeQuery(query)) {
            while (rs.next()) {
                int accountNumber = rs.getInt("AccountNumber");
                String ifscCode = rs.getString("IFSC");
                String accountType = rs.getString("AccountType");

                AccountFactory accountFactory;
                if (accountType.equalsIgnoreCase("savings")) {
                    accountFactory = new SavingsAccountFactory();
                } else if (accountType.equalsIgnoreCase("checking")) {
                    accountFactory = new CheckingAccountFactory();
                } else {
                    continue;
                }

                AbstractAccount account = accountFactory.createAccount(accountNumber, ifscCode);

                JPanel accountPanel = new JPanel(new BorderLayout());
                accountPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.GRAY, 1)
                ));

                JLabel accountLabel = new JLabel("Account: " + accountNumber);
                accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
                accountPanel.add(accountLabel, BorderLayout.NORTH);

                JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                detailsPanel.add(new JLabel("Account Number:"));
                detailsPanel.add(new JLabel(String.valueOf(accountNumber)));
                detailsPanel.add(new JLabel("IFSC Code:"));
                detailsPanel.add(new JLabel(ifscCode));
                detailsPanel.add(new JLabel("Account Type:"));
                detailsPanel.add(new JLabel(accountType));
                accountPanel.add(detailsPanel, BorderLayout.CENTER);

                JButton viewDetails = new JButton("View Details");
                viewDetails.setPreferredSize(new Dimension(120, 30));
                viewDetails.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        account.displayDetails();
                    }
                });
                accountPanel.add(viewDetails, BorderLayout.SOUTH);

                panel.add(accountPanel);
            }
        }
    }

    // AbstractProduct
    interface AbstractAccount {
        void displayDetails();
    }

    // ConcreteProducts
    class SavingsAccount implements AbstractAccount {
        private int accountNumber;
        private String ifscCode;

        public SavingsAccount(int accountNumber, String ifscCode) {
            this.accountNumber = accountNumber;
            this.ifscCode = ifscCode;
        }

        public void displayDetails() {
            System.out.println("Savings Account Number: " + accountNumber);
            System.out.println("IFSC Code: " + ifscCode);
        }
    }

    class CheckingAccount implements AbstractAccount {
        private int accountNumber;
        private String ifscCode;

        public CheckingAccount(int accountNumber, String ifscCode) {
            this.accountNumber = accountNumber;
            this.ifscCode = ifscCode;
        }

        public void displayDetails() {
            System.out.println("Checking Account Number: " + accountNumber);
            System.out.println("IFSC Code: " + ifscCode);
        }
    }

    // AbstractFactory
    interface AccountFactory {
        AbstractAccount createAccount(int accountNumber, String ifscCode);
    }

    // ConcreteFactories
    class SavingsAccountFactory implements AccountFactory {
        public AbstractAccount createAccount(int accountNumber, String ifscCode) {
            return new SavingsAccount(accountNumber, ifscCode);
        }
    }

    class CheckingAccountFactory implements AccountFactory {
        public AbstractAccount createAccount(int accountNumber, String ifscCode) {
            return new CheckingAccount(accountNumber, ifscCode);
        }
    }
}