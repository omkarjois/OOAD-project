package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountsOverview extends JFrame {

    private Connn connectionManager;
    private int customerId;

    private static final Map<String, AccountDisplayStrategy> STRATEGY_MAP = new HashMap<>();

    static {
        STRATEGY_MAP.put("savings", new SavingsAccountDisplayStrategy());
        STRATEGY_MAP.put("checking", new CheckingAccountDisplayStrategy());
    }

    public AccountsOverview(int customerId) {
        this.customerId = customerId;
        this.connectionManager = new Connn();
        setTitle("Accounts Overview");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel accountsPanel = new JPanel();
        accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));

        try {
            displayAccounts(accountsPanel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JScrollPane scrollPane = new JScrollPane(accountsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> {
            setVisible(false);
            new Home(String.valueOf(customerId));
        });
        buttonPanel.add(backButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void displayAccounts(JPanel panel) throws SQLException {
        String query = "SELECT AccountNumber, IFSC, AccountType FROM Account WHERE customerId = " + customerId;

        try (ResultSet rs = connectionManager.statement.executeQuery(query)) {
            int accountCount = 1;
            while (rs.next()) {
                int accountNumber = rs.getInt("AccountNumber");
                String ifscCode = rs.getString("IFSC");
                String accountType = rs.getString("AccountType");

                if (!STRATEGY_MAP.containsKey(accountType.toLowerCase())) {
                    continue;
                }

                AccountDisplayStrategy strategy = STRATEGY_MAP.get(accountType.toLowerCase());
                AbstractAccount account = new AbstractAccount(strategy, accountNumber, ifscCode, accountCount);

                JPanel accountPanel = new JPanel(new BorderLayout());
                accountPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.GRAY, 1)
                ));

                JLabel accountLabel = new JLabel("Account: " + accountCount);
                accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
                accountPanel.add(accountLabel, BorderLayout.NORTH);

                JPanel detailsPanel = strategy.createPanel(accountNumber, ifscCode, accountCount);
                accountPanel.add(detailsPanel, BorderLayout.CENTER);

                JButton viewDetails = new JButton("View Details");
                viewDetails.setPreferredSize(new Dimension(120, 30));
                viewDetails.addActionListener(e -> {
                    setVisible(false);
                    new AccountDetails(customerId, accountNumber);  // Open AccountDetails with necessary values
                });
                accountPanel.add(viewDetails, BorderLayout.SOUTH);

                panel.add(accountPanel);
                accountCount++;
            }
        }
    }

    private static class SavingsAccountDisplayStrategy implements AccountDisplayStrategy {
        @Override
        public JPanel createPanel(int accountNumber, String ifscCode, int accountCount) {
            JPanel detailsPanel = new JPanel(new GridLayout(4, 2));
            detailsPanel.add(new JLabel("Account: " + accountCount));
            detailsPanel.add(new JLabel("Account Number:"));
            detailsPanel.add(new JLabel(String.valueOf(accountNumber)));
            detailsPanel.add(new JLabel("IFSC Code:"));
            detailsPanel.add(new JLabel(ifscCode));
            detailsPanel.add(new JLabel("Account Type:"));
            detailsPanel.add(new JLabel("Savings"));
            detailsPanel.add(new JLabel("Interest Rate:"));
            detailsPanel.add(new JLabel("5%"));
            return detailsPanel;
        }
    }

    private static class CheckingAccountDisplayStrategy implements AccountDisplayStrategy {
        @Override
        public JPanel createPanel(int accountNumber, String ifscCode, int accountCount) {
            JPanel detailsPanel = new JPanel(new GridLayout(4, 2));
            detailsPanel.add(new JLabel("Account: " + accountCount));
            detailsPanel.add(new JLabel("Account Number:"));
            detailsPanel.add(new JLabel(String.valueOf(accountNumber)));
            detailsPanel.add(new JLabel("IFSC Code:"));
            detailsPanel.add(new JLabel(ifscCode));
            detailsPanel.add(new JLabel("Account Type:"));
            detailsPanel.add(new JLabel("Checking"));
            detailsPanel.add(new JLabel("Overdraft Limit:"));
            detailsPanel.add(new JLabel("$500"));
            return detailsPanel;
        }
    }

    private static class AbstractAccount {
        private final AccountDisplayStrategy strategy;
        private final int accountNumber;
        private final String ifscCode;
        private final int accountCount;

        public AbstractAccount(AccountDisplayStrategy strategy, int accountNumber, String ifscCode, int accountCount) {
            this.strategy = strategy;
            this.accountNumber = accountNumber;
            this.ifscCode = ifscCode;
            this.accountCount = accountCount;
        }

        public JPanel createPanel() {
            JPanel accountPanel = new JPanel(new BorderLayout());
            accountPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.GRAY, 1)
            ));

            JLabel accountLabel = new JLabel("Account: " + accountCount);
            accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
            accountPanel.add(accountLabel, BorderLayout.NORTH);

            JPanel detailsPanel = strategy.createPanel(accountNumber, ifscCode, accountCount);
            accountPanel.add(detailsPanel, BorderLayout.CENTER);

            JButton viewDetails = new JButton("View Details");
            viewDetails.setPreferredSize(new Dimension(120, 30));
            viewDetails.addActionListener(e -> {
                // Handle view details action
                JOptionPane.showMessageDialog(accountPanel, "View Details for Account " + accountCount);
            });
            accountPanel.add(viewDetails, BorderLayout.SOUTH);

            return accountPanel;
        }
    }

    private interface AccountDisplayStrategy {
        JPanel createPanel(int accountNumber, String ifscCode, int accountCount);
    }
}
