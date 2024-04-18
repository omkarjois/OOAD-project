package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

// AbstractProduct
interface AbstractAccount {
    void displayDetails();
}

// ConcreteProducts
class SavingsAccount implements AbstractAccount {
    private int accountNumber;
    private String ifscCode;
    private double balance;

    public SavingsAccount(int accountNumber, String ifscCode, double balance) {
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.balance = balance;
    }

    public void displayDetails() {
        System.out.println("Savings Account Number: " + accountNumber);
        System.out.println("IFSC Code: " + ifscCode);
        System.out.println("Balance: $" + String.format("%.2f", balance));
    }
}

class CheckingAccount implements AbstractAccount {
    private int accountNumber;
    private String ifscCode;
    private double balance;

    public CheckingAccount(int accountNumber, String ifscCode, double balance) {
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.balance = balance;
    }

    public void displayDetails() {
        System.out.println("Checking Account Number: " + accountNumber);
        System.out.println("IFSC Code: " + ifscCode);
        System.out.println("Balance: $" + String.format("%.2f", balance));
    }
}

// AbstractFactory
interface AccountFactory {
    AbstractAccount createAccount(int accountNumber, String ifscCode, double balance);
}

// ConcreteFactories
class SavingsAccountFactory implements AccountFactory {
    public AbstractAccount createAccount(int accountNumber, String ifscCode, double balance) {
        return new SavingsAccount(accountNumber, ifscCode, balance);
    }
}

class CheckingAccountFactory implements AccountFactory {
    public AbstractAccount createAccount(int accountNumber, String ifscCode, double balance) {
        return new CheckingAccount(accountNumber, ifscCode, balance);
    }
}

public class AccountDetails extends JFrame {

    private Connn connectionManager;
    private int customerId;

    public AccountDetails(int customerId, int accountNumber) {
        this.customerId = customerId;  // Store customer ID for AccountsOverview
        this.connectionManager = new Connn();
        setTitle("Account Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose this frame on close
        setSize(1000, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        // Create main panel with slight padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Account details panel with a titled border
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Account Balance"));

        // Text area for account details (non-editable)
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        detailsPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        // Transactions panel with a titled border
        JPanel transactionsPanel = new JPanel(new BorderLayout());
        transactionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Recent Transactions"));

        // Text area for transactions (non-editable)
        JTextArea transactionsArea = new JTextArea();
        transactionsArea.setEditable(false);
        transactionsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        transactionsPanel.add(new JScrollPane(transactionsArea), BorderLayout.CENTER);

        // Add panels to main panel
        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(transactionsPanel, BorderLayout.CENTER);

        // Back button with custom styling
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.setBackground(new Color(0, 128, 128)); // Teal color
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBorder(BorderFactory.createRaisedBevelBorder()); // Raised border for a 3D effect
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Hide the current frame
                new AccountsOverview(customerId); // Open AccountsOverview with the stored customer ID
            }
        });

        // Create a separate panel for the button to align it to the right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // Right alignment, no horizontal/vertical gaps
        buttonPanel.add(backButton);

        // Add button panel to the bottom of the main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        try {
            displayDetails(detailsArea, transactionsArea, accountNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void displayDetails(JTextArea detailsArea, JTextArea transactionsArea, int accountNumber) throws SQLException {
        // Get account balance and IFSC code
        String ifscCode = getIFSCCode(accountNumber);
        double balance = getBalance(accountNumber);

        // Create the appropriate AccountFactory based on the account type
        AccountFactory accountFactory;
        String accountType = getAccountType(accountNumber);
        if (accountType.equalsIgnoreCase("savings")) {
            accountFactory = new SavingsAccountFactory();
        } else if (accountType.equalsIgnoreCase("checking")) {
            accountFactory = new CheckingAccountFactory();
        } else {
            throw new IllegalArgumentException("Invalid account type: " + accountType);
        }

        // Create the account using the factory
        AbstractAccount account = accountFactory.createAccount(accountNumber, ifscCode, balance);

        // Update details area
        StringBuilder details = new StringBuilder();
        details.append("Account Balance: " + balance + "\n\n");
        account.displayDetails(); // Display account details using the AbstractAccount implementation
        detailsArea.setText(details.toString());

        // Get last 5 transactions
        String transactions = getTransactions(accountNumber, 5);
        transactionsArea.setText(transactions);
    }

    private String getIFSCCode(int accountNumber) throws SQLException {
        String query = "SELECT IFSC FROM Account WHERE AccountNumber = " + accountNumber;
        try (ResultSet rs = connectionManager.statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("IFSC");
            } else {
                throw new SQLException("Account not found.");
            }
        }
    }

    private double getBalance(int accountNumber) throws SQLException {
        String query = "SELECT Balance FROM Account WHERE AccountNumber = " + accountNumber;
        try (ResultSet rs = connectionManager.statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble("Balance");
            } else {
                throw new SQLException("Account not found.");
            }
        }
    }

    private String getAccountType(int accountNumber) throws SQLException {
        String query = "SELECT AccountType FROM Account WHERE AccountNumber = " + accountNumber;
        try (ResultSet rs = connectionManager.statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("AccountType");
            } else {
                throw new SQLException("Account not found.");
            }
        }
    }

    private String getTransactions(int accountNumber, int limit) throws SQLException {
        StringBuilder transactions = new StringBuilder();
        String query = "SELECT TransactionID, CustomerID, AccountNumber, TransactionDateTime, Amount, TransactionFrom, TransactionTo, TransactionNote FROM Transaction WHERE AccountNumber = " + accountNumber + " ORDER BY TransactionID DESC LIMIT " + limit;

        try (ResultSet rs = connectionManager.statement.executeQuery(query)) {
            if (!rs.isBeforeFirst()) { // Check if the ResultSet is empty
                transactions.append("No recent transactions found.");
            } else {
                transactions.append(String.format("%-18s %-18s %-18s %-25s %-18s %-18s %-18s %s", "Transaction ID", "Customer ID", "Account Number", "Date Time", "Amount", "From", "To", "Note"));
                transactions.append("\n");
                transactions.append(String.join("", java.util.Collections.nCopies(160, "-")));
                transactions.append("\n");

                while (rs.next()) {
                    int transactionID = rs.getInt("TransactionID");
                    int customerID = rs.getInt("CustomerID");
                    java.util.Date transactionDateTime = rs.getTimestamp("TransactionDateTime");
                    double amount = rs.getDouble("Amount");
                    String transactionFrom = rs.getString("TransactionFrom");
                    String transactionTo = rs.getString("TransactionTo");
                    String transactionNote = rs.getString("TransactionNote");

                    transactions.append(String.format("%-18d %-18d %-18d %-25s %-18.2f %-18s %-18s %s\n",
                            transactionID, customerID, accountNumber, new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(transactionDateTime), amount, transactionFrom, transactionTo, transactionNote));
                }
            }
        }
        return transactions.toString();
    }
}