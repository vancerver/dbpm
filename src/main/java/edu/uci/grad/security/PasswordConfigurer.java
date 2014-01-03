/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uci.grad.security;

import edu.uci.grad.security.dao.AccountInfoDao;
import edu.uci.grad.security.dao.EncryptedPasswordDao;
import edu.uci.grad.security.dao.KeyDao;
import edu.uci.grad.security.model.AccountInfo;
import edu.uci.grad.security.util.PasswordUtils;
import java.util.Properties;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 *
 * @author vcagle
 */
public class PasswordConfigurer extends PropertyPlaceholderConfigurer{
	  /**
   * Default constructor, does nothing
   */
  public PasswordConfigurer() {
    super();
  }

  /**
   * Takes the placeholder and returns the password in the file
   * with the same name as the placeholder
   * @param placeholder
   * @return
   */
  protected String resolvePlaceholder(final String placeholder) {
    	// datasource password initally set to file path of encrypted password store
	System.setProperty(SystemwideConstants.MAIN_DATABASE_SERVER_ROOT_URL_SYSTEM_PROPERTY_NAME, "C:\\gdorapps\\security\\passwd\\");

	// determine account info used later to retrieve and decipher stored entries
	AccountInfo accountInfo = new AccountInfoDao().getAccountInfoForUser(placeholder);
	
	return new String(
		// decrypt cryptic password using salt and key
		PasswordUtils.decrypt(
		// read encrypted password from password store
		new EncryptedPasswordDao().getEncryptedPasswordForUser(
		accountInfo.getUserName(),
		accountInfo.getPasswordContainer().getDatabaseFileUrl()).getEncryptedPassword(),
		accountInfo.getUserName(),
		new KeyDao().getKeyForUser(accountInfo.getUserName())));
  }


  /* (non-Javadoc)
   * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties)
   */
  @Override
  protected String resolvePlaceholder(
      final String placeholder,
      final Properties props) {
    return resolvePlaceholder(placeholder);
  }
}
