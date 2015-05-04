package edu.nyu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public  class DatabaseConnection {


    private static String driverClassName = "com.mysql.jdbc.Driver";
//    private static String url = "jdbc:mysql://54.187.12.208:3306/foodiehoodie";
//    private static String username = "foodiehoodie-dev";
//    private static String password = "@foodiehoodie#69";

    private static String url = "jdbc:mysql://localhost:3306/foodiehoodie";
    private static String username = "root";
    private static String password = "root";

    public static Connection getConnection(){
        Connection dbConnection = null;
        try {

            Class.forName(driverClassName);
            dbConnection = DriverManager.getConnection(url, username, password);

        } catch(ClassNotFoundException cnfe) {
            System.err.println("Error loading driver: " + cnfe);
        } catch(SQLException sqle) {
            System.err.println("Error connecting: " + sqle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbConnection;
    }

}
