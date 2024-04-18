package bank.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

interface ButtonFactory {
    JButton createButton();
}

class BackToHomeButtonFactory implements ButtonFactory {
    private final LoansDisplayPage context;

    public BackToHomeButtonFactory(LoansDisplayPage context) {
        this.context = context;
    }

    @Override
    public JButton createButton() {
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // Open Home.java
                        new Home(context.connection.toString()).setVisible(true);
                        // Close current window
                        context.dispose();
                    }
                });
            }
        });
        return backButton;
    }
}

class NewLoanButtonFactory implements ButtonFactory {
    private final LoansDisplayPage context;

    public NewLoanButtonFactory(LoansDisplayPage context) {
        this.context = context;
    }

    @Override
    public JButton createButton() {
        JButton newLoanButton = new JButton("New Loan");
        newLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open new loan application page (Loan.java)
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new Loan(String.valueOf(context.customerID)).setVisible(true);
                    }
                });
            }
        });
        return newLoanButton;
    }
}

public class LoansDisplayPage extends JFrame {
    final Connn connection;
    final int customerID; // Customer ID passed from the other page

    public LoansDisplayPage(Connn connection, int customerID) {
        super("Loan Display Page");
        this.connection = connection;
        this.customerID = customerID;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        // Fetch customer details
        String[] customerDetails = fetchCustomerDetails(customerID);

        // Create panel to hold customer details
        JPanel customerDetailsPanel = createCustomerDetailsPanel(customerDetails);

        // Create panel to hold loan table
        JPanel loanTablePanel = createLoanTablePanel();

        // Create panel to hold buttons
        JPanel buttonPanel = createButtonPanel();

        // Add components to the frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(customerDetailsPanel, BorderLayout.NORTH);
        contentPane.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        contentPane.add(loanTablePanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private String[] fetchCustomerDetails(int customerID) {
        // Query to fetch customer details
        String query = "SELECT * FROM Customer WHERE CustomerID = ?";
        String[] customerDetails = new String[5];

        try {
            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerID);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve customer details
            if (resultSet.next()) {
                customerDetails[0] = resultSet.getString("FirstName") + " " + resultSet.getString("LastName");
                customerDetails[1] = resultSet.getString("Email");
                customerDetails[2] = resultSet.getString("ContactNumber");
                customerDetails[3] = resultSet.getString("Address") + ", " + resultSet.getString("City") + ", " +
                        resultSet.getString("State") + ", " + resultSet.getString("Country") + " - " +
                        resultSet.getInt("Zip");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
        return customerDetails;
    }

    private JPanel createCustomerDetailsPanel(String[] customerDetails) {
        JPanel customerDetailsPanel = new JPanel(new GridBagLayout());
        customerDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Name:", "Email:", "Contact Number:", "Address:"};
        Font boldFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(boldFont);
            gbc.gridx = 0;
            gbc.gridy = i;
            customerDetailsPanel.add(label, gbc);

            JLabel detail = new JLabel(customerDetails[i]);
            gbc.gridx = 1;
            customerDetailsPanel.add(detail, gbc);
        }

        return customerDetailsPanel;
    }

    private JPanel createLoanTablePanel() {
        JPanel loanTablePanel = new JPanel(new BorderLayout());

        // Add label for table heading
        JLabel headingLabel = new JLabel("Existing Loans");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust font size and style as needed
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loanTablePanel.add(headingLabel, BorderLayout.NORTH);

        // Table model to hold loan data
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the table cells non-editable
                return false;
            }
        };
        JTable loanTable = new JTable(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Loan ID");
        tableModel.addColumn("Amount in Rupees");
        tableModel.addColumn("Interest Rate");
        tableModel.addColumn("Tenure in years");
        tableModel.addColumn("Pay EMI");

        // Fetch and populate loan data
        String query = "SELECT * FROM Loan WHERE CustomerID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getInt("LoanID"),
                        resultSet.getDouble("Amount"),
                        resultSet.getDouble("InterestRate"),
                        resultSet.getInt("Tenure"),
                        "Pay EMI"
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }

        // Add button renderer and editor for the "Pay EMI" column
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        loanTable.getColumnModel().getColumn(4).setCellRenderer(buttonRenderer);
        loanTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JTextField()));

        // Set font size and style for table headers
        Font headerFont = loanTable.getTableHeader().getFont();
        Font boldHeaderFont = new Font(headerFont.getFontName(), Font.BOLD, 14); // Change font size as needed
        loanTable.getTableHeader().setFont(boldHeaderFont);

        // Set padding for table cells
        loanTable.setRowHeight(30); // Adjust row height as needed
        loanTable.setIntercellSpacing(new Dimension(10, 10)); // Adjust spacing as needed

        // Add the table to a scroll pane and then to the panel
        JScrollPane scrollPane = new JScrollPane(loanTable);
        loanTablePanel.add(scrollPane, BorderLayout.CENTER);

        return loanTablePanel;
    }

    // Renderer for the "Pay EMI" button column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor for the "Pay EMI" button column
    class ButtonEditor extends DefaultCellEditor {
        private String label;

        public ButtonEditor(JTextField textField) {
            super(textField);
            setClickCountToStart(1);

            // Button action
            editorComponent = new JButton("Pay EMI");
            ((JButton) editorComponent).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(LoansDisplayPage.this, "EMI paid for Loan ID: " + label);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            ((JButton) editorComponent).setText(label);
            return editorComponent;
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Create Back to Home button using factory
        ButtonFactory backButtonFactory = new BackToHomeButtonFactory(this);
        JButton backButton = backButtonFactory.createButton();

        // Create New Loan button using factory
        ButtonFactory newLoanButtonFactory = new NewLoanButtonFactory(this);
        JButton newLoanButton = newLoanButtonFactory.createButton();

        // Add buttons to button panel
        buttonPanel.add(backButton, BorderLayout.WEST);
        buttonPanel.add(newLoanButton, BorderLayout.EAST);
        return buttonPanel;
    }

    public static void main(String[] args) {
        // Establish database connection
        Connn connection = new Connn();
        // Provide the customer ID for the loan display
        int customerID = 1; // Change this to the desired customer ID
        // Create and display the loan display page
        SwingUtilities.invokeLater(() -> new LoansDisplayPage(connection, customerID).setVisible(true));
    }
}
