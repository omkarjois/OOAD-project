package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;


public class Payment extends JFrame {

    JTextField from, to, IFSC, amount;
    public String customerId;

    public Payment(String customerId)
    {
        this.customerId = customerId;
        setTitle("Payment Page");
        getContentPane().setBackground(new Color(255, 255, 255));
        setLayout(null);
        setSize(850, 600);
        setLocation(360, 40);
        setVisible(true);

        JLabel payment = new JLabel("Make a Payment");
        payment.setBounds(250, 50, 300, 50);
        payment.setFont(new Font("Raleway", Font.BOLD, 38));
        add(payment);

        JLabel labelFrom = new JLabel("From (Account Number)");
        labelFrom.setBounds(50, 150, 300, 50);
        labelFrom.setFont(new Font("Raleway", Font.BOLD, 20));
        add(labelFrom);

        from = new JTextField();
        from.setFont(new Font("Raleway", Font.BOLD, 14));
        from.setBounds(400, 155, 400, 40);
        add(from);

        JLabel labelTo = new JLabel("To (Account Number)");
        labelTo.setBounds(50, 200, 300, 50);
        labelTo.setFont(new Font("Raleway", Font.BOLD, 20));
        add(labelTo);

        to = new JTextField();
        to.setFont(new Font("Raleway", Font.BOLD, 14));
        to.setBounds(400, 205, 400, 40);
        add(to);

        JLabel labelIFSC = new JLabel("IFSC");
        labelIFSC.setBounds(50, 250, 300, 50);
        labelIFSC.setFont(new Font("Raleway", Font.BOLD, 20));
        add(labelIFSC);

        IFSC = new JTextField();
        IFSC.setFont(new Font("Raleway", Font.BOLD, 14));
        IFSC.setBounds(400, 255, 400, 40);
        add(IFSC);

        JLabel labelAmount = new JLabel("Amount");
        labelAmount.setBounds(50, 300, 300, 50);
        labelAmount.setFont(new Font("Raleway", Font.BOLD, 20));
        add(labelAmount);

        amount = new JTextField();
        amount.setFont(new Font("Raleway", Font.BOLD, 14));
        amount.setBounds(400, 305, 400, 40);
        add(amount);

        JButton payButton = new JButton("Make Payment");
        payButton.setBounds(300, 400, 200, 50);
        payButton.setFont(new Font("Raleway", Font.BOLD, 20));
        payButton.setBackground(Color.BLACK);
        payButton.setForeground(Color.WHITE);
        add(payButton);

        JButton backButton = new JButton("Go back");
        backButton.setBounds(50, 50, 130, 40);
        backButton.setFont(new Font("Raleway", Font.BOLD, 16));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home(customerId);
                setVisible(false);
            }
        });

        // Add action listener to the payButton
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Process payment logic here
                String fromAccount = from.getText();
                String toAccount = to.getText();
                String ifscCode = IFSC.getText();
                String paymentAmount = amount.getText();

                //Check if valid credentials
                try {
                    if (doAccountsExist(new Connn().connection, fromAccount, toAccount)){
                        //Enter Pin
                        new PinInputDialog(fromAccount, toAccount, ifscCode, paymentAmount, customerId);
                        setVisible(false);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Invalid Account Numbers!");
                        new Home(customerId);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
    }

    public static boolean doAccountsExist(Connection connection, String fromAccount, String toAccount) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Account WHERE AccountNumber IN (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(fromAccount));
        statement.setInt(2, Integer.parseInt(toAccount));
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count == 2; // Both accounts should exist
        } else {
            return false;
        }
    }


    // Method to make the payment and update the database
    private static boolean makePayment(String fromAccount, String toAccount, String ifscCode, double amount) {
        Random random = new Random();
        int randomNumber = random.nextInt(100000);

        if ( fromAccount == toAccount){
            JOptionPane.showMessageDialog(null, "You have entered invalid Account credentials!");
            return false;
        }

        try (Connection connection = new Connn().connection) {
            String sql = "INSERT INTO Transaction (TransactionID, CustomerID, AccountNumber, TransactionDateTime, Amount, TransactionFrom, TransactionTo, TransactionNote) " +
                    "VALUES (?,?, ?, NOW(), ?, ?, ?, 'Payment')";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, randomNumber);
            statement.setInt(2, 1); // Assuming CustomerID for the payer
            statement.setInt(3, Integer.parseInt(fromAccount));
            statement.setDouble(4, amount);
            statement.setString(5, fromAccount);
            statement.setString(6, toAccount);

            int rowsInserted = statement.executeUpdate();

            // Update sender's account
            String updateSenderSql = "UPDATE Account SET Balance = Balance - ? WHERE AccountNumber = ?";
            PreparedStatement senderStatement = connection.prepareStatement(updateSenderSql);
            senderStatement.setDouble(1, amount);
            senderStatement.setInt(2, Integer.parseInt(fromAccount));
            int senderRowsUpdated = senderStatement.executeUpdate();

            // Update receiver's account
            String updateReceiverSql = "UPDATE Account SET Balance = Balance + ? WHERE AccountNumber = ?";
            PreparedStatement receiverStatement = connection.prepareStatement(updateReceiverSql);
            receiverStatement.setDouble(1, amount);
            receiverStatement.setInt(2, Integer.parseInt(toAccount));
            int receiverRowsUpdated = receiverStatement.executeUpdate();

            System.out.println( rowsInserted+senderRowsUpdated+receiverRowsUpdated );
            return rowsInserted+senderRowsUpdated+receiverRowsUpdated > 2;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static class PinInputDialog {
        public String AccountNumber, toAccountNumber, IFSC, amount, CustomerId;
        public PinInputDialog(String AccountNumber, String toAccountNumber, String IFSC,String amount,
                              String CustomerId){
            this.AccountNumber = AccountNumber;
            this.amount = amount;
            this.IFSC = IFSC;
            this.toAccountNumber = toAccountNumber;
            this.CustomerId = CustomerId;

            // Show an input dialog to ask for PIN
            String pinInput = JOptionPane.showInputDialog(null, "Enter your PIN:", "PIN Input", JOptionPane.PLAIN_MESSAGE);

            // Check if user entered a PIN or canceled the dialog
            if (pinInput != null && !pinInput.isEmpty()) {
                // User entered a PIN
                String _pin;
                System.out.println("Entered PIN: " + pinInput);
                // You can add further logic here, such as checking the PIN against a stored value
                Connn sql = new Connn();
                PreparedStatement statement = null;
                try {
                    statement = sql.connection.prepareStatement("SELECT _password FROM Customer WHERE CustomerID = ?");
                    statement.setString(1, CustomerId);
                    ResultSet resultSet = null;
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        _pin = resultSet.getString("_password");
                        System.out.println("PIN: " + _pin);

                        if (pinInput.equals(_pin)){
                            // Update database with payment information
                            if (makePayment(AccountNumber, toAccountNumber, IFSC, Double.parseDouble(amount))) {
                                JOptionPane.showMessageDialog(null, "Payment successful!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Payment failed. Please try again.");
                            }
                            new Home(CustomerId);
                        }
                    } else {
                        // Handle case where no rows were found (PIN verification failed)
                        JOptionPane.showMessageDialog(null, "Invalid PIN!");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // User canceled or closed the dialog
                System.out.println("PIN input canceled or no PIN entered.");
            }

        }
    }

//    public static void main(String[] args) {
//        new Payment("1");
//    }

}
