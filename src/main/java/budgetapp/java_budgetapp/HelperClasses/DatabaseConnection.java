package budgetapp.java_budgetapp.HelperClasses;

import java.sql.Connection;
import java.sql.DriverManager;


//JDBC Connection to DB
public class DatabaseConnection {
    public static Connection databaseLink;

    public static Connection getConnection() {
        String databaseName = "budgetlogin";
        String databaseUser = "username";
        String databasePassword = "password";
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;

        try{
            Class.forName("org.postgresql.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
