package edu.uci.grad.security.dao;

import edu.uci.grad.security.SystemwideConstants;
import edu.uci.grad.security.model.EncryptedPassword;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class EncryptedPasswordDao {

    public static final String QUERY_TO_GET_ENCRYPTEDPASSWORD_FOR_USER = "SELECT ep FROM EncryptedPassword ep WHERE ep.userName = :userName"; // Username
    EntityManagerFactory emf;
    EntityManager em;

    public EncryptedPassword getEncryptedPasswordForUser(String userName, String databaseFilePath) {
	setEntityManager(databaseFilePath);
	TypedQuery<EncryptedPassword> q = em.createQuery(QUERY_TO_GET_ENCRYPTEDPASSWORD_FOR_USER, EncryptedPassword.class);
	q.setParameter("userName", userName);
	EncryptedPassword password = q.getResultList().get(0);
	closeEntityManager();
	return password;
    }

    public void setNewEncryptedPassword(byte[] newPassword, String accountUserName, String databaseFilePath) {
	EncryptedPassword password = new EncryptedPassword();
	password.setEncryptedPassword(newPassword);
	password.setIv(null);
	password.setUserName(accountUserName);
	setEntityManager(databaseFilePath);
	em.getTransaction().begin();
	em.persist(password);
	em.getTransaction().commit();
	closeEntityManager();
    }

    public void removeEncryptedPasswordByUserName(String userName, String databaseFilePath) {
	setEntityManager(databaseFilePath);
	TypedQuery<EncryptedPassword> q = em.createQuery(QUERY_TO_GET_ENCRYPTEDPASSWORD_FOR_USER, EncryptedPassword.class);
	q.setParameter("userName", userName);
	EncryptedPassword password = q.getResultList().get(0);
	em.getTransaction().begin();
	em.remove(password);
	em.getTransaction().commit();
	closeEntityManager();
    }

    private void setEntityManager(String databasePath) {
	Map<String, String> properties = new HashMap<String, String>();
	properties.put("javax.persistence.jdbc.user", SystemwideConstants.OBJECT_DB_SERVER_USERNAME);
	properties.put("javax.persistence.jdbc.password", SystemwideConstants.OBJECT_DB_SERVER_PASSWORD);
	emf = Persistence.createEntityManagerFactory(databasePath, properties);
	em = emf.createEntityManager();
    }

    private void closeEntityManager() {
	em.close();
	emf.close();
    }

    public void setExistingEncryptedPassword(byte[] encryptedPassword,
	    String userNameOfNewPassword, String databaseFilePath) {
	setEntityManager(databaseFilePath);
	em.getTransaction().begin();
	TypedQuery<EncryptedPassword> q = em.createQuery(QUERY_TO_GET_ENCRYPTEDPASSWORD_FOR_USER, EncryptedPassword.class);
	q.setParameter("userName", userNameOfNewPassword);
	EncryptedPassword modifiedPassword = q.getResultList().get(0);

	modifiedPassword.setEncryptedPassword(encryptedPassword);
	em.merge(modifiedPassword);
	em.getTransaction().commit();
	closeEntityManager();
    }

    public void moveEncryptedPassword(String currentPasswordLocation, String newPasswordLocation, String username) {
	EncryptedPassword currentPassword = getEncryptedPasswordForUser(username, currentPasswordLocation);
	setEntityManager(newPasswordLocation);
	em.getTransaction().begin();
	em.persist(currentPassword);
	em.getTransaction().commit();
	closeEntityManager();
	removeEncryptedPasswordByUserName(username, currentPasswordLocation);
    }
}
