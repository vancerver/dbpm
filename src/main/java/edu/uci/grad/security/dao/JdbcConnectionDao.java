package edu.uci.grad.security.dao;

import edu.uci.grad.security.model.JdbcConnection;
import java.util.List;
import javax.persistence.TypedQuery;

public class JdbcConnectionDao extends AbstractDao {

    private static final String QUERY_TO_GET_ALL_JDBC_CONNECTIONS = "SELECT jc FROM JdbcConnection jc";
    private static final String QUERY_TO_GET_DATABASE_CONNECTION_FROM_URL_DESC = "SELECT jc FROM JdbcConnection jc WHERE jc.jdbcUrl = :jdbcUrl AND jc.descriptiveName = :descriptiveName";
    private static final String QUERY_TO_GET_DEFAULT_JDBCCONNECTION = "SELECT jc FROM JdbcConnection jc WHERE jc.defaultDatabase = true";
    private static final String QUERY_TO_GET_HIGHEST_JDBCCONNECTIONID = "SELECT MAX(jc.jdbcConnectionId) FROM JdbcConnection jc";
    private static final String QUERY_TO_GET_JDBCCONNECTION_FROM_ID = "SELECT jc FROM JdbcConnection jc WHERE jc.jdbcConnectionId = %d";

    /**
     * Method addNewJdbcConnection() will add a new JdbcConnection object to the
     * database with the given JDBC URL and name.
     *
     * @param jdbcURL the JDBC URL String for this connection.
     * @param descriptiveName a descriptive name for user identification.
     */
    public void addNewJdbcConnection(String jdbcURL, String descriptiveName, String driverClass) {
        JdbcConnection newConnection = new JdbcConnection();
        newConnection.setJdbcUrl(jdbcURL);
        newConnection.setDescriptiveName(descriptiveName);
        newConnection.setJdbcDriverClass(driverClass);
        newConnection.setDefaultDatabase(true);
        getEntityManager().getTransaction().begin();
        for (JdbcConnection connection : getEntityManager().createQuery(QUERY_TO_GET_ALL_JDBC_CONNECTIONS, JdbcConnection.class).getResultList()) {
            connection.setDefaultDatabase(false);
            getEntityManager().merge(connection);
        }
        getEntityManager().persist(newConnection);
        getEntityManager().getTransaction().commit();
    }

    /**
     * Method getAllJdbcConnections() will return all JdbcConnection objects in
     * the database.
     *
     * @return List<JdbcConnection> of all JdbcConnections in the database.
     */
    public List<JdbcConnection> getAllJdbcConnections() {
        return getEntityManager().createQuery(QUERY_TO_GET_ALL_JDBC_CONNECTIONS, JdbcConnection.class).getResultList();
    }

    /**
     * Method removeDatabaseConnection() will remove the provided JdbcConnection
     * object from the database. The object will be re-queried using the info
     * given and then removed.
     *
     * @param selectedItem JdbcConnection to remove from the database.
     */
    public void removeDatabaseConnection(Object selectedItem) {
        if (selectedItem instanceof JdbcConnection) {
            JdbcConnection connectionToRemove = (JdbcConnection) selectedItem;
            TypedQuery<JdbcConnection> query = getEntityManager().createQuery(QUERY_TO_GET_DATABASE_CONNECTION_FROM_URL_DESC, JdbcConnection.class);
            query.setParameter("jdbcUrl", connectionToRemove.getJdbcUrl());
            query.setParameter("descriptiveName", connectionToRemove.getDescriptiveName());
            List<JdbcConnection> connectionsToRemove = query.getResultList();
            getEntityManager().getTransaction().begin();
            for (JdbcConnection connection : connectionsToRemove) {
                getEntityManager().remove(connection);
            }
            getEntityManager().getTransaction().commit();
        }
    }

    /**
     * Method getDefaultDatabaseConnection() will return the default
     * JdbcConnection object. This is determined by the defaultDatabase flag in
     * the JdbcConnection object.
     *
     * @return the default JdbcConnection
     */
    public JdbcConnection getDefaultDatabaseConection() {
        List<JdbcConnection> connections = getEntityManager().createQuery(QUERY_TO_GET_DEFAULT_JDBCCONNECTION, JdbcConnection.class).getResultList();
        if (!connections.isEmpty()) {
            return connections.get(0);
        }
        return null;
    }

    /**
     * Method getJdbcUrlFromId() will return the JdbcConnection in the database
     * that matches the given ID.
     *
     * @param jdbcConnectionId the JdbcConnection ID to return
     * @return a JdbcConnection with the matching ID.
     */
    public String getJdbcUrlFromId(Integer jdbcConnectionId) {
        JdbcConnection connection = getEntityManager().createQuery(String.format(QUERY_TO_GET_JDBCCONNECTION_FROM_ID, jdbcConnectionId), JdbcConnection.class).getResultList().get(0);
        return connection.getJdbcUrl();
    }

    public void addExistingJdbcConnection(JdbcConnection selected) {
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(selected);
        getEntityManager().getTransaction().commit();
    }
    /**
     * Method resetDefaultConnection() will
     */
//	public void resetDefaultConnection() {
//		getEntityManager().getTransaction().begin();
//		JdbcConnection connectionToAddNewJdbcConnection = getEntityManager().createQuery(QUERY_TO_GET_ALL_JDBC_CONNECTIONS, JdbcConnection.class).getResultList().get(JDBCCONNECTIONID_FOR_ADDING_NEW_CONNECTION);
//		connectionToAddNewJdbcConnection.setDefaultDatabase(true);
//		getEntityManager().merge(connectionToAddNewJdbcConnection);
//		getEntityManager().getTransaction().commit();
//	}
}
