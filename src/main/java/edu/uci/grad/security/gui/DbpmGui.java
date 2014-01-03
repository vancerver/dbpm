/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uci.grad.security.gui;

import static edu.uci.grad.security.SystemwideConstants.*;
import edu.uci.grad.security.dao.*;
import edu.uci.grad.security.model.*;
import edu.uci.grad.security.util.JdbcDatabaseUtil;
import edu.uci.grad.security.util.PasswordUtils;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.net.URI;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.basic.BasicSliderUI.TrackListener;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 *
 * @author VizzlePuff
 */
public class DbpmGui extends javax.swing.JFrame {

    // Default JDBC URL Strings
    public static final String JDBC_URL_PREFIX = "jdbc:jtds:sqlserver://";
    public static final String JDBC_DEFAULT_PORT = "1433";
    public static final String DEFAULT_DATABASE_DRIVER_CLASS = "net.sourceforge.jtds.jdbc.Driver";
// <editor-fold desc="Default Account Associations" defaultstate="collapsed">
    // Default Database Server Information
    public static String DEFAULT_DATABASE_SERVER_NAME = "Local Host";
    public static String DEFAULT_DATABASE_SERVER_URL = JDBC_URL_PREFIX + "localhost:" + JDBC_DEFAULT_PORT;
    // Default Password Container Information
    public static final String DEFAULT_PASSWORDFILE_NAME = "Default Password File";
    public static final String DEFAULT_PASSWORD_FILE_PATH = "default.odb";
    // Default password strength criteria.
    // These can be changed through the tool at any time.
    public static final int PASSWORD_CRITERIA_DEFAULT_NUMBER_OF_CHAR_REPEATS_TO_ALLOW = 4;
    public static final boolean PASSWORD_CRITERIA_DEFAULT_DISALLOW_ALPHA_SEQUENCE = true;
    public static final boolean PASSWORD_CRITERIA_DEFAULT_DISALLOW_CHAR_REPEATS = true;
    public static final boolean PASSWORD_CRITERIA_DEFAULT_DISALLOW_QWERTY_SEQUENCE = true;
    public static final boolean PASSWORD_CRITERIA_DEFAULT_DISALLOW_WHITESPACE = true;
    public static final int PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_DIGITS = 1;
    public static final int PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_LOWERCASE = 1;
    public static final int PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_SYMBOLS = 1;
    public static final int PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_UPPERCASE = 1;
    public static final int PASSWORD_CRITERIA_DEFAULT_MINIMUM_PASSWORD_LENGTH = 8;
    public static final int PASSWORD_CRITERIA_DEFAULT_MAXIMUM_PASSWORD_LENGTH = 16;
    // Default ColdFusion admin information.
    public static final String DEFAULT_COLDFUSION_SERVER_NAME = "Local Host";
    public static final String DEFAULT_COLDFUSION_ADMIN_PASSWORD = null;
    public static final String DEFAULT_COLDFUSION_ADMIN_USERNAME = "admin";
    public static final String DEFAULT_RESET_CFSCRIPT_URL = "http://localhost/changePassword.cfm";
// </editor-fold>    
// <editor-fold desc="Icon Image Paths" defaultstate="collapsed">
    private static final String ADD_DATABASE_USER_ACCOUNT_ICON = "/images/addDatabaseUserAccount.png";
    private static final String ADD_DATABASE_USER_ACCOUNT_SMALL = "/images/addDatabaseUserAccountSmall.png";
    private static final String EDIT_DATABASE_USER_ACCOUNT_ICON_SMALL = "/images/editDatabaseUserAccountSmall.png";
    private static final String CONFIRM_DATABASE_USER_ACCOUNT_CHANGES_ICON = "/images/confirmDatabaseUserAccountChanges.png";
    private static final String REMOVE_DATABASE_LARGE_IMAGE_PATH = "/images/removeDatabase.png";
    private static final String REMOVE_PASSWORD_FILE_LARGE_IMAGE_PATH = "/images/RemovePasswordFile.png";
    private static final String ADD_DATABASE_LARGE_IMAGE_PATH = "/images/addDatabase.png";
    private static final String ADD_PASSWORD_CONTAINER_LARGE_IMAGE_PATH = "/images/addPasswordFile.png";
    private static final String ADD_COLDFUSION_SERVER_LARGE_IMAGE = "/images/addColdFusionServer.png";
    private static final String REMOVE_COLDFUSION_SERVER_LARGE_IMAGE_PATH = "/images/removeColdFusionServer.png";
    private static final String DATABASE_SERVERS_ICON_IMAGE_PATH = "/images/databases.png";
    private static final String PASSWORD_CONTAINERS_ICON_IMAGE_PATH = "/images/passwordContainers.png";
    private static final String COLDFUSION_SERVERS_ICON_IMAGE_PATH = "/images/cfPage.png";
    private static final String DATABASE_ICON_IMAGE_PATH = "/images/database.png";
    private static final String PASSWORD_CONTAINER_ICON_IMAGE_PATH = "/images/passwordContainerSmall.png";
    private static final String COLDFUSION_SERVER_ICON_IMAGE_PATH = "/images/coldFusionSmall.png";
// </editor-fold>   
// <editor-fold desc="System Help HTML Text" defaultstate="collapsed">
    private static final String GETTING_STARTED_HELP_TITLE = "Getting Started",
	    DATABASE_SERVERS_HELP_ROOT_TITLE = "Configure Database Servers",
	    DATABASE_SERVERS_HELP_INTRO_TITLE = "Database Servers Overview",
	    ADDING_DATABASE_SERVERS_HELP_TITLE = "Adding Database Servers",
	    EDITING_DATABASE_SERVERS_HELP_TITLE = "Editing Database Servers",
	    REMOVING_DATABASE_SERVERS_HELP_TITLE = "Removing Database Servers",
	    PASSWORD_CONTAINERS_HELP_ROOT_TITLE = "Configuring Password Containers",
	    PASSWORD_CONTAINERS_HELP_INTRO_TITLE = "Password Containers Overview",
	    ADDING_PASSWORD_CONTAINERS_HELP_TITLE = "Adding Password Containers",
	    EDITING_PASSWORD_CONTAINERS_HELP_TITLE = "Editing Password Containers",
	    REMOVING_PASSWORD_CONTAINERS_HELP_TITLE = "Removing Password Containers",
	    COLDFUSION_INTEGRATION_HELP_ROOT_TITLE = "Configuring ColdFusion Servers",
	    COLDFUSION_SERVERS_HELP_INTRO_TITLE = "ColdFusion Integration Overview",
	    ADDING_COLDFUSION_SERVERS_HELP_TITLE = "Adding ColdFusion Servers",
	    EDITING_COLDFUSION_SERVERS_HELP_TITLE = "Editing ColdFusion Servers",
	    REMOVING_COLDFUSION_SERVERS_HELP_TITLE = "Removing ColdFusion Servers",
	    PASSWORD_GENERATION_HELP_ROOT_TITLE = "Generating Passwords",
	    GENERATING_PASSWORDS_INTRO_TITLE = "Password Generation Overview",
	    MODIFYING_PASSWORD_STRENGTH_HELP_TITLE = "Modifying Password Strength Criteria",
	    DATABASE_USER_ACCOUNTS_HELP_ROOT_TITLE = "Configuring Database User Accounts",
	    DATABASE_USER_ACCOUNTS_HELP_INTRO_TITLE = "Database User Accounts Overview",
	    ADDING_DATABASE_USER_ACCOUNTS_HELP_TITLE = "Adding Database User Accounts",
	    EDITING_DATABASE_USER_ACCOUNTS_HELP_TITLE = "Editing Database User Accounts",
	    REMOVING_DATABASE_USER_ACCOUNTS_HELP_TITLE = "Removing Database User Accounts";
    private static final String GETTING_STARTED_HELP_HTML_BODY = "The " + PROJECT_NAME + " (" + PROJECT_SHORT_NAME + ") is meant to be a self-explanatory tool. "
	    + "The system can theoretically be launched and operated on any current JVM and requires no prior configuration. "
	    + "The system does, however, need an ObjectDB Server instance that will control access to both system data and encrypted passwords. "
	    + "<br><br>"
	    + "When starting the system without any command line arguments, you will be presented with a dialog that will help you setup or connect to an ObjectDB Server instance. "
	    + "This dialog will provide the following options: "
	    + "<ul><li><b>Startup & Use A New ObjectDB Server Instance @ localhost</b></li>"
	    + "<li><b>Connect to the provided ObjectDB Server Instance URL</b></li></ul> "
	    + "The first option will start a new OS process running ObjectDB on your local machine. "
	    + "If you have never used " + PROJECT_NAME + " on your system, you will be presented with a brand new " + PROJECT_NAME + " database. "
	    + "You will see an empty accounts list and some pre-populated default associations. "
	    + "If you have used " + PROJECT_NAME + " on the system before, this new ObjectDB Server instance will use your previously created database. "
	    + "<br><br>"
	    + "The second option should be used if ObjectDB Server is already running on your machine or if you want to connect to another instance, perhaps on a remote machine.",
	    DATABASE_SERVERS_HELP_ROOT_HTML_BODY = getGenericAssociationHelpRootHtml("database server"),
	    DATABASE_SERVERS_HELP_INTRO_HTML_BODY = "A database server association is essentially an object holding the connection properties necessary to connect to a database. "
	    + "<br><br>"
	    + "Since many different user accounts can exist on a single database instance, this object may be referenced by many different user account objects. "
	    + "Changing some property of a database server association will affect all user accounts referencing the object as well. "
	    + "This allows quick updates if, for example, the URL of a database changes. This change only needs to be made in one place (See the Editing Database Servers section for more details). "
	    + "<br><br>"
	    + "The ability to support database server associations is the key to most of the actions that " + PROJECT_SHORT_NAME + " can perform. "
	    + "A database server association is therefore required for every database user account created.",
	    ADDING_DATABASE_SERVERS_HELP_HTML_BODY = "Adding a database server association is a simple process:<ol>"
	    + "<li>Right-click on \"Database Servers\" in the \"Configure Account Associations\" table and select \"Add Database Server\" from the drop-down list.</li>"
	    + "<li>The \"Add JDBC Database Connection\" window should appear.</li>"
	    + "<li>Fill out this form with the necessary server information and make up a descriptive name for the database server association itself. Note that if you provide a server address and port number, the JDBC Connection URL will be created for you automatically. You can also ignore the top two form fields and simply enter the JDBC connection URL manually.</li>"
	    + "<li>Click \"Add New Database Connection\" to create the association.</li></ol> "
	    + "The new database server association should now be on your \"Configure Account Associations\" tree under \"Database Servers\".",
	    EDITING_DATABASE_SERVERS_HELP_HTML_BODY = getGenericEditingAssociationHtml("database server"),
	    REMOVING_DATABASE_SERVERS_HELP_HTML_BODY = getGenericRemovingAssociationHtml("database server"),
	    PASSWORD_CONTAINERS_HELP_ROOT_HTML_BODY = getGenericAssociationHelpRootHtml("password container"),
	    PASSWORD_CONTAINERS_HELP_INTRO_HTML_BODY = "A password container is a seperate database file that is capable of holding one or more encrypted passwords. "
	    + "Password containers offer the ability to customize access controls on certain sets of passwords. "
	    + "The idea is that if you seperate passwords into different files, different Operating System-based access controls can be applied to each file."
	    + "If this feature is not needed by your team, the default password file (created when starting " + PROJECT_SHORT_NAME + " for the first time) should be used. "
	    + "Every database user account must have a password container assigned to it.",
	    ADDING_PASSWORD_CONTAINERS_HELP_HTML_BODY = "Adding a password container is a simple process:<ol>"
	    + "<li>Right-click on \"Password Containers\" in the \"Configure Account Associations\" table and select \"Add Password Container\".</li>"
	    + "<li>The \"Add New Password Container\" window should appear.</li>"
	    + "<li>Fill in a name for the new password container. You may also change the location URL for the password container but this should not be required unless you are using some advanced distributed security techniques.</li>"
	    + "<li>Click \"Add New Password Container\"</li></ol> "
	    + "The new password container should now be on your \"Configure Account Associations\" tree under \"Password Containers\".",
	    EDITING_PASSWORD_CONTAINERS_HELP_HTML_BODY = getGenericEditingAssociationHtml("password container"),
	    REMOVING_PASSWORD_CONTAINERS_HELP_HTML_BODY = getGenericRemovingAssociationHtml("password container"),
	    COLDFUSION_SERVERS_HELP_ROOT_HTML_BODY = getGenericAssociationHelpRootHtml("coldFusion server"),
	    COLDFUSION_SERVERS_HELP_INTRO_HTML_BODY = "",
	    ADDING_COLDFUSION_SERVERS_HELP_HTML_BODY = "",
	    EDITING_COLDFUSION_SERVERS_HELP_HTML_BODY = "",
	    REMOVING_COLDFUSION_SERVERS_HELP_HTML_BODY = getGenericRemovingAssociationHtml("coldFusion server"),
	    GENERATING_PASSWORDS_HELP_ROOT_HTML_BODY = "" + PROJECT_NAME + " comes equiped with a strong and highly customizable password generator and password strength checker. "
	    + "This folder contains all you need to know about password generation in " + PROJECT_SHORT_NAME + ". It contains the following sections:<ul>"
	    + "<li><b>Password Generation Overview</b>: The basics of password generation and strength validation in " + PROJECT_SHORT_NAME + ".</li>"
	    + "<li><b>Modifying Password Strength Criteria</b>: How to customize password generation and strength validation properties.",
	    GENERATING_PASSWORDS_HELP_INTRO_HTML_BODY = "" + PROJECT_NAME + "'s password generator is able to create highly secure passwords with a single click. "
	    + "The generated passwords will always obey the password strength criteria specified by " + PROJECT_SHORT_NAME + " administrators. "
	    + "There are currently two places within " + PROJECT_SHORT_NAME + " where the password generator is used:<ul>"
	    + "<li>When adding a new database user account or editing an existing one, you can generate a password to use for the account. "
	    + "This password must still match the password on the database if adding a new account. "
	    + "For editing an existing account, the generated password will automatically replace the existing password both in " + PROJECT_SHORT_NAME + " and on the database server itself.</li>"
	    + "<li>When performing an automatic password rotation, all new passwords will be generated using the password generator.</li></ul>",
	    MODIFYING_PASSWORD_STRENGTH_CRITERIA_HELP_HTML_BODY = "" + PROJECT_NAME + " provides a simple way to customize password strength criteria from within the application itself. "
	    + "This is an important function because the password strength criteria are used to generate strong new passwords and also to validate the strength of existing passwords that have been input by the user. "
	    + "The system will not allow a user to add a password that violates the password strength criteria. "
	    + "To modify the password strength criteria of the system:<ol>"
	    + "<li>Click on \"Options\" in the file menu of the " + PROJECT_NAME + " main window and then click \"Password Strength Criteria\".</li>"
	    + "<li>The \"Password Strength Criteria\" window should now appear.</li>"
	    + "<li>Modify some criteria.</li>"
	    + "<li>Click \"Apply\" to set your new criteria active.</li></ol> "
	    + "The password strength criteria fields are described below:<ul>"
	    + "<li><b>Password length</b>: dictates minimum and maximum allowable length for the password. "
	    + "Slide the blue circle to the desired position of the lower bound of acceptable password length. "
	    + "Slide the red circle to the desired position of the upper bound of acceptable password length. "
	    + "The range is displayed on the label to the right of the slider.</li>"
	    + "<li><b>Mimimum number of digits in password</b>: dictates the minimum number of digits (numbers 0-9) that must be present in the password for it to be accepted. Use the spinner to the right to select a desired minimum.</li>"
	    + "<li><b>Mimimum number of symbols in password</b>: dictates the minimum number of symbols (non-alphanumeric characters I.E. %^&*@) that must be present in the password for it to be accepted. Use the spinner to the right to select a desired minimum.</li>"
	    + "<li><b>Mimimum number of lowercase letters in password</b>: dictates the minimum number of lowercase letters (a-z) that must be present in the password for it to be accepted. Use the spinner to the right to select a desired minimum.</li>"
	    + "<li><b>Mimimum number of uppercase letters in password</b>: dictates the minimum number of uppercase letters (A-Z) that must be present in the password for it to be accepted. Use the spinner to the right to select a desired minimum.</li>"
	    + "<li><b>Disallow char repeats more than this many times</b>: dictates the minimum number of times a single character can be in a password. "
	    + "A password such as \"ffggfupfgf\" has 5 character repeats on letter f. "
	    + "This password would be denied if character repeats greater than 4 are not allowed. "
	    + "Select the checkbox to the left to enable this filter and then use the spinner to the right to select a desired maximum.</li>"
	    + "<li><b>Disallow dictionary words that are longer than this length</b>: dictates the maximum length of character sequences within the password that can match a dictionary word. "
	    + "If a password of \"asdfasgreensa\" will be denied if this is set to 4 because the password contains the sequence \"green\" which is a dictionary word longer than 4 characters. "
	    + "Select the checkbox to the left to enable this filter and then use the spinner to the right to select a desired value.</li>"
	    + "<li><b>Disallow whitespace</b>: dictates whether or not whitespace (spaces, tabs) can be present in the password. If the checkbox to the right is selected no whitespace will be allowed in a password.</li>"
	    + "<li><b>Disallow QWERTY sequences</b>: dictates if characters within the password can be in the same order as they are on a QWERTY keyboard. If the checkbox to the right is selected, passwords that have characters in the same order as on a keyboard will not be allowed.</li>"
	    + "<li><b>Disallow alphabetical sequences</b>: dictates if characters within the password can be in alphabetical order. If the checkbox to the right is selected, no passwords that have characters in alphabetical order will be accepted.</li>",
	    DATABASE_USER_ACCOUNTS_HELP_ROOT_HTML_BODY = "This folder contains all the information you need to know about database user accounts in " + PROJECT_SHORT_NAME + ". "
	    + "It contains the following sections:<ul>"
	    + "<li><b>" + DATABASE_USER_ACCOUNTS_HELP_INTRO_TITLE + "</b>: Explains what database user accounts are and how they are used in " + PROJECT_SHORT_NAME + "</li>"
	    + "<li><b>" + ADDING_DATABASE_USER_ACCOUNTS_HELP_TITLE + "</b>: Explains how to add database user accounts to " + PROJECT_SHORT_NAME + ".</li>"
	    + "<li><b>" + EDITING_DATABASE_USER_ACCOUNTS_HELP_TITLE + "</b>: Explains how to modify database user account properties after the account has been created.</li>"
	    + "<li><b>" + REMOVING_DATABASE_USER_ACCOUNTS_HELP_TITLE + "</b>: Explains how to remove database user accounts from " + PROJECT_SHORT_NAME + ".</li></ul>",
	    DATABASE_USER_ACCOUNTS_HELP_INTRO_HTML_BODY = "Database user accounts are " + PROJECT_SHORT_NAME + "'s representation of actual user accounts on some database system. "
	    + PROJECT_NAME + " insures that the username and password of a database user account object always match the actual database username and password. "
	    + "It does this by:<ul>"
	    + "<li>Requiring a database connection test when creating a new database user account.</li>"
	    + "<li>Updating the password on the database when it is changed on " + PROJECT_SHORT_NAME + ".</li></ul> "
	    + "Note that if a password is changed manually on a database system without going through " + PROJECT_SHORT_NAME + " then the passwords will no longer be in sync. "
	    + "For this reason, it is important to always do password changes through " + PROJECT_NAME + " once the account has been added to the system.",
	    ADDING_DATABASE_USER_ACCOUNTS_HELP_HTML_BODY = "",
	    EDITING_DATABASE_USER_ACCOUNTS_HELP_HTML_BODY = "",
	    REMOVING_DATABASE_USER_ACCOUNTS_HELP_HTML_BODY = "";

    private static String getGenericEditingAssociationHtml(String association) {
	return "Editing a " + association + " association is a simple process:<ol>"
		+ "<li>Expand \"" + capitalize(association) + "s\" in the \"Configure Account Associations\" tree.</li>"
		+ "<li>Click on the " + association + " association that you would like to edit so that is is highlighted</li>"
		+ "<li>You should now notice that the properties for the selected " + association + " are displayed in the \"Selected Association Details\" table below.</li>"
		+ "<li>Double-click the the cell in the \"Value\" column for the property you would like to edit, change the value, and then press ENTER. You can also select the row of the property, click the pencil button below to edit, change the value, and then click on the check mark icon to save the change.</li></ol> "
		+ "The property should now be changed on the selected " + association + " association and all user accounts linked to this " + association + " will now use the new value.";
    }

    private static String getGenericRemovingAssociationHtml(String association) {
	return "Removing a " + association + " association is a simple process:<ol>"
		+ "<li>Expand \"" + capitalize(association) + "s\" in the \"Configure Account Associations\" tree.</li>"
		+ "<li>Right-click on the " + association + " association that you would like to remove and click \"Remove " + capitalize(association) + "\".</li></ol> "
		+ "The " + association + " should now be removed. Note that any user accounts that are linked to the " + association + " will also be removed. If you want to keep these user accounts, you must assign them to a different " + association + " before deleting.";
    }

    private static String getGenericAssociationHelpRootHtml(String association) {
	return "This folder contains all the information you need to know about " + PROJECT_SHORT_NAME + " " + capitalize(association) + " objects. "
		+ "<br><br>"
		+ "It contains the following sections: <ul>"
		+ "<li><b>" + capitalize(association) + "s Overview</b>: Explains what " + association + " associations are and how are they used in " + PROJECT_SHORT_NAME + "</li>"
		+ "<li><b>Adding " + capitalize(association) + "s</b>: Explains the process of adding " + association + " associations to " + PROJECT_SHORT_NAME + "</li>"
		+ "<li><b>Editing " + capitalize(association) + "s</b>: Explains the process of modifying the details of " + association + " associations after they have been created.</li>"
		+ "<li><b>Removing " + capitalize(association) + "s</b>: Explains the process of removing " + association + " associations from " + PROJECT_SHORT_NAME + ".</li></ul>";
    }

    public static String capitalize(String string) {
	char[] chars = string.toLowerCase().toCharArray();
	boolean found = false;
	for (int i = 0; i < chars.length; i++) {
	    if (!found && Character.isLetter(chars[i])) {
		chars[i] = Character.toUpperCase(chars[i]);
		found = true;
	    } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
		found = false;
	    }
	}
	return String.valueOf(chars);
    }
// </editor-fold>
// <editor-fold desc="GUI Labels" defaultstate="collapsed">
    private static final String DATABASE_SERVERS_NODE_LABEL = "Database Servers";
    private static final String COLDFUSION_SERVERS_NODE_LABEL = "ColdFusion Servers";
    private static final String PASSWORD_CONTAINERS_NODE_LABEL = "Password Containers";
    private static final String ALL_ACCOUNTS_SHOWING_LABEL = "All Database User Accounts";
    private static final String SEARCH_ACCOUNTS_DEFAULT_MESSAGE = "** Click to Search Accounts **";
    // GUI Messages Displayed On Window For Various Events
    public static final String MESSAGE_FOR_PASSWORD_REMOVED = "Password has just been removed for "; // Username is inserted after this String.
    public static final String MESSAGE_FOR_PASSWORD_ADDED = "Password has just been added for "; // Username is inserted after this String. 
    public static final String MESSAGE_FOR_MISSING_USERNAME = "Username field cannot be blank.";
    public static final String MESSAGE_FOR_PASSWORD_NOT_MEETING_CRITERIA = "The password given does not meet strength criteria";
    public static final String MESSAGE_FOR_PASSWORDS_NOT_MATCHING = "Passwords do not match.";
    public static final String MESSAGE_FOR_NEW_PASSWORD_GENERATED = "A new password has been generated";
    public static final String MESSAGE_FOR_PASSWORD_MODIFIED = "Password has just been modified for "; // Username is inserted after this String.
    public static final String MESSAGE_FOR_USERNAME_NOT_SELECTED = "You must select a username to modify";
    public static final String JDBC_URL_GENERATION_MESSAGE = "Generated By Fields Above - Click Here To Edit Directly";
    public static final Object MESSAGE_FOR_JDBC_URL_AND_DESCRIPTION_EMPTY = "JDBC URL and Descriptive Name fields must not be empty";
    public static final String MESSAGE_FOR_IN_JDBC_URL_MANUAL_ENTRY_MODE = "In Manual JDBC URL Entry Mode - Click Here to Undo";
    public static final String MESSAGE_FOR_PASSWORD_FILE_NOT_SELCTED = "You must select a password file to store this new account in";
    public static final String MESSAGE_FOR_DATABASE_CONNECTION_NOT_SELECTED = "You must select a database connection.";
    public static final Object MESSAGE_FOR_INVALID_PASSWORD_CRITERIA_INPUT = "All Text Fields Must Have a Numeric Value. Use 0 to not include a certain rule";
    public static final String MESSAGE_CONFIRMING_NEW_PASSWORD_ADDITION = "You are about to add a new password to the system:\nDatabase path: %s\n" + "Username added: %s\n" + "Database association: %s\n" + "ColdFusion: %s\n\nIs this correct?"; // Params: {JDBC connction URL, username of new password, descriptive connection name, is this a coldfusion app?}
    public static final String MESSAGE_CONFIRMING_PASSWORD_MODIFICATION = "You are about to submit a new password for the database user %s\n\nIs this correct?"; // Params: {username of selected account}
    public static final Object MESSAGE_FOR_ACCOUNT_NOT_SELECTED_IN_VIEW_PASSWORDS = "You must select an database user account to view a password for";
    public static final String MESSAGE_CONFIRMING_NEW_DATABASE_CONNECTION_ADDITION = "The database connection %s at JDBC URL %s has been added.";
    // GUI JOptionPane Window Titles
    public static final String WINDOW_TITLE_FOR_ADD_DATABASE_FIELDS_MISSING = "Error: Blank Field";
    public static final String WINDOW_TITLE_FOR_PASSWORD_NOT_MEETING_CRITERIA_OPTION_DIALOG = "Error: Weak Password";
// </editor-fold>
// <editor-fold desc="GUI Tool Tip Text" defaultstate="collapsed">
    private static final String DATABASE_SERVERS_TOOL_TIP_TEXT = "Database servers that this tool manages passwords for.";
    private static final String DATABASE_SERVER_TOOL_TIP_TEXT = "";
    private static final String PASSWORD_CONTAINERS_TOOL_TIP_TEXT = "";
    private static final String PASSWORD_CONTAINER_TOOL_TIP_TEXT = "";
    private static final String COLDFUSION_SERVERS_TOOL_TIP_TEXT = "ColdFusion Server configuration to associate passwords with.";
    private static final String COLDFUSION_SERVER_TOOL_TIP_TEXT = "";
// </editor-fold>
    // Declare the various DAOs used to access the Master Passwords Database
    private ColdFusionConfigurationDao coldFusionConfigurationDao = new ColdFusionConfigurationDao();
    private JdbcConnectionDao jdbcConnectionDao = new JdbcConnectionDao();
    private KeyDao keyDao = new KeyDao();
    private AccountInfoDao accountInfoDao = new AccountInfoDao();
    private PasswordPolicyDao passwordPolicyDao = new PasswordPolicyDao();
    private PasswordContainerDao passwordFileDao = new PasswordContainerDao();
    private EncryptedPasswordDao encryptedPasswordDao = new EncryptedPasswordDao();

// <editor-fold desc="System Startup Methods" defaultstate="collapsed">
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

//        File file = new File("dbpm.odb");  
//	String databaseUrl2 = file.getAbsolutePath();
//        file = new File(databaseUrl2);
        final String databaseUrl = "objectdb://localhost:6136/";

	System.setProperty(MAIN_DATABASE_SERVER_ROOT_URL_SYSTEM_PROPERTY_NAME, databaseUrl);
	/*
	 * Set the Nimbus look and feel
	 */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
	 * If Nimbus (introduced in Java SE 6) is not available, stay with the
	 * default look and feel. For details see
	 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(DbpmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(DbpmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(DbpmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(DbpmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	final SpinnerDialog spinnerDialog = new SpinnerDialog(null, true);
	spinnerDialog.setVisible(true);
	/*
	 * Create and display the form
	 */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		// If the master database file is empty, it must have just
		// been created and must be filled with default data.
		if (new PasswordPolicyDao().isMasterDatabaseEmpty()) {
		    showMessageDialog(databaseUrl + "dbpm.odb");
		    createNewMasterDatabase();
		}
		new DbpmGui().setVisible(true);
		spinnerDialog.setVisible(false);
	    }
	});
    }

    private static void showMessageDialog(String path) throws HeadlessException {
	JOptionPane.showMessageDialog(
		null,
		"Welcome to the " + PROJECT_NAME + "\n\n"
		+ "No existing password database installation has been detected.\n"
		+ "A new one will now be created at the path:\n"
		+ path + "\n",
		"Create New " + PROJECT_NAME + " Instance",
		JOptionPane.INFORMATION_MESSAGE);
    }

    public static void createNewMasterDatabase() {
	PasswordPolicy defaultPolicy = new PasswordPolicy();
	defaultPolicy.setAmountOfCharRepeatsToAllow(PASSWORD_CRITERIA_DEFAULT_NUMBER_OF_CHAR_REPEATS_TO_ALLOW);
	defaultPolicy.setDefault(true);
	defaultPolicy.setDisallowAlphaSequence(PASSWORD_CRITERIA_DEFAULT_DISALLOW_ALPHA_SEQUENCE);
	defaultPolicy.setDisallowCharRepeats(PASSWORD_CRITERIA_DEFAULT_DISALLOW_CHAR_REPEATS);
	defaultPolicy.setDisallowQwertySequence(PASSWORD_CRITERIA_DEFAULT_DISALLOW_QWERTY_SEQUENCE);
	defaultPolicy.setDisallowWhitespace(PASSWORD_CRITERIA_DEFAULT_DISALLOW_WHITESPACE);
	defaultPolicy.setMaximumNumberOfDigits(PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_DIGITS);
	defaultPolicy.setMaximumNumberOfLowercase(PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_LOWERCASE);
	defaultPolicy.setMaximumNumberOfSymbols(PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_SYMBOLS);
	defaultPolicy.setMaximumNumberOfUppercase(PASSWORD_CRITERIA_DEFAULT_REQUIRED_NUMBER_OF_UPPERCASE);
	defaultPolicy.setMaximumPasswordLength(PASSWORD_CRITERIA_DEFAULT_MAXIMUM_PASSWORD_LENGTH);
	defaultPolicy.setMinimumPasswordLength(PASSWORD_CRITERIA_DEFAULT_MINIMUM_PASSWORD_LENGTH);
	defaultPolicy.setPolicyActive(true);

	ColdFusionConfiguration defaultConfiguration = new ColdFusionConfiguration();
	defaultConfiguration.setColdFusionServerName(DEFAULT_COLDFUSION_SERVER_NAME);
	defaultConfiguration.setCfAdminPassword(DEFAULT_COLDFUSION_ADMIN_PASSWORD);
	defaultConfiguration.setCfAdminUsername(DEFAULT_COLDFUSION_ADMIN_USERNAME);
	defaultConfiguration.setResetCfScriptUrl(DEFAULT_RESET_CFSCRIPT_URL);

	PasswordContainer defaultGroup = new PasswordContainer();
	defaultGroup.setGroupName(DEFAULT_PASSWORDFILE_NAME);
	defaultGroup.setDatabaseFileUrl(System.getProperty(MAIN_DATABASE_SERVER_ROOT_URL_SYSTEM_PROPERTY_NAME) + DEFAULT_PASSWORD_FILE_PATH);

	JdbcConnection localHostConnection = new JdbcConnection();
	localHostConnection.setDescriptiveName(DEFAULT_DATABASE_SERVER_NAME);
	localHostConnection.setJdbcUrl(DEFAULT_DATABASE_SERVER_URL);
	localHostConnection.setJdbcDriverClass(DEFAULT_DATABASE_DRIVER_CLASS);

	new MasterDatabaseDao().persistMaster(defaultConfiguration, defaultGroup, defaultPolicy, localHostConnection);
    }

// </editor-fold>
// <editor-fold desc="Constructor" defaultstate="collapsed">
    /**
     * Creates new form DbpmGui
     */
    public DbpmGui() {
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addDatabaseConnectionDialog = new javax.swing.JDialog();
        addDatabaseLabel = new javax.swing.JLabel();
        serverAddressLabel = new javax.swing.JLabel();
        portNumberLabel = new javax.swing.JLabel();
        descriptiveNameLabel = new javax.swing.JLabel();
        serverAddressTextField = new javax.swing.JTextField();
        portNumberTextField = new javax.swing.JTextField();
        descriptiveNameTextField = new javax.swing.JTextField();
        jdbcConnectionUrlLabel = new javax.swing.JLabel();
        jdbcConnectionUrlTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        addDatabaseConnectionButton = new javax.swing.JButton();
        driverClassLabel = new javax.swing.JLabel();
        driverClassTextField = new javax.swing.JTextField();
        addDatabaseConnectionHelpButton = new javax.swing.JButton();
        helpFrame = new javax.swing.JFrame();
        helpSplitPane = new javax.swing.JSplitPane();
        helpTreeScrollPane = new javax.swing.JScrollPane();
        helpTree = new javax.swing.JTree();
        helpContentScrollPane = new javax.swing.JScrollPane();
        helpContentTextPane = new javax.swing.JTextPane();
        addDatabaseUserAccountDialog = new javax.swing.JDialog();
        addDatabaseInstructionsLabel = new javax.swing.JLabel();
        accountUsernameFieldLabel = new javax.swing.JLabel();
        accountUsernameTextField = new javax.swing.JTextField();
        generateNewPasswordButton = new javax.swing.JButton();
        accountPasswordFieldLabel = new javax.swing.JLabel();
        accountPasswordField = new javax.swing.JPasswordField();
        databaseServerComboBoxLabel = new javax.swing.JLabel();
        databaseServerComboBox = new javax.swing.JComboBox();
        passwordContainerComboBoxLabel = new javax.swing.JLabel();
        passwordContainerComboBox = new javax.swing.JComboBox();
        coldFusionIntegrationCheckBox = new javax.swing.JCheckBox();
        coldFusionConfigurationButton = new javax.swing.JButton();
        integrationsSeparator1 = new javax.swing.JSeparator();
        integrationsSeparator2 = new javax.swing.JSeparator();
        addAccountStatusLabel = new javax.swing.JLabel();
        addAccountCancelButton = new javax.swing.JButton();
        addDatabaseUserAccountButton = new javax.swing.JButton();
        confirmAccountPasswordLabel = new javax.swing.JLabel();
        confirmAccountPasswordField = new javax.swing.JPasswordField();
        addDatabaseForNewAccountButton = new javax.swing.JButton();
        addPasswordFileForNewAccountButton = new javax.swing.JButton();
        testConnectionButton = new javax.swing.JButton();
        passwordsStrengthCriteriaFrame = new javax.swing.JFrame();
        disallowRepeatCharsCheckBox = new javax.swing.JCheckBox();
        disallowWhitespaceCheckBox = new javax.swing.JCheckBox();
        disallowQwertySequenceCheckBox = new javax.swing.JCheckBox();
        disallowAlphaSequenceCheckBox = new javax.swing.JCheckBox();
        passwordStrengthPropertiesLabel = new javax.swing.JLabel();
        numDigitsLabel = new javax.swing.JLabel();
        passwordLengthLabel = new javax.swing.JLabel();
        numSymbolsLabel = new javax.swing.JLabel();
        numUCaseLabel = new javax.swing.JLabel();
        numLCaseLabel = new javax.swing.JLabel();
        restorePasswordPolicyDefaultsButton = new javax.swing.JButton();
        applyPasswordPolicyButton = new javax.swing.JButton();
        disallowDictWordsCheckBox = new javax.swing.JCheckBox();
        aboutCriteriaLabel1 = new javax.swing.JLabel();
        aboutCriteriaLabel2 = new javax.swing.JLabel();
        rangeSlider = new RangeSlider();
        passwordRangeLabel = new javax.swing.JLabel();
        numDigitsSpinner = new javax.swing.JSpinner();
        numSymbolsSpinner = new javax.swing.JSpinner();
        numLowerCaseSpinner = new javax.swing.JSpinner();
        numUpperCaseSpinner = new javax.swing.JSpinner();
        numCharRepeatsSpinner = new javax.swing.JSpinner();
        dictionaryWordLengthsSpinner = new javax.swing.JSpinner();
        coldFusionServerIntegrationDialog = new javax.swing.JDialog();
        coldFusionServerIntegrationInformationLabel = new javax.swing.JLabel();
        coldFusionServerComboBoxLabel = new javax.swing.JLabel();
        coldFusionComboBox = new javax.swing.JComboBox();
        addColdFusionServerButton = new javax.swing.JButton();
        coldFusionDatabaseHostUrlLabel = new javax.swing.JLabel();
        coldFusionDatabaseHostUrlTextField = new javax.swing.JTextField();
        coldFusionDatabaseNameLabel = new javax.swing.JLabel();
        coldFusionDatabaseNameTextField = new javax.swing.JTextField();
        coldFusionDatabasePortLabel = new javax.swing.JLabel();
        coldFusionDatabasePortTextField = new javax.swing.JTextField();
        coldFusionDatabaseUsernameLabel = new javax.swing.JLabel();
        coldFusionDatabaseUsernameTextField = new javax.swing.JTextField();
        coldFusionDsnNameLabel = new javax.swing.JLabel();
        coldFusionDsnNameTextField = new javax.swing.JTextField();
        cancelColdFusionIntegrationButton = new javax.swing.JButton();
        setColdFusionIntegrationButton = new javax.swing.JButton();
        coldFusionIntegrationStatusLabel = new javax.swing.JLabel();
        passwordContainerConfigurationDialog = new javax.swing.JDialog();
        odbFilePathTextField = new javax.swing.JTextField();
        odbFilePathLabel = new javax.swing.JLabel();
        addNewPasswordContainerLabel = new javax.swing.JLabel();
        passwordContainerNameLabel = new javax.swing.JLabel();
        passwordContainerNameTextField = new javax.swing.JTextField();
        addPasswordContainerFinalButton = new javax.swing.JButton();
        addPasswordContainerHelpButton = new javax.swing.JButton();
        addPasswordContainerStatusLabel = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        passwordContainersPopupMenu = new javax.swing.JPopupMenu();
        addPasswordContainerMenuItem = new javax.swing.JMenuItem();
        databaseServersPopupMenu = new javax.swing.JPopupMenu();
        addDatabaseServerMenuItem = new javax.swing.JMenuItem();
        coldFusionServersPopupMenu = new javax.swing.JPopupMenu();
        addColdFusionServerMenuItem = new javax.swing.JMenuItem();
        passwordContainerPopupMenu = new javax.swing.JPopupMenu();
        showPCAssociatedAccountsMenuItem = new javax.swing.JMenuItem();
        removePasswordContainerMenuItem = new javax.swing.JMenuItem();
        databaseServerPopupMenu = new javax.swing.JPopupMenu();
        showDBAssociatedAccountsMenuItem = new javax.swing.JMenuItem();
        removeDatabaseServerMenuItem = new javax.swing.JMenuItem();
        importUserAccountsMenuItem = new javax.swing.JMenuItem();
        coldFusionServerPopupMenu = new javax.swing.JPopupMenu();
        showCFAssociatedAccountsMenuItem = new javax.swing.JMenuItem();
        removeColdFusionServerMenuItem = new javax.swing.JMenuItem();
        aboutFrame = new javax.swing.JFrame();
        projectNameLabel = new javax.swing.JLabel();
        departmentLabel = new javax.swing.JLabel();
        institutionLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        productPageLabel = new javax.swing.JLabel();
        productPageTextField = new javax.swing.JTextField();
        openProductPageButton = new javax.swing.JButton();
        copyProductPageToClipboardButton = new javax.swing.JButton();
        coldFusionServerConfigurationDialog = new javax.swing.JDialog();
        coldFusionServerAdditionInstructionsLabel = new javax.swing.JLabel();
        enterUrlLabel = new javax.swing.JLabel();
        cfPasswordChangeUrlTextField = new javax.swing.JTextField();
        enterUsernameLabel = new javax.swing.JLabel();
        cfAdminUsernameTextField = new javax.swing.JTextField();
        enterPasswordLabel = new javax.swing.JLabel();
        cfAdminPasswordField = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        coldFusionServerNameTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        doColdFusionServerAddButton = new javax.swing.JButton();
        urlParametersWarningLabel = new javax.swing.JLabel();
        coldFusionServerHelpButton = new javax.swing.JButton();
        connectionTestSpinnerFrame = new javax.swing.JFrame();
        jLabel3 = new javax.swing.JLabel();
        importDatabaseUserAccountsDialog = new javax.swing.JDialog();
        importAccountsScrollPane = new javax.swing.JScrollPane();
        importAccountsTable = new javax.swing.JTable();
        importAccoutnsDoneButton = new javax.swing.JButton();
        importAccountsCancelButton = new javax.swing.JButton();
        importAccountsInformationLabel = new javax.swing.JLabel();
        databaseUserImportLoginDialog = new javax.swing.JDialog();
        importAccountsLoginInformationLabel = new javax.swing.JLabel();
        importAccountsUsernameLabel = new javax.swing.JLabel();
        importAccountsUsernameTextField = new javax.swing.JTextField();
        importAccountsPasswordLabel = new javax.swing.JLabel();
        importAccountsLoginPasswordField = new javax.swing.JPasswordField();
        importAccountsLoginCancelButton = new javax.swing.JButton();
        importAccountsLoginSubmitButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        databaseForImportComboBox = new javax.swing.JComboBox();
        topLevelSplitPane = new javax.swing.JSplitPane();
        passwordRepositoriesSplitPane = new javax.swing.JSplitPane();
        configureDatabaseServersPanel = new javax.swing.JPanel();
        removeUserAccountAssociationButton = new javax.swing.JButton();
        accountAssociationsScrollPane = new javax.swing.JScrollPane();
        accountAssociationsTree = new javax.swing.JTree();
        configureDatabaseServersLabel = new javax.swing.JLabel();
        addUserAccountAssociationButton = new javax.swing.JButton();
        accountAssociationsTreeHelpButton = new javax.swing.JButton();
        showAssociatedAccountsButton = new javax.swing.JButton();
        selectedAssociationDetailsPanel = new javax.swing.JPanel();
        selectedAssociationDetailsLabel = new javax.swing.JLabel();
        applyAssociationDetailsChangeButton = new javax.swing.JButton();
        associationDetailsTableHelpButton = new javax.swing.JButton();
        editSelectedAssociationDetailButton = new javax.swing.JButton();
        associationDetailsScrollPane = new javax.swing.JScrollPane();
        associationDetailsTable = new javax.swing.JTable();
        databaseUserAccountsPanel = new javax.swing.JPanel();
        databaseUserAccountsLabel = new javax.swing.JLabel();
        databaseUserAccountsScrollPane = new javax.swing.JScrollPane();
        databasePasswordStoresTable = new javax.swing.JTable();
        addNewDatabaseUserAccountButton = new javax.swing.JButton();
        removeSelectedDatabaseUserAccountsButton = new javax.swing.JButton();
        databaseUserAccountsHelpButton = new javax.swing.JButton();
        runPasswordRotationButton = new javax.swing.JButton();
        selectedPasswordsCountLabel = new javax.swing.JLabel();
        viewSelectedPasswordButton = new javax.swing.JButton();
        editSelectedDatabaseUserAccountButton = new javax.swing.JButton();
        passwordDisplayTextField = new javax.swing.JTextField();
        copyPasswordToClipboardButton = new javax.swing.JButton();
        searchAccountsTextField = new javax.swing.JTextField();
        accountsTableTitleLabel = new javax.swing.JLabel();
        selectAllAccountsInListButton = new javax.swing.JButton();
        showAllAccountsButton = new javax.swing.JButton();
        topMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        importDatabaseUserAccountsMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        passwordStrengthCriteriaMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpContentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        addDatabaseConnectionDialog.setTitle("Add JDBC Database Connection");
        addDatabaseConnectionDialog.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/addDatabseSmall.png"))).getImage());
        addDatabaseConnectionDialog.setLocationByPlatform(true);
        addDatabaseConnectionDialog.setMinimumSize(new java.awt.Dimension(550, 280));
        addDatabaseConnectionDialog.setModal(true);

        addDatabaseLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        addDatabaseLabel.setText("Add a new database connection with the following configuration:");

        serverAddressLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        serverAddressLabel.setText("Server Address:");

        portNumberLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        portNumberLabel.setText("Port Number:");

        descriptiveNameLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        descriptiveNameLabel.setText("Descriptive Name:");

        serverAddressTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        serverAddressTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serverAddressTextFieldMouseClicked(evt);
            }
        });
        serverAddressTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                serverAddressTextFieldKeyReleased(evt);
            }
        });

        portNumberTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        portNumberTextField.setText("1433");
        portNumberTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                portNumberTextFieldMouseClicked(evt);
            }
        });
        portNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                portNumberTextFieldKeyReleased(evt);
            }
        });

        descriptiveNameTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N

        jdbcConnectionUrlLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        jdbcConnectionUrlLabel.setText("JDBC Connection URL:");

        jdbcConnectionUrlTextField.setEditable(false);
        jdbcConnectionUrlTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        jdbcConnectionUrlTextField.setText(JDBC_URL_GENERATION_MESSAGE);
        jdbcConnectionUrlTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jdbcConnectionUrlTextFieldMouseClicked(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        addDatabaseConnectionButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addDatabaseConnectionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addDatabseSmall.png"))); // NOI18N
        addDatabaseConnectionButton.setText("Add New Database Connection");
        addDatabaseConnectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatabaseConnectionButtonActionPerformed(evt);
            }
        });

        driverClassLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        driverClassLabel.setText("JDBC Driver Class:");

        driverClassTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        driverClassTextField.setText(DEFAULT_DATABASE_DRIVER_CLASS );

        addDatabaseConnectionHelpButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addDatabaseConnectionHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        addDatabaseConnectionHelpButton.setToolTipText("Get Help Adding Database Servers");
        addDatabaseConnectionHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatabaseConnectionHelpButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addDatabaseConnectionDialogLayout = new javax.swing.GroupLayout(addDatabaseConnectionDialog.getContentPane());
        addDatabaseConnectionDialog.getContentPane().setLayout(addDatabaseConnectionDialogLayout);
        addDatabaseConnectionDialogLayout.setHorizontalGroup(
            addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                        .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptiveNameLabel)
                            .addComponent(portNumberLabel)
                            .addComponent(serverAddressLabel))
                        .addGap(35, 35, 35)
                        .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portNumberTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(descriptiveNameTextField)
                            .addComponent(serverAddressTextField)))
                    .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                        .addComponent(jdbcConnectionUrlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdbcConnectionUrlTextField))
                    .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                        .addComponent(addDatabaseLabel)
                        .addGap(0, 60, Short.MAX_VALUE))
                    .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addDatabaseConnectionHelpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addDatabaseConnectionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                        .addComponent(driverClassLabel)
                        .addGap(31, 31, 31)
                        .addComponent(driverClassTextField)))
                .addContainerGap())
        );
        addDatabaseConnectionDialogLayout.setVerticalGroup(
            addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addDatabaseLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverAddressLabel)
                    .addComponent(serverAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portNumberLabel)
                    .addComponent(portNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptiveNameLabel)
                    .addComponent(descriptiveNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jdbcConnectionUrlLabel)
                    .addComponent(jdbcConnectionUrlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(driverClassLabel)
                    .addComponent(driverClassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addDatabaseConnectionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(addDatabaseConnectionDialogLayout.createSequentialGroup()
                        .addComponent(addDatabaseConnectionHelpButton)
                        .addGap(0, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );

        helpFrame.setTitle(PROJECT_NAME + " Help");
        helpFrame.setAlwaysOnTop(true);
        helpFrame.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/helpSmall.png"))).getImage());
        helpFrame.setLocationByPlatform(true);
        helpFrame.setMinimumSize(new java.awt.Dimension(666, 388));
        helpFrame.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        helpSplitPane.setDividerLocation(280);

        helpTree.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        helpTree.setModel(getHelpTreeModel());
        helpTree.setCellRenderer(getHelpTreeCellRenderer());
        helpTree.setRootVisible(false);
        helpTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                helpTreeMouseReleased(evt);
            }
        });
        helpTree.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                helpTreeKeyReleased(evt);
            }
        });
        helpTreeScrollPane.setViewportView(helpTree);

        helpSplitPane.setLeftComponent(helpTreeScrollPane);

        helpContentTextPane.setContentType("text/html"); // NOI18N
        helpContentScrollPane.setViewportView(helpContentTextPane);

        helpSplitPane.setRightComponent(helpContentScrollPane);

        javax.swing.GroupLayout helpFrameLayout = new javax.swing.GroupLayout(helpFrame.getContentPane());
        helpFrame.getContentPane().setLayout(helpFrameLayout);
        helpFrameLayout.setHorizontalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
                .addContainerGap())
        );
        helpFrameLayout.setVerticalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );

        addDatabaseUserAccountDialog.setTitle("Add Database User Account");
        addDatabaseUserAccountDialog.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/addDatabaseUserAccountSmall.png"))).getImage());
        addDatabaseUserAccountDialog.setLocationByPlatform(true);
        addDatabaseUserAccountDialog.setMinimumSize(new java.awt.Dimension(500, 425));
        addDatabaseUserAccountDialog.setModal(true);

        addDatabaseInstructionsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addDatabaseInstructionsLabel.setText("Fill in the following fields to add a new database user account:");

        accountUsernameFieldLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountUsernameFieldLabel.setText("Username:");

        accountUsernameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountUsernameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                accountUsernameTextFieldKeyReleased(evt);
            }
        });

        generateNewPasswordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Generate.png"))); // NOI18N
        generateNewPasswordButton.setToolTipText("Generate New Password");
        generateNewPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateNewPasswordButtonActionPerformed(evt);
            }
        });

        accountPasswordFieldLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountPasswordFieldLabel.setText("Password:");

        accountPasswordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                accountPasswordFieldKeyReleased(evt);
            }
        });

        databaseServerComboBoxLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        databaseServerComboBoxLabel.setText("Database Server:");

        databaseServerComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        databaseServerComboBox.setModel(getDatabaseServerComboBoxModel());
        databaseServerComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                databaseServerComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        passwordContainerComboBoxLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passwordContainerComboBoxLabel.setText("Password Container:");

        passwordContainerComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passwordContainerComboBox.setModel(getPasswordContainerComboBoxModel());
        passwordContainerComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                passwordContainerComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        passwordContainerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordContainerComboBoxActionPerformed(evt);
            }
        });

        coldFusionIntegrationCheckBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionIntegrationCheckBox.setText("Used by ColdFusion Application Server");
        coldFusionIntegrationCheckBox.setToolTipText("Select this if a ColfFusion application uses this account.");
        coldFusionIntegrationCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coldFusionIntegrationCheckBoxActionPerformed(evt);
            }
        });

        coldFusionConfigurationButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionConfigurationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ColdFusion.png"))); // NOI18N
        coldFusionConfigurationButton.setText("Configure ColdFusion Server Integration");
        coldFusionConfigurationButton.setEnabled(false);
        coldFusionConfigurationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coldFusionConfigurationButtonActionPerformed(evt);
            }
        });

        addAccountStatusLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        addAccountStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
        addAccountStatusLabel.setText(MESSAGE_FOR_MISSING_USERNAME);

        addAccountCancelButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addAccountCancelButton.setText("Cancel");
        addAccountCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccountCancelButtonActionPerformed(evt);
            }
        });

        addDatabaseUserAccountButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addDatabaseUserAccountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addDatabaseUserAccount.png"))); // NOI18N
        addDatabaseUserAccountButton.setText("Add Database User Account");
        addDatabaseUserAccountButton.setToolTipText("Add Database User Account Information Into " + PROJECT_NAME);
        addDatabaseUserAccountButton.setEnabled(false);
        addDatabaseUserAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatabaseUserAccountButtonActionPerformed(evt);
            }
        });

        confirmAccountPasswordLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        confirmAccountPasswordLabel.setText("Confirm Password:");

        confirmAccountPasswordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        confirmAccountPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                confirmAccountPasswordFieldKeyReleased(evt);
            }
        });

        addDatabaseForNewAccountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addDatabseSmall.png"))); // NOI18N
        addDatabaseForNewAccountButton.setToolTipText("Add New Database Server Connection");
        addDatabaseForNewAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatabaseForNewAccountButtonActionPerformed(evt);
            }
        });

        addPasswordFileForNewAccountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addPasswordFileSmall.png"))); // NOI18N
        addPasswordFileForNewAccountButton.setToolTipText("Add New Password File");
        addPasswordFileForNewAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPasswordFileForNewAccountButtonActionPerformed(evt);
            }
        });

        testConnectionButton.setText("Test Connection");
        testConnectionButton.setEnabled(false);
        testConnectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testConnectionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addDatabaseUserAccountDialogLayout = new javax.swing.GroupLayout(addDatabaseUserAccountDialog.getContentPane());
        addDatabaseUserAccountDialog.getContentPane().setLayout(addDatabaseUserAccountDialogLayout);
        addDatabaseUserAccountDialogLayout.setHorizontalGroup(
            addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(integrationsSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(integrationsSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                        .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(accountUsernameFieldLabel)
                            .addComponent(accountPasswordFieldLabel))
                        .addGap(64, 64, 64)
                        .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                                .addComponent(accountPasswordField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(generateNewPasswordButton))
                            .addComponent(accountUsernameTextField)))
                    .addComponent(addAccountStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(coldFusionConfigurationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                        .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordContainerComboBoxLabel)
                            .addComponent(confirmAccountPasswordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(databaseServerComboBoxLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                                .addComponent(passwordContainerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addPasswordFileForNewAccountButton))
                            .addComponent(confirmAccountPasswordField)
                            .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                                .addComponent(databaseServerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addDatabaseForNewAccountButton))))
                    .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                        .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coldFusionIntegrationCheckBox)
                            .addComponent(addDatabaseInstructionsLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                        .addComponent(addAccountCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(testConnectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addDatabaseUserAccountButton, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)))
                .addContainerGap())
        );
        addDatabaseUserAccountDialogLayout.setVerticalGroup(
            addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDatabaseUserAccountDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addDatabaseInstructionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountUsernameFieldLabel)
                    .addComponent(accountUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountPasswordFieldLabel)
                    .addComponent(accountPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generateNewPasswordButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmAccountPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmAccountPasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(databaseServerComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addDatabaseForNewAccountButton)
                    .addComponent(databaseServerComboBoxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(passwordContainerComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addPasswordFileForNewAccountButton)
                    .addComponent(passwordContainerComboBoxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(integrationsSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(coldFusionIntegrationCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(coldFusionConfigurationButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(integrationsSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addAccountStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addDatabaseUserAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(testConnectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addDatabaseUserAccountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addAccountCancelButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        passwordsStrengthCriteriaFrame.setTitle("Password Strength Criteria");
        passwordsStrengthCriteriaFrame.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/editPasswordStrengthCriteria.png"))).getImage());
        passwordsStrengthCriteriaFrame.setLocationByPlatform(true);
        passwordsStrengthCriteriaFrame.setMinimumSize(new java.awt.Dimension(480, 480));
        passwordsStrengthCriteriaFrame.setName("passwordStrengthCriteriaFrame"); // NOI18N
        passwordsStrengthCriteriaFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                passwordsStrengthCriteriaFrameWindowOpened(evt);
            }
        });

        disallowRepeatCharsCheckBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        disallowRepeatCharsCheckBox.setText("Disallow char repeats more than this many times:");

        disallowWhitespaceCheckBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        disallowWhitespaceCheckBox.setText("Disallow whitespace");

        disallowQwertySequenceCheckBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        disallowQwertySequenceCheckBox.setText("Disallow QWERTY sequences");

        disallowAlphaSequenceCheckBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        disallowAlphaSequenceCheckBox.setText("Disallow alphabetical sequences");

        passwordStrengthPropertiesLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 14)); // NOI18N
        passwordStrengthPropertiesLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        passwordStrengthPropertiesLabel.setText("Password Strength Criteria:");

        numDigitsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numDigitsLabel.setText("Minimum number of digits in password:");

        passwordLengthLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passwordLengthLabel.setText("Password length:");

        numSymbolsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numSymbolsLabel.setText("Minimum number of symbols in password:");

        numUCaseLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numUCaseLabel.setText("Maximum number of uppercase letters in password:");

        numLCaseLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numLCaseLabel.setText("Minimum number of lowercase letters in password:");

        restorePasswordPolicyDefaultsButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        restorePasswordPolicyDefaultsButton.setText("Restore Defaults");
        restorePasswordPolicyDefaultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restorePasswordPolicyDefaultsButtonActionPerformed(evt);
            }
        });

        applyPasswordPolicyButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        applyPasswordPolicyButton.setText("Apply");
        applyPasswordPolicyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyPasswordPolicyButtonActionPerformed(evt);
            }
        });

        disallowDictWordsCheckBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        disallowDictWordsCheckBox.setText("Disallow dictionary words that are longer than this length:");

        aboutCriteriaLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 14)); // NOI18N
        aboutCriteriaLabel1.setText("These password strength criteria are used for generating new");

        aboutCriteriaLabel2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 14)); // NOI18N
        aboutCriteriaLabel2.setText("passwords and acceptance of any user-entered passwords.");

        rangeSlider.setMaximum(24);
        rangeSlider.setMinimum(4);
        rangeSlider.setSnapToTicks(true);
        rangeSlider.setValue(12);
        rangeSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rangeSliderMouseReleased(evt);
            }
        });
        rangeSlider.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rangeSliderKeyReleased(evt);
            }
        });

        passwordRangeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        passwordRangeLabel.setText("25 - 25");

        numDigitsSpinner.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        numSymbolsSpinner.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        numLowerCaseSpinner.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        numUpperCaseSpinner.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        numCharRepeatsSpinner.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        dictionaryWordLengthsSpinner.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dictionaryWordLengthsSpinner.setModel(new javax.swing.SpinnerNumberModel());

        javax.swing.GroupLayout passwordsStrengthCriteriaFrameLayout = new javax.swing.GroupLayout(passwordsStrengthCriteriaFrame.getContentPane());
        passwordsStrengthCriteriaFrame.getContentPane().setLayout(passwordsStrengthCriteriaFrameLayout);
        passwordsStrengthCriteriaFrameLayout.setHorizontalGroup(
            passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(disallowAlphaSequenceCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(passwordLengthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rangeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordRangeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(numDigitsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numDigitsSpinner))
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(numSymbolsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numSymbolsSpinner))
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(numLCaseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numLowerCaseSpinner))
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(numUCaseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numUpperCaseSpinner))
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(disallowRepeatCharsCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numCharRepeatsSpinner))
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(disallowDictWordsCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dictionaryWordLengthsSpinner))
                    .addComponent(disallowWhitespaceCheckBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(disallowQwertySequenceCheckBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordStrengthPropertiesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aboutCriteriaLabel1)
                            .addComponent(aboutCriteriaLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                        .addComponent(restorePasswordPolicyDefaultsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyPasswordPolicyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        passwordsStrengthCriteriaFrameLayout.setVerticalGroup(
            passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordsStrengthCriteriaFrameLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(passwordStrengthPropertiesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aboutCriteriaLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aboutCriteriaLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(passwordLengthLabel)
                        .addComponent(rangeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(passwordRangeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numDigitsLabel)
                    .addComponent(numDigitsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numSymbolsLabel)
                    .addComponent(numSymbolsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numLCaseLabel)
                    .addComponent(numLowerCaseSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numUCaseLabel)
                    .addComponent(numUpperCaseSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(disallowRepeatCharsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numCharRepeatsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(disallowDictWordsCheckBox)
                    .addComponent(dictionaryWordLengthsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disallowWhitespaceCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disallowQwertySequenceCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disallowAlphaSequenceCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(passwordsStrengthCriteriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(restorePasswordPolicyDefaultsButton)
                    .addComponent(applyPasswordPolicyButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        coldFusionServerIntegrationDialog.setTitle("Configure ColdFusion Server Integration");
        coldFusionServerIntegrationDialog.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/dbpmLogo.png"))).getImage());
        coldFusionServerIntegrationDialog.setLocationByPlatform(true);
        coldFusionServerIntegrationDialog.setMinimumSize(new java.awt.Dimension(550, 390));
        coldFusionServerIntegrationDialog.setModal(true);

        coldFusionServerIntegrationInformationLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionServerIntegrationInformationLabel.setText("Fill in the following fields to integrate this account with a ColdFusion server.");

        coldFusionServerComboBoxLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionServerComboBoxLabel.setText("ColdFusion Server:");

        coldFusionComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionComboBox.setModel(getColdFusionServersComboBoxModel());
        coldFusionComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                coldFusionComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        addColdFusionServerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addSmall.png"))); // NOI18N
        addColdFusionServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addColdFusionServerButtonActionPerformed(evt);
            }
        });

        coldFusionDatabaseHostUrlLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabaseHostUrlLabel.setText("Database Host URL:");

        coldFusionDatabaseHostUrlTextField.setEditable(false);
        coldFusionDatabaseHostUrlTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabaseHostUrlTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coldFusionDatabaseHostUrlTextFieldKeyReleased(evt);
            }
        });

        coldFusionDatabaseNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabaseNameLabel.setText("Database Name:");

        coldFusionDatabaseNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabaseNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coldFusionDatabaseNameTextFieldKeyReleased(evt);
            }
        });

        coldFusionDatabasePortLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabasePortLabel.setText("Database Port:");

        coldFusionDatabasePortTextField.setEditable(false);
        coldFusionDatabasePortTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabasePortTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coldFusionDatabasePortTextFieldKeyReleased(evt);
            }
        });

        coldFusionDatabaseUsernameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabaseUsernameLabel.setText("Database Username:");

        coldFusionDatabaseUsernameTextField.setEditable(false);
        coldFusionDatabaseUsernameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDatabaseUsernameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coldFusionDatabaseUsernameTextFieldKeyReleased(evt);
            }
        });

        coldFusionDsnNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDsnNameLabel.setText("DSN Name:");

        coldFusionDsnNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionDsnNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coldFusionDsnNameTextFieldActionPerformed(evt);
            }
        });
        coldFusionDsnNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coldFusionDsnNameTextFieldKeyReleased(evt);
            }
        });

        cancelColdFusionIntegrationButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cancelColdFusionIntegrationButton.setText("Cancel");
        cancelColdFusionIntegrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelColdFusionIntegrationButtonActionPerformed(evt);
            }
        });

        setColdFusionIntegrationButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        setColdFusionIntegrationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ColdFusion.png"))); // NOI18N
        setColdFusionIntegrationButton.setText("Set ColdFusion Server Integration");
        setColdFusionIntegrationButton.setEnabled(false);
        setColdFusionIntegrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setColdFusionIntegrationButtonActionPerformed(evt);
            }
        });

        coldFusionIntegrationStatusLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        coldFusionIntegrationStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
        coldFusionIntegrationStatusLabel.setText("Please select a ColdFusion server to integrate with.");

        javax.swing.GroupLayout coldFusionServerIntegrationDialogLayout = new javax.swing.GroupLayout(coldFusionServerIntegrationDialog.getContentPane());
        coldFusionServerIntegrationDialog.getContentPane().setLayout(coldFusionServerIntegrationDialogLayout);
        coldFusionServerIntegrationDialogLayout.setHorizontalGroup(
            coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coldFusionServerIntegrationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(coldFusionServerIntegrationDialogLayout.createSequentialGroup()
                        .addComponent(cancelColdFusionIntegrationButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setColdFusionIntegrationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(coldFusionServerIntegrationDialogLayout.createSequentialGroup()
                        .addComponent(coldFusionServerComboBoxLabel)
                        .addGap(18, 18, 18)
                        .addComponent(coldFusionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addColdFusionServerButton))
                    .addComponent(coldFusionIntegrationStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addComponent(coldFusionServerIntegrationInformationLabel)
                    .addGroup(coldFusionServerIntegrationDialogLayout.createSequentialGroup()
                        .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coldFusionDatabaseHostUrlLabel)
                            .addComponent(coldFusionDatabaseNameLabel)
                            .addComponent(coldFusionDatabasePortLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coldFusionDatabaseHostUrlTextField)
                            .addComponent(coldFusionDatabaseNameTextField)
                            .addComponent(coldFusionDatabasePortTextField)))
                    .addGroup(coldFusionServerIntegrationDialogLayout.createSequentialGroup()
                        .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coldFusionDatabaseUsernameLabel)
                            .addComponent(coldFusionDsnNameLabel))
                        .addGap(6, 6, 6)
                        .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coldFusionDsnNameTextField)
                            .addComponent(coldFusionDatabaseUsernameTextField))))
                .addContainerGap())
        );
        coldFusionServerIntegrationDialogLayout.setVerticalGroup(
            coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coldFusionServerIntegrationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coldFusionServerIntegrationInformationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addColdFusionServerButton)
                    .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(coldFusionComboBox)
                        .addComponent(coldFusionServerComboBoxLabel)))
                .addGap(15, 15, 15)
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coldFusionDatabaseHostUrlLabel)
                    .addComponent(coldFusionDatabaseHostUrlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coldFusionDatabaseNameLabel)
                    .addComponent(coldFusionDatabaseNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coldFusionDatabasePortLabel)
                    .addComponent(coldFusionDatabasePortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coldFusionDatabaseUsernameLabel)
                    .addComponent(coldFusionDatabaseUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coldFusionDsnNameLabel)
                    .addComponent(coldFusionDsnNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(coldFusionIntegrationStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerIntegrationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setColdFusionIntegrationButton)
                    .addComponent(cancelColdFusionIntegrationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        passwordContainerConfigurationDialog.setTitle("Add New Password Container");
        passwordContainerConfigurationDialog.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/addPasswordFileSmall.png"))).getImage());
        passwordContainerConfigurationDialog.setLocationByPlatform(true);
        passwordContainerConfigurationDialog.setMinimumSize(new java.awt.Dimension(500, 195));
        passwordContainerConfigurationDialog.setModal(true);

        odbFilePathTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        odbFilePathTextField.setEnabled(false);

        odbFilePathLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        odbFilePathLabel.setText("Password Container Path: ");

        addNewPasswordContainerLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        addNewPasswordContainerLabel.setText("Fill out the following fields to add a new password conatiner:");

        passwordContainerNameLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        passwordContainerNameLabel.setText("Password Container Name:");

        passwordContainerNameTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        passwordContainerNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwordContainerNameTextFieldKeyReleased(evt);
            }
        });

        addPasswordContainerFinalButton.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        addPasswordContainerFinalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addPasswordFileSmall.png"))); // NOI18N
        addPasswordContainerFinalButton.setText("Add New Password Conatiner");
        addPasswordContainerFinalButton.setEnabled(false);
        addPasswordContainerFinalButton.setMaximumSize(new java.awt.Dimension(239, 33));
        addPasswordContainerFinalButton.setMinimumSize(new java.awt.Dimension(239, 33));
        addPasswordContainerFinalButton.setPreferredSize(new java.awt.Dimension(239, 33));
        addPasswordContainerFinalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPasswordContainerFinalButtonActionPerformed(evt);
            }
        });

        addPasswordContainerHelpButton.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        addPasswordContainerHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        addPasswordContainerHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPasswordContainerHelpButtonActionPerformed(evt);
            }
        });

        addPasswordContainerStatusLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 14)); // NOI18N
        addPasswordContainerStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
        addPasswordContainerStatusLabel.setText("Enter a name for the new password container.");

        jButton2.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        jButton2.setText("Browse");

        javax.swing.GroupLayout passwordContainerConfigurationDialogLayout = new javax.swing.GroupLayout(passwordContainerConfigurationDialog.getContentPane());
        passwordContainerConfigurationDialog.getContentPane().setLayout(passwordContainerConfigurationDialogLayout);
        passwordContainerConfigurationDialogLayout.setHorizontalGroup(
            passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordContainerConfigurationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(passwordContainerConfigurationDialogLayout.createSequentialGroup()
                        .addGroup(passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(odbFilePathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(passwordContainerNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordContainerNameTextField)
                            .addGroup(passwordContainerConfigurationDialogLayout.createSequentialGroup()
                                .addComponent(odbFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(addPasswordContainerStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(passwordContainerConfigurationDialogLayout.createSequentialGroup()
                        .addComponent(addNewPasswordContainerLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(passwordContainerConfigurationDialogLayout.createSequentialGroup()
                        .addComponent(addPasswordContainerHelpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addPasswordContainerFinalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        passwordContainerConfigurationDialogLayout.setVerticalGroup(
            passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordContainerConfigurationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addNewPasswordContainerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordContainerNameLabel)
                    .addComponent(passwordContainerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(odbFilePathLabel)
                    .addComponent(odbFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addPasswordContainerStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordContainerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addPasswordContainerHelpButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addPasswordContainerFinalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11))
        );

        addPasswordContainerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addPasswordFileSmall.png"))); // NOI18N
        addPasswordContainerMenuItem.setText("Add Password Container");
        addPasswordContainerMenuItem.setToolTipText("Add a new password container to store passwords in. Each password container can have unique access permissions.");
        addPasswordContainerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPasswordContainerMenuItemActionPerformed(evt);
            }
        });
        passwordContainersPopupMenu.add(addPasswordContainerMenuItem);

        addDatabaseServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addDatabseSmall.png"))); // NOI18N
        addDatabaseServerMenuItem.setText("Add Database Server");
        addDatabaseServerMenuItem.setToolTipText("Add a database server to associate accounts with. Each account must be associated with one database server.");
        addDatabaseServerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatabaseServerMenuItemActionPerformed(evt);
            }
        });
        databaseServersPopupMenu.add(addDatabaseServerMenuItem);

        addColdFusionServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addColdFusionServerSmall.png"))); // NOI18N
        addColdFusionServerMenuItem.setText("Add ColdFusion Server");
        addColdFusionServerMenuItem.setToolTipText("Add a ColdFusion server configuration to associate with any accounts that are used by ColdFusion applications.");
        addColdFusionServerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addColdFusionServerMenuItemActionPerformed(evt);
            }
        });
        coldFusionServersPopupMenu.add(addColdFusionServerMenuItem);

        showPCAssociatedAccountsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/associatedAccountsSmall.png"))); // NOI18N
        showPCAssociatedAccountsMenuItem.setText("Show Associated Accounts");
        showPCAssociatedAccountsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPCAssociatedAccountsMenuItemActionPerformed(evt);
            }
        });
        passwordContainerPopupMenu.add(showPCAssociatedAccountsMenuItem);

        removePasswordContainerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/RemovePasswordContainerSmall.png"))); // NOI18N
        removePasswordContainerMenuItem.setText("Remove Password Container");
        removePasswordContainerMenuItem.setToolTipText("Remove the selected password container. All accounts with passwords in this container will be removed as well.");
        removePasswordContainerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePasswordContainerMenuItemActionPerformed(evt);
            }
        });
        passwordContainerPopupMenu.add(removePasswordContainerMenuItem);

        showDBAssociatedAccountsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/associatedAccountsSmall.png"))); // NOI18N
        showDBAssociatedAccountsMenuItem.setText("Show Associated Accounts");
        showDBAssociatedAccountsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDBAssociatedAccountsMenuItemActionPerformed(evt);
            }
        });
        databaseServerPopupMenu.add(showDBAssociatedAccountsMenuItem);

        removeDatabaseServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/removeDatabaseSmall.png"))); // NOI18N
        removeDatabaseServerMenuItem.setText("Remove Database Server");
        removeDatabaseServerMenuItem.setToolTipText("Remove the selected database server. All accounts associated with this database server will also be removed.");
        removeDatabaseServerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDatabaseServerMenuItemActionPerformed(evt);
            }
        });
        databaseServerPopupMenu.add(removeDatabaseServerMenuItem);

        importUserAccountsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/importAccountsFromDatabase.png"))); // NOI18N
        importUserAccountsMenuItem.setText("Import User Accounts From This Database");
        importUserAccountsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importUserAccountsMenuItemActionPerformed(evt);
            }
        });
        databaseServerPopupMenu.add(importUserAccountsMenuItem);

        showCFAssociatedAccountsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/associatedAccountsSmall.png"))); // NOI18N
        showCFAssociatedAccountsMenuItem.setText("Show Associated Accounts");
        showCFAssociatedAccountsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showCFAssociatedAccountsMenuItemActionPerformed(evt);
            }
        });
        coldFusionServerPopupMenu.add(showCFAssociatedAccountsMenuItem);

        removeColdFusionServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/removeColdFusionServerSmall.png"))); // NOI18N
        removeColdFusionServerMenuItem.setText("Remove ColdFusion Server");
        removeColdFusionServerMenuItem.setToolTipText("Remove the selected ColdFusion server. This will also remove all accounts associated with this server.");
        removeColdFusionServerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeColdFusionServerMenuItemActionPerformed(evt);
            }
        });
        coldFusionServerPopupMenu.add(removeColdFusionServerMenuItem);

        aboutFrame.setTitle("About " + PROJECT_NAME);
        aboutFrame.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/about.png"))).getImage());
        aboutFrame.setLocationByPlatform(true);
        aboutFrame.setMinimumSize(new java.awt.Dimension(350, 228));

        projectNameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        projectNameLabel.setText(PROJECT_NAME);

        departmentLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        departmentLabel.setText(" Office of Information Technology");

        institutionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        institutionLabel.setText(" University of California, Irvine.");

        versionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        versionLabel.setText("Version" + VERSION);

        productPageLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        productPageLabel.setText(" Project Page:");

        productPageTextField.setText(PRODUCT_INFORMATION_PAGE_URL);

        openProductPageButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        openProductPageButton.setText("Open Product Page");
        openProductPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openProductPageButtonActionPerformed(evt);
            }
        });

        copyProductPageToClipboardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clipboardSmall.png"))); // NOI18N
        copyProductPageToClipboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyProductPageToClipboardButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aboutFrameLayout = new javax.swing.GroupLayout(aboutFrame.getContentPane());
        aboutFrame.getContentPane().setLayout(aboutFrameLayout);
        aboutFrameLayout.setHorizontalGroup(
            aboutFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(openProductPageButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(aboutFrameLayout.createSequentialGroup()
                        .addComponent(productPageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productPageTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(copyProductPageToClipboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(institutionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(versionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(aboutFrameLayout.createSequentialGroup()
                        .addComponent(departmentLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(projectNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        aboutFrameLayout.setVerticalGroup(
            aboutFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departmentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(institutionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(versionLabel)
                .addGap(18, 18, 18)
                .addComponent(openProductPageButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productPageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productPageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(copyProductPageToClipboardButton))
                .addContainerGap())
        );

        coldFusionServerConfigurationDialog.setTitle("Add ColdFusionServer");
        coldFusionServerConfigurationDialog.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/addColdFusionServerSmall.png"))).getImage());
        coldFusionServerConfigurationDialog.setLocationByPlatform(true);
        coldFusionServerConfigurationDialog.setMinimumSize(new java.awt.Dimension(500, 310));
        coldFusionServerConfigurationDialog.setModal(true);

        coldFusionServerAdditionInstructionsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coldFusionServerAdditionInstructionsLabel.setText("Enter the following fields to add a new ColdFusion server:");

        enterUrlLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        enterUrlLabel.setText("Enter the URL of a CFM file that contains the password modifying CFScript:");

        cfPasswordChangeUrlTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        cfPasswordChangeUrlTextField.setText(DEFAULT_RESET_CFSCRIPT_URL);

        enterUsernameLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        enterUsernameLabel.setText("ColdFusion Administrator Username:");

        cfAdminUsernameTextField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        cfAdminUsernameTextField.setText("admin");

        enterPasswordLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        enterPasswordLabel.setText("ColdFusion Administrator Password (leave blank to be prompted each time):");

        cfAdminPasswordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Server Name");

        coldFusionServerNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Cancel");

        doColdFusionServerAddButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        doColdFusionServerAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ColdFusion.png"))); // NOI18N
        doColdFusionServerAddButton.setText("Add ColdFusion Server");
        doColdFusionServerAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doColdFusionServerAddButtonActionPerformed(evt);
            }
        });

        urlParametersWarningLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        urlParametersWarningLabel.setText("(Do not add URL parameters to this field. They are added automatically.)");

        coldFusionServerHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        coldFusionServerHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coldFusionServerHelpButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout coldFusionServerConfigurationDialogLayout = new javax.swing.GroupLayout(coldFusionServerConfigurationDialog.getContentPane());
        coldFusionServerConfigurationDialog.getContentPane().setLayout(coldFusionServerConfigurationDialogLayout);
        coldFusionServerConfigurationDialogLayout.setHorizontalGroup(
            coldFusionServerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coldFusionServerConfigurationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(coldFusionServerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(coldFusionServerAdditionInstructionsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(coldFusionServerConfigurationDialogLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(coldFusionServerNameTextField))
                    .addGroup(coldFusionServerConfigurationDialogLayout.createSequentialGroup()
                        .addComponent(enterUsernameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cfAdminUsernameTextField))
                    .addComponent(enterPasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                    .addComponent(cfAdminPasswordField)
                    .addComponent(enterUrlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cfPasswordChangeUrlTextField)
                    .addComponent(urlParametersWarningLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(coldFusionServerConfigurationDialogLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(coldFusionServerHelpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(doColdFusionServerAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        coldFusionServerConfigurationDialogLayout.setVerticalGroup(
            coldFusionServerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coldFusionServerConfigurationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coldFusionServerAdditionInstructionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(coldFusionServerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enterUsernameLabel)
                    .addComponent(cfAdminUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enterPasswordLabel)
                .addGap(5, 5, 5)
                .addComponent(cfAdminPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enterUrlLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(urlParametersWarningLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cfPasswordChangeUrlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coldFusionServerConfigurationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(doColdFusionServerAddButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(coldFusionServerHelpButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        connectionTestSpinnerFrame.setMinimumSize(new java.awt.Dimension(300, 75));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect.gif"))); // NOI18N
        jLabel3.setText("Please wait while we test this connection");

        javax.swing.GroupLayout connectionTestSpinnerFrameLayout = new javax.swing.GroupLayout(connectionTestSpinnerFrame.getContentPane());
        connectionTestSpinnerFrame.getContentPane().setLayout(connectionTestSpinnerFrameLayout);
        connectionTestSpinnerFrameLayout.setHorizontalGroup(
            connectionTestSpinnerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionTestSpinnerFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap())
        );
        connectionTestSpinnerFrameLayout.setVerticalGroup(
            connectionTestSpinnerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionTestSpinnerFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap())
        );

        importDatabaseUserAccountsDialog.setTitle("Import Database User Accounts Editable Table");
        importDatabaseUserAccountsDialog.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/importAccountsFromDatabase.png"))).getImage());
        importDatabaseUserAccountsDialog.setLocationByPlatform(true);
        importDatabaseUserAccountsDialog.setMinimumSize(new java.awt.Dimension(720, 475));
        importDatabaseUserAccountsDialog.setModal(true);

        importAccountsTable.setModel(getImportAccountsTableModel(new ArrayList<String>(), new JdbcConnection()));
        importAccountsTable.setCellSelectionEnabled(true);
        TableColumn comboboxColumn = importAccountsTable.getColumnModel().getColumn(3);
        JComboBox passwordContainersComboBox = new JComboBox();
        passwordContainersComboBox.setModel(getPasswordContainerComboBoxModel());
        comboboxColumn.setCellEditor(new DefaultCellEditor(passwordContainersComboBox));
        TableColumn checkboxColumn = importAccountsTable.getColumnModel().getColumn(0);
        JCheckBox importAccountCheckBox = new JCheckBox();
        checkboxColumn.setCellEditor(new DefaultCellEditor(importAccountCheckBox));
        importAccountsScrollPane.setViewportView(importAccountsTable);

        importAccoutnsDoneButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccoutnsDoneButton.setText("Done");
        importAccoutnsDoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importAccoutnsDoneButtonActionPerformed(evt);
            }
        });

        importAccountsCancelButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccountsCancelButton.setText("Cancel");
        importAccountsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importAccountsCancelButtonActionPerformed(evt);
            }
        });

        importAccountsInformationLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccountsInformationLabel.setText("Select and provide information for all the database user accounts that you would like to import.");

        javax.swing.GroupLayout importDatabaseUserAccountsDialogLayout = new javax.swing.GroupLayout(importDatabaseUserAccountsDialog.getContentPane());
        importDatabaseUserAccountsDialog.getContentPane().setLayout(importDatabaseUserAccountsDialogLayout);
        importDatabaseUserAccountsDialogLayout.setHorizontalGroup(
            importDatabaseUserAccountsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(importDatabaseUserAccountsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(importDatabaseUserAccountsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(importDatabaseUserAccountsDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(importAccountsCancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importAccoutnsDoneButton))
                    .addComponent(importAccountsScrollPane)
                    .addComponent(importAccountsInformationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        importDatabaseUserAccountsDialogLayout.setVerticalGroup(
            importDatabaseUserAccountsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(importDatabaseUserAccountsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(importAccountsInformationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(importAccountsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(importDatabaseUserAccountsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importAccountsCancelButton)
                    .addComponent(importAccoutnsDoneButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        databaseUserImportLoginDialog.setTitle("Import Users Database Login");
        databaseUserImportLoginDialog.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/importAccountsFromDatabase.png"))).getImage());
        databaseUserImportLoginDialog.setLocationByPlatform(true);
        databaseUserImportLoginDialog.setMinimumSize(new java.awt.Dimension(320, 270));

        importAccountsLoginInformationLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccountsLoginInformationLabel.setText("<html>Enter the credentials of a user on this database.<br>This user must have SELECT privilages on the<br>system table containing user information.</html>");

        importAccountsUsernameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccountsUsernameLabel.setText("Username:");

        importAccountsUsernameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        importAccountsPasswordLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccountsPasswordLabel.setText("Password:");

        importAccountsLoginPasswordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        importAccountsLoginCancelButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccountsLoginCancelButton.setText("Cancel");
        importAccountsLoginCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importAccountsLoginCancelButtonActionPerformed(evt);
            }
        });

        importAccountsLoginSubmitButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        importAccountsLoginSubmitButton.setText("Submit");
        importAccountsLoginSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importAccountsLoginSubmitButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Select a database to import users from:");

        databaseForImportComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        databaseForImportComboBox.setModel(getDatabaseServerComboBoxModel());

        javax.swing.GroupLayout databaseUserImportLoginDialogLayout = new javax.swing.GroupLayout(databaseUserImportLoginDialog.getContentPane());
        databaseUserImportLoginDialog.getContentPane().setLayout(databaseUserImportLoginDialogLayout);
        databaseUserImportLoginDialogLayout.setHorizontalGroup(
            databaseUserImportLoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseUserImportLoginDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(databaseUserImportLoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(importAccountsLoginInformationLabel)
                    .addGroup(databaseUserImportLoginDialogLayout.createSequentialGroup()
                        .addComponent(importAccountsLoginCancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importAccountsLoginSubmitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(databaseUserImportLoginDialogLayout.createSequentialGroup()
                        .addComponent(importAccountsPasswordLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(importAccountsLoginPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(databaseUserImportLoginDialogLayout.createSequentialGroup()
                        .addComponent(importAccountsUsernameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(importAccountsUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(databaseUserImportLoginDialogLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(databaseForImportComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        databaseUserImportLoginDialogLayout.setVerticalGroup(
            databaseUserImportLoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseUserImportLoginDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseForImportComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(importAccountsLoginInformationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseUserImportLoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importAccountsUsernameLabel)
                    .addComponent(importAccountsUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseUserImportLoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importAccountsPasswordLabel)
                    .addComponent(importAccountsLoginPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(databaseUserImportLoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importAccountsLoginCancelButton)
                    .addComponent(importAccountsLoginSubmitButton))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(PROJECT_NAME);
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/images/dbpmLogo.png"))).getImage());
        setLocationByPlatform(true);

        topLevelSplitPane.setDividerLocation(300);

        passwordRepositoriesSplitPane.setDividerLocation(300);
        passwordRepositoriesSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        removeUserAccountAssociationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/removeDatabase.png"))); // NOI18N
        removeUserAccountAssociationButton.setToolTipText("Remove Selected Database Server Connection");
        removeUserAccountAssociationButton.setEnabled(false);

        accountAssociationsTree.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountAssociationsTree.setModel(getDatabaseServersTreeModel());
        accountAssociationsTree.setCellRenderer(getDatabaseServersTreeCellRenderer());
        accountAssociationsTree.setRootVisible(false);
        accountAssociationsTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                accountAssociationsTreeMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                accountAssociationsTreeMouseReleased(evt);
            }
        });
        accountAssociationsTree.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                accountAssociationsTreeKeyReleased(evt);
            }
        });
        accountAssociationsScrollPane.setViewportView(accountAssociationsTree);

        configureDatabaseServersLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        configureDatabaseServersLabel.setText("Configure Account Associations");

        addUserAccountAssociationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addDatabase.png"))); // NOI18N
        addUserAccountAssociationButton.setToolTipText("Add New Database Server Connection");
        addUserAccountAssociationButton.setEnabled(false);

        accountAssociationsTreeHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        accountAssociationsTreeHelpButton.setToolTipText("Help With Database Server Connections");
        accountAssociationsTreeHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountAssociationsTreeHelpButtonActionPerformed(evt);
            }
        });

        showAssociatedAccountsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/associatedAccounts.png"))); // NOI18N
        showAssociatedAccountsButton.setToolTipText("Show Associated Accounts");
        showAssociatedAccountsButton.setDefaultCapable(false);
        showAssociatedAccountsButton.setEnabled(false);

        javax.swing.GroupLayout configureDatabaseServersPanelLayout = new javax.swing.GroupLayout(configureDatabaseServersPanel);
        configureDatabaseServersPanel.setLayout(configureDatabaseServersPanelLayout);
        configureDatabaseServersPanelLayout.setHorizontalGroup(
            configureDatabaseServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configureDatabaseServersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configureDatabaseServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(accountAssociationsScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(configureDatabaseServersPanelLayout.createSequentialGroup()
                        .addComponent(configureDatabaseServersLabel)
                        .addGap(0, 84, Short.MAX_VALUE))
                    .addGroup(configureDatabaseServersPanelLayout.createSequentialGroup()
                        .addComponent(addUserAccountAssociationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeUserAccountAssociationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showAssociatedAccountsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(accountAssociationsTreeHelpButton)))
                .addContainerGap())
        );
        configureDatabaseServersPanelLayout.setVerticalGroup(
            configureDatabaseServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, configureDatabaseServersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(configureDatabaseServersLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountAssociationsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configureDatabaseServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addUserAccountAssociationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeUserAccountAssociationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accountAssociationsTreeHelpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showAssociatedAccountsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        passwordRepositoriesSplitPane.setLeftComponent(configureDatabaseServersPanel);

        selectedAssociationDetailsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        selectedAssociationDetailsLabel.setText("Selected Association Details");

        applyAssociationDetailsChangeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check.png"))); // NOI18N
        applyAssociationDetailsChangeButton.setToolTipText("Add New Password File");
        applyAssociationDetailsChangeButton.setEnabled(false);
        applyAssociationDetailsChangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyAssociationDetailsChangeButtonActionPerformed(evt);
            }
        });

        associationDetailsTableHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        associationDetailsTableHelpButton.setToolTipText("Help With Password Files");
        associationDetailsTableHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                associationDetailsTableHelpButtonActionPerformed(evt);
            }
        });

        editSelectedAssociationDetailButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        editSelectedAssociationDetailButton.setToolTipText("Edit Selected Password File");
        editSelectedAssociationDetailButton.setEnabled(false);
        editSelectedAssociationDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSelectedAssociationDetailButtonActionPerformed(evt);
            }
        });

        associationDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Property", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        associationDetailsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        associationDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                associationDetailsTableMouseClicked(evt);
            }
        });
        associationDetailsScrollPane.setViewportView(associationDetailsTable);

        javax.swing.GroupLayout selectedAssociationDetailsPanelLayout = new javax.swing.GroupLayout(selectedAssociationDetailsPanel);
        selectedAssociationDetailsPanel.setLayout(selectedAssociationDetailsPanelLayout);
        selectedAssociationDetailsPanelLayout.setHorizontalGroup(
            selectedAssociationDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectedAssociationDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectedAssociationDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(selectedAssociationDetailsPanelLayout.createSequentialGroup()
                        .addComponent(editSelectedAssociationDetailButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyAssociationDetailsChangeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                        .addComponent(associationDetailsTableHelpButton))
                    .addGroup(selectedAssociationDetailsPanelLayout.createSequentialGroup()
                        .addComponent(selectedAssociationDetailsLabel)
                        .addGap(0, 110, Short.MAX_VALUE))
                    .addComponent(associationDetailsScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        selectedAssociationDetailsPanelLayout.setVerticalGroup(
            selectedAssociationDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectedAssociationDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectedAssociationDetailsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(associationDetailsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selectedAssociationDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(associationDetailsTableHelpButton)
                    .addComponent(editSelectedAssociationDetailButton)
                    .addComponent(applyAssociationDetailsChangeButton))
                .addContainerGap())
        );

        passwordRepositoriesSplitPane.setRightComponent(selectedAssociationDetailsPanel);

        topLevelSplitPane.setLeftComponent(passwordRepositoriesSplitPane);

        databaseUserAccountsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        databaseUserAccountsLabel.setText("Configure Database User Accounts");

        databasePasswordStoresTable.setAutoCreateRowSorter(true);
        databasePasswordStoresTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        databasePasswordStoresTable.setModel(getFullAccountListTableModel());
        databasePasswordStoresTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                databasePasswordStoresTableMouseReleased(evt);
            }
        });
        databasePasswordStoresTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                databasePasswordStoresTableKeyReleased(evt);
            }
        });
        databaseUserAccountsScrollPane.setViewportView(databasePasswordStoresTable);

        addNewDatabaseUserAccountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addDatabaseUserAccount.png"))); // NOI18N
        addNewDatabaseUserAccountButton.setToolTipText("Add New Database User Account Information");
        addNewDatabaseUserAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewDatabaseUserAccountButtonActionPerformed(evt);
            }
        });

        removeSelectedDatabaseUserAccountsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/removeDatabaseUserAccount.png"))); // NOI18N
        removeSelectedDatabaseUserAccountsButton.setToolTipText("Remove Selected Database User Account Information");
        removeSelectedDatabaseUserAccountsButton.setEnabled(false);
        removeSelectedDatabaseUserAccountsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedDatabaseUserAccountsButtonActionPerformed(evt);
            }
        });

        databaseUserAccountsHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        databaseUserAccountsHelpButton.setToolTipText("Help With Database User Account Information");
        databaseUserAccountsHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                databaseUserAccountsHelpButtonActionPerformed(evt);
            }
        });

        runPasswordRotationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/rotatePassword.png"))); // NOI18N
        runPasswordRotationButton.setToolTipText("Rotate Selected Password(s)");
        runPasswordRotationButton.setEnabled(false);
        runPasswordRotationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runPasswordRotationButtonActionPerformed(evt);
            }
        });

        selectedPasswordsCountLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        selectedPasswordsCountLabel.setForeground(new java.awt.Color(102, 102, 255));
        selectedPasswordsCountLabel.setText("0 Items Selected");

        viewSelectedPasswordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/previewer.png"))); // NOI18N
        viewSelectedPasswordButton.setToolTipText("View Selected Password");
        viewSelectedPasswordButton.setEnabled(false);
        viewSelectedPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSelectedPasswordButtonActionPerformed(evt);
            }
        });

        editSelectedDatabaseUserAccountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editDatabaseUserAccount.png"))); // NOI18N
        editSelectedDatabaseUserAccountButton.setToolTipText("Edit Selected Password");
        editSelectedDatabaseUserAccountButton.setEnabled(false);
        editSelectedDatabaseUserAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSelectedDatabaseUserAccountButtonActionPerformed(evt);
            }
        });

        passwordDisplayTextField.setEditable(false);
        passwordDisplayTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passwordDisplayTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordDisplayTextField.setText("** Password Display Field **");
        passwordDisplayTextField.setEnabled(false);

        copyPasswordToClipboardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clipboard.png"))); // NOI18N
        copyPasswordToClipboardButton.setToolTipText("Copy Selected Password To Clipboard");
        copyPasswordToClipboardButton.setEnabled(false);
        copyPasswordToClipboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyPasswordToClipboardButtonActionPerformed(evt);
            }
        });

        searchAccountsTextField.setEditable(false);
        searchAccountsTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        searchAccountsTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchAccountsTextField.setText(SEARCH_ACCOUNTS_DEFAULT_MESSAGE);
        searchAccountsTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchAccountsTextFieldMouseClicked(evt);
            }
        });
        searchAccountsTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchAccountsTextFieldKeyReleased(evt);
            }
        });

        accountsTableTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountsTableTitleLabel.setText(ALL_ACCOUNTS_SHOWING_LABEL + getRowCountString());

        selectAllAccountsInListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/selectAllSmall.png"))); // NOI18N
        selectAllAccountsInListButton.setToolTipText("Select All Accounts In This List");
        selectAllAccountsInListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllAccountsInListButtonActionPerformed(evt);
            }
        });

        showAllAccountsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/removeFilter.png"))); // NOI18N
        showAllAccountsButton.setToolTipText("Show All Database User Accounts (Remove Filters)");
        showAllAccountsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllAccountsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout databaseUserAccountsPanelLayout = new javax.swing.GroupLayout(databaseUserAccountsPanel);
        databaseUserAccountsPanel.setLayout(databaseUserAccountsPanelLayout);
        databaseUserAccountsPanelLayout.setHorizontalGroup(
            databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseUserAccountsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(databaseUserAccountsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(databaseUserAccountsPanelLayout.createSequentialGroup()
                        .addComponent(addNewDatabaseUserAccountButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeSelectedDatabaseUserAccountsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editSelectedDatabaseUserAccountButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runPasswordRotationButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewSelectedPasswordButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordDisplayTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(copyPasswordToClipboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(databaseUserAccountsHelpButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, databaseUserAccountsPanelLayout.createSequentialGroup()
                        .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(accountsTableTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(databaseUserAccountsPanelLayout.createSequentialGroup()
                                .addComponent(databaseUserAccountsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchAccountsTextField)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, databaseUserAccountsPanelLayout.createSequentialGroup()
                                .addComponent(selectAllAccountsInListButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(showAllAccountsButton))
                            .addComponent(selectedPasswordsCountLabel))))
                .addContainerGap())
        );
        databaseUserAccountsPanelLayout.setVerticalGroup(
            databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseUserAccountsPanelLayout.createSequentialGroup()
                .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(databaseUserAccountsPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(databaseUserAccountsLabel)
                            .addComponent(searchAccountsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(databaseUserAccountsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(selectedPasswordsCountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, databaseUserAccountsPanelLayout.createSequentialGroup()
                        .addComponent(accountsTableTitleLabel)
                        .addGap(6, 6, 6))
                    .addGroup(databaseUserAccountsPanelLayout.createSequentialGroup()
                        .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(selectAllAccountsInListButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(showAllAccountsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(databaseUserAccountsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseUserAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addNewDatabaseUserAccountButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeSelectedDatabaseUserAccountsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editSelectedDatabaseUserAccountButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewSelectedPasswordButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(databaseUserAccountsHelpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(runPasswordRotationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordDisplayTextField)
                    .addComponent(copyPasswordToClipboardButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        topLevelSplitPane.setBottomComponent(databaseUserAccountsPanel);

        topMenuBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        fileMenu.setText("File");
        fileMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        importDatabaseUserAccountsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        importDatabaseUserAccountsMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        importDatabaseUserAccountsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/importAccountsFromDatabase.png"))); // NOI18N
        importDatabaseUserAccountsMenuItem.setText("Import Database User Accounts");
        importDatabaseUserAccountsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importDatabaseUserAccountsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importDatabaseUserAccountsMenuItem);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem1.setText("Import CryptoJCE Password Containers");
        fileMenu.add(jMenuItem1);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        topMenuBar.add(fileMenu);

        toolsMenu.setText("Options");
        toolsMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        passwordStrengthCriteriaMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        passwordStrengthCriteriaMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordStrengthCriteriaMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editPasswordStrengthCriteria.png"))); // NOI18N
        passwordStrengthCriteriaMenuItem.setText("Password Strength Criteria");
        passwordStrengthCriteriaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordStrengthCriteriaMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(passwordStrengthCriteriaMenuItem);

        topMenuBar.add(toolsMenu);

        helpMenu.setText("Help");
        helpMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        helpContentsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpContentsMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        helpContentsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/helpSmall.png"))); // NOI18N
        helpContentsMenuItem.setText("Help Contents");
        helpContentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpContentsMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpContentsMenuItem);

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        aboutMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about.png"))); // NOI18N
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        topMenuBar.add(helpMenu);

        setJMenuBar(topMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topLevelSplitPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topLevelSplitPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

// </editor-fold>
// <editor-fold desc="Swing Event Action Methods" defaultstate="collapsed">
    private void addNewDatabaseUserAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewDatabaseUserAccountButtonActionPerformed
	addDatabaseUserAccountDialog.setVisible(true);
    }//GEN-LAST:event_addNewDatabaseUserAccountButtonActionPerformed

    private void viewSelectedPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSelectedPasswordButtonActionPerformed
	viewSelectedAccountPassword();
    }//GEN-LAST:event_viewSelectedPasswordButtonActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
	aboutFrame.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void serverAddressTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serverAddressTextFieldMouseClicked
	undoJdbcManualEntry();
    }//GEN-LAST:event_serverAddressTextFieldMouseClicked

    private void serverAddressTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_serverAddressTextFieldKeyReleased
	if (!jdbcConnectionUrlTextField.isEditable()) {
	    updateJDBCUrlIfAllFieldsNonEmpty();
	}
    }//GEN-LAST:event_serverAddressTextFieldKeyReleased

    private void portNumberTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_portNumberTextFieldMouseClicked
	undoJdbcManualEntry();
    }//GEN-LAST:event_portNumberTextFieldMouseClicked

    private void portNumberTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_portNumberTextFieldKeyReleased
	if (!jdbcConnectionUrlTextField.isEditable()) {
	    updateJDBCUrlIfAllFieldsNonEmpty();
	}
    }//GEN-LAST:event_portNumberTextFieldKeyReleased

    private void jdbcConnectionUrlTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jdbcConnectionUrlTextFieldMouseClicked
	final String manualEntryMode = MESSAGE_FOR_IN_JDBC_URL_MANUAL_ENTRY_MODE;
	jdbcConnectionUrlTextField.setEditable(true);
	serverAddressTextField.setEditable(false);
	serverAddressTextField.setText(manualEntryMode);
	portNumberTextField.setEditable(false);
	portNumberTextField.setText(manualEntryMode);
	if (jdbcConnectionUrlTextField.getText().equals(
		JDBC_URL_GENERATION_MESSAGE)) {
	    jdbcConnectionUrlTextField.setText(JDBC_URL_PREFIX);
	}
    }//GEN-LAST:event_jdbcConnectionUrlTextFieldMouseClicked

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
	addDatabaseConnectionDialog.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addDatabaseConnectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatabaseConnectionButtonActionPerformed
	// TODO add your handling code here:
	if (jdbcConnectionUrlTextField.getText().isEmpty()
		|| descriptiveNameTextField.getText().isEmpty()
		|| jdbcConnectionUrlTextField.getText().equals(
		JDBC_URL_GENERATION_MESSAGE)) {
	    JOptionPane.showMessageDialog(
		    null,
		    MESSAGE_FOR_JDBC_URL_AND_DESCRIPTION_EMPTY,
		    WINDOW_TITLE_FOR_ADD_DATABASE_FIELDS_MISSING,
		    JOptionPane.ERROR_MESSAGE, null);
	} else if (jdbcConnectionUrlTextField.isEditable()) {
	    jdbcConnectionDao.addNewJdbcConnection(
		    jdbcConnectionUrlTextField.getText(),
		    descriptiveNameTextField.getText(),
		    driverClassTextField.getText());
	    runDatabaseAdditionFinalTasks();
	} else {
	    jdbcConnectionDao.addNewJdbcConnection(getJdbcUrlFromFields(),
		    descriptiveNameTextField.getText(), driverClassTextField.getText());
	    runDatabaseAdditionFinalTasks();
	}
    }//GEN-LAST:event_addDatabaseConnectionButtonActionPerformed

    private void passwordContainerNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordContainerNameTextFieldKeyReleased
	odbFilePathTextField.setText(System.getProperty(MAIN_DATABASE_SERVER_ROOT_URL_SYSTEM_PROPERTY_NAME)
		+ passwordContainerNameTextField.getText() + ".odb");
	verifyAddPasswordContainerFields();
    }//GEN-LAST:event_passwordContainerNameTextFieldKeyReleased

    private void addPasswordContainerFinalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPasswordContainerFinalButtonActionPerformed
	passwordFileDao.addPasswordContainer(passwordContainerNameTextField.getText(), odbFilePathTextField.getText());
	passwordContainerConfigurationDialog.setVisible(false);
	JOptionPane.showMessageDialog(null, String.format(
		"You have added the following new password file: \nName: %s\nFile Path: %s",
		passwordContainerNameTextField.getText(), odbFilePathTextField.getText()),
		"New Password File Added", JOptionPane.INFORMATION_MESSAGE);
	passwordContainerNameTextField.setText("");
	odbFilePathTextField.setText("");
	verifyAddPasswordContainerFields();
    }//GEN-LAST:event_addPasswordContainerFinalButtonActionPerformed

    private void accountAssociationsTreeHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountAssociationsTreeHelpButtonActionPerformed
	helpFrame.setVisible(true);
	helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.GETTING_STARTED_NODE.getNode().getPath()));
	setHelpContentAfterSelection();
    }//GEN-LAST:event_accountAssociationsTreeHelpButtonActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
	System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void searchAccountsTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchAccountsTextFieldMouseClicked
	if (!searchAccountsTextField.isEditable()) {
	    searchAccountsTextField.setText("");
	    searchAccountsTextField.setEditable(true);
	}
    }//GEN-LAST:event_searchAccountsTextFieldMouseClicked

    private void searchAccountsTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchAccountsTextFieldKeyReleased
	if (searchAccountsTextField.getText().isEmpty()) {
	    resetDatabaseUserAccountsTable();
	} else {
	    List<AccountInfo> resultList = accountInfoDao.searchAllAccountInfoFieldsForTerm(searchAccountsTextField.getText());
	    databasePasswordStoresTable.setModel(getCustomAccountListTableModel(resultList));
	    analyzeAccountTableSelectedRows();
	    accountsTableTitleLabel.setText("Search Results For All Fields On Term " + searchAccountsTextField.getText());
	}
    }//GEN-LAST:event_searchAccountsTextFieldKeyReleased

    private void coldFusionConfigurationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coldFusionConfigurationButtonActionPerformed
	if (databaseServerComboBox.getSelectedItem() instanceof JdbcConnection) {
	    JdbcConnection selectedDatabaseServer = (JdbcConnection) databaseServerComboBox.getSelectedItem();
	    String jdbcUrl = selectedDatabaseServer.getJdbcUrl();
	    URI uri = URI.create(jdbcUrl.substring(5));
	    coldFusionDatabaseHostUrlTextField.setText(uri.getHost());
	    coldFusionDatabasePortTextField.setText(uri.getPort() + "");
	    coldFusionDatabaseUsernameTextField.setText(accountUsernameTextField.getText());
	}
	coldFusionServerIntegrationDialog.setVisible(true);
	verifyAddAccountFrameFields();
    }//GEN-LAST:event_coldFusionConfigurationButtonActionPerformed

    private void coldFusionIntegrationCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coldFusionIntegrationCheckBoxActionPerformed
	boolean coldFusionIntegrationStatus = coldFusionIntegrationCheckBox.isSelected();
	coldFusionConfigurationButton.setEnabled(coldFusionIntegrationStatus);
	verifyAddAccountFrameFields();
    }//GEN-LAST:event_coldFusionIntegrationCheckBoxActionPerformed

    private void generateNewPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateNewPasswordButtonActionPerformed
	char[] generatedPassword = PasswordUtils.generateNewPassword(passwordPolicyDao.getCurrentPasswordPolicy());
	if (generatedPassword.length != 0) {
	    String generatedPasswordString = new String(generatedPassword);
	    accountPasswordField.setText(generatedPasswordString);
	    confirmAccountPasswordField.setText(generatedPasswordString);
	    verifyAddAccountFrameFields();
	}
    }//GEN-LAST:event_generateNewPasswordButtonActionPerformed

    private void addDatabaseForNewAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatabaseForNewAccountButtonActionPerformed
	addDatabaseConnectionDialog.setVisible(true);
	databaseServerComboBox.setModel(getDatabaseServerComboBoxModel());
	verifyAddAccountFrameFields();
    }//GEN-LAST:event_addDatabaseForNewAccountButtonActionPerformed

    private void passwordStrengthCriteriaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordStrengthCriteriaMenuItemActionPerformed
	passwordsStrengthCriteriaFrame.setVisible(true);
    }//GEN-LAST:event_passwordStrengthCriteriaMenuItemActionPerformed

    private void restorePasswordPolicyDefaultsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restorePasswordPolicyDefaultsButtonActionPerformed
	passwordPolicyDao.setDefaultPasswordPolicy();
	passwordsStrengthCriteriaFrame.setVisible(false);
    }//GEN-LAST:event_restorePasswordPolicyDefaultsButtonActionPerformed

    private void applyPasswordPolicyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyPasswordPolicyButtonActionPerformed
	applyPasswordPolicySpecified();
    }//GEN-LAST:event_applyPasswordPolicyButtonActionPerformed

    private void passwordsStrengthCriteriaFrameWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_passwordsStrengthCriteriaFrameWindowOpened
	populatePasswordStrengthCriteriaFrameWithCurrentCriteria();
    }//GEN-LAST:event_passwordsStrengthCriteriaFrameWindowOpened

    private void addPasswordFileForNewAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPasswordFileForNewAccountButtonActionPerformed
	passwordContainerConfigurationDialog.setVisible(true);
    }//GEN-LAST:event_addPasswordFileForNewAccountButtonActionPerformed

    private void accountUsernameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_accountUsernameTextFieldKeyReleased
	verifyAddAccountFrameFields();
    }//GEN-LAST:event_accountUsernameTextFieldKeyReleased

    private void accountPasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_accountPasswordFieldKeyReleased
	verifyAddAccountFrameFields();
    }//GEN-LAST:event_accountPasswordFieldKeyReleased

    private void confirmAccountPasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmAccountPasswordFieldKeyReleased
	verifyAddAccountFrameFields();
    }//GEN-LAST:event_confirmAccountPasswordFieldKeyReleased

    private void databaseServerComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_databaseServerComboBoxPopupMenuWillBecomeInvisible
	verifyAddAccountFrameFields();
	if (databaseServerComboBox.getSelectedItem() instanceof JdbcConnection) {
	    testConnectionButton.setEnabled(true);
	}
    }//GEN-LAST:event_databaseServerComboBoxPopupMenuWillBecomeInvisible

    private void passwordContainerComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_passwordContainerComboBoxPopupMenuWillBecomeInvisible
	verifyAddAccountFrameFields();
    }//GEN-LAST:event_passwordContainerComboBoxPopupMenuWillBecomeInvisible

    private void addAccountCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccountCancelButtonActionPerformed
	addDatabaseUserAccountDialog.setVisible(false);
    }//GEN-LAST:event_addAccountCancelButtonActionPerformed

    private void addColdFusionServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addColdFusionServerButtonActionPerformed
	addColdFusionServerActionInitiated();
    }//GEN-LAST:event_addColdFusionServerButtonActionPerformed

    private void cancelColdFusionIntegrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelColdFusionIntegrationButtonActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cancelColdFusionIntegrationButtonActionPerformed

    private void coldFusionDsnNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coldFusionDsnNameTextFieldActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_coldFusionDsnNameTextFieldActionPerformed

    private void addDatabaseUserAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatabaseUserAccountButtonActionPerformed
	runAddDatabaseUserActions();
    }//GEN-LAST:event_addDatabaseUserAccountButtonActionPerformed

    private void setColdFusionIntegrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setColdFusionIntegrationButtonActionPerformed

	coldFusionServerIntegrationDialog.setVisible(false);
    }//GEN-LAST:event_setColdFusionIntegrationButtonActionPerformed

    private void removeSelectedDatabaseUserAccountsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedDatabaseUserAccountsButtonActionPerformed
	removeDatabaseUserAccountActionInitiated();
    }//GEN-LAST:event_removeSelectedDatabaseUserAccountsButtonActionPerformed

    private void accountAssociationsTreeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_accountAssociationsTreeKeyReleased
	adjustAccountAssoiationsTreeAfterEvent();
    }//GEN-LAST:event_accountAssociationsTreeKeyReleased

    private void databasePasswordStoresTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_databasePasswordStoresTableKeyReleased
	analyzeAccountTableSelectedRows();
    }//GEN-LAST:event_databasePasswordStoresTableKeyReleased

    private void copyPasswordToClipboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyPasswordToClipboardButtonActionPerformed
	copySelectedAccountPasswordToClipboard();
    }//GEN-LAST:event_copyPasswordToClipboardButtonActionPerformed

    private void editSelectedDatabaseUserAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectedDatabaseUserAccountButtonActionPerformed
	editSelectedAccountActionInitiated();
    }//GEN-LAST:event_editSelectedDatabaseUserAccountButtonActionPerformed

    private void passwordContainerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordContainerComboBoxActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_passwordContainerComboBoxActionPerformed

    private void runPasswordRotationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runPasswordRotationButtonActionPerformed
	runPasswordRotationOnSelectedAccounts();
    }//GEN-LAST:event_runPasswordRotationButtonActionPerformed

    private void helpContentsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpContentsMenuItemActionPerformed
	helpFrame.setVisible(true);
    }//GEN-LAST:event_helpContentsMenuItemActionPerformed

    private void accountAssociationsTreeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountAssociationsTreeMousePressed
	if (evt.isPopupTrigger()) {
	    doRightClickProcessing(evt);
	}
    }//GEN-LAST:event_accountAssociationsTreeMousePressed

    private void accountAssociationsTreeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountAssociationsTreeMouseReleased
	adjustAccountAssoiationsTreeAfterEvent();
	if (evt.isPopupTrigger()) {
	    doRightClickProcessing(evt);
	}

    }//GEN-LAST:event_accountAssociationsTreeMouseReleased

    private void addPasswordContainerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPasswordContainerMenuItemActionPerformed
	addPasswordContainerActionInitiated();
    }//GEN-LAST:event_addPasswordContainerMenuItemActionPerformed

    private void addDatabaseServerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatabaseServerMenuItemActionPerformed
	addDatabaseServerActionInitiated();
    }//GEN-LAST:event_addDatabaseServerMenuItemActionPerformed

    private void removePasswordContainerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePasswordContainerMenuItemActionPerformed
	removePasswordContainerActionInitiated();
    }//GEN-LAST:event_removePasswordContainerMenuItemActionPerformed

    private void removeDatabaseServerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDatabaseServerMenuItemActionPerformed
	removeDatabaseServerActionInitiated();
    }//GEN-LAST:event_removeDatabaseServerMenuItemActionPerformed

    private void associationDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_associationDetailsTableMouseClicked
	if (associationDetailsTable.getSelectedRow() != -1) {
	    editSelectedAssociationDetailButton.setEnabled(true);
	} else {
	    editSelectedAssociationDetailButton.setEnabled(false);
	}
    }//GEN-LAST:event_associationDetailsTableMouseClicked

    private void editSelectedAssociationDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectedAssociationDetailButtonActionPerformed
	associationDetailsTable.editCellAt(associationDetailsTable.getSelectedRow(), 1);
	editSelectedAssociationDetailButton.setEnabled(false);
	applyAssociationDetailsChangeButton.setEnabled(true);
    }//GEN-LAST:event_editSelectedAssociationDetailButtonActionPerformed

    private void applyAssociationDetailsChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyAssociationDetailsChangeButtonActionPerformed
	associationDetailsTable.getCellEditor().stopCellEditing();
    }//GEN-LAST:event_applyAssociationDetailsChangeButtonActionPerformed

    private void removeColdFusionServerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeColdFusionServerMenuItemActionPerformed
	removeColdFusionServerActionInitiated();
    }//GEN-LAST:event_removeColdFusionServerMenuItemActionPerformed

    private void addColdFusionServerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addColdFusionServerMenuItemActionPerformed
	addColdFusionServerActionInitiated();
    }//GEN-LAST:event_addColdFusionServerMenuItemActionPerformed

    private void doColdFusionServerAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doColdFusionServerAddButtonActionPerformed
	ColdFusionConfiguration newConfig = new ColdFusionConfiguration();
	newConfig.setCfAdminUsername(cfAdminUsernameTextField.getText());
	newConfig.setColdFusionServerName(coldFusionServerNameTextField.getText());
	newConfig.setResetCfScriptUrl(cfPasswordChangeUrlTextField.getText());
	coldFusionConfigurationDao.persistNewColdFusionConfiguration(newConfig);
	coldFusionServerConfigurationDialog.setVisible(false);
    }//GEN-LAST:event_doColdFusionServerAddButtonActionPerformed

    private void coldFusionComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_coldFusionComboBoxPopupMenuWillBecomeInvisible
	verifyColdFusionIntegrationFields();
    }//GEN-LAST:event_coldFusionComboBoxPopupMenuWillBecomeInvisible

    private void coldFusionDatabaseHostUrlTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coldFusionDatabaseHostUrlTextFieldKeyReleased
	verifyColdFusionIntegrationFields();
    }//GEN-LAST:event_coldFusionDatabaseHostUrlTextFieldKeyReleased

    private void coldFusionDatabaseNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coldFusionDatabaseNameTextFieldKeyReleased
	verifyColdFusionIntegrationFields();
    }//GEN-LAST:event_coldFusionDatabaseNameTextFieldKeyReleased

    private void coldFusionDatabasePortTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coldFusionDatabasePortTextFieldKeyReleased
	verifyColdFusionIntegrationFields();
    }//GEN-LAST:event_coldFusionDatabasePortTextFieldKeyReleased

    private void coldFusionDatabaseUsernameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coldFusionDatabaseUsernameTextFieldKeyReleased
	verifyColdFusionIntegrationFields();
    }//GEN-LAST:event_coldFusionDatabaseUsernameTextFieldKeyReleased

    private void coldFusionDsnNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coldFusionDsnNameTextFieldKeyReleased
	verifyColdFusionIntegrationFields();
    }//GEN-LAST:event_coldFusionDsnNameTextFieldKeyReleased

    private void openProductPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openProductPageButtonActionPerformed
	try {
	    String url = PRODUCT_INFORMATION_PAGE_URL;
	    java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
	} catch (java.io.IOException e) {
	    System.out.println(e.getMessage());
	}
    }//GEN-LAST:event_openProductPageButtonActionPerformed

    private void copyProductPageToClipboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyProductPageToClipboardButtonActionPerformed
	StringSelection stringSelection = new StringSelection(
		PRODUCT_INFORMATION_PAGE_URL);
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_copyProductPageToClipboardButtonActionPerformed

    private void databasePasswordStoresTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_databasePasswordStoresTableMouseReleased
	analyzeAccountTableSelectedRows();
    }//GEN-LAST:event_databasePasswordStoresTableMouseReleased

    private void showCFAssociatedAccountsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCFAssociatedAccountsMenuItemActionPerformed
	showColdFusionServerAssociatedAccountsActionInitiated();
    }//GEN-LAST:event_showCFAssociatedAccountsMenuItemActionPerformed

    private void showDBAssociatedAccountsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDBAssociatedAccountsMenuItemActionPerformed
	showDatabaseServerAssociatedAccountsActionInitiated();
    }//GEN-LAST:event_showDBAssociatedAccountsMenuItemActionPerformed

    private void showPCAssociatedAccountsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPCAssociatedAccountsMenuItemActionPerformed
	showPasswordContainerAssociatedAccountsActionInitiated();
    }//GEN-LAST:event_showPCAssociatedAccountsMenuItemActionPerformed

    private void showAllAccountsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllAccountsButtonActionPerformed
	databasePasswordStoresTable.setModel(getFullAccountListTableModel());
	resetSearchAccountsTextField();
	accountsTableTitleLabel.setText(ALL_ACCOUNTS_SHOWING_LABEL + getRowCountString());
    }//GEN-LAST:event_showAllAccountsButtonActionPerformed

    private void selectAllAccountsInListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllAccountsInListButtonActionPerformed
	databasePasswordStoresTable.selectAll();
	analyzeAccountTableSelectedRows();
    }//GEN-LAST:event_selectAllAccountsInListButtonActionPerformed

    private void testConnectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testConnectionButtonActionPerformed

	connectionTestSpinnerFrame.setVisible(true);
	JdbcConnection selectedConnection = (JdbcConnection) databaseServerComboBox.getSelectedItem();
	if (JdbcDatabaseUtil.checkConnection(selectedConnection.getJdbcDriverClass(), selectedConnection.getJdbcUrl(), accountUsernameTextField.getText(), accountPasswordField.getText())) {
	    connectionTestSpinnerFrame.setVisible(false);
	    JOptionPane.showMessageDialog(null, "Connection Success", "Connection Success", JOptionPane.INFORMATION_MESSAGE);
	    databaseServerConnectionTestComplete = true;
	    verifyAddAccountFrameFields();
	} else {
	    connectionTestSpinnerFrame.setVisible(false);
	    JOptionPane.showMessageDialog(null, "Failed to connect to database using \nJDBC URL: " + selectedConnection.getJdbcUrl() + "\nUsername: " + accountUsernameTextField.getText() + "\nPlease verify JDBC connection URL, username and password.", "Connection Failed", JOptionPane.ERROR_MESSAGE);
	}
    }//GEN-LAST:event_testConnectionButtonActionPerformed

    private void coldFusionServerHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coldFusionServerHelpButtonActionPerformed
	helpFrame.setVisible(true);
	helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.ADDING_COLDFUSION_SERVERS_NODE.getNode().getPath()));
	setHelpContentAfterSelection();
    }//GEN-LAST:event_coldFusionServerHelpButtonActionPerformed

    private void helpTreeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpTreeMouseReleased
	setHelpContentAfterSelection();
    }//GEN-LAST:event_helpTreeMouseReleased

    private void helpTreeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_helpTreeKeyReleased
	setHelpContentAfterSelection();
    }//GEN-LAST:event_helpTreeKeyReleased

    private void databaseUserAccountsHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_databaseUserAccountsHelpButtonActionPerformed
	helpFrame.setVisible(true);
	helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.CONFIGURING_DATABASE_USER_ACCOUNTS_NODE.getNode().getPath()));
	setHelpContentAfterSelection();
    }//GEN-LAST:event_databaseUserAccountsHelpButtonActionPerformed

    private void associationDetailsTableHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_associationDetailsTableHelpButtonActionPerformed
	helpFrame.setVisible(true);
    }//GEN-LAST:event_associationDetailsTableHelpButtonActionPerformed

    private void addDatabaseConnectionHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatabaseConnectionHelpButtonActionPerformed
	helpFrame.setVisible(true);
	helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.ADDING_DATABASE_SERVERS_NODE.getNode().getPath()));
	setHelpContentAfterSelection();
    }//GEN-LAST:event_addDatabaseConnectionHelpButtonActionPerformed

    private void addPasswordContainerHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPasswordContainerHelpButtonActionPerformed
	helpFrame.setVisible(true);
	helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.ADDING_PASSWORD_CONTAINERS_NODE.getNode().getPath()));
	setHelpContentAfterSelection();
    }//GEN-LAST:event_addPasswordContainerHelpButtonActionPerformed

    private void importDatabaseUserAccountsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importDatabaseUserAccountsMenuItemActionPerformed
	databaseForImportComboBox.setModel(getDatabaseServerComboBoxModel());
	databaseUserImportLoginDialog.setVisible(true);
    }//GEN-LAST:event_importDatabaseUserAccountsMenuItemActionPerformed

    private void importAccoutnsDoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importAccoutnsDoneButtonActionPerformed
	importDatabaseUserAccountsDialog.setVisible(false);
    }//GEN-LAST:event_importAccoutnsDoneButtonActionPerformed

    private void importUserAccountsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importUserAccountsMenuItemActionPerformed
	DefaultMutableTreeNode databaseServerNode = (DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent();
	databaseForImportComboBox.setModel(getDatabaseServerComboBoxModel());
	databaseForImportComboBox.setSelectedItem((JdbcConnection) databaseServerNode.getUserObject());
	databaseUserImportLoginDialog.setVisible(true);
    }//GEN-LAST:event_importUserAccountsMenuItemActionPerformed

    private void importAccountsLoginSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importAccountsLoginSubmitButtonActionPerformed
	JdbcConnection selectedDatabaseServer = (JdbcConnection) databaseForImportComboBox.getSelectedItem();
	List<String> importAccountsUsersList = JdbcDatabaseUtil.getAllDatabaseUserAccountsForTest(selectedDatabaseServer, importAccountsUsernameTextField.getText(), importAccountsLoginPasswordField.getText());
	importAccountsTable.setModel(getImportAccountsTableModel(importAccountsUsersList, selectedDatabaseServer));
	JComboBox passwordContainersComboBox = new JComboBox(getAllPasswordContainerNames());
	importAccountsTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(passwordContainersComboBox));
	databaseUserImportLoginDialog.setVisible(false);
	importDatabaseUserAccountsDialog.setVisible(true);
    }//GEN-LAST:event_importAccountsLoginSubmitButtonActionPerformed

    private void importAccountsLoginCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importAccountsLoginCancelButtonActionPerformed
	databaseUserImportLoginDialog.setVisible(false);
    }//GEN-LAST:event_importAccountsLoginCancelButtonActionPerformed

    private void importAccountsCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importAccountsCancelButtonActionPerformed
	importDatabaseUserAccountsDialog.setVisible(false);
    }//GEN-LAST:event_importAccountsCancelButtonActionPerformed

    private void rangeSliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rangeSliderMouseReleased
	setPasswordLengthRangeLabel();
    }//GEN-LAST:event_rangeSliderMouseReleased

    private void rangeSliderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rangeSliderKeyReleased
	setPasswordLengthRangeLabel();
    }//GEN-LAST:event_rangeSliderKeyReleased

    private void verifyAddPasswordContainerFields() {
	addPasswordContainerStatusLabel.setText("Verifying Input - Please Wait");
	addPasswordContainerFinalButton.setEnabled(false);
	addPasswordContainerStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
	if (passwordContainerNameTextField.getText().isEmpty()) {
	    addPasswordContainerStatusLabel.setText("Enter a name for the new password container.");
	} else {
	    addPasswordContainerStatusLabel.setText("All input has been verified!");
	    addPasswordContainerStatusLabel.setForeground(new java.awt.Color(51, 153, 0));
	    addPasswordContainerFinalButton.setEnabled(true);
	}
    }

    private String[] getAllPasswordContainerNames() {
	List<PasswordContainer> passwordContainers = passwordFileDao.getAllPasswordContainers();
	String[] containers = new String[passwordContainers.size()];
	for (int i = 0; i < containers.length; i++) {
	    containers[i] = passwordContainers.get(i).getGroupName();
	}
	return containers;
    }

    public void doRightClickProcessing(java.awt.event.MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	JTree tree = (JTree) evt.getSource();
	TreePath path = tree.getPathForLocation(x, y);
	if (path == null) {
	    return;
	}

	tree.setSelectionPath(path);

	Object obj = path.getLastPathComponent();
	if (obj instanceof DefaultMutableTreeNode) {
	    DefaultMutableTreeNode thisNode = (DefaultMutableTreeNode) obj;
	    if (DATABASE_SERVERS_NODE_LABEL.equals(thisNode.getUserObject())) {
		databaseServersPopupMenu.show(tree, x, y);
		// thisNode.
	    } else if (COLDFUSION_SERVERS_NODE_LABEL.equals(thisNode.getUserObject())) {
		coldFusionServersPopupMenu.show(tree, x, y);
	    } else if (PASSWORD_CONTAINERS_NODE_LABEL.equals(thisNode.getUserObject())) {
		passwordContainersPopupMenu.show(tree, x, y);
	    } else if (thisNode.getUserObject() instanceof JdbcConnection) {
		databaseServerPopupMenu.show(tree, x, y);
	    } else if (thisNode.getUserObject() instanceof PasswordContainer) {
		passwordContainerPopupMenu.show(tree, x, y);
	    } else if (thisNode.getUserObject() instanceof ColdFusionConfiguration) {
		coldFusionServerPopupMenu.show(tree, x, y);
	    }
	}
    }
// </editor-fold>
// <editor-fold desc="Global Variables Declaration" defaultstate="collapsed">    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aboutCriteriaLabel1;
    private javax.swing.JLabel aboutCriteriaLabel2;
    private javax.swing.JFrame aboutFrame;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JScrollPane accountAssociationsScrollPane;
    private javax.swing.JTree accountAssociationsTree;
    private javax.swing.JButton accountAssociationsTreeHelpButton;
    private javax.swing.JPasswordField accountPasswordField;
    private javax.swing.JLabel accountPasswordFieldLabel;
    private javax.swing.JLabel accountUsernameFieldLabel;
    private javax.swing.JTextField accountUsernameTextField;
    private javax.swing.JLabel accountsTableTitleLabel;
    private javax.swing.JButton addAccountCancelButton;
    private javax.swing.JLabel addAccountStatusLabel;
    private javax.swing.JButton addColdFusionServerButton;
    private javax.swing.JMenuItem addColdFusionServerMenuItem;
    private javax.swing.JButton addDatabaseConnectionButton;
    private javax.swing.JDialog addDatabaseConnectionDialog;
    private javax.swing.JButton addDatabaseConnectionHelpButton;
    private javax.swing.JButton addDatabaseForNewAccountButton;
    private javax.swing.JLabel addDatabaseInstructionsLabel;
    private javax.swing.JLabel addDatabaseLabel;
    private javax.swing.JMenuItem addDatabaseServerMenuItem;
    private javax.swing.JButton addDatabaseUserAccountButton;
    private javax.swing.JDialog addDatabaseUserAccountDialog;
    private javax.swing.JButton addNewDatabaseUserAccountButton;
    private javax.swing.JLabel addNewPasswordContainerLabel;
    private javax.swing.JButton addPasswordContainerFinalButton;
    private javax.swing.JButton addPasswordContainerHelpButton;
    private javax.swing.JMenuItem addPasswordContainerMenuItem;
    private javax.swing.JLabel addPasswordContainerStatusLabel;
    private javax.swing.JButton addPasswordFileForNewAccountButton;
    private javax.swing.JButton addUserAccountAssociationButton;
    private javax.swing.JButton applyAssociationDetailsChangeButton;
    private javax.swing.JButton applyPasswordPolicyButton;
    private javax.swing.JScrollPane associationDetailsScrollPane;
    private javax.swing.JTable associationDetailsTable;
    private javax.swing.JButton associationDetailsTableHelpButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton cancelColdFusionIntegrationButton;
    private javax.swing.JPasswordField cfAdminPasswordField;
    private javax.swing.JTextField cfAdminUsernameTextField;
    private javax.swing.JTextField cfPasswordChangeUrlTextField;
    private javax.swing.JComboBox coldFusionComboBox;
    private javax.swing.JButton coldFusionConfigurationButton;
    private javax.swing.JLabel coldFusionDatabaseHostUrlLabel;
    private javax.swing.JTextField coldFusionDatabaseHostUrlTextField;
    private javax.swing.JLabel coldFusionDatabaseNameLabel;
    private javax.swing.JTextField coldFusionDatabaseNameTextField;
    private javax.swing.JLabel coldFusionDatabasePortLabel;
    private javax.swing.JTextField coldFusionDatabasePortTextField;
    private javax.swing.JLabel coldFusionDatabaseUsernameLabel;
    private javax.swing.JTextField coldFusionDatabaseUsernameTextField;
    private javax.swing.JLabel coldFusionDsnNameLabel;
    private javax.swing.JTextField coldFusionDsnNameTextField;
    private javax.swing.JCheckBox coldFusionIntegrationCheckBox;
    private javax.swing.JLabel coldFusionIntegrationStatusLabel;
    private javax.swing.JLabel coldFusionServerAdditionInstructionsLabel;
    private javax.swing.JLabel coldFusionServerComboBoxLabel;
    private javax.swing.JDialog coldFusionServerConfigurationDialog;
    private javax.swing.JButton coldFusionServerHelpButton;
    private javax.swing.JDialog coldFusionServerIntegrationDialog;
    private javax.swing.JLabel coldFusionServerIntegrationInformationLabel;
    private javax.swing.JTextField coldFusionServerNameTextField;
    private javax.swing.JPopupMenu coldFusionServerPopupMenu;
    private javax.swing.JPopupMenu coldFusionServersPopupMenu;
    private javax.swing.JLabel configureDatabaseServersLabel;
    private javax.swing.JPanel configureDatabaseServersPanel;
    private javax.swing.JPasswordField confirmAccountPasswordField;
    private javax.swing.JLabel confirmAccountPasswordLabel;
    private javax.swing.JFrame connectionTestSpinnerFrame;
    private javax.swing.JButton copyPasswordToClipboardButton;
    private javax.swing.JButton copyProductPageToClipboardButton;
    private javax.swing.JComboBox databaseForImportComboBox;
    private javax.swing.JTable databasePasswordStoresTable;
    private javax.swing.JComboBox databaseServerComboBox;
    private javax.swing.JLabel databaseServerComboBoxLabel;
    private javax.swing.JPopupMenu databaseServerPopupMenu;
    private javax.swing.JPopupMenu databaseServersPopupMenu;
    private javax.swing.JButton databaseUserAccountsHelpButton;
    private javax.swing.JLabel databaseUserAccountsLabel;
    private javax.swing.JPanel databaseUserAccountsPanel;
    private javax.swing.JScrollPane databaseUserAccountsScrollPane;
    private javax.swing.JDialog databaseUserImportLoginDialog;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel descriptiveNameLabel;
    private javax.swing.JTextField descriptiveNameTextField;
    private javax.swing.JSpinner dictionaryWordLengthsSpinner;
    private javax.swing.JCheckBox disallowAlphaSequenceCheckBox;
    private javax.swing.JCheckBox disallowDictWordsCheckBox;
    private javax.swing.JCheckBox disallowQwertySequenceCheckBox;
    private javax.swing.JCheckBox disallowRepeatCharsCheckBox;
    private javax.swing.JCheckBox disallowWhitespaceCheckBox;
    private javax.swing.JButton doColdFusionServerAddButton;
    private javax.swing.JLabel driverClassLabel;
    private javax.swing.JTextField driverClassTextField;
    private javax.swing.JButton editSelectedAssociationDetailButton;
    private javax.swing.JButton editSelectedDatabaseUserAccountButton;
    private javax.swing.JLabel enterPasswordLabel;
    private javax.swing.JLabel enterUrlLabel;
    private javax.swing.JLabel enterUsernameLabel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton generateNewPasswordButton;
    private javax.swing.JScrollPane helpContentScrollPane;
    private javax.swing.JTextPane helpContentTextPane;
    private javax.swing.JMenuItem helpContentsMenuItem;
    private javax.swing.JFrame helpFrame;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JSplitPane helpSplitPane;
    private javax.swing.JTree helpTree;
    private javax.swing.JScrollPane helpTreeScrollPane;
    private javax.swing.JButton importAccountsCancelButton;
    private javax.swing.JLabel importAccountsInformationLabel;
    private javax.swing.JButton importAccountsLoginCancelButton;
    private javax.swing.JLabel importAccountsLoginInformationLabel;
    private javax.swing.JPasswordField importAccountsLoginPasswordField;
    private javax.swing.JButton importAccountsLoginSubmitButton;
    private javax.swing.JLabel importAccountsPasswordLabel;
    private javax.swing.JScrollPane importAccountsScrollPane;
    private javax.swing.JTable importAccountsTable;
    private javax.swing.JLabel importAccountsUsernameLabel;
    private javax.swing.JTextField importAccountsUsernameTextField;
    private javax.swing.JButton importAccoutnsDoneButton;
    private javax.swing.JDialog importDatabaseUserAccountsDialog;
    private javax.swing.JMenuItem importDatabaseUserAccountsMenuItem;
    private javax.swing.JMenuItem importUserAccountsMenuItem;
    private javax.swing.JLabel institutionLabel;
    private javax.swing.JSeparator integrationsSeparator1;
    private javax.swing.JSeparator integrationsSeparator2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JLabel jdbcConnectionUrlLabel;
    private javax.swing.JTextField jdbcConnectionUrlTextField;
    private javax.swing.JSpinner numCharRepeatsSpinner;
    private javax.swing.JLabel numDigitsLabel;
    private javax.swing.JSpinner numDigitsSpinner;
    private javax.swing.JLabel numLCaseLabel;
    private javax.swing.JSpinner numLowerCaseSpinner;
    private javax.swing.JLabel numSymbolsLabel;
    private javax.swing.JSpinner numSymbolsSpinner;
    private javax.swing.JLabel numUCaseLabel;
    private javax.swing.JSpinner numUpperCaseSpinner;
    private javax.swing.JLabel odbFilePathLabel;
    private javax.swing.JTextField odbFilePathTextField;
    private javax.swing.JButton openProductPageButton;
    private javax.swing.JComboBox passwordContainerComboBox;
    private javax.swing.JLabel passwordContainerComboBoxLabel;
    private javax.swing.JDialog passwordContainerConfigurationDialog;
    private javax.swing.JLabel passwordContainerNameLabel;
    private javax.swing.JTextField passwordContainerNameTextField;
    private javax.swing.JPopupMenu passwordContainerPopupMenu;
    private javax.swing.JPopupMenu passwordContainersPopupMenu;
    private javax.swing.JTextField passwordDisplayTextField;
    private javax.swing.JLabel passwordLengthLabel;
    private javax.swing.JLabel passwordRangeLabel;
    private javax.swing.JSplitPane passwordRepositoriesSplitPane;
    private javax.swing.JMenuItem passwordStrengthCriteriaMenuItem;
    private javax.swing.JLabel passwordStrengthPropertiesLabel;
    private javax.swing.JFrame passwordsStrengthCriteriaFrame;
    private javax.swing.JLabel portNumberLabel;
    private javax.swing.JTextField portNumberTextField;
    private javax.swing.JLabel productPageLabel;
    private javax.swing.JTextField productPageTextField;
    private javax.swing.JLabel projectNameLabel;
    private javax.swing.JSlider rangeSlider;
    private javax.swing.JMenuItem removeColdFusionServerMenuItem;
    private javax.swing.JMenuItem removeDatabaseServerMenuItem;
    private javax.swing.JMenuItem removePasswordContainerMenuItem;
    private javax.swing.JButton removeSelectedDatabaseUserAccountsButton;
    private javax.swing.JButton removeUserAccountAssociationButton;
    private javax.swing.JButton restorePasswordPolicyDefaultsButton;
    private javax.swing.JButton runPasswordRotationButton;
    private javax.swing.JTextField searchAccountsTextField;
    private javax.swing.JButton selectAllAccountsInListButton;
    private javax.swing.JLabel selectedAssociationDetailsLabel;
    private javax.swing.JPanel selectedAssociationDetailsPanel;
    private javax.swing.JLabel selectedPasswordsCountLabel;
    private javax.swing.JLabel serverAddressLabel;
    private javax.swing.JTextField serverAddressTextField;
    private javax.swing.JButton setColdFusionIntegrationButton;
    private javax.swing.JButton showAllAccountsButton;
    private javax.swing.JButton showAssociatedAccountsButton;
    private javax.swing.JMenuItem showCFAssociatedAccountsMenuItem;
    private javax.swing.JMenuItem showDBAssociatedAccountsMenuItem;
    private javax.swing.JMenuItem showPCAssociatedAccountsMenuItem;
    private javax.swing.JButton testConnectionButton;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JSplitPane topLevelSplitPane;
    private javax.swing.JMenuBar topMenuBar;
    private javax.swing.JLabel urlParametersWarningLabel;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JButton viewSelectedPasswordButton;
    // End of variables declaration//GEN-END:variables
    private boolean coldFusionIntegrationInformationComplete = false;
    private boolean databaseServerConnectionTestComplete = false;
// </editor-fold>
// <editor-fold desc="Database Servers Configuration Methods" defaultstate="collapsed">
    private void undoJdbcManualEntry() {
	if (jdbcConnectionUrlTextField.isEditable()) {
	    jdbcConnectionUrlTextField.setEditable(false);
	    jdbcConnectionUrlTextField.setText(JDBC_URL_GENERATION_MESSAGE);
	    serverAddressTextField.setEditable(true);
	    serverAddressTextField.setText("");
	    portNumberTextField.setEditable(true);
	    portNumberTextField.setText(JDBC_DEFAULT_PORT);
	}
    }

    private void updateJDBCUrlIfAllFieldsNonEmpty() {
	if (!serverAddressTextField.getText().isEmpty()
		&& !portNumberTextField.getText().isEmpty()) {
	    jdbcConnectionUrlTextField.setText(getJdbcUrlFromFields());
	}
    }

    private String getJdbcUrlFromFields() {
	return JDBC_URL_PREFIX
		+ serverAddressTextField.getText() + ":"
		+ portNumberTextField.getText();
    }

    private void runDatabaseAdditionFinalTasks() {
	accountAssociationsTree.setModel(getDatabaseServersTreeModel());
	addDatabaseConnectionDialog.setVisible(false);
	JOptionPane.showMessageDialog(
		null,
		String.format(
		MESSAGE_CONFIRMING_NEW_DATABASE_CONNECTION_ADDITION,
		descriptiveNameTextField.getText(),
		jdbcConnectionUrlTextField.getText()));
    }
// </editor-fold>
// <editor-fold desc="Password Verification Methods" defaultstate="collapsed">
    private boolean confirmPasswordsSame(char[] newPassword,
	    char[] newPasswordConfirmation) {
	return new String(newPassword).equals(new String(
		newPasswordConfirmation));
    }

    /**
     * Method passwordMeetsPolicy will determine if the given password meets the
     * minimum password strength critera that is set by the system.
     *
     * @param newPassword the password to be checked
     * @return true if the password meets policy, false if it does not.
     */
    private boolean passwordMeetsPolicy(char[] newPassword) {
	String passwordValidationResult = PasswordUtils.passwordMeetsPolicy(
		newPassword, passwordPolicyDao.getCurrentPasswordPolicy());
	if (passwordValidationResult.equals("Valid password")) {
	    return true;
	}
	JOptionPane.showOptionDialog(
		null,
		passwordValidationResult,
		WINDOW_TITLE_FOR_PASSWORD_NOT_MEETING_CRITERIA_OPTION_DIALOG,
		JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
		null, null, null);
	return false;

    }

    private void copySelectedAccountPasswordToClipboard() {
	AccountInfo selectedAccountInfo = (AccountInfo) databasePasswordStoresTable.getValueAt(databasePasswordStoresTable.getSelectedRow(), 0);
	char[] decryptedPassword = PasswordUtils.decrypt(
		encryptedPasswordDao.getEncryptedPasswordForUser(
		selectedAccountInfo.getUserName(),
		selectedAccountInfo.getPasswordContainer().getDatabaseFileUrl()).getEncryptedPassword(), selectedAccountInfo.getUserName(),
		keyDao.getKeyForUser(selectedAccountInfo.getUserName()));

	StringSelection stringSelection = new StringSelection(new String(
		decryptedPassword));
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	clipboard.setContents(stringSelection, null);
	JOptionPane.showMessageDialog(null, String.format(
		"The password for user %s has been added to your clipboard.",
		selectedAccountInfo.getUserName()),
		"Password Added to Clipboard", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewSelectedAccountPassword() {
	AccountInfo selectedAccountInfo = (AccountInfo) accountInfoDao.getAccountInfoForUser((String)
			databasePasswordStoresTable.getValueAt(databasePasswordStoresTable.getSelectedRow(), 0));
	if (selectedAccountInfo instanceof AccountInfo) {
	    char[] decryptedPassword = PasswordUtils.decrypt(
		    encryptedPasswordDao.getEncryptedPasswordForUser(
		    selectedAccountInfo.getUserName(),
		    selectedAccountInfo.getPasswordContainer().getDatabaseFileUrl()).getEncryptedPassword(), selectedAccountInfo.getUserName(), keyDao.getKeyForUser(selectedAccountInfo.getUserName()));

	    passwordDisplayTextField.setText(new String(decryptedPassword));
	    Timer timer = new Timer();
	    timer.schedule(new ClearPasswordTimerTask(),
		    new Date(System.currentTimeMillis() + 5000));
	} else {
	    JOptionPane.showMessageDialog(
		    null,
		    MESSAGE_FOR_ACCOUNT_NOT_SELECTED_IN_VIEW_PASSWORDS);
	}
    }

    private void applyPasswordPolicySpecified() {
	try {
	    RangeSlider slider = (RangeSlider) rangeSlider;
	    PasswordPolicy newPolicy = new PasswordPolicy();
	    newPolicy.setMinimumPasswordLength(slider.getValue());
	    newPolicy.setMaximumPasswordLength(slider.getUpperValue());
	    newPolicy.setMaximumNumberOfDigits((Integer) numDigitsSpinner.getValue());
	    newPolicy.setMaximumNumberOfSymbols((Integer) numSymbolsSpinner.getValue());
	    newPolicy.setMaximumNumberOfLowercase((Integer) numLowerCaseSpinner.getValue());
	    newPolicy.setMaximumNumberOfUppercase((Integer) numUpperCaseSpinner.getValue());
	    newPolicy.setDisallowAlphaSequence(disallowAlphaSequenceCheckBox.isSelected());
	    newPolicy.setDisallowQwertySequence(disallowQwertySequenceCheckBox.isSelected());
	    newPolicy.setDisallowWhitespace(disallowWhitespaceCheckBox.isSelected());
	    newPolicy.setDisallowCharRepeats(disallowRepeatCharsCheckBox.isSelected());
	    newPolicy.setAmountOfCharRepeatsToAllow((Integer) numCharRepeatsSpinner.getValue());
	    newPolicy.setPolicyActive(true);

	    passwordPolicyDao.persistNewPasswordPolicy(newPolicy);

	    passwordsStrengthCriteriaFrame.setVisible(false);
	} catch (Exception e) {
            e.printStackTrace();
	    JOptionPane.showMessageDialog(
		    null,
		    MESSAGE_FOR_INVALID_PASSWORD_CRITERIA_INPUT);
	}
    }

    private void populatePasswordStrengthCriteriaFrameWithCurrentCriteria() {
	PasswordPolicy policy = passwordPolicyDao.getCurrentPasswordPolicy();
	((RangeSlider) rangeSlider).setValue(policy.getMinimumPasswordLength());
	((RangeSlider) rangeSlider).setUpperValue(policy.getMaximumPasswordLength());
	setPasswordLengthRangeLabel();

	numDigitsSpinner.setValue(policy.getMaximumNumberOfDigits());
	numSymbolsSpinner.setValue(policy.getMaximumNumberOfSymbols());
	numLowerCaseSpinner.setValue(policy.getMaximumNumberOfLowercase());
	numUpperCaseSpinner.setValue(policy.getMaximumNumberOfUppercase());
	numCharRepeatsSpinner.setValue(policy.getAmountOfCharRepeatsToAllow());
	//disallowDictWordsLongerThanTextField.setText(policy.)
	disallowRepeatCharsCheckBox.setSelected(policy.isDisallowCharRepeats());
	//disallowDictWordsCheckBox.setSelected(policy.)
	disallowWhitespaceCheckBox.setSelected(policy.isDisallowWhitespace());
	disallowQwertySequenceCheckBox.setSelected(policy.isDisallowQwertySequence());
	disallowAlphaSequenceCheckBox.setSelected(policy.isDisallowAlphaSequence());
    }

    private void setPasswordLengthRangeLabel() {
	RangeSlider slider = (RangeSlider) rangeSlider;
	passwordRangeLabel.setText("<html><FONT COLOR=BLUE>" + slider.getValue() + "</FONT> - <FONT COLOR=RED>" + slider.getUpperValue() + "</FONT></html>");
    }
// </editor-fold>
// <editor-fold desc="Add/Modify Account Methods" defaultstate="collapsed">
    private void editSelectedAccountActionInitiated() {
	AccountInfo selectedAccountInfo = (AccountInfo) databasePasswordStoresTable.getValueAt(databasePasswordStoresTable.getSelectedRow(), 0);
	setAddDatabaseUserAccountDialogToEditMode(selectedAccountInfo);
	addDatabaseUserAccountDialog.setVisible(true);
	unsetAddDatabaseUserAccountDialogToEditMode();
    }

    private void verifyAddAccountFrameFields() {
	addAccountStatusLabel.setText("Verifying Input - Please Wait");
	addDatabaseUserAccountButton.setEnabled(false);
	addAccountStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
	if (accountUsernameTextField.getText().isEmpty()) {
	    addAccountStatusLabel.setText(MESSAGE_FOR_MISSING_USERNAME);
	} else if(accountInfoDao.userExists(accountUsernameTextField.getText())){
            addAccountStatusLabel.setText("pecified username already exists.");
        } else if (accountPasswordField.getPassword().length == 0
		|| confirmAccountPasswordField.getPassword().length == 0) {
	    addAccountStatusLabel.setText("Enter the account password and confirm.");
	} else if (!confirmPasswordsSame(accountPasswordField.getPassword(), confirmAccountPasswordField.getPassword())) {
	    addAccountStatusLabel.setText(MESSAGE_FOR_PASSWORDS_NOT_MATCHING);
	} else if (!passwordMeetsPolicy(accountPasswordField.getPassword())) {
	    addAccountStatusLabel.setText(MESSAGE_FOR_PASSWORD_NOT_MEETING_CRITERIA);
	} else if (!(databaseServerComboBox.getSelectedItem() instanceof JdbcConnection)) {
	    addAccountStatusLabel.setText(MESSAGE_FOR_DATABASE_CONNECTION_NOT_SELECTED);
	} else if (!(passwordContainerComboBox.getSelectedItem() instanceof PasswordContainer)) {
	    addAccountStatusLabel.setText(MESSAGE_FOR_PASSWORD_FILE_NOT_SELCTED);
	} else if (coldFusionIntegrationCheckBox.isSelected() && !coldFusionIntegrationInformationComplete) {
	    addAccountStatusLabel.setText("You must complete ColdFusion server configuration.");
	} else if (!databaseServerConnectionTestComplete) {
	    addAccountStatusLabel.setText("You must test this connection.");
	} else {
	    addAccountStatusLabel.setText("All input has been verified!");
	    addAccountStatusLabel.setForeground(new java.awt.Color(51, 153, 0));
	    addDatabaseUserAccountButton.setEnabled(true);
	}
    }

    private void verifyColdFusionIntegrationFields() {
	coldFusionIntegrationStatusLabel.setText("Verifying Input - Please Wait");
	setColdFusionIntegrationButton.setEnabled(false);
	coldFusionIntegrationStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
	if (coldFusionDatabaseHostUrlTextField.getText().isEmpty()) {
	    coldFusionIntegrationStatusLabel.setText("Enter a Host URL.");
	} else if (coldFusionDatabaseNameTextField.getText().isEmpty()) {
	    coldFusionIntegrationStatusLabel.setText("Enter a database name.");
	} else if (coldFusionDatabasePortTextField.getText().isEmpty()) {
	    coldFusionIntegrationStatusLabel.setText("Please enter a port.");
	} else if (coldFusionDatabaseUsernameTextField.getText().isEmpty()) {
	    coldFusionIntegrationStatusLabel.setText("Please enter a database username.");
	} else if (!(coldFusionComboBox.getSelectedItem() instanceof ColdFusionConfiguration)) {
	    coldFusionIntegrationStatusLabel.setText("You must select a ColdFusion server.");
	} else {
	    coldFusionIntegrationStatusLabel.setText("All input has been verified!");
	    coldFusionIntegrationStatusLabel.setForeground(new java.awt.Color(51, 153, 0));
	    setColdFusionIntegrationButton.setEnabled(true);
	    this.coldFusionIntegrationInformationComplete = true;
	}
    }

    private void submitNewAccount() {
	submitAccount(false);
    }

    private void submitExistingAccount() {
	submitAccount(true);
    }

    private void submitAccount(boolean passwordExists) {
	AccountInfo newPass = new AccountInfo();
	if (passwordExists) {
	    newPass = (AccountInfo) databasePasswordStoresTable.getValueAt(databasePasswordStoresTable.getSelectedRow(), 0);
	}
	PasswordContainer detachedPasswordContainer = (PasswordContainer) passwordContainerComboBox.getSelectedItem();;
	byte[] encryptedPassword = PasswordUtils.encrypt(
		accountPasswordField.getPassword(),
		keyDao.getKeyForUser(accountUsernameTextField.getText()));
	if (!passwordExists) {
	    newPass.setPasswordContainer(passwordFileDao.getPasswordContainerById(detachedPasswordContainer.getPasswordContainerId()));
	    encryptedPassword = setEncryptedPasswordForNewAccount(
		    newPass, accountPasswordField.getPassword(), accountUsernameTextField.getText());
	} else {
	    PasswordContainer originalPasswordContainer = newPass.getPasswordContainer();

	    if (getPasswordFromAccountInfo(newPass) != accountPasswordField.getPassword()) {
		encryptedPasswordDao.setExistingEncryptedPassword(
			encryptedPassword, accountUsernameTextField.getText(),
			originalPasswordContainer.getDatabaseFileUrl());
	    }

	    if (!originalPasswordContainer.getDatabaseFileUrl().equals(detachedPasswordContainer.getDatabaseFileUrl())) {
		encryptedPasswordDao.moveEncryptedPassword(originalPasswordContainer.getDatabaseFileUrl(), detachedPasswordContainer.getDatabaseFileUrl(), accountUsernameTextField.getText());
		newPass.setPasswordContainer(detachedPasswordContainer);
	    }
	}
	newPass.setUserName(accountUsernameTextField.getText());
	JdbcConnection passwordConnection = (JdbcConnection) databaseServerComboBox.getSelectedItem();
	if (coldFusionIntegrationCheckBox.isSelected()) {
	    newPass.setColdFusionConfiguration((ColdFusionConfiguration) coldFusionComboBox.getSelectedItem());
	    newPass.setDatabaseName(coldFusionDatabaseNameTextField.getText());
	    newPass.setDsnName(coldFusionDsnNameTextField.getText());
	}
	newPass.setJdbcConnection(passwordConnection);

	if (!passwordExists) {
	    accountInfoDao.persistNewAccountInfo(newPass);
	} else {
	    accountInfoDao.persistExistingAccountInfo(newPass, keyDao.getKeyForUser(accountUsernameTextField.getText()), encryptedPassword);
	}
    }

    private char[] getPasswordFromAccountInfo(AccountInfo newPass) {
	return PasswordUtils.decrypt(
		encryptedPasswordDao.getEncryptedPasswordForUser(
		newPass.getUserName(),
		newPass.getPasswordContainer().getDatabaseFileUrl()).getEncryptedPassword(), newPass.getUserName(), keyDao.getKeyForUser(newPass.getUserName()));
    }

    private byte[] setEncryptedPasswordForNewAccount(AccountInfo newPass,
	    char[] newPassword, String userNameOfNewPassword) {
	PasswordContainer passwordFile = (PasswordContainer) passwordContainerComboBox.getSelectedItem();
	byte[] encryptedPassword = PasswordUtils.encrypt(newPassword,
		keyDao.getKeyForUser(userNameOfNewPassword));
	encryptedPasswordDao.setNewEncryptedPassword(encryptedPassword,
		userNameOfNewPassword, newPass.getPasswordContainer().getDatabaseFileUrl());
	return encryptedPassword;
    }

    private void clearDatabaseUserAccountDialogFields() {
	accountUsernameTextField.setText("");
	accountPasswordField.setText(null);
	confirmAccountPasswordField.setText(null);
	coldFusionIntegrationCheckBox.setSelected(false);
	coldFusionConfigurationButton.setEnabled(false);
	coldFusionDatabaseHostUrlTextField.setText(null);
	coldFusionDatabaseNameTextField.setText(null);
	coldFusionDatabasePortTextField.setText(null);
	coldFusionDatabaseUsernameTextField.setText(null);
	coldFusionDsnNameTextField.setText(null);
	setColdFusionIntegrationButton.setEnabled(false);
	addAccountStatusLabel.setText(MESSAGE_FOR_MISSING_USERNAME);
	addAccountStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
	databaseServerConnectionTestComplete = false;
	addDatabaseUserAccountButton.setEnabled(false);
	this.coldFusionIntegrationInformationComplete = false;
    }

    private void runPasswordRotationOnSelectedAccounts() {
	int[] selectedAccountInfos = databasePasswordStoresTable.getSelectedRows();
	String plural = selectedAccountInfos.length > 1 ? "s" : "";
	int response = JOptionPane.showConfirmDialog(null, String.format(
		"You are about to rotate passwords on %d database user account%s.\n"
		+ "This action will change passwords on the actual DBMS as well.\n"
		+ "Do you want to proceed?", selectedAccountInfos.length, plural),
		String.format("Rotate %d Password%s - Conformation", selectedAccountInfos.length, plural),
		JOptionPane.YES_NO_OPTION);
	if (response == JOptionPane.YES_OPTION) {
	    for (int i = 0; i < selectedAccountInfos.length; i++) {
		char[] newPassword = PasswordUtils.generateNewPassword(passwordPolicyDao.getCurrentPasswordPolicy());
		if (databasePasswordStoresTable.getValueAt(selectedAccountInfos[i], 0) instanceof AccountInfo) {
		    AccountInfo thisAccountInfo = (AccountInfo) databasePasswordStoresTable.getValueAt(selectedAccountInfos[i], 0);

		    char[] decryptedOldPassword = PasswordUtils.decrypt(
			    encryptedPasswordDao.getEncryptedPasswordForUser(
			    thisAccountInfo.getUserName(),
			    thisAccountInfo.getPasswordContainer().getDatabaseFileUrl()).getEncryptedPassword(), thisAccountInfo.getUserName(), keyDao.getKeyForUser(thisAccountInfo.getUserName()));
		    try {
			JdbcDatabaseUtil.updatePassword(thisAccountInfo.getUserName(),
				new String(newPassword), thisAccountInfo.getUserName(),
				new String(decryptedOldPassword), thisAccountInfo.getJdbcConnection().getJdbcUrl(),
				thisAccountInfo.getJdbcConnection().getJdbcDriverClass());
		    } catch (Exception e) {
			if (e instanceof SQLException) {
			    SQLException se = (SQLException) e;
			    System.out.println("SQL Exception:");

			    // Loop through the SQL Exceptions
			    while (se != null) {
				System.out.println("State  : " + se.getSQLState());
				System.out.println("Message: " + se.getMessage());
				System.out.println("Error  : " + se.getErrorCode());

				se = se.getNextException();
			    }
			}
			System.out.println(e);
			int choice = JOptionPane.showOptionDialog(null, "Error occured during password update on database server " + thisAccountInfo.getJdbcConnection().getDescriptiveName() + ".\n"
				+ "for user " + thisAccountInfo.getUserName() + ". This password has not been changed.\n"
				+ "Would you like to continue rotating remaining passwords?", "Password Rotation Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
			if (choice == JOptionPane.YES_OPTION) {
			    continue;
			} else {
			    return;
			}
		    }
		    encryptedPasswordDao.setExistingEncryptedPassword(PasswordUtils.encrypt(newPassword, keyDao.getKeyForUser(thisAccountInfo.getUserName())),
			    thisAccountInfo.getUserName(), thisAccountInfo.getPasswordContainer().getDatabaseFileUrl());
		}
	    }
	}
    }

    private void removeDatabaseUserAccountActionInitiated() {
	int[] selectedRows = databasePasswordStoresTable.getSelectedRows();
	for (int i = 0; i < selectedRows.length; i++) {
	    AccountInfo selectedAccountInfo = (AccountInfo) databasePasswordStoresTable.getValueAt(selectedRows[i], 0);
	    int confirmed = JOptionPane.showConfirmDialog(null, String.format("Are you sure you would like to remove the following database user account:\n\nUsername: %s", selectedAccountInfo.getUserName()), "Remove Database User Account", JOptionPane.YES_NO_OPTION);
	    if (confirmed == JOptionPane.YES_OPTION) {
		AccountInfo accountInfoToRemove = (AccountInfo) databasePasswordStoresTable.getValueAt(selectedRows[i], 0);
		encryptedPasswordDao.removeEncryptedPasswordByUserName(accountInfoToRemove.getUserName(), accountInfoToRemove.getPasswordContainer().getDatabaseFileUrl());
		accountInfoDao.removeAccountInfo((AccountInfo) databasePasswordStoresTable.getValueAt(selectedRows[i], 0));
	    }
	}
	resetDatabaseUserAccountsTable();
    }
// </editor-fold>
// <editor-fold desc="Accounts Table Methods" defaultstate="collapsed">    
    private void resetSearchAccountsTextField() {
	searchAccountsTextField.setText(SEARCH_ACCOUNTS_DEFAULT_MESSAGE);
	searchAccountsTextField.setEditable(false);
    }

    private void analyzeAccountTableSelectedRows() {
	int selectedRowCount = databasePasswordStoresTable.getSelectedRowCount();
	if (selectedRowCount == 1) {
	    selectedPasswordsCountLabel.setText("1 Item Selected");
	    selectedPasswordsCountLabel.setForeground(new java.awt.Color(51, 153, 0));
	    viewSelectedPasswordButton.setEnabled(true);
	    copyPasswordToClipboardButton.setEnabled(true);
	    editSelectedDatabaseUserAccountButton.setEnabled(true);
	} else {
	    selectedPasswordsCountLabel.setText(selectedRowCount + " Items Selected");
	    selectedPasswordsCountLabel.setForeground(new java.awt.Color(102, 102, 225));
	    viewSelectedPasswordButton.setEnabled(false);
	    copyPasswordToClipboardButton.setEnabled(false);
	    editSelectedDatabaseUserAccountButton.setEnabled(false);
	}
	if (selectedRowCount > 0) {
	    removeSelectedDatabaseUserAccountsButton.setEnabled(true);
	    runPasswordRotationButton.setEnabled(true);
	} else {
	    removeSelectedDatabaseUserAccountsButton.setEnabled(false);
	    runPasswordRotationButton.setEnabled(false);
	}
    }

    private void resetDatabaseUserAccountsTable() {
	databasePasswordStoresTable.setModel(getFullAccountListTableModel());
	resetSearchAccountsTextField();
	analyzeAccountTableSelectedRows();
	accountsTableTitleLabel.setText(ALL_ACCOUNTS_SHOWING_LABEL + getRowCountString());
    }

    private String getRowCountString() {
	String accountsString = " Accounts)";
	if (databasePasswordStoresTable.getRowCount() == 1) {
	    accountsString = " Account)";
	}
	return " (" + databasePasswordStoresTable.getRowCount() + accountsString;
    }
// </editor-fold>
// <editor-fold desc="Associations Tree Methods" defaultstate="collapsed">
    private void adjustAccountAssoiationsTreeAfterEvent() {
	Object value = accountAssociationsTree.getLastSelectedPathComponent();
	switch (AssociationTreeSelectionType.getSelectionTypeFromNode(value)) {
	    case DATABASE_SERVERS_NODE:
		setAddAssociationButtonForDatabaseServer();
		setAssociationTreeHelpButtonToConfiguringDatabaseServers();
		break;
	    case PASSWORD_CONTAINERS_NODE:
		setAddAssociationButtonForPasswordContainers();
		setAssociationTreeHelpButtonToConfiguringPasswordContainers();
		break;
	    case COLDFUSION_SERVERS_NODE:
		setAddAssociationButtonForColdFusionServers();
		setAssociationTreeHelpButtonToConfiguringColdFusionServers();
		break;
	    case DATABASE_SERVER_NODE:
		setAddAssociationButtonForDatabaseServer();
		setRemoveAssociationButton(
			REMOVE_DATABASE_LARGE_IMAGE_PATH,
			new java.awt.event.ActionListener() {
			    @Override
			    public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeDatabaseServerActionInitiated();
			    }
			});
		setAssociationTreeHelpButtonToConfiguringDatabaseServers();
		removeUserAccountAssociationButton.setToolTipText("Remove Selected Database Server");
		setShowAssociatedAccountsButton(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
			showDatabaseServerAssociatedAccountsActionInitiated();
		    }
		});
		setEditAssociationsHelpButton(new java.awt.event.ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			helpFrame.setVisible(true);
			helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.EDITING_DATABASE_SERVERS_NODE.getNode().getPath()));
			setHelpContentAfterSelection();
		    }
		});
		associationDetailsTableHelpButton.setToolTipText("Get Help Editing " + DATABASE_SERVERS_NODE_LABEL);
		associationDetailsTable.setModel(getAssociationDetailsTableModelForDatabaseServer());
		break;
	    case PASSWORD_CONTAINER_NODE:
		setAddAssociationButtonForPasswordContainers();
		setRemoveAssociationButton(
			REMOVE_PASSWORD_FILE_LARGE_IMAGE_PATH,
			new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent ae) {
				removePasswordContainerActionInitiated();
			    }
			});
		setAssociationTreeHelpButtonToConfiguringPasswordContainers();
		removeUserAccountAssociationButton.setToolTipText("Remove Selected Password Container");
		setShowAssociatedAccountsButton(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
			showPasswordContainerAssociatedAccountsActionInitiated();
		    }
		});
		setEditAssociationsHelpButton(new java.awt.event.ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			helpFrame.setVisible(true);
			helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.EDITING_PASSWORD_CONTAINERS_NODE.getNode().getPath()));
			setHelpContentAfterSelection();
		    }
		});
		associationDetailsTableHelpButton.setToolTipText("Get Help Editing " + PASSWORD_CONTAINERS_NODE_LABEL);
		associationDetailsTable.setModel(getAssociationDetailsTableModelForPasswordContainer());
		break;
	    case COLDFUSION_SERVER_NODE:
		setAddAssociationButtonForColdFusionServers();
		setRemoveAssociationButton(
			REMOVE_COLDFUSION_SERVER_LARGE_IMAGE_PATH,
			new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent ae) {
				removeColdFusionServerActionInitiated();
			    }
			});
		setAssociationTreeHelpButtonToConfiguringColdFusionServers();
		removeUserAccountAssociationButton.setToolTipText("Remove Selected ColdFusion Server");
		setShowAssociatedAccountsButton(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
			showColdFusionServerAssociatedAccountsActionInitiated();
		    }
		});
		setEditAssociationsHelpButton(new java.awt.event.ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			helpFrame.setVisible(true);
			helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.EDITING_COLDFUSION_SERVERS_NODE.getNode().getPath()));
			setHelpContentAfterSelection();
		    }
		});
		associationDetailsTableHelpButton.setToolTipText("Get Help Editing " + COLDFUSION_SERVERS_NODE_LABEL);
		associationDetailsTable.setModel(getAssociationDetailsTableModelForColdFusionServer());
		break;
	    default:
		removeUserAccountAssociationButton.setEnabled(false);
		addUserAccountAssociationButton.setEnabled(false);
		showAssociatedAccountsButton.setEnabled(false);
		break;

	}
    }

    private void setAddAssociationButton(String imagePath, ActionListener listener) {
	removeUserAccountAssociationButton.setEnabled(false);
	addUserAccountAssociationButton.setEnabled(true);
	showAssociatedAccountsButton.setEnabled(false);
	removeAllActionListeners(addUserAccountAssociationButton);
	addUserAccountAssociationButton.setIcon(new ImageIcon(getClass().getResource(imagePath)));
	addUserAccountAssociationButton.addActionListener(listener);
    }

    private void setRemoveAssociationButton(String imagePath, ActionListener listener) {
	removeUserAccountAssociationButton.setEnabled(true);
	addUserAccountAssociationButton.setEnabled(true);
	removeAllActionListeners(removeUserAccountAssociationButton);
	removeUserAccountAssociationButton.setIcon(new ImageIcon(getClass().getResource(imagePath)));
	removeUserAccountAssociationButton.addActionListener(listener);
    }

    private void setAddAssociationButtonForDatabaseServer() {
	setAddAssociationButton(ADD_DATABASE_LARGE_IMAGE_PATH,
		new java.awt.event.ActionListener() {
		    @Override
		    public void actionPerformed(java.awt.event.ActionEvent evt) {
			addDatabaseServerActionInitiated();
		    }
		});
	addUserAccountAssociationButton.setToolTipText("Add Database Server Connection");
    }

    private void setAddAssociationButtonForPasswordContainers() {
	setAddAssociationButton(ADD_PASSWORD_CONTAINER_LARGE_IMAGE_PATH,
		new java.awt.event.ActionListener() {
		    @Override
		    public void actionPerformed(java.awt.event.ActionEvent evt) {
			addPasswordContainerActionInitiated();
		    }
		});
	addUserAccountAssociationButton.setToolTipText("Add Password Container");
    }

    private void setAddAssociationButtonForColdFusionServers() {
	setAddAssociationButton(ADD_COLDFUSION_SERVER_LARGE_IMAGE,
		new java.awt.event.ActionListener() {
		    @Override
		    public void actionPerformed(java.awt.event.ActionEvent evt) {
			addColdFusionServerActionInitiated();
		    }
		});
	addUserAccountAssociationButton.setToolTipText("Add ColdFusion Server");
    }

    private void setAssociationTreeHelpButtonToConfiguringDatabaseServers() {
	setAssociationTreeHelpButton(new java.awt.event.ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		helpFrame.setVisible(true);
		helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.CONFIGURING_DATABASE_SERVERS_NODE.getNode().getPath()));
		setHelpContentAfterSelection();
	    }
	});
	accountAssociationsTreeHelpButton.setToolTipText("Get Help Configuring " + DATABASE_SERVERS_NODE_LABEL);
    }

    private void setAssociationTreeHelpButtonToConfiguringPasswordContainers() {
	setAssociationTreeHelpButton(new java.awt.event.ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		helpFrame.setVisible(true);
		helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.CONFIGURING_PASSWORD_CONTAINERS_NODE.getNode().getPath()));
		setHelpContentAfterSelection();
	    }
	});
	accountAssociationsTreeHelpButton.setToolTipText("Get Help Configuring " + PASSWORD_CONTAINERS_NODE_LABEL);
    }

    private void setAssociationTreeHelpButtonToConfiguringColdFusionServers() {
	setAssociationTreeHelpButton(new java.awt.event.ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		helpFrame.setVisible(true);
		helpTree.setSelectionPath(new TreePath(HelpTreeSelectionType.CONFIGURING_COLDFUSION_SERVERS_NODE.getNode().getPath()));
		setHelpContentAfterSelection();
	    }
	});
	accountAssociationsTreeHelpButton.setToolTipText("Get Help Configuring " + COLDFUSION_SERVERS_NODE_LABEL);
    }

    private void setHelpContentAfterSelection() {
	if (helpTree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode) {
	    String helpContent = HelpTreeSelectionType.getHtmlContentFromNode((DefaultMutableTreeNode) helpTree.getLastSelectedPathComponent());
	    helpContentTextPane.setText(helpContent);
	} else {
	    helpContentTextPane.setText("No Topic Selected");
	}
    }

    private void setAssociationTreeHelpButton(ActionListener actionListener) {
	removeAllActionListeners(accountAssociationsTreeHelpButton);
	accountAssociationsTreeHelpButton.addActionListener(actionListener);
    }

    private void setEditAssociationsHelpButton(ActionListener actionListener) {
	removeAllActionListeners(associationDetailsTableHelpButton);
	associationDetailsTableHelpButton.addActionListener(actionListener);
    }

    private void addDatabaseServerActionInitiated() {
	addDatabaseConnectionDialog.setVisible(true);
	adjustAccountAssoiationsTreeAfterEvent();
	databaseServerComboBox.setModel(getDatabaseServerComboBoxModel());
    }

    private void removeDatabaseServerActionInitiated() {
	DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent();
	if (!(nodeToRemove.getUserObject() instanceof JdbcConnection)) {
	    nodeToRemove = (DefaultMutableTreeNode) nodeToRemove.getParent();
	}
	JdbcConnection connectionToRemove = (JdbcConnection) nodeToRemove.getUserObject();
	List<AccountInfo> associatedAccounts = accountInfoDao.getAccountsAssociatedWithJdbcConnection(connectionToRemove);
//        System.out.println(associatedAccounts.size());
	int response = 3356980;
	if (!associatedAccounts.isEmpty()) {
	    String pluralIs = "are", plural = "s", pluralThis = "These", inversePlural = "";
	    if (associatedAccounts.size() == 1) {
		pluralIs = "is";
		plural = "";
		pluralThis = "This";
		inversePlural = "s";
	    }
	    response = JOptionPane.showConfirmDialog(null, String.format(
		    "There %s %d account%s associated with this database server.\n"
		    + "%s account%s will be removed by this operation.\n"
		    + "Do you want to proceed?", pluralIs, associatedAccounts.size(), plural, pluralThis, plural),
		    String.format("Warning: Linked Account%s Exist%s", plural, inversePlural), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    if (response == JOptionPane.OK_OPTION) {
		for (AccountInfo ai : associatedAccounts) {
		    accountInfoDao.removeAccountInfo(ai);
		}
		resetDatabaseUserAccountsTable();
	    }
	}
	if (response == JOptionPane.OK_OPTION || associatedAccounts.isEmpty()) {
	    jdbcConnectionDao.removeDatabaseConnection(connectionToRemove);
	    JOptionPane.showMessageDialog(null, String.format(
		    "You have now removed database connection %s(%s)",
		    connectionToRemove.getDescriptiveName(),
		    connectionToRemove.getJdbcConnectionId()));
	    accountAssociationsTree.setModel(getDatabaseServersTreeModel());
	    adjustAccountAssoiationsTreeAfterEvent();
	    databaseServerComboBox.setModel(getDatabaseServerComboBoxModel());
	}
    }

    private void addPasswordContainerActionInitiated() {
	passwordContainerConfigurationDialog.setVisible(true);
	adjustAccountAssoiationsTreeAfterEvent();
	accountAssociationsTree.setModel(getDatabaseServersTreeModel());
	passwordContainerComboBox.setModel(getPasswordContainerComboBoxModel());
    }

    private void removePasswordContainerActionInitiated() {
	DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent();
	if (!(nodeToRemove.getUserObject() instanceof PasswordContainer)) {
	    nodeToRemove = (DefaultMutableTreeNode) nodeToRemove.getParent();
	}
	PasswordContainer containerToRemove = (PasswordContainer) nodeToRemove.getUserObject();
	List<AccountInfo> associatedAccounts = accountInfoDao.getAccountsAssociatedWithPasswordContainer(containerToRemove);
//        System.out.println(associatedAccounts.size());
	int response = 3356980;
	if (!associatedAccounts.isEmpty()) {
	    String pluralIs = "are", plural = "s", pluralThis = "These", inversePlural = "";
	    if (associatedAccounts.size() == 1) {
		pluralIs = "is";
		plural = "";
		pluralThis = "This";
		inversePlural = "s";
	    }
	    response = JOptionPane.showConfirmDialog(null, String.format(
		    "There %s %d account%s associated with this password container.\n"
		    + "%s account%s will be removed by this operation.\n"
		    + "Do you want to proceed?", pluralIs, associatedAccounts.size(), plural, pluralThis, plural),
		    String.format("Warning: Linked Account%s Exist%s", plural, inversePlural), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    if (response == JOptionPane.OK_OPTION) {
		for (AccountInfo ai : associatedAccounts) {
		    accountInfoDao.removeAccountInfo(ai);
		}
		resetDatabaseUserAccountsTable();
	    }
	}
	if (response == JOptionPane.OK_OPTION || associatedAccounts.isEmpty()) {
	    passwordFileDao.removePasswordContainer(containerToRemove);
	    JOptionPane.showMessageDialog(null, String.format(
		    "You have now removed password container %s(%s)",
		    containerToRemove.getGroupName(),
		    containerToRemove.getDatabaseFileUrl()));
	    accountAssociationsTree.setModel(getDatabaseServersTreeModel());
	    adjustAccountAssoiationsTreeAfterEvent();
	    passwordContainerComboBox.setModel(getPasswordContainerComboBoxModel());
	}
    }

    private void addColdFusionServerActionInitiated() {
	coldFusionServerConfigurationDialog.setVisible(true);
	accountAssociationsTree.setModel(getDatabaseServersTreeModel());
	adjustAccountAssoiationsTreeAfterEvent();
	coldFusionComboBox.setModel(getColdFusionServersComboBoxModel());
    }

    private void removeColdFusionServerActionInitiated() {
	DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent();
	if (!(nodeToRemove.getUserObject() instanceof ColdFusionConfiguration)) {
	    nodeToRemove = (DefaultMutableTreeNode) nodeToRemove.getParent();
	}
	ColdFusionConfiguration serverToRemove = (ColdFusionConfiguration) nodeToRemove.getUserObject();
	List<AccountInfo> associatedAccounts = accountInfoDao.getAccountsAssociatedWithColdFusionServer(serverToRemove);
	//       System.out.println(associatedAccounts.size());
	int response = 3356980;
	if (!associatedAccounts.isEmpty()) {
	    String pluralIs = "are", plural = "s", pluralThis = "These", inversePlural = "";
	    if (associatedAccounts.size() == 1) {
		pluralIs = "is";
		plural = "";
		pluralThis = "This";
		inversePlural = "s";
	    }
	    response = JOptionPane.showConfirmDialog(null, String.format(
		    "There %s %d account%s associated with this ColdFusion server.\n"
		    + "%s account%s will be removed by this operation.\n"
		    + "Do you want to proceed?", pluralIs, associatedAccounts.size(), plural, pluralThis, plural),
		    String.format("Warning: Linked Account%s Exist%s", plural, inversePlural), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    if (response == JOptionPane.OK_OPTION) {
		for (AccountInfo ai : associatedAccounts) {
		    accountInfoDao.removeAccountInfo(ai);
		}
		resetDatabaseUserAccountsTable();
	    }
	}
	if (response == JOptionPane.OK_OPTION || associatedAccounts.isEmpty()) {
	    coldFusionConfigurationDao.removeColdFusionConfiguration(serverToRemove);
	    JOptionPane.showMessageDialog(null, String.format(
		    "You have now removed ColdFusionServer %s(%s)",
		    serverToRemove.getColdFusionServerName(),
		    serverToRemove.getResetCfScriptUrl()));
	    accountAssociationsTree.setModel(getDatabaseServersTreeModel());
	    adjustAccountAssoiationsTreeAfterEvent();
	}
    }

    private void removeAllActionListeners(JButton button) {
	ActionListener[] listeners = button.getActionListeners();
	for (int i = 0; i < listeners.length; i++) {
	    button.removeActionListener(listeners[i]);
	}
    }

    private void showPasswordContainerAssociatedAccountsActionInitiated() {
	DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent();
	PasswordContainer selected = (PasswordContainer) nodeToRemove.getUserObject();
	List<AccountInfo> associatedAccounts = accountInfoDao.getAccountsAssociatedWithPasswordContainer(selected);
	databasePasswordStoresTable.setModel(getCustomAccountListTableModel(associatedAccounts));
	accountsTableTitleLabel.setText("Accounts Associated With Password Container " + selected.getGroupName() + getRowCountString());
	resetSearchAccountsTextField();
	analyzeAccountTableSelectedRows();
    }

    private void showDatabaseServerAssociatedAccountsActionInitiated() {
	DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent();
	JdbcConnection selected = (JdbcConnection) nodeToRemove.getUserObject();
	List<AccountInfo> associatedAccounts = accountInfoDao.getAccountsAssociatedWithJdbcConnection(selected);
	databasePasswordStoresTable.setModel(getCustomAccountListTableModel(associatedAccounts));
	accountsTableTitleLabel.setText("Accounts Associated With Database Server " + selected.getDescriptiveName() + getRowCountString());
	resetSearchAccountsTextField();
	analyzeAccountTableSelectedRows();
    }

    private void showColdFusionServerAssociatedAccountsActionInitiated() {
	DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent();
	ColdFusionConfiguration selected = (ColdFusionConfiguration) nodeToRemove.getUserObject();
	List<AccountInfo> associatedAccounts = accountInfoDao.getAccountsAssociatedWithColdFusionServer(selected);
	databasePasswordStoresTable.setModel(getCustomAccountListTableModel(associatedAccounts));
	accountsTableTitleLabel.setText("Accounts Associated With ColdFusion Server " + selected.getColdFusionServerName() + getRowCountString());
	resetSearchAccountsTextField();
	analyzeAccountTableSelectedRows();
    }

    private void setShowAssociatedAccountsButton(ActionListener actionListener) {
	showAssociatedAccountsButton.setEnabled(true);
	removeAllActionListeners(showAssociatedAccountsButton);
	showAssociatedAccountsButton.addActionListener(actionListener);
    }
// </editor-fold>
// <editor-fold desc="Account Dialog Modifying Methods" defaultstate="collapsed">
    private void setAddDatabaseUserAccountDialogToEditMode(AccountInfo ai) {
	addDatabaseUserAccountDialog.setTitle("Edit Database User Account");
	addDatabaseUserAccountDialog.setIconImage(new javax.swing.ImageIcon(getClass().getResource(EDIT_DATABASE_USER_ACCOUNT_ICON_SMALL)).getImage());
	addDatabaseInstructionsLabel.setText("Fill in the following fields to modify this account:");
	addDatabaseUserAccountButton.setText("Apply Changes");
	addDatabaseUserAccountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(CONFIRM_DATABASE_USER_ACCOUNT_CHANGES_ICON)));
	removeAllActionListeners(addDatabaseUserAccountButton);
	addDatabaseUserAccountButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		runEditDatabaseUserActions();
	    }
	});
	accountUsernameTextField.setText(ai.getUserName());
	accountUsernameTextField.setEditable(false);
	char[] decryptedPassword = PasswordUtils.decrypt(
		encryptedPasswordDao.getEncryptedPasswordForUser(
		ai.getUserName(),
		ai.getPasswordContainer().getDatabaseFileUrl()).getEncryptedPassword(),
		ai.getUserName(), keyDao.getKeyForUser(ai.getUserName()));
	accountPasswordField.setText(new String(decryptedPassword));
	confirmAccountPasswordField.setText(new String(decryptedPassword));
	databaseServerComboBox.setSelectedItem(ai.getJdbcConnection());
	passwordContainerComboBox.setSelectedItem(ai.getPasswordContainer());
	if (ai.getColdFusionConfiguration() != null) {
	    coldFusionIntegrationCheckBox.setSelected(true);
	    coldFusionConfigurationButton.setEnabled(true);
	    ColdFusionConfiguration thisConfiguration = ai.getColdFusionConfiguration();
	    coldFusionComboBox.setSelectedItem(thisConfiguration);
	    coldFusionDatabaseNameTextField.setText(ai.getDatabaseName());
	    coldFusionDsnNameTextField.setText(ai.getDsnName());

	    URI uri = URI.create(ai.getJdbcConnection().getJdbcUrl().substring(5));

	    coldFusionDatabaseHostUrlTextField.setText(uri.getHost());
	    coldFusionDatabasePortTextField.setText(uri.getPort() + "");
	    coldFusionDatabaseUsernameTextField.setText(ai.getUserName());

	}
    }

    private void unsetAddDatabaseUserAccountDialogToEditMode() {
	addDatabaseUserAccountDialog.setTitle("Add Database User Account");
	addDatabaseUserAccountDialog.setIconImage(new javax.swing.ImageIcon(getClass().getResource(ADD_DATABASE_USER_ACCOUNT_SMALL)).getImage());
	addDatabaseInstructionsLabel.setText("Fill in the following fields to add a new database user account:");
	addDatabaseUserAccountButton.setText("Add Database User Accunt");
	addDatabaseUserAccountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(ADD_DATABASE_USER_ACCOUNT_ICON)));
	addDatabaseUserAccountButton.setEnabled(false);
	removeAllActionListeners(addDatabaseUserAccountButton);
	addDatabaseUserAccountButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		runAddDatabaseUserActions();
	    }
	});
	accountUsernameTextField.setText(null);
	accountUsernameTextField.setEditable(true);

	accountPasswordField.setText(null);
	confirmAccountPasswordField.setText(null);
	coldFusionConfigurationButton.setEnabled(false);
	coldFusionIntegrationCheckBox.setSelected(false);
	coldFusionDatabaseNameTextField.setText(null);
	coldFusionDsnNameTextField.setText(null);
	coldFusionDatabaseHostUrlTextField.setText(null);
	coldFusionDatabasePortTextField.setText(null);
	coldFusionDatabaseUsernameTextField.setText(null);

    }

    private void runAddDatabaseUserActions() {
	String userAdded = runDatabaseUserActions(false);
	JOptionPane.showMessageDialog(null, String.format("Added new database user account for %s", userAdded), "New Account Added", JOptionPane.INFORMATION_MESSAGE);
    }

    private void runEditDatabaseUserActions() {
	runDatabaseUserActions(true);
    }

    private String runDatabaseUserActions(boolean isEdit) {
	String userAdded = accountUsernameTextField.getText();
	submitAccount(isEdit);
	clearDatabaseUserAccountDialogFields();
	resetDatabaseUserAccountsTable();
	addDatabaseUserAccountDialog.setVisible(false);
	return userAdded;
    }

// </editor-fold>
// <editor-fold desc="TreeModel Generating Methods" defaultstate="collapsed">
    private TreeModel getDatabaseServersTreeModel() {
	List<JdbcConnection> connections = jdbcConnectionDao.getAllJdbcConnections();
	DefaultMutableTreeNode rootDs = new DefaultMutableTreeNode(
		DATABASE_SERVERS_NODE_LABEL);
	for (JdbcConnection connection : connections) {
	    DefaultMutableTreeNode newDatabaseNode = new DefaultMutableTreeNode(connection);
	    rootDs.add(newDatabaseNode);
	}

	List<PasswordContainer> passwordFiles = passwordFileDao.getAllPasswordContainers();
	DefaultMutableTreeNode rootPf = new DefaultMutableTreeNode(PASSWORD_CONTAINERS_NODE_LABEL);
	for (PasswordContainer pf : passwordFiles) {
	    DefaultMutableTreeNode passwordFileNode = new DefaultMutableTreeNode(pf);
	    rootPf.add(passwordFileNode);
	}

	List<ColdFusionConfiguration> cFCs = coldFusionConfigurationDao.getAllColdFusionConfigurations();
	DefaultMutableTreeNode rootCf = new DefaultMutableTreeNode(COLDFUSION_SERVERS_NODE_LABEL);
	for (ColdFusionConfiguration cFC : cFCs) {
	    DefaultMutableTreeNode newCfcNode = new DefaultMutableTreeNode(cFC);
	    rootCf.add(newCfcNode);
	}

	DefaultMutableTreeNode topRoot = new DefaultMutableTreeNode();
	topRoot.add(rootDs);
	topRoot.add(rootPf);
	topRoot.add(rootCf);
	return new DefaultTreeModel(topRoot);
    }

    private TreeModel getHelpTreeModel() {
	DefaultMutableTreeNode topRoot = new DefaultMutableTreeNode();
	topRoot.add(HelpTreeSelectionType.GETTING_STARTED_NODE.getNode());

	DefaultMutableTreeNode databaseServersHelpNode = HelpTreeSelectionType.CONFIGURING_DATABASE_SERVERS_NODE.getNode();
	databaseServersHelpNode.add(HelpTreeSelectionType.DATABASE_SERVERS_OVERVIEW_NODE.getNode());
	databaseServersHelpNode.add(HelpTreeSelectionType.ADDING_DATABASE_SERVERS_NODE.getNode());
	databaseServersHelpNode.add(HelpTreeSelectionType.EDITING_DATABASE_SERVERS_NODE.getNode());
	databaseServersHelpNode.add(HelpTreeSelectionType.REMOVING_DATABASE_SERVERS_NODE.getNode());
	topRoot.add(databaseServersHelpNode);

	DefaultMutableTreeNode passwordContainersNode = HelpTreeSelectionType.CONFIGURING_PASSWORD_CONTAINERS_NODE.getNode();
	passwordContainersNode.add(HelpTreeSelectionType.PASSWORD_CONTAINERS_OVERVIEW_NODE.getNode());
	passwordContainersNode.add(HelpTreeSelectionType.ADDING_PASSWORD_CONTAINERS_NODE.getNode());
	passwordContainersNode.add(HelpTreeSelectionType.EDITING_PASSWORD_CONTAINERS_NODE.getNode());
	passwordContainersNode.add(HelpTreeSelectionType.REMOVING_PASSWORD_CONTAINERS_NODE.getNode());
	topRoot.add(passwordContainersNode);

	DefaultMutableTreeNode coldFusionServersNode = HelpTreeSelectionType.CONFIGURING_COLDFUSION_SERVERS_NODE.getNode();
	coldFusionServersNode.add(HelpTreeSelectionType.COLDFUSION_INTEGRATION_OVERVIEW_NODE.getNode());
	coldFusionServersNode.add(HelpTreeSelectionType.ADDING_COLDFUSION_SERVERS_NODE.getNode());
	coldFusionServersNode.add(HelpTreeSelectionType.EDITING_COLDFUSION_SERVERS_NODE.getNode());
	coldFusionServersNode.add(HelpTreeSelectionType.REMOVING_COLDFUSION_SERVERS_NODE.getNode());
	topRoot.add(coldFusionServersNode);

	DefaultMutableTreeNode passwordGenerationNode = HelpTreeSelectionType.PASSWORD_GENERATION_NODE.getNode();
	passwordGenerationNode.add(HelpTreeSelectionType.PASSWORD_GENERATION_OVERVIEW_NODE.getNode());
	passwordGenerationNode.add(HelpTreeSelectionType.MODIFYING_PASSWORD_STRENGTH_CRITERIA_NODE.getNode());
	topRoot.add(passwordGenerationNode);

	DefaultMutableTreeNode accountsNode = HelpTreeSelectionType.CONFIGURING_DATABASE_USER_ACCOUNTS_NODE.getNode();
	accountsNode.add(HelpTreeSelectionType.DATABASE_USER_ACCOUNTS_INTRO_NODE.getNode());
	accountsNode.add(HelpTreeSelectionType.ADDING_DATABASE_USER_ACCOUNTS_NODE.getNode());
	accountsNode.add(HelpTreeSelectionType.EDITING_DATABASE_USER_ACCOUNTS_NODE.getNode());
	accountsNode.add(HelpTreeSelectionType.REMOVING_DATABASE_USER_ACCOUNTS_NODE.getNode());
	topRoot.add(accountsNode);

	return new DefaultTreeModel(topRoot);
    }

// </editor-fold>
// <editor-fold desc="TreeCellRenderer Generating Methods" defaultstate="collapsed">
    private TreeCellRenderer getDatabaseServersTreeCellRenderer() {
	return new DefaultTreeCellRenderer() {
	    @Override
	    public Component getTreeCellRendererComponent(
		    JTree tree,
		    Object value,
		    boolean sel,
		    boolean expanded,
		    boolean leaf,
		    int row,
		    boolean hasFocus) {

		super.getTreeCellRendererComponent(
			tree, value, sel,
			expanded, leaf, row,
			hasFocus);
		if (row > -1) {
		    switch (AssociationTreeSelectionType.getSelectionTypeFromNode(value)) {
			case DATABASE_SERVERS_NODE:
			    setIcon(new ImageIcon(getClass().getResource(DATABASE_SERVERS_ICON_IMAGE_PATH)));
			    setToolTipText(DATABASE_SERVERS_TOOL_TIP_TEXT);
			    break;
			case PASSWORD_CONTAINERS_NODE:
			    setIcon(new ImageIcon(getClass().getResource(PASSWORD_CONTAINERS_ICON_IMAGE_PATH)));
			    setToolTipText(PASSWORD_CONTAINERS_TOOL_TIP_TEXT);
			    break;
			case COLDFUSION_SERVERS_NODE:
			    setIcon(new ImageIcon(getClass().getResource(COLDFUSION_SERVERS_ICON_IMAGE_PATH)));
			    setToolTipText(COLDFUSION_SERVERS_TOOL_TIP_TEXT);
			    break;
			case DATABASE_SERVER_NODE:
			    setIcon(new ImageIcon(getClass().getResource(DATABASE_ICON_IMAGE_PATH)));
			    setToolTipText(DATABASE_SERVER_TOOL_TIP_TEXT);
			    break;
			case PASSWORD_CONTAINER_NODE:
			    setIcon(new ImageIcon(getClass().getResource(PASSWORD_CONTAINER_ICON_IMAGE_PATH)));
			    setToolTipText(PASSWORD_CONTAINER_TOOL_TIP_TEXT);
			    break;
			case COLDFUSION_SERVER_NODE:
			    setIcon(new ImageIcon(getClass().getResource(COLDFUSION_SERVER_ICON_IMAGE_PATH)));
			    setToolTipText(COLDFUSION_SERVER_TOOL_TIP_TEXT);
			    break;
			default:
			    break;

		    }
		}
		return this;
	    }
	};
    }

    private TreeCellRenderer getHelpTreeCellRenderer() {
	return new DefaultTreeCellRenderer() {
	    @Override
	    public Component getTreeCellRendererComponent(
		    JTree tree,
		    Object value,
		    boolean sel,
		    boolean expanded,
		    boolean leaf,
		    int row,
		    boolean hasFocus) {

		super.getTreeCellRendererComponent(
			tree, value, sel,
			expanded, leaf, row,
			hasFocus);
		if (row > -1) {
		    switch (AssociationTreeSelectionType.getSelectionTypeFromNode(value)) {
			case DATABASE_SERVERS_NODE:
			    setIcon(new ImageIcon(getClass().getResource(DATABASE_SERVERS_ICON_IMAGE_PATH)));
			    setToolTipText(DATABASE_SERVERS_TOOL_TIP_TEXT);
			    break;
			case PASSWORD_CONTAINERS_NODE:
			    setIcon(new ImageIcon(getClass().getResource(PASSWORD_CONTAINERS_ICON_IMAGE_PATH)));
			    setToolTipText(PASSWORD_CONTAINERS_TOOL_TIP_TEXT);
			    break;
			case COLDFUSION_SERVERS_NODE:
			    setIcon(new ImageIcon(getClass().getResource(COLDFUSION_SERVERS_ICON_IMAGE_PATH)));
			    setToolTipText(COLDFUSION_SERVERS_TOOL_TIP_TEXT);
			    break;
			case DATABASE_SERVER_NODE:
			    setIcon(new ImageIcon(getClass().getResource(DATABASE_ICON_IMAGE_PATH)));
			    setToolTipText(DATABASE_SERVER_TOOL_TIP_TEXT);
			    break;
			case PASSWORD_CONTAINER_NODE:
			    setIcon(new ImageIcon(getClass().getResource(PASSWORD_CONTAINER_ICON_IMAGE_PATH)));
			    setToolTipText(PASSWORD_CONTAINER_TOOL_TIP_TEXT);
			    break;
			case COLDFUSION_SERVER_NODE:
			    setIcon(new ImageIcon(getClass().getResource(COLDFUSION_SERVER_ICON_IMAGE_PATH)));
			    setToolTipText(COLDFUSION_SERVER_TOOL_TIP_TEXT);
			    break;
			default:
			    break;

		    }
		}
		return this;
	    }
	};
    }
// </editor-fold>
// <editor-fold desc="TableModel Generating Methods" defaultstate="collapsed">
    private TableModel getCustomAccountListTableModel(List<AccountInfo> accounts) {
	return getAccountListTableModel(accounts);
    }

    private TableModel getFullAccountListTableModel() {
	return getAccountListTableModel(accountInfoDao.getAllAccountInfos());
    }

    private TableModel getAccountListTableModel(final List<AccountInfo> allAccountInfos) {
	return new AbstractTableModel() {
	    String[] columnNames = {"Username", "Database Server", "Password Container", "ColdFusion Server"};

	    @Override
	    public int getRowCount() {
		return allAccountInfos.size();
	    }

	    public String getColumnName(int col) {
		return columnNames[col];
	    }

	    @Override
	    public int getColumnCount() {
		return 4;
	    }

	    @Override
	    public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		    case 0:
			return allAccountInfos.get(rowIndex).getUserName();
		    case 1:
			return allAccountInfos.get(rowIndex).getJdbcConnection();
		    case 2:
			return allAccountInfos.get(rowIndex).getPasswordContainer();
		    case 3:
			return allAccountInfos.get(rowIndex).getColdFusionConfiguration();
		}
		return null;
	    }

	    public AccountInfo getAccountInfoForRow(int rowIndex) {
		return allAccountInfos.get(rowIndex);
	    }
	};
    }

    private TableModel getAssociationDetailsTableModelForDatabaseServer() {
	final JdbcConnection selected = (JdbcConnection) ((DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent()).getUserObject();
	return new AbstractTableModel() {
	    String[] columnNames = {"Property", "Value"};
	    String[][] data = {{"Name", selected.getDescriptiveName()},
		{"JDBC URL", selected.getJdbcUrl()},
		{"JDBC Driver Class", selected.getJdbcDriverClass()}};

	    @Override
	    public int getRowCount() {
		return data.length;
	    }

	    @Override
	    public String getColumnName(int col) {
		return columnNames[col];
	    }

	    @Override
	    public int getColumnCount() {
		return columnNames.length;
	    }

	    @Override
	    public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	    }

	    @Override
	    public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,             
		//no matter where the cell appears onscreen.             
		if (col < 1) {
		    return false;
		} else {
		    return true;
		}
	    }

	    @Override
	    public void setValueAt(Object value, int row, int col) {
		switch (row) {
		    case 0:
			selected.setDescriptiveName(value.toString());
			break;
		    case 1:
			selected.setJdbcUrl(value.toString());
			break;
		    case 2:
			selected.setJdbcDriverClass(value.toString());
			break;
		}
		applyAssociationDetailsChangeButton.setEnabled(false);
		jdbcConnectionDao.addExistingJdbcConnection(selected);
		data[row][col] = value.toString();
		fireTableCellUpdated(row, col);
		accountAssociationsTree.setModel(getDatabaseServersTreeModel());
		resetDatabaseUserAccountsTable();
	    }
	};
    }

    private TableModel getAssociationDetailsTableModelForPasswordContainer() {
	return new AbstractTableModel() {
	    PasswordContainer selected = (PasswordContainer) ((DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent()).getUserObject();
	    String[] columnNames = {"Property", "Value"};
	    String[][] data = {{"Name", selected.getGroupName()}, {"File Path", selected.getDatabaseFileUrl()}};

	    @Override
	    public int getRowCount() {
		return data.length;
	    }

	    @Override
	    public String getColumnName(int col) {
		return columnNames[col];
	    }

	    @Override
	    public int getColumnCount() {
		return columnNames.length;
	    }

	    @Override
	    public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	    }

	    @Override
	    public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,             
		//no matter where the cell appears onscreen.             
		if (col < 1) {
		    return false;
		} else {
		    return true;
		}
	    }

	    @Override
	    public void setValueAt(Object value, int row, int col) {
		switch (row) {
		    case 0:
			selected.setGroupName(value.toString());
			break;
		    case 1:
			selected.setDatabaseFileUrl(value.toString());
			break;
		}
		applyAssociationDetailsChangeButton.setEnabled(false);
		passwordFileDao.setExistingPasswordContainer(selected);
		data[row][col] = value.toString();
		fireTableCellUpdated(row, col);
		accountAssociationsTree.setModel(getDatabaseServersTreeModel());
		resetDatabaseUserAccountsTable();
	    }
	};
    }

    private TableModel getAssociationDetailsTableModelForColdFusionServer() {
	return new AbstractTableModel() {
	    ColdFusionConfiguration selected = (ColdFusionConfiguration) ((DefaultMutableTreeNode) accountAssociationsTree.getLastSelectedPathComponent()).getUserObject();
	    String[] columnNames = {"Property", "Value"};
	    String[][] data = {{"Name", selected.getColdFusionServerName()},
		{"CF Admin Username", selected.getCfAdminUsername()},
		{"CF Admin Password", selected.getCfAdminPassword()},
		{"CF Script URL", selected.getResetCfScriptUrl()}};

	    @Override
	    public int getRowCount() {
		return data.length;
	    }

	    @Override
	    public String getColumnName(int col) {
		return columnNames[col];
	    }

	    @Override
	    public int getColumnCount() {
		return columnNames.length;
	    }

	    @Override
	    public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	    }

	    @Override
	    public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,             
		//no matter where the cell appears onscreen.             
		if (col < 1) {
		    return false;
		} else {
		    return true;
		}
	    }

	    @Override
	    public void setValueAt(Object value, int row, int col) {
		switch (row) {
		    case 0:
			selected.setColdFusionServerName(value.toString());
			break;
		    case 1:
			selected.setCfAdminUsername(value.toString());
			break;
		    case 2:
			selected.setCfAdminPassword(value.toString());
			break;
		    case 3:
			selected.setResetCfScriptUrl(value.toString());
			break;
		}
		applyAssociationDetailsChangeButton.setEnabled(false);
		coldFusionConfigurationDao.setExistingColdFusionConfiguration(selected);
		data[row][col] = value.toString();
		fireTableCellUpdated(row, col);
		accountAssociationsTree.setModel(getDatabaseServersTreeModel());
		resetDatabaseUserAccountsTable();
	    }
	};
    }

    private DefaultTableModel getImportAccountsTableModel(final List<String> importAccountsUsersList, final JdbcConnection importAccountsDatabaseServerConnection) {

	return new DefaultTableModel() {
	    final int numAccounts = importAccountsUsersList.size();
	    JComboBox[] passwordContainerSelections = new JComboBox[numAccounts];
	    String[] columnNames = {"Import", "Username", "Password", "Password Container", "Status"};
	    Class[] columnClasses = {Boolean.class, String.class, String.class, String.class, String.class};
	    Object[][] data = initilizeDataArray();

	    @Override
	    public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	    }

	    @Override
	    public int getRowCount() {
		return numAccounts;
	    }

	    @Override
	    public int getColumnCount() {
		return columnNames.length;
	    }

	    @Override
	    public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	    }

	    private Object[][] initilizeDataArray() {
		Object[][] dataInitilized = new Object[numAccounts][5];
		for (int i = 0; i < numAccounts; i++) {
		    AccountInfo account = accountInfoDao.getAccountInfoForUser(importAccountsUsersList.get(i));
		    if (account != null) {
			if (!account.getJdbcConnection().equals(importAccountsDatabaseServerConnection)) {
			    account = null;
			}
		    }
		    dataInitilized[i][0] = getSelectionBox(account);
		    dataInitilized[i][1] = importAccountsUsersList.get(i);
		    dataInitilized[i][2] = "";
		    dataInitilized[i][3] = getPasswordContainer(account);
		    dataInitilized[i][4] = getImportStatusString(account);
		}
		return dataInitilized;
	    }

	    private String getImportStatusString(AccountInfo account) {
		if (account != null) {
		    return "Imported";
		} else {
		    return "Not Selected";
		}
	    }

	    @Override
	    public Class getColumnClass(int c) {
		return columnClasses[c];
	    }

	    private String getPasswordContainer(AccountInfo account) {
		if (account != null) {
		    return account.getPasswordContainer().getGroupName();
		}
		return "";
	    }

	    private Boolean getSelectionBox(AccountInfo account) {
		if (account != null) {
		    return true;
		} else {
		    return false;
		}
	    }

	    @Override
	    public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,             
		//no matter where the cell appears onscreen.             
		switch (col) {
		    case 0:
			return true;
		    case 2:
			return true;
		    case 3:
			return true;
		    default:
			return false;
		}
	    }

	    @Override
	    public void setValueAt(Object value, int row, int col) {
		switch (col) {
		    case 0:
			data[row][col] = value;
			adjustStatusString(row);
			break;
		    case 2:
			data[row][col] = value;
			adjustStatusString(row);
			break;
		    case 3:
			data[row][col] = value;
			adjustStatusString(row);
			JOptionPane.showMessageDialog(null, value);
			break;
		    case 4:
			data[row][col] = value;
			fireTableCellUpdated(row, col);
			break;
		}
	    }

	    private void adjustStatusString(int row) {
		Boolean rowSelected = (Boolean) data[row][0];
		if (rowSelected) {
		    if (((String) data[row][2]).isEmpty()) {
			setValueAt("Waiting For Password", row, 4);
		    } else if (((String) data[row][3]).isEmpty()) {
			setValueAt("Select Password Container", row, 4);
		    } else {
			if (JdbcDatabaseUtil.checkConnection(importAccountsDatabaseServerConnection.getJdbcDriverClass(), importAccountsDatabaseServerConnection.getJdbcUrl(), (String) getValueAt(row, 1), (String) getValueAt(row, 2))) {
			    setValueAt("Ready To Import", row, 4);
			} else {
			    setValueAt("Unable To Login", row, 4);
			}
		    }
		} else {
		    setValueAt("Not Selected", row, 4);
		}

	    }
	};
    }
// </editor-fold>
//<editor-fold desc="ComboBoxModel Generating Methods" defaultstate="collapsed">
    private ComboBoxModel getDatabaseServerComboBoxModel() {
	return new ComboBoxModel() {
	    private JdbcConnectionDao jdbcConnectionDao = new JdbcConnectionDao();
	    private List<JdbcConnection> connections = jdbcConnectionDao.getAllJdbcConnections();
	    private JdbcConnection selectedItem;

	    @Override
	    public void setSelectedItem(Object selectedConnection) {
		this.selectedItem = (JdbcConnection) selectedConnection;
	    }

	    @Override
	    public Object getSelectedItem() {
		return selectedItem;
	    }

	    @Override
	    public int getSize() {
		return connections.size();
	    }

	    @Override
	    public Object getElementAt(int index) {
		return connections.get(index);
	    }

	    @Override
	    public void addListDataListener(ListDataListener l) {
		//
	    }

	    @Override
	    public void removeListDataListener(ListDataListener l) {
		//
	    }
	};
    }

    private ComboBoxModel getPasswordContainerComboBoxModel() {
	return new ComboBoxModel() {
            PasswordContainerDao passwordContainerDao = new PasswordContainerDao();
            List<PasswordContainer> passwordContainers = passwordContainerDao.getAllPasswordContainers();
	    private PasswordContainer selectedGroup;

	    @Override
	    public int getSize() {
		return passwordContainers.size();
	    }

	    @Override
	    public Object getElementAt(int index) {
		return passwordContainers.get(index);
	    }

	    @Override
	    public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
	    }

	    @Override
	    public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
	    }

	    @Override
	    public void setSelectedItem(Object anItem) {
		selectedGroup = (PasswordContainer) anItem;
	    }

	    @Override
	    public Object getSelectedItem() {
		return selectedGroup;
	    }
	};
    }

    private ComboBoxModel getColdFusionServersComboBoxModel() {

	return new ComboBoxModel() {
	    List<ColdFusionConfiguration> coldFusionServers = coldFusionConfigurationDao.getAllColdFusionConfigurations();
	    ColdFusionConfiguration selectedServer;

	    @Override
	    public void setSelectedItem(Object selected) {
		if (selected instanceof ColdFusionConfiguration) {
		    this.selectedServer = (ColdFusionConfiguration) selected;
		}
	    }

	    @Override
	    public Object getSelectedItem() {
		return selectedServer;
	    }

	    @Override
	    public int getSize() {
		return coldFusionServers.size();
	    }

	    @Override
	    public Object getElementAt(int index) {
		return coldFusionServers.get(index);
	    }

	    @Override
	    public void addListDataListener(ListDataListener l) {
		//
	    }

	    @Override
	    public void removeListDataListener(ListDataListener l) {
		//
	    }
	};
    }
// </editor-fold>
// <editor-fold desc="GUI Required Inner-Classes" defaultstate="collapsed">
    /**
     * Class ClearPasswordTimerTask is a Java Timer task that simply clears the
     * view password display after 5 seconds.
     *
     * @author vcagle
     *
     */
    public class ClearPasswordTimerTask extends TimerTask {

	@Override
	public void run() {
	    passwordDisplayTextField.setText("");
	}
    }

    public enum AssociationTreeSelectionType {

	DATABASE_SERVERS_NODE(0),
	DATABASE_SERVER_NODE(1),
	PASSWORD_CONTAINERS_NODE(2),
	PASSWORD_CONTAINER_NODE(3),
	COLDFUSION_SERVERS_NODE(4),
	COLDFUSION_SERVER_NODE(5),
	NONE(6);
	private int selectionCode;

	public int getSelectionCode() {
	    return selectionCode;
	}

	public void setSelectionCode(int selectionCode) {
	    this.selectionCode = selectionCode;
	}

	AssociationTreeSelectionType(int type) {
	    this.selectionCode = type;
	}

	public static AssociationTreeSelectionType getSelectionTypeFromNode(Object treeNodeObject) {
	    if (treeNodeObject instanceof DefaultMutableTreeNode) {
		DefaultMutableTreeNode thisNode = (DefaultMutableTreeNode) treeNodeObject;
		if (DATABASE_SERVERS_NODE_LABEL.equals(thisNode.getUserObject())) {
		    return DATABASE_SERVERS_NODE;
		    // thisNode.
		} else if (COLDFUSION_SERVERS_NODE_LABEL.equals(thisNode.getUserObject())) {
		    return COLDFUSION_SERVERS_NODE;
		} else if (PASSWORD_CONTAINERS_NODE_LABEL.equals(thisNode.getUserObject())) {
		    return PASSWORD_CONTAINERS_NODE;
		} else if (thisNode.getUserObject() instanceof JdbcConnection) {
		    return DATABASE_SERVER_NODE;
		} else if (thisNode.getUserObject() instanceof PasswordContainer) {
		    return PASSWORD_CONTAINER_NODE;
		} else if (thisNode.getUserObject() instanceof ColdFusionConfiguration) {
		    return COLDFUSION_SERVER_NODE;
		}
	    }
	    return NONE;
	}
    }

    public enum HelpTreeSelectionType {

	GETTING_STARTED_NODE(1, new DefaultMutableTreeNode(GETTING_STARTED_HELP_TITLE), "<h1>" + GETTING_STARTED_HELP_TITLE + "</h1>" + GETTING_STARTED_HELP_HTML_BODY),
	CONFIGURING_DATABASE_SERVERS_NODE(2, new DefaultMutableTreeNode(DATABASE_SERVERS_HELP_ROOT_TITLE), "<h1>" + DATABASE_SERVERS_HELP_ROOT_TITLE + "</h1>" + DATABASE_SERVERS_HELP_ROOT_HTML_BODY),
	DATABASE_SERVERS_OVERVIEW_NODE(3, new DefaultMutableTreeNode(DATABASE_SERVERS_HELP_INTRO_TITLE), "<h1>" + DATABASE_SERVERS_HELP_INTRO_TITLE + "</h1>" + DATABASE_SERVERS_HELP_INTRO_HTML_BODY),
	ADDING_DATABASE_SERVERS_NODE(4, new DefaultMutableTreeNode(ADDING_DATABASE_SERVERS_HELP_TITLE), "<h1>" + ADDING_DATABASE_SERVERS_HELP_TITLE + "</h1>" + ADDING_DATABASE_SERVERS_HELP_HTML_BODY),
	EDITING_DATABASE_SERVERS_NODE(5, new DefaultMutableTreeNode(EDITING_DATABASE_SERVERS_HELP_TITLE), "<h1>" + EDITING_DATABASE_SERVERS_HELP_TITLE + "</h1>" + EDITING_DATABASE_SERVERS_HELP_HTML_BODY),
	REMOVING_DATABASE_SERVERS_NODE(6, new DefaultMutableTreeNode(REMOVING_DATABASE_SERVERS_HELP_TITLE), "<h1>" + REMOVING_DATABASE_SERVERS_HELP_TITLE + "</h1>" + REMOVING_DATABASE_SERVERS_HELP_HTML_BODY),
	CONFIGURING_PASSWORD_CONTAINERS_NODE(7, new DefaultMutableTreeNode(PASSWORD_CONTAINERS_HELP_ROOT_TITLE), "<h1>" + PASSWORD_CONTAINERS_HELP_ROOT_TITLE + "</h1>" + PASSWORD_CONTAINERS_HELP_ROOT_HTML_BODY),
	PASSWORD_CONTAINERS_OVERVIEW_NODE(8, new DefaultMutableTreeNode(PASSWORD_CONTAINERS_HELP_INTRO_TITLE), "<h1>" + PASSWORD_CONTAINERS_HELP_INTRO_TITLE + "</h1>" + PASSWORD_CONTAINERS_HELP_INTRO_HTML_BODY),
	ADDING_PASSWORD_CONTAINERS_NODE(9, new DefaultMutableTreeNode(ADDING_PASSWORD_CONTAINERS_HELP_TITLE), "<h1>" + ADDING_PASSWORD_CONTAINERS_HELP_TITLE + "</h1>" + ADDING_PASSWORD_CONTAINERS_HELP_HTML_BODY),
	EDITING_PASSWORD_CONTAINERS_NODE(10, new DefaultMutableTreeNode(EDITING_PASSWORD_CONTAINERS_HELP_TITLE), "<h1>" + EDITING_PASSWORD_CONTAINERS_HELP_TITLE + "</h1>" + EDITING_PASSWORD_CONTAINERS_HELP_HTML_BODY),
	REMOVING_PASSWORD_CONTAINERS_NODE(11, new DefaultMutableTreeNode(REMOVING_PASSWORD_CONTAINERS_HELP_TITLE), "<h1>" + REMOVING_PASSWORD_CONTAINERS_HELP_TITLE + "</h1>" + REMOVING_PASSWORD_CONTAINERS_HELP_HTML_BODY),
	CONFIGURING_COLDFUSION_SERVERS_NODE(12, new DefaultMutableTreeNode(COLDFUSION_INTEGRATION_HELP_ROOT_TITLE), "<h1>" + COLDFUSION_INTEGRATION_HELP_ROOT_TITLE + "</h1>" + COLDFUSION_SERVERS_HELP_ROOT_HTML_BODY),
	COLDFUSION_INTEGRATION_OVERVIEW_NODE(13, new DefaultMutableTreeNode(COLDFUSION_SERVERS_HELP_INTRO_TITLE), "<h1>" + COLDFUSION_SERVERS_HELP_INTRO_TITLE + "</h1>" + COLDFUSION_SERVERS_HELP_INTRO_HTML_BODY),
	ADDING_COLDFUSION_SERVERS_NODE(14, new DefaultMutableTreeNode(ADDING_COLDFUSION_SERVERS_HELP_TITLE), "<h1>" + ADDING_COLDFUSION_SERVERS_HELP_TITLE + "</h1>" + ADDING_COLDFUSION_SERVERS_HELP_HTML_BODY),
	EDITING_COLDFUSION_SERVERS_NODE(15, new DefaultMutableTreeNode(EDITING_COLDFUSION_SERVERS_HELP_TITLE), "<h1>" + EDITING_COLDFUSION_SERVERS_HELP_TITLE + "</h1>" + EDITING_COLDFUSION_SERVERS_HELP_HTML_BODY),
	REMOVING_COLDFUSION_SERVERS_NODE(16, new DefaultMutableTreeNode(REMOVING_COLDFUSION_SERVERS_HELP_TITLE), "<h1>" + REMOVING_COLDFUSION_SERVERS_HELP_TITLE + "</h1>" + REMOVING_COLDFUSION_SERVERS_HELP_HTML_BODY),
	PASSWORD_GENERATION_NODE(17, new DefaultMutableTreeNode(PASSWORD_GENERATION_HELP_ROOT_TITLE), "<h1>" + PASSWORD_GENERATION_HELP_ROOT_TITLE + "</h1>" + GENERATING_PASSWORDS_HELP_ROOT_HTML_BODY),
	PASSWORD_GENERATION_OVERVIEW_NODE(18, new DefaultMutableTreeNode(GENERATING_PASSWORDS_INTRO_TITLE), "<h1>" + GENERATING_PASSWORDS_INTRO_TITLE + "</h1>" + GENERATING_PASSWORDS_HELP_INTRO_HTML_BODY),
	MODIFYING_PASSWORD_STRENGTH_CRITERIA_NODE(19, new DefaultMutableTreeNode(MODIFYING_PASSWORD_STRENGTH_HELP_TITLE), "<h1>" + MODIFYING_PASSWORD_STRENGTH_HELP_TITLE + "</h1>" + MODIFYING_PASSWORD_STRENGTH_CRITERIA_HELP_HTML_BODY),
	CONFIGURING_DATABASE_USER_ACCOUNTS_NODE(20, new DefaultMutableTreeNode(DATABASE_USER_ACCOUNTS_HELP_ROOT_TITLE), "<h1>" + DATABASE_USER_ACCOUNTS_HELP_ROOT_TITLE + "</h1>" + DATABASE_USER_ACCOUNTS_HELP_ROOT_HTML_BODY),
	DATABASE_USER_ACCOUNTS_INTRO_NODE(21, new DefaultMutableTreeNode(DATABASE_USER_ACCOUNTS_HELP_INTRO_TITLE), "<h1>" + DATABASE_USER_ACCOUNTS_HELP_INTRO_TITLE + "</h1>" + DATABASE_USER_ACCOUNTS_HELP_INTRO_HTML_BODY),
	ADDING_DATABASE_USER_ACCOUNTS_NODE(22, new DefaultMutableTreeNode(ADDING_DATABASE_USER_ACCOUNTS_HELP_TITLE), "<h1>" + ADDING_DATABASE_USER_ACCOUNTS_HELP_TITLE + "</h1>" + ADDING_DATABASE_USER_ACCOUNTS_HELP_HTML_BODY),
	EDITING_DATABASE_USER_ACCOUNTS_NODE(23, new DefaultMutableTreeNode(EDITING_DATABASE_USER_ACCOUNTS_HELP_TITLE), "<h1>" + EDITING_DATABASE_USER_ACCOUNTS_HELP_TITLE + "</h1>" + EDITING_DATABASE_USER_ACCOUNTS_HELP_HTML_BODY),
	REMOVING_DATABASE_USER_ACCOUNTS_NODE(24, new DefaultMutableTreeNode(REMOVING_DATABASE_USER_ACCOUNTS_HELP_TITLE), "<h1>" + REMOVING_DATABASE_USER_ACCOUNTS_HELP_TITLE + "</h1>" + REMOVING_DATABASE_USER_ACCOUNTS_HELP_HTML_BODY);
	private int selectionCode;
	private DefaultMutableTreeNode node;
	private String htmlHelpContent;

	public String getHtmlHelpContent() {
	    return htmlHelpContent;
	}

	public void setHtmlHelpContent(String htmlHelpContent) {
	    this.htmlHelpContent = htmlHelpContent;
	}

	public DefaultMutableTreeNode getNode() {
	    return node;
	}

	public void setNode(DefaultMutableTreeNode node) {
	    this.node = node;
	}

	public int getSelectionCode() {
	    return selectionCode;
	}

	public void setSelectionCode(int selectionCode) {
	    this.selectionCode = selectionCode;
	}

	HelpTreeSelectionType(int selection, DefaultMutableTreeNode node, String htmlHelpContent) {
	    this.selectionCode = selection;
	    this.node = node;
	    this.htmlHelpContent = htmlHelpContent;
	}

	public static String getHtmlContentFromNode(DefaultMutableTreeNode node) {
	    for (HelpTreeSelectionType type : HelpTreeSelectionType.values()) {
		if (type.getNode().equals(node)) {
		    return type.getHtmlHelpContent();
		}
	    }
	    return "Help Content Error";
	}

	@Override
	public String toString() {
	    return node.toString();
	}
    }

    /**
     * Class OdbFileFilter is a FileFilter that only allows files with the .odb
     * file extention or directories.
     */
    public class OdbFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
	    if (f.getAbsolutePath().toLowerCase().contains(".odb")) {
		return true;
	    } else if (f.isDirectory()) {
		return true;
	    }
	    return false;
	}

	@Override
	public String getDescription() {
	    return "ObjectDB Database File (.odb)";
	}
    }

    /**
     * An extension of JSlider to select a range of values using two thumb
     * controls. The thumb controls are used to select the lower and upper value
     * of a range with predetermined minimum and maximum values.
     *     
* <p>Note that RangeSlider makes use of the default BoundedRangeModel,
     * which supports an inner range defined by a value and an extent. The upper
     * value returned by RangeSlider is simply the lower value plus the
     * extent.</p>
     */
    public class RangeSlider extends JSlider {

	/**
	 * Constructs a RangeSlider with default minimum and maximum values of 0
	 * and 100.
	 */
	public RangeSlider() {
	    initSlider();
	}

	/**
	 * Constructs a RangeSlider with the specified default minimum and
	 * maximum values.
	 */
	public RangeSlider(int min, int max) {
	    super(min, max);
	    initSlider();
	}

	/**
	 * Initializes the slider by setting default properties.
	 */
	private void initSlider() {
	    setOrientation(HORIZONTAL);
	}

	/**
	 * Overrides the superclass method to install the UI delegate to draw
	 * two thumbs.
	 */
	@Override
	public void updateUI() {
	    setUI(new RangeSliderUI(this));
	    // Update UI for slider labels. This must be called after updating the
	    // UI of the slider. Refer to JSlider.updateUI().
	    updateLabelUIs();
	}

	/**
	 * Returns the lower value in the range.
	 */
	@Override
	public int getValue() {
	    return super.getValue();
	}

	/**
	 * Sets the lower value in the range.
	 */
	@Override
	public void setValue(int value) {
	    int oldValue = getValue();
	    if (oldValue == value) {
		return;
	    }

	    // Compute new value and extent to maintain upper value.
	    int oldExtent = getExtent();
	    int newValue = Math.min(Math.max(getMinimum(), value), oldValue + oldExtent);
	    int newExtent = oldExtent + oldValue - newValue;

	    // Set new value and extent, and fire a single change event.
	    getModel().setRangeProperties(newValue, newExtent, getMinimum(),
		    getMaximum(), getValueIsAdjusting());
	}

	/**
	 * Returns the upper value in the range.
	 */
	public int getUpperValue() {
	    return getValue() + getExtent();
	}

	/**
	 * Sets the upper value in the range.
	 */
	public void setUpperValue(int value) {
	    // Compute new extent.
	    int lowerValue = getValue();
	    int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);

	    // Set extent to set upper value.
	    setExtent(newExtent);
	}
    }

    /**
     * UI delegate for the RangeSlider component. RangeSliderUI paints two
     * thumbs, one for the lower value and one for the upper value.
     */
    class RangeSliderUI extends BasicSliderUI {

	/**
	 * Color of selected range.
	 */
	private Color rangeColor = Color.MAGENTA;
	/**
	 * Location and size of thumb for upper value.
	 */
	private Rectangle upperThumbRect;
	/**
	 * Indicator that determines whether upper thumb is selected.
	 */
	private boolean upperThumbSelected;
	/**
	 * Indicator that determines whether lower thumb is being dragged.
	 */
	private transient boolean lowerDragging;
	/**
	 * Indicator that determines whether upper thumb is being dragged.
	 */
	private transient boolean upperDragging;

	/**
	 * Constructs a RangeSliderUI for the specified slider component.
	 *
	 * @param b RangeSlider
	 */
	public RangeSliderUI(RangeSlider b) {
	    super(b);
	}

	/**
	 * Installs this UI delegate on the specified component.
	 */
	@Override
	public void installUI(JComponent c) {
	    upperThumbRect = new Rectangle();
	    super.installUI(c);
	}

	/**
	 * Creates a listener to handle track events in the specified slider.
	 */
	@Override
	protected TrackListener createTrackListener(JSlider slider) {
	    return new RangeTrackListener();
	}

	/**
	 * Creates a listener to handle change events in the specified slider.
	 */
	@Override
	protected ChangeListener createChangeListener(JSlider slider) {
	    return new ChangeHandler();
	}

	/**
	 * Updates the dimensions for both thumbs.
	 */
	@Override
	protected void calculateThumbSize() {
	    // Call superclass method for lower thumb size.
	    super.calculateThumbSize();

	    // Set upper thumb size.
	    upperThumbRect.setSize(thumbRect.width, thumbRect.height);
	}

	/**
	 * Updates the locations for both thumbs.
	 */
	@Override
	protected void calculateThumbLocation() {
	    // Call superclass method for lower thumb location.
	    super.calculateThumbLocation();

	    // Adjust upper value to snap to ticks if necessary.
	    if (slider.getSnapToTicks()) {
		int upperValue = slider.getValue() + slider.getExtent();
		int snappedValue = upperValue;
		int majorTickSpacing = slider.getMajorTickSpacing();
		int minorTickSpacing = slider.getMinorTickSpacing();
		int tickSpacing = 0;

		if (minorTickSpacing > 0) {
		    tickSpacing = minorTickSpacing;
		} else if (majorTickSpacing > 0) {
		    tickSpacing = majorTickSpacing;
		}

		if (tickSpacing != 0) {
		    // If it's not on a tick, change the value
		    if ((upperValue - slider.getMinimum()) % tickSpacing != 0) {
			float temp = (float) (upperValue - slider.getMinimum()) / (float) tickSpacing;
			int whichTick = Math.round(temp);
			snappedValue = slider.getMinimum() + (whichTick * tickSpacing);
		    }

		    if (snappedValue != upperValue) {
			slider.setExtent(snappedValue - slider.getValue());
		    }
		}
	    }

	    // Calculate upper thumb location. The thumb is centered over its
	    // value on the track.
	    if (slider.getOrientation() == JSlider.HORIZONTAL) {
		int upperPosition = xPositionForValue(slider.getValue() + slider.getExtent());
		upperThumbRect.x = upperPosition - (upperThumbRect.width / 2);
		upperThumbRect.y = trackRect.y;

	    } else {
		int upperPosition = yPositionForValue(slider.getValue() + slider.getExtent());
		upperThumbRect.x = trackRect.x;
		upperThumbRect.y = upperPosition - (upperThumbRect.height / 2);
	    }
	}

	/**
	 * Returns the size of a thumb.
	 */
	@Override
	protected Dimension getThumbSize() {
	    return new Dimension(12, 12);
	}

	/**
	 * Paints the slider. The selected thumb is always painted on top of the
	 * other thumb.
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
	    super.paint(g, c);

	    Rectangle clipRect = g.getClipBounds();
	    if (upperThumbSelected) {
		// Paint lower thumb first, then upper thumb.
		if (clipRect.intersects(thumbRect)) {
		    paintLowerThumb(g);
		}
		if (clipRect.intersects(upperThumbRect)) {
		    paintUpperThumb(g);
		}

	    } else {
		// Paint upper thumb first, then lower thumb.
		if (clipRect.intersects(upperThumbRect)) {
		    paintUpperThumb(g);
		}
		if (clipRect.intersects(thumbRect)) {
		    paintLowerThumb(g);
		}
	    }
	}

	/**
	 * Paints the track.
	 */
	@Override
	public void paintTrack(Graphics g) {
	    // Draw track.
	    super.paintTrack(g);

	    Rectangle trackBounds = trackRect;

	    if (slider.getOrientation() == JSlider.HORIZONTAL) {
		// Determine position of selected range by moving from the middle
		// of one thumb to the other.
		int lowerX = thumbRect.x + (thumbRect.width / 2);
		int upperX = upperThumbRect.x + (upperThumbRect.width / 2);

		// Determine track position.
		int cy = (trackBounds.height / 2) - 2;

		// Save color and shift position.
		Color oldColor = g.getColor();
		g.translate(trackBounds.x, trackBounds.y + cy);

		// Draw selected range.
		g.setColor(rangeColor);
		for (int y = 0; y <= 3; y++) {
		    g.drawLine(lowerX - trackBounds.x, y, upperX - trackBounds.x, y);
		}

		// Restore position and color.
		g.translate(-trackBounds.x, -(trackBounds.y + cy));
		g.setColor(oldColor);

	    } else {
		// Determine position of selected range by moving from the middle
		// of one thumb to the other.
		int lowerY = thumbRect.x + (thumbRect.width / 2);
		int upperY = upperThumbRect.x + (upperThumbRect.width / 2);

		// Determine track position.
		int cx = (trackBounds.width / 2) - 2;

		// Save color and shift position.
		Color oldColor = g.getColor();
		g.translate(trackBounds.x + cx, trackBounds.y);

		// Draw selected range.
		g.setColor(rangeColor);
		for (int x = 0; x <= 3; x++) {
		    g.drawLine(x, lowerY - trackBounds.y, x, upperY - trackBounds.y);
		}

		// Restore position and color.
		g.translate(-(trackBounds.x + cx), -trackBounds.y);
		g.setColor(oldColor);
	    }
	}

	/**
	 * Overrides superclass method to do nothing. Thumb painting is handled
	 * within the
	 * <code>paint()</code> method.
	 */
	@Override
	public void paintThumb(Graphics g) {
	    // Do nothing.
	}

	/**
	 * Paints the thumb for the lower value using the specified graphics
	 * object.
	 */
	private void paintLowerThumb(Graphics g) {
	    Rectangle knobBounds = thumbRect;
	    int w = knobBounds.width;
	    int h = knobBounds.height;

	    // Create graphics copy.
	    Graphics2D g2d = (Graphics2D) g.create();

	    // Create default thumb shape.
	    Shape thumbShape = createThumbShape(w - 1, h - 1);

	    // Draw thumb.
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.translate(knobBounds.x, knobBounds.y);

	    g2d.setColor(Color.BLUE);
	    g2d.fill(thumbShape);

	    g2d.setColor(Color.BLACK);
	    g2d.draw(thumbShape);

	    // Dispose graphics.
	    g2d.dispose();
	}

	/**
	 * Paints the thumb for the upper value using the specified graphics
	 * object.
	 */
	private void paintUpperThumb(Graphics g) {
	    Rectangle knobBounds = upperThumbRect;
	    int w = knobBounds.width;
	    int h = knobBounds.height;

	    // Create graphics copy.
	    Graphics2D g2d = (Graphics2D) g.create();

	    // Create default thumb shape.
	    Shape thumbShape = createThumbShape(w - 1, h - 1);

	    // Draw thumb.
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.translate(knobBounds.x, knobBounds.y);

	    g2d.setColor(Color.RED);
	    g2d.fill(thumbShape);

	    g2d.setColor(Color.BLACK);
	    g2d.draw(thumbShape);

	    // Dispose graphics.
	    g2d.dispose();
	}

	/**
	 * Returns a Shape representing a thumb.
	 */
	private Shape createThumbShape(int width, int height) {
	    // Use circular shape.
	    Ellipse2D shape = new Ellipse2D.Double(0, 0, width, height);
	    return shape;
	}

	/**
	 * Sets the location of the upper thumb, and repaints the slider. This
	 * is called when the upper thumb is dragged to repaint the slider. The
	 * <code>setThumbLocation()</code> method performs the same task for the
	 * lower thumb.
	 */
	private void setUpperThumbLocation(int x, int y) {
	    Rectangle upperUnionRect = new Rectangle();
	    upperUnionRect.setBounds(upperThumbRect);

	    upperThumbRect.setLocation(x, y);

	    SwingUtilities.computeUnion(upperThumbRect.x, upperThumbRect.y, upperThumbRect.width, upperThumbRect.height, upperUnionRect);
	    slider.repaint(upperUnionRect.x, upperUnionRect.y, upperUnionRect.width, upperUnionRect.height);
	}

	/**
	 * Moves the selected thumb in the specified direction by a block
	 * increment. This method is called when the user presses the Page Up or
	 * Down keys.
	 */
	public void scrollByBlock(int direction) {
	    synchronized (slider) {
		int blockIncrement = (slider.getMaximum() - slider.getMinimum()) / 10;
		if (blockIncrement <= 0 && slider.getMaximum() > slider.getMinimum()) {
		    blockIncrement = 1;
		}
		int delta = blockIncrement * ((direction > 0) ? POSITIVE_SCROLL : NEGATIVE_SCROLL);

		if (upperThumbSelected) {
		    int oldValue = ((RangeSlider) slider).getUpperValue();
		    ((RangeSlider) slider).setUpperValue(oldValue + delta);
		} else {
		    int oldValue = slider.getValue();
		    slider.setValue(oldValue + delta);
		}
	    }
	}

	/**
	 * Moves the selected thumb in the specified direction by a unit
	 * increment. This method is called when the user presses one of the
	 * arrow keys.
	 */
	public void scrollByUnit(int direction) {
	    synchronized (slider) {
		int delta = 1 * ((direction > 0) ? POSITIVE_SCROLL : NEGATIVE_SCROLL);

		if (upperThumbSelected) {
		    int oldValue = ((RangeSlider) slider).getUpperValue();
		    ((RangeSlider) slider).setUpperValue(oldValue + delta);
		} else {
		    int oldValue = slider.getValue();
		    slider.setValue(oldValue + delta);
		}
	    }
	}

	/**
	 * Listener to handle model change events. This calculates the thumb
	 * locations and repaints the slider if the value change is not caused
	 * by dragging a thumb.
	 */
	public class ChangeHandler implements ChangeListener {

	    public void stateChanged(ChangeEvent arg0) {
		if (!lowerDragging && !upperDragging) {
		    calculateThumbLocation();
		    slider.repaint();
		}
	    }
	}

	/**
	 * Listener to handle mouse movements in the slider track.
	 */
	public class RangeTrackListener extends TrackListener {

	    @Override
	    public void mousePressed(MouseEvent e) {
		if (!slider.isEnabled()) {
		    return;
		}

		currentMouseX = e.getX();
		currentMouseY = e.getY();

		if (slider.isRequestFocusEnabled()) {
		    slider.requestFocus();
		}

		// Determine which thumb is pressed. If the upper thumb is
		// selected (last one dragged), then check its position first;
		// otherwise check the position of the lower thumb first.
		boolean lowerPressed = false;
		boolean upperPressed = false;
		if (upperThumbSelected) {
		    if (upperThumbRect.contains(currentMouseX, currentMouseY)) {
			upperPressed = true;
		    } else if (thumbRect.contains(currentMouseX, currentMouseY)) {
			lowerPressed = true;
		    }
		} else {
		    if (thumbRect.contains(currentMouseX, currentMouseY)) {
			lowerPressed = true;
		    } else if (upperThumbRect.contains(currentMouseX, currentMouseY)) {
			upperPressed = true;
		    }
		}

		// Handle lower thumb pressed.
		if (lowerPressed) {
		    switch (slider.getOrientation()) {
			case JSlider.VERTICAL:
			    offset = currentMouseY - thumbRect.y;
			    break;
			case JSlider.HORIZONTAL:
			    offset = currentMouseX - thumbRect.x;
			    break;
		    }
		    upperThumbSelected = false;
		    lowerDragging = true;
		    return;
		}
		lowerDragging = false;

		// Handle upper thumb pressed.
		if (upperPressed) {
		    switch (slider.getOrientation()) {
			case JSlider.VERTICAL:
			    offset = currentMouseY - upperThumbRect.y;
			    break;
			case JSlider.HORIZONTAL:
			    offset = currentMouseX - upperThumbRect.x;
			    break;
		    }
		    upperThumbSelected = true;
		    upperDragging = true;
		    return;
		}
		upperDragging = false;
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		lowerDragging = false;
		upperDragging = false;
		slider.setValueIsAdjusting(false);
		super.mouseReleased(e);
	    }

	    @Override
	    public void mouseDragged(MouseEvent e) {
		if (!slider.isEnabled()) {
		    return;
		}

		currentMouseX = e.getX();
		currentMouseY = e.getY();

		if (lowerDragging) {
		    slider.setValueIsAdjusting(true);
		    moveLowerThumb();

		} else if (upperDragging) {
		    slider.setValueIsAdjusting(true);
		    moveUpperThumb();
		}
	    }

	    @Override
	    public boolean shouldScroll(int direction) {
		return false;
	    }

	    /**
	     * Moves the location of the lower thumb, and sets its corresponding
	     * value in the slider.
	     */
	    private void moveLowerThumb() {
		int thumbMiddle = 0;

		switch (slider.getOrientation()) {
		    case JSlider.VERTICAL:
			int halfThumbHeight = thumbRect.height / 2;
			int thumbTop = currentMouseY - offset;
			int trackTop = trackRect.y;
			int trackBottom = trackRect.y + (trackRect.height - 1);
			int vMax = yPositionForValue(slider.getValue() + slider.getExtent());

			// Apply bounds to thumb position.
			if (drawInverted()) {
			    trackBottom = vMax;
			} else {
			    trackTop = vMax;
			}
			thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
			thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);

			setThumbLocation(thumbRect.x, thumbTop);

			// Update slider value.
			thumbMiddle = thumbTop + halfThumbHeight;
			slider.setValue(valueForYPosition(thumbMiddle));
			break;

		    case JSlider.HORIZONTAL:
			int halfThumbWidth = thumbRect.width / 2;
			int thumbLeft = currentMouseX - offset;
			int trackLeft = trackRect.x;
			int trackRight = trackRect.x + (trackRect.width - 1);
			int hMax = xPositionForValue(slider.getValue() + slider.getExtent());

			// Apply bounds to thumb position.
			if (drawInverted()) {
			    trackLeft = hMax;
			} else {
			    trackRight = hMax;
			}
			thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
			thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);

			setThumbLocation(thumbLeft, thumbRect.y);

			// Update slider value.
			thumbMiddle = thumbLeft + halfThumbWidth;
			slider.setValue(valueForXPosition(thumbMiddle));
			break;

		    default:
			return;
		}
	    }

	    /**
	     * Moves the location of the upper thumb, and sets its corresponding
	     * value in the slider.
	     */
	    private void moveUpperThumb() {
		int thumbMiddle = 0;

		switch (slider.getOrientation()) {
		    case JSlider.VERTICAL:
			int halfThumbHeight = thumbRect.height / 2;
			int thumbTop = currentMouseY - offset;
			int trackTop = trackRect.y;
			int trackBottom = trackRect.y + (trackRect.height - 1);
			int vMin = yPositionForValue(slider.getValue());

			// Apply bounds to thumb position.
			if (drawInverted()) {
			    trackTop = vMin;
			} else {
			    trackBottom = vMin;
			}
			thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
			thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);

			setUpperThumbLocation(thumbRect.x, thumbTop);

			// Update slider extent.
			thumbMiddle = thumbTop + halfThumbHeight;
			slider.setExtent(valueForYPosition(thumbMiddle) - slider.getValue());
			break;

		    case JSlider.HORIZONTAL:
			int halfThumbWidth = thumbRect.width / 2;
			int thumbLeft = currentMouseX - offset;
			int trackLeft = trackRect.x;
			int trackRight = trackRect.x + (trackRect.width - 1);
			int hMin = xPositionForValue(slider.getValue());

			// Apply bounds to thumb position.
			if (drawInverted()) {
			    trackRight = hMin;
			} else {
			    trackLeft = hMin;
			}
			thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
			thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);

			setUpperThumbLocation(thumbLeft, thumbRect.y);

			// Update slider extent.
			thumbMiddle = thumbLeft + halfThumbWidth;
			slider.setExtent(valueForXPosition(thumbMiddle) - slider.getValue());
			break;

		    default:
			return;
		}
	    }
	}
    }
//        private static class PasswordContainerComboBoxRenderer extends JComboBox implements TableCellRenderer {
//
//        public PasswordContainerComboBoxRenderer() {
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        if (isSelected) {
//            setForeground(table.getSelectionForeground());
//            super.setBackground(table.getSelectionBackground());
//        } else {
//            setForeground(table.getForeground());
//            setBackground(table.getBackground());
//        }
//
//        // Select the current value
//        setSelectedItem(value);
//        return this;
//        }
//    }
// </editor-fold>
}
