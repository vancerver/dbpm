package edu.uci.grad.security.dao;

import edu.uci.grad.security.SystemwideConstants;
import edu.uci.grad.security.model.AccountInfo;
import edu.uci.grad.security.model.ColdFusionConfiguration;
import edu.uci.grad.security.util.JdbcDatabaseUtil;
import java.util.List;

public class ColdFusionConfigurationDao extends AbstractDao {

    // URL Parameter names for password changing ColdFusion script URL
    public static final String COLDFUSION_URL_FORMAT = "%s?%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s";
    public static final String COLDFUSION_URL_ADMIN_USERNAME_PARAM = "cfAdminUser";
    public static final String COLDFUSION_URL_ADMIN_PASSWORD_PARAM = "cfAdminPass";
    public static final Object COLDFUSION_DSN_HOST_URL_PARAM = "dbHostUrl";
    public static final Object COLDFUSION_DSN_DATABASE_NAME_PARAM = "dbName";
    public static final Object COLDFUSION_DSN_PORT_PARAM = "dbPort";
    public static final Object COLDFUSION_DSN_NAME_PARAM = "dsnName";
    private static final String QUERY_TO_GET_COLDFUSIONCONFIGURATION_FROM_ID = "SELECT cfc FROM ColdFusionConfiguration cfc WHERE cfc.coldFusionConfigurationId = %s";
    private static final String QUERY_TO_GET_HIGHEST_COLDFUSIONCONFIGURATIONID = "SELECT MAX(cfc.coldFusionConfigurationId) FROM ColdFusionConfiguration cfc";

    /**
     * Method modifyColdFusionDatasource will make an URL request to a cfm file
     * containing a cfscript that will change a DSN password.
     *
     * @param passwordToModify the Password object that needs to be modified.
     */
    public void modifyColdfusionDatasource(AccountInfo passwordToModify) {
        ColdFusionConfiguration cfConfig = passwordToModify.getColdFusionConfiguration();
        JdbcDatabaseUtil.urlQuery(String.format(COLDFUSION_URL_FORMAT,
                cfConfig.getResetCfScriptUrl(),
                COLDFUSION_URL_ADMIN_USERNAME_PARAM, cfConfig.getCfAdminUsername(),
                COLDFUSION_URL_ADMIN_PASSWORD_PARAM, cfConfig.getCfAdminPassword(),
                COLDFUSION_DSN_HOST_URL_PARAM, cfConfig.getDatabaseHost(),
                COLDFUSION_DSN_DATABASE_NAME_PARAM, cfConfig.getDatabaseName(),
                COLDFUSION_DSN_PORT_PARAM, cfConfig.getDatabasePort(),
                COLDFUSION_DSN_NAME_PARAM, cfConfig.getDsnName()));
    }

//	/**
//	 * Method getColdFusionConfigurationFromId will return the ColdFusionConfiguration
//	 * object from the database that has the specified coldFusionConfigurationId
//	 * 
//	 * @param coldFusionConfigurationId the ID of the ColdFusionConfiguration to get.
//	 * @return the ColdFusionConfiguration object with the specified ID.
//	 */
//	private ColdFusionConfiguration getColdfusionConfigurationFromId(
//			Integer coldFusionConfigurationId) {
//		ColdFusionConfiguration cfc = 
//				getEntityManager().createQuery(
//				String.format(
//				SystemwideConstants.QUERY_TO_GET_COLDFUSIONCONFIGURATION_FROM_ID, 
//				coldFusionConfigurationId), 
//				ColdFusionConfiguration.class).getResultList().get(0);
//		return cfc;
//	}
    /**
     * Method persistNewColdFusionConfiguration() will save the given new
     * ColdFusionConfiguration object to the database. If there is already a
     * ColdFusionConfiguration with this coldFusionConfigurationId in the
     * database, this method will fail.
     *
     * @param cfConfig the new ColdFusionConfiguration file to persist.
     */
    public void persistNewColdFusionConfiguration(
            ColdFusionConfiguration cfConfig) {
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(cfConfig);
        getEntityManager().getTransaction().commit();
    }

    /**
     * Method getCurrentColdFusionConfiguration() will return the first
     * ColdFusionConfiguration object from the database.
     *
     * @return the first ColdFusionConfiguration in the database.
     */
    public ColdFusionConfiguration getCurrentColdFusionConfiguration() {
        ColdFusionConfiguration cfConfig = getEntityManager().createQuery(
                "SELECT cfc FROM ColdFusionConfiguration cfc ",
                ColdFusionConfiguration.class).getResultList().get(0);
        return cfConfig;
    }

    /**
     * Method getNextColdFusionConfigurationId() will return the value of the
     * highest current coldFusionConfigurationId + 1. This number can then be
     * used as the next coldFusionConfigurationId to assign to a new
     * ColdFusionconfiguration.
     *
     * @return highest current coldFusionconfigurationId + 1
     */
    public Integer getNextColdFusionConfigurationId() {
        Integer nextColdFusionConfigurationId;
        nextColdFusionConfigurationId = (Integer) getEntityManager().createQuery(
                QUERY_TO_GET_HIGHEST_COLDFUSIONCONFIGURATIONID).getSingleResult();
        return (int) nextColdFusionConfigurationId + 1;
    }

    public List<ColdFusionConfiguration> getAllColdFusionConfigurations() {
        return getEntityManager().createQuery(
                "SELECT cfc FROM ColdFusionConfiguration cfc ",
                ColdFusionConfiguration.class).getResultList();
    }

    public void setExistingColdFusionConfiguration(ColdFusionConfiguration selected) {
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(selected);
        getEntityManager().getTransaction().commit();
    }

    public void removeColdFusionConfiguration(ColdFusionConfiguration serverToRemove) {
        getEntityManager().getTransaction().begin();
        getEntityManager().remove(serverToRemove);
        getEntityManager().getTransaction().commit();
    }
}
