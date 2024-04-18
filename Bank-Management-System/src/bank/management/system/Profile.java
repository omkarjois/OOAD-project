package bank.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Profile extends JFrame {
    private JTextField tfCustomerId, tfFirstName, tfLastName, tfEmail, tfContact, tfAddress, tfCity, tfState, tfCountry, tfZip, tfAadhar, tfPan, tfGender, tfMaritalStatus;

    private Profile(ProfileBuilder builder, String customerId) {
        setTitle("User Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(15, 2, 10, 10));
        setVisible(true);

        JLabel lblCustomerId = new JLabel("Customer ID:");
        contentPane.add(lblCustomerId);
        tfCustomerId = new JTextField();
        tfCustomerId.setEditable(builder.editable);
        contentPane.add(tfCustomerId);

        JLabel lblFirstName = new JLabel("First Name:");
        contentPane.add(lblFirstName);
        tfFirstName = new JTextField();
        tfFirstName.setEditable(builder.editable);
        contentPane.add(tfFirstName);

        JLabel lblLastName = new JLabel("Last Name:");
        contentPane.add(lblLastName);
        tfLastName = new JTextField();
        tfLastName.setEditable(builder.editable);
        contentPane.add(tfLastName);

        JLabel lblEmail = new JLabel("Email:");
        contentPane.add(lblEmail);
        tfEmail = new JTextField();
        tfEmail.setEditable(builder.editable);
        contentPane.add(tfEmail);

        JLabel lblContact = new JLabel("Contact Number:");
        contentPane.add(lblContact);
        tfContact = new JTextField();
        tfContact.setEditable(builder.editable);
        contentPane.add(tfContact);

        JLabel lblAddress = new JLabel("Address:");
        contentPane.add(lblAddress);
        tfAddress = new JTextField();
        tfAddress.setEditable(builder.editable);
        contentPane.add(tfAddress);

        JLabel lblCity = new JLabel("City:");
        contentPane.add(lblCity);
        tfCity = new JTextField();
        tfCity.setEditable(builder.editable);
        contentPane.add(tfCity);

        JLabel lblState = new JLabel("State:");
        contentPane.add(lblState);
        tfState = new JTextField();
        tfState.setEditable(builder.editable);
        contentPane.add(tfState);

        JLabel lblCountry = new JLabel("Country:");
        contentPane.add(lblCountry);
        tfCountry = new JTextField();
        tfCountry.setEditable(builder.editable);
        contentPane.add(tfCountry);

        JLabel lblZip = new JLabel("Zip:");
        contentPane.add(lblZip);
        tfZip = new JTextField();
        tfZip.setEditable(builder.editable);
        contentPane.add(tfZip);

        JLabel lblAadhar = new JLabel("Aadhar Number:");
        contentPane.add(lblAadhar);
        tfAadhar = new JTextField();
        tfAadhar.setEditable(builder.editable);
        contentPane.add(tfAadhar);

        JLabel lblPan = new JLabel("PAN Number:");
        contentPane.add(lblPan);
        tfPan = new JTextField();
        tfPan.setEditable(builder.editable);
        contentPane.add(tfPan);

        JLabel lblGender = new JLabel("Gender:");
        contentPane.add(lblGender);
        tfGender = new JTextField();
        tfGender.setEditable(builder.editable);
        contentPane.add(tfGender);

        JLabel lblMaritalStatus = new JLabel("Marital Status:");
        contentPane.add(lblMaritalStatus);
        tfMaritalStatus = new JTextField();
        tfMaritalStatus.setEditable(builder.editable);
        contentPane.add(tfMaritalStatus);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomer(builder.customerId);
            }
        });
        contentPane.add(updateButton);

        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to open the Home page
                dispose(); // Close the current window
                new Home(customerId); // Open the Home page
            }
        });
        contentPane.add(backButton);

        String stmt = "SELECT * FROM Customer WHERE CustomerID = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connn sql = new Connn();
        try {
            statement = sql.connection.prepareStatement(stmt);
            statement.setString(1, String.valueOf(builder.customerId));

            rs = statement.executeQuery();

            if (rs.next()) {
                System.out.println("Updating the text fields");
                tfCustomerId.setText(String.valueOf(rs.getInt("CustomerID")));
                tfFirstName.setText(rs.getString("FirstName"));
                tfLastName.setText(rs.getString("LastName"));
                tfEmail.setText(rs.getString("Email"));
                tfContact.setText(rs.getString("ContactNumber"));
                tfAddress.setText(rs.getString("Address"));
                tfCity.setText(rs.getString("City"));
                tfState.setText(rs.getString("State"));
                tfCountry.setText(rs.getString("Country"));
                tfZip.setText(String.valueOf(rs.getInt("Zip")));
                tfAadhar.setText(rs.getString("AadharNumber"));
                tfPan.setText(rs.getString("PANNumber"));
                tfGender.setText(rs.getString("Gender"));
                tfMaritalStatus.setText(rs.getString("MaritalStatus"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateCustomer(int customerId) {
        // Code to update customer data in the database
        String updateStmt = "UPDATE Customer SET FirstName=?, LastName=?, Email=?, ContactNumber=?, Address=?, City=?, State=?, Country=?, Zip=?, AadharNumber=?, PANNumber=?, Gender=?, MaritalStatus=? WHERE CustomerID=?";
        Connn sql = new Connn(); // Assuming this is your connection class
        try {
            PreparedStatement updateStatement = sql.connection.prepareStatement(updateStmt);
            updateStatement.setString(1, tfFirstName.getText());
            updateStatement.setString(2, tfLastName.getText());
            updateStatement.setString(3, tfEmail.getText());
            updateStatement.setString(4, tfContact.getText());
            updateStatement.setString(5, tfAddress.getText());
            updateStatement.setString(6, tfCity.getText());
            updateStatement.setString(7, tfState.getText());
            updateStatement.setString(8, tfCountry.getText());
            updateStatement.setInt(9, Integer.parseInt(tfZip.getText()));
            updateStatement.setString(10, tfAadhar.getText());
            updateStatement.setString(11, tfPan.getText());
            updateStatement.setString(12, tfGender.getText());
            updateStatement.setString(13, tfMaritalStatus.getText());
            updateStatement.setInt(14, customerId);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Customer data updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update customer data.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ProfileBuilder {
        private boolean editable;
        private int customerId;

        public ProfileBuilder(int customerId) {
            this.customerId = customerId;
        }

        public ProfileBuilder editable(boolean editable) {
            this.editable = editable;
            return this;
        }

        public Profile build() {
            return new Profile(this, String.valueOf(customerId));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Profile frame = new Profile.ProfileBuilder(1).editable(true).build(); // Replace with the actual customer ID
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}