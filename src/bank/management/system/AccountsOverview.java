package bank.management.system;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AccountsOverview extends JFrame {
    public static void main(String[] args) {
        new AccountsOverview(1);
    }
    private Connn connectionManager;
    private int customerId; // Store the received customer ID

    public AccountsOverview(int customerId) {
        this.customerId = customerId;  // Store passed customer ID
        this.connectionManager = new Connn (); // Establish database connection
        setTitle("Accounts Overview");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2)); // Dynamic rows

        try {
            displayAccounts(panel);  // Call method to display accounts
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Home(String.valueOf(customerId));
            }
        });
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }

    private void displayAccounts(JPanel panel) throws SQLException {
        String query = "SELECT AccountNumber, IFSC, AccountType FROM Account WHERE customerId = " + customerId;
        // Assuming you have an Account table
        try (ResultSet rs = connectionManager.statement.executeQuery(query)) {
            int accountCount = 1; // Counter for account numbering
            while (rs.next()) {
                int accountNumber = rs.getInt("AccountNumber");
                String ifscCode = rs.getString("IFSC");
                String accountType = rs.getString("AccountType");

                // Create a container panel for each account
                JPanel accountPanel = new JPanel();
                accountPanel.setLayout(new GridLayout(3, 2));  // 3 rows, 2 columns

                // Add account details to the container panel
                accountPanel.add(new JLabel("Account Number:"));
                accountPanel.add(new JLabel(String.valueOf(accountNumber)));
                accountPanel.add(new JLabel("IFSC Code:"));
                accountPanel.add(new JLabel(ifscCode));
                accountPanel.add(new JLabel("Account Type:"));
                accountPanel.add(new JLabel(accountType));
                JButton viewDetails = new JButton("View Details");
                viewDetails.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new AccountDetails(accountNumber);
                        setVisible(false);
                    }
                });
                accountPanel.add(viewDetails);

                // Add a label at the top of the container panel for account numbering
                JLabel accountLabel = new JLabel("Account " + accountCount);
                accountLabel.setFont(accountLabel.getFont().deriveFont(Font.BOLD)); // Bold font for account label
                panel.add(accountLabel);

                // Add the container panel with account details to the main panel
                panel.add(accountPanel);

                accountCount++;
            }
        }
    }
}