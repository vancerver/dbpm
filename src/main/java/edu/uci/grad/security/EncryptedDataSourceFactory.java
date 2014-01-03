package edu.uci.grad.security;

import edu.uci.grad.security.dao.AccountInfoDao;
import edu.uci.grad.security.dao.EncryptedPasswordDao;
import edu.uci.grad.security.dao.KeyDao;
import edu.uci.grad.security.model.AccountInfo;
import edu.uci.grad.security.util.PasswordUtils;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class EncryptedDataSourceFactory extends org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory {

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {

	// retrieve object
	Object instance = super.getObjectInstance(obj, name, nameCtx, environment);
	if (instance == null) {
	    return null;
	}

	// cast to expected type
	BasicDataSource datasource = (BasicDataSource) instance;

	// datasource password initally set to file path of encrypted password store
	System.setProperty(SystemwideConstants.MAIN_DATABASE_SERVER_ROOT_URL_SYSTEM_PROPERTY_NAME, "objectdb://localhost:6136/");

	// determine account info used later to retrieve and decipher stored entries
	AccountInfo accountInfo = new AccountInfoDao().getAccountInfoForUser(datasource.getUsername());

	datasource.setPassword(
		new String(
		// decrypt cryptic password using salt and key
		PasswordUtils.decrypt(
		// read encrypted password from password store
		new EncryptedPasswordDao().getEncryptedPasswordForUser(
		accountInfo.getUserName(),
		accountInfo.getPasswordContainer().getDatabaseFileUrl()).getEncryptedPassword(),
		accountInfo.getUserName(),
		new KeyDao().getKeyForUser(accountInfo.getUserName()))));

	return datasource;

    }
}
