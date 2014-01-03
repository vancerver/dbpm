package edu.uci.grad.security.util;

import edu.uci.grad.security.model.JdbcConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class JdbcDatabaseUtil {

    // argument 0 is password used to connect user, argument 1 is the new password.
    public static final String QUERY_TO_CHANGE_PASSWORD = "sp_password @old='%s', @new='%s'";

    public static void updatePassword(String applicationUsername, String newPassword, String connectionUsername, String connectionPassword, String jdbcUrl, String driverClass) throws ClassNotFoundException, SQLException {
	//    try {
	// Load the database driver
	Class.forName(driverClass);

	// Get a connection to the database
	Connection conn = DriverManager.getConnection(jdbcUrl, connectionUsername, connectionPassword);

	// Print all warnings
	for (SQLWarning warn = conn.getWarnings(); warn != null; warn = warn.getNextWarning()) {
	    System.out.println("SQL Warning:");
	    System.out.println("State  : " + warn.getSQLState());
	    System.out.println("Message: " + warn.getMessage());
	    System.out.println("Error  : " + warn.getErrorCode());
	}

	// Get a statement from the connection
	Statement stmt = conn.createStatement();

	// Execute the Update
	int rows = stmt.executeUpdate(String.format(QUERY_TO_CHANGE_PASSWORD, connectionPassword, newPassword));// + applicationUsername + " WITH PASSWORD = '" + newPassword + "';" ) ;

	// Print how many rows were modified
	System.out.println(rows + " Rows modified");

	// Close the statement and the connection
	stmt.close();
	conn.close();

    }

    public static String urlQuery(String urlString) {
	try {
	    URL url;

	    try {
		url = new URL(urlString);
	    } catch (MalformedURLException e) {
		throw new Error(e);
	    }
	    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	    Scanner s = new Scanner(urlConnection.getInputStream());
	    String response = "";
	    try {
		response = s.nextLine();
	    } catch (NoSuchElementException e) {
	    }
	    urlConnection.disconnect();
	    return response;
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Was not able to connect to the URL: \n"
		    + urlString + "\n"
		    + "No ColdFusion passwords have been changed.\n"
		    + "You can change ColdFusion Settings in the Options tab.");
	}
	return null;
    }

    public static boolean checkConnection(String driver, String url,
	    String user, String pwd) {

	// attempt to get a connection to the data source using the given
	// driver, url, user, and password
	Connection connection = null;
	boolean bConnected = false;
	try {
	    Class.forName(driver);
	    connection = DriverManager.getConnection(url, user, pwd);
	    bConnected = true;
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (connection != null) {
		try {
		    connection.close();
		} catch (SQLException se) {
		}
		connection = null;
	    }
	    return bConnected;
	}
    }

    public static List<String> getAllDatabaseUserAccounts(JdbcConnection databaseServerConnection, String username, String password) throws ClassNotFoundException, SQLException {
	Class.forName(databaseServerConnection.getJdbcDriverClass());
	List<String> databases = new ArrayList<String>();
	List<String> users = new ArrayList<String>();
	// Get a connection to the database
	Connection conn = DriverManager.getConnection(databaseServerConnection.getJdbcUrl(), username, password);

	DatabaseMetaData meta = conn.getMetaData();
	ResultSet rs = meta.getCatalogs();
	System.out.println(meta.getSystemFunctions());
//	Statement statement = conn.createStatement();
//	ResultSet rs = statement.executeQuery("exec sp_databases");
////	rs.beforeFirst();
	while (rs.next()) {
	    databases.add(rs.getString("TABLE_CAT"));
	}
	rs.close();
	Statement statement = conn.createStatement();
	rs = statement.executeQuery("SELECT * FROM moviedb.sys.sysusers");
	while (rs.next()) {
	    users.add(rs.getString("name"));
	}
	conn.close();
	System.out.println(users.size());
	return users;
    }

    public static List<String> getAllDatabaseUserAccountsForTest(JdbcConnection importAccountsDatabaseServerConnection, String JDBC_URL_PREFIX, String JDBC_URL_PREFIX0) {
	try {
	    return getAllDatabaseUserAccounts(importAccountsDatabaseServerConnection, JDBC_URL_PREFIX, JDBC_URL_PREFIX0);
	} catch (ClassNotFoundException ex) {
	    Logger.getLogger(JdbcDatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
	} catch (SQLException ex) {
	    Logger.getLogger(JdbcDatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
