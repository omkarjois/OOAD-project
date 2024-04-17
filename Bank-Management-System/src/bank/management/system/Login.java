package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {
    private JTextField customerIdField;
    private JPasswordField passwordField;

    public Login() {
        super("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame

        // Create components
        JLabel loginLabel = new JLabel("Login");
        JLabel customerIdLabel = new JLabel("Customer ID:");
        JLabel passwordLabel = new JLabel("Password:");
        customerIdField = new JTextField(20);
        customerIdField.setSize(300,30);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // Layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(loginLabel);
        add(customerIdLabel);
        add(customerIdField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        // Button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = customerIdField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                // Check login credentials
                if (isValidLogin(customerId, password)) {
                    new Home(customerId);
                    setVisible(false);
                    // Open main application or next page
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Invalid credentials. Please try again.");
                }
            }
        });
    }

    private boolean isValidLogin(String customerId, String password) {
        boolean isValid = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection (replace databaseUrl, username, and password with your own)
            String databaseUrl = "jdbc:mysql://localhost:3306/bankSystem";
            String username = "root";
            String passwordDb = "omkar2003";
            connection = DriverManager.getConnection(databaseUrl, username, passwordDb);

            // Prepare SQL statement
            String sql = "SELECT * FROM Customer WHERE CustomerID = ? AND _password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, customerId);
            statement.setString(2, password);

            // Execute query
            resultSet = statement.executeQuery();

            // Check if there is a matching record
            if (resultSet.next()) {
                isValid = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return isValid;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login loginPage = new Login();
                loginPage.setVisible(true);
            }
        });
    }
}
