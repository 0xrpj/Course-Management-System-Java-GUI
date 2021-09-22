package Helpers;

import java.sql.*;

public class DBUtils {
    private static Statement stmt;
    private static Connection conn;
    int exist = 0;

    public DBUtils() { // if db exists?

        Connection con = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306?useSSL=false";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            if (con != null) {
                System.out.println("check if a database exists using java");
                rs = con.getMetaData().getCatalogs();
                while (rs.next()) {
                    String catalogs = rs.getString(1);
                    if (Config.dbName.equals(catalogs)) {
                        System.out.println("the database " + Config.dbName + " exists");
                        exist = 1;
                    }
                }
            } else {
                System.out.println("unable to create database connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static Connection getdbConnection() throws SQLException {
        String connectionString;

        // Registering JDBC driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ce) {
            System.out.println("Something is wrong with mysql driver.");
        }

        // connectionString = "jdbc:mysql://" + Config.dbHost + ":" + Config.dbPort +
        // "/" + Config.dbName
        // + "?useSSL=false";

        if (new DBUtils().exist == 0) {
            try {
                // Opening a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection("jdbc:mysql://" + Config.dbHost + "/", Config.dbUser, Config.dbPass);

                // Executing a query
                System.out.println("Creating database...");
                stmt = conn.createStatement();

                String sql = "CREATE DATABASE coursemgmt";
                // PreparedStatement statement = conn.prepareStatement(sql);
                // statement.setString(1, Config.dbName);

                stmt.executeUpdate(sql);
                System.out.println("Database created successfully...");
            } catch (SQLException se) {
                // Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                // Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                // finally block used to close resources
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                } // nothing we can do
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                } // end finally try
            } // end try

        }

        connectionString = "jdbc:mysql://" + Config.dbHost + ":" + Config.dbPort + "/" + Config.dbName
                + "?useSSL=false";
        return DriverManager.getConnection(connectionString, Config.dbUser, Config.dbPass);
    }
}
