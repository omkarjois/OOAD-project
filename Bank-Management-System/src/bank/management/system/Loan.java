package bank.management.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Date; // Import Date class
import java.util.ArrayList;
import java.util.List;

// Define an interface for observers
interface LoanObserver {
    void updateMonthlyPayment(double monthlyPayment);
}

// Loan class now extends Observable
public class Loan extends JFrame {
    private final JTextField loanAmountField;
    private final JTextField loanTenureField;
    private final JLabel interestRateLabel;
    private final JLabel minimumAgeLabel;
    private final JLabel minimumIncomeLabel;
    private final JLabel minimumCreditScoreLabel;
    private final JCheckBox requiresCollateralCheckBox;
    private final JLabel monthlyPaymentLabel;
    private final List<LoanObserver> observers;

    private static final double INTEREST_RATE = 12.5;
    private static final double MINIMUM_AGE = 21;
    private static final double MINIMUM_INCOME = 25000;
    private static final int MINIMUM_CREDIT_SCORE = 750;
    private static final double COLLATERAL_THRESHOLD = 1500000;

    // Adding a field for the database connection
    private Connn connection = new Connn();
    public String customerId;
    public Loan(String customerId) {
        super("Personal Loan Application");
        this.customerId = customerId;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        observers = new ArrayList<>();

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel loanDetailsLabel = new JLabel("Loan Details");
        loanDetailsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(loanDetailsLabel, gbc);

        JLabel loanAmountLabel = new JLabel("Loan Amount:");
        loanAmountField = new JTextField(20);

        JLabel loanTenureLabel = new JLabel("Loan Tenure (Years):");
        loanTenureField = new JTextField(20);

        interestRateLabel = new JLabel("Interest Rate (%): " + INTEREST_RATE);
        minimumAgeLabel = new JLabel("Minimum Age Requirement: " + MINIMUM_AGE + " years");
        minimumIncomeLabel = new JLabel("Minimum Income Requirement: ₹" + MINIMUM_INCOME);
        minimumCreditScoreLabel = new JLabel("Minimum Credit Score: " + MINIMUM_CREDIT_SCORE);

        requiresCollateralCheckBox = new JCheckBox("Requires Collateral");
        requiresCollateralCheckBox.setEnabled(false);

        monthlyPaymentLabel = new JLabel("Estimated Monthly Payment:");
        monthlyPaymentLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        contentPanel.add(loanAmountLabel, gbc);

        gbc.gridx = 10;
        gbc.weightx = 0.6;
        contentPanel.add(loanAmountField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        contentPanel.add(loanTenureLabel, gbc);

        gbc.gridx = 10;
        gbc.weightx = 0.6;
        contentPanel.add(loanTenureField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentPanel.add(new JLabel("Conditions"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentPanel.add(interestRateLabel, gbc);

        gbc.gridy++;
        contentPanel.add(minimumAgeLabel, gbc);

        gbc.gridy++;
        contentPanel.add(minimumIncomeLabel, gbc);

        gbc.gridy++;
        contentPanel.add(minimumCreditScoreLabel, gbc);

        gbc.gridy++;
        contentPanel.add(requiresCollateralCheckBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentPanel.add(new JLabel(), gbc);

        JButton submitButton = new JButton("Submit Application");
        submitButton.addActionListener(new SubmitButtonListener());
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(submitButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentPanel.add(monthlyPaymentLabel, gbc);

        // Add the "Back to Home" button at the bottom left
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to HomePage
                Home home = new Home(customerId);
                home.setVisible(true);
                dispose(); // Close the current Loan frame
            }
        });
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Place it at the bottom
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align it to the left
        gbc.weightx = 1; // Make it expand horizontally
        contentPanel.add(backButton, gbc);

        add(contentPanel, BorderLayout.CENTER);
        pack();
    }

    // Add observer registration method
    public void addObserver(LoanObserver observer) {
        observers.add(observer);
    }

    // Notify all observers of the monthly payment update
    private void notifyObservers(double monthlyPayment) {
        for (LoanObserver observer : observers) {
            observer.updateMonthlyPayment(monthlyPayment);
        }
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double loanAmount = Double.parseDouble(loanAmountField.getText());
                int loanTenure = Integer.parseInt(loanTenureField.getText());
                double monthlyInterestRate = INTEREST_RATE / (12 * 100);
                int numberOfPayments = loanTenure * 12;
                double monthlyPayment = loanAmount * monthlyInterestRate /
                        (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));

                DecimalFormat df = new DecimalFormat("#.##");
                monthlyPaymentLabel.setText("Estimated Monthly Payment: ₹" + df.format(monthlyPayment));

                // Notify observers of the monthly payment update
                notifyObservers(monthlyPayment);

                requiresCollateralCheckBox.setSelected(loanAmount > COLLATERAL_THRESHOLD);

                // Insert loan application into database
                // Format the date properly before inserting into the database
                java.sql.Date date = new java.sql.Date(new Date().getTime());
                String insertQuery = "INSERT INTO Loan ( CustomerID, Amount, LoanCleared, InterestRate, Tenure, LoanDetails, LoanConditions, DateApplied) VALUES " +
                        "( "+customerId+", " + loanAmount + ", NULL, " + INTEREST_RATE + ", " + loanTenure + ", NULL, NULL, " +
                        "'" + date + "')";

                int rowsAffected = connection.executeUpdate(insertQuery);
                if (rowsAffected > 0) {
                    System.out.println("Loan application inserted successfully.");
                } else {
                    System.out.println("Failed to insert loan application.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Loan.this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}