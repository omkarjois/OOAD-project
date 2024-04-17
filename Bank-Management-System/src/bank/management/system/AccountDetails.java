package bank.management.system;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AccountDetails extends JFrame {

    private Connn connectionManager;

    public AccountDetails(int accountNumber)  {
        this.connectionManager = new Connn();
        setTitle("Account Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        panel.add(detailsArea);

        add(panel, BorderLayout.CENTER);

        try {
            displayDetails(detailsArea, accountNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayDetails(JTextArea detailsArea, int accountNumber) throws SQLException {
        // Get account balance
        double balance = getBalance(accountNumber);

        // Get last 5 transactions
        String transactions = getTransactions(accountNumber, 5);

        // Update details area
        StringBuilder details = new StringBuilder();
        details.append("Account Number: " + accountNumber + "\n\n");
        details.append("Balance: $" + String.format("%.2f", balance) + "\n\n");
        details.append("Last 5 Transactions:\n\n");
        details.append(transactions);

        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); // Set monospaced font
        detailsArea.setText(details.toString());
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
    public static void main(String[] args) throws SQLException {
        int accountNumber = 1001;
        new AccountDetails(accountNumber);
    }
}
