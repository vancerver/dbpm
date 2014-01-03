package edu.uci.grad.security.dao;

import edu.uci.grad.security.model.AccountInfo;
import edu.uci.grad.security.model.ColdFusionConfiguration;
import edu.uci.grad.security.model.JdbcConnection;
import edu.uci.grad.security.model.PasswordContainer;
import edu.uci.grad.security.util.JdbcDatabaseUtil;
import edu.uci.grad.security.util.PasswordUtils;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

public class AccountInfoDao extends AbstractDao {

    private static final String QUERY_TO_GET_ALL_ACCOUNTINFOS = "SELECT ai FROM AccountInfo ai";
    private static final String QUERY_TO_GET_ACCOUNTINFO_BY_USERNAME = "SELECT ai FROM AccountInfo ai WHERE ai.userName = '"; // Username + "'" is inserted after this String.
    private static final String QUERY_TO_GET_ACCOUNTINFO_FROM_JDBCCONNECTION = "SELECT ai FROM AccountInfo ai WHERE ai.jdbcConnection.jdbcConnectionId = :jdbcConnectionId";
    private static final String QUERY_TO_GET_ALL_ACCOUNTS_FROM_COLDFUSIONCONFIGURATION = "SELECT ai FROM AccountInfo ai WHERE ai.coldFusionConfiguration.coldFusionConfigurationId = :coldFusionConfigurationId";

    /**
     * Method getAccountInfoForUser will return the AccountInfo object for the
     * specified user name.
     *
     * @param username the username to get a AccountInfo object for
     * @return the AccountInfo object for this username.
     */
    public AccountInfo getAccountInfoForUser(String username) {
        List<AccountInfo> accountInfosForUser = getEntityManager().createQuery(QUERY_TO_GET_ACCOUNTINFO_BY_USERNAME + username + "'", AccountInfo.class).getResultList();
        if (accountInfosForUser.size() > 0) {
            return accountInfosForUser.get(0);
        }
        return null;
    }

    /**
     * Method createPasswordQuery() will take any JPQL query that selects a list
     * of Password objects and returns the result.
     *
     * @param query a JPQL query that selects AccountInfo objects
     * @return a List<AccountInfo> containing the results.
     */
    public List<AccountInfo> createAccountInfoQuery(String query) {
        return getEntityManager().createQuery(query, AccountInfo.class).getResultList();
    }

    /**
     * Method removeAccountInfo() will remove the given accountInfo from the
     * database.
     *
     * @param accountInfoToRemove the AccountInfo object to remove, detached or
     * active.
     */
    public void removeAccountInfo(AccountInfo accountInfoToRemove) {
        getEntityManager().getTransaction().begin();
        getEntityManager().remove(getEntityManager().createQuery(QUERY_TO_GET_ACCOUNTINFO_BY_USERNAME + accountInfoToRemove.getUserName() + "'", AccountInfo.class).getResultList().get(0));
        getEntityManager().getTransaction().commit();
    }

    /**
     * Method persistNewAccountInfo will add a new AccountInfo object to the
     * database. It will not add or modify if the accountInfo is already in the
     * database.
     *
     * @param newPass a new AccountInfo object to persist.
     */
    public void persistNewAccountInfo(AccountInfo newPass) {
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(newPass);
        getEntityManager().getTransaction().commit();
    }

    /**
     * Method persistExistingAccountInfo() will take a given AccountInfo object
     * referencing an existing AccountInfo in the database and update the
     * AccountInfo in the database based on the AccountInfo object provided.
     *
     * @param accountInfoToModify the AccountInfo object with updates.
     * @param key the AES encryption key for this AccountInfo.
     */
    public void persistExistingAccountInfo(AccountInfo accountInfoToModify, byte[] key, byte[] encryptedPassword) {

        String userName = accountInfoToModify.getUserName();

        String currentPasswordString = PasswordUtils.getDecryptedPasswordForUser(userName, encryptedPassword, key);
        try {
            JdbcDatabaseUtil.updatePassword(userName,
                    currentPasswordString,
                    userName,
                    currentPasswordString,
                    accountInfoToModify.getJdbcConnection().getJdbcUrl(),
                    accountInfoToModify.getJdbcConnection().getJdbcDriverClass());
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
            JOptionPane.showMessageDialog(null, "Error occured during password update on database server " + accountInfoToModify.getJdbcConnection().getDescriptiveName() + ".\n"
                    + "for user " + userName + ". This password has not been changed.\n", "Password Update Error", JOptionPane.ERROR_MESSAGE);
        }
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(accountInfoToModify);
        getEntityManager().getTransaction().commit();
    }

    public List<AccountInfo> getAccountsAssociatedWithJdbcConnection(JdbcConnection connection) {
        TypedQuery<AccountInfo> query = getEntityManager().createQuery(QUERY_TO_GET_ACCOUNTINFO_FROM_JDBCCONNECTION, AccountInfo.class);
        query.setParameter("jdbcConnectionId", connection.getJdbcConnectionId());
        return query.getResultList();
    }

    public List<AccountInfo> getAccountsAssociatedWithColdFusionServer(ColdFusionConfiguration server) {
        TypedQuery<AccountInfo> query = getEntityManager().createQuery(QUERY_TO_GET_ALL_ACCOUNTS_FROM_COLDFUSIONCONFIGURATION, AccountInfo.class);
        query.setParameter("coldFusionConfigurationId", server.getColdFusionConfigurationId());
        return query.getResultList();
    }

    public List<AccountInfo> getAllAccountInfos() {
        return getEntityManager().createQuery(QUERY_TO_GET_ALL_ACCOUNTINFOS, AccountInfo.class).getResultList();
    }

    public List<AccountInfo> searchAllAccountInfoFieldsForTerm(String searchTerm) {
        TypedQuery<AccountInfo> query = getEntityManager().createQuery(String.format("SELECT ai FROM AccountInfo ai WHERE "
                + "(UPPER(ai.userName) LIKE UPPER(:searchTerm) OR UPPER(ai.jdbcConnection.descriptiveName) "
                + "LIKE UPPER(:searchTerm) OR UPPER(ai.passwordContainer.groupName) LIKE UPPER(:searchTerm) "
                + "OR UPPER(ai.coldFusionServerName) LIKE UPPER(:searchTerm))",
                //  + "OR ((ai.coldFusionConfiguration IS NOT NULL) AND (UPPER(ai.coldFusionConfiguration.coldFusionServerName) LIKE UPPER(:searchTerm))))",
                searchTerm, searchTerm, searchTerm), AccountInfo.class);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        return query.getResultList();
    }

    public List<AccountInfo> getAccountsAssociatedWithPasswordContainer(PasswordContainer containerToRemove) {
        TypedQuery<AccountInfo> query = getEntityManager().createQuery("SELECT ai FROM AccountInfo ai WHERE ai.passwordContainer.passwordContainerId = :passwordContainerId", AccountInfo.class);
        query.setParameter("passwordContainerId", containerToRemove.getPasswordContainerId());
        return query.getResultList();
    }

    public boolean userExists(String username) {
        return (getAccountInfoForUser(username) != null);
}
}
