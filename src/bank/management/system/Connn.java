package bank.management.system;

import java.sql.*;

public class Connn {
    Connection connection;
    Statement statement;
    public Connn(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankSystem","root","omkar2003");
            statement = connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public int executeUpdate(String query) {
        try {
            // Execute the update query
            return statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error occurred
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            // Execute the query and return the result set
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return null to indicate an error occurred
        }
    }

    // Method to prepare a parameterized statement
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }


}
