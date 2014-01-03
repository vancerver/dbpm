package edu.uci.grad.security.dao;

import edu.uci.grad.security.model.PasswordPolicy;
import java.util.List;

public class PasswordPolicyDao extends AbstractDao {

    private static final String QUERY_TO_GET_ACTIVE_PASSWORD_POLICY = "SELECT pp FROM PasswordPolicy pp WHERE pp.policyActive = true";
    private static final String QUERY_TO_GET_DEFAULT_PASSWORD_POLICY = "SELECT pp FROM PasswordPolicy pp WHERE pp.isDefault = true";
    private static final String QUERY_TO_GET_ALL_NON_DEFAULT_PASSWORD_POLICIES = "SELECT pp FROM PasswordPolicy pp WHERE pp.isDefault = false";

    /**
     * Method persistNewPasswordPolicy() will add a new PasswordPolicy object to
     * the database. The PasswordPolicy object provided needs to not already
     * exist on the database for this to work.
     *
     * @param newPolicy the new PasswordPolicy object to save to the database.
     */
    public void persistNewPasswordPolicy(PasswordPolicy newPolicy) {
        getEntityManager().getTransaction().begin();
        removeExistingPolicy(false);
        getEntityManager().persist(newPolicy);
        getEntityManager().getTransaction().commit();
    }

    /**
     * Method removeExistingPolicy() will remove all existing PasswordPolicy
     * objects from the database except the default one. The provided boolean
     * flag specifies if this remaining default PasswordPolicy should be set
     * active or not.
     *
     * @param setDefaultActive boolean flag if the default PasswordPolicy should
     * be set active.
     */
    private void removeExistingPolicy(boolean setDefaultActive) {
        List<PasswordPolicy> ppList = getEntityManager().createQuery(QUERY_TO_GET_ALL_NON_DEFAULT_PASSWORD_POLICIES, PasswordPolicy.class).getResultList();
        for (PasswordPolicy pp : ppList) {
            getEntityManager().remove(pp);
        }
        List<PasswordPolicy> defaultPpList = getEntityManager().createQuery(QUERY_TO_GET_DEFAULT_PASSWORD_POLICY, PasswordPolicy.class).getResultList();
        for (PasswordPolicy pp : defaultPpList) {
            pp.setPolicyActive(setDefaultActive);
            getEntityManager().persist(pp);
        }
    }

    /**
     * Method getCurrentPasswordPolicy() will return the current active
     * PasswordPolicy object.
     *
     * @return the current active PasswordPolicy.
     */
    public PasswordPolicy getCurrentPasswordPolicy() {
        List<PasswordPolicy> pps = getEntityManager().createQuery(QUERY_TO_GET_ACTIVE_PASSWORD_POLICY, PasswordPolicy.class).getResultList();
        if (pps.isEmpty()) {
            return null;
        }
        return pps.get(0);


    }

    /**
     * Method setDefaultPasswordPolicy() will set the default PasswordPolicy
     * active and remove all other PasswordPolicy objects from the database.
     *
     */
    public void setDefaultPasswordPolicy() {
        getEntityManager().getTransaction().begin();
        removeExistingPolicy(true);
        getEntityManager().getTransaction().commit();
    }

    /**
     * Method isMasterDatabaseEmpty() checks if the current master database is
     * empty by checking if it has any current password policy. Only a newly
     * created empty database will have no current password policy because a
     * default password policy is created on database initialization.
     *
     * @return true if the master database is empty, false otherwise
     */
    public boolean isMasterDatabaseEmpty() {
        if (getCurrentPasswordPolicy() == null) {
            return true;
        }
        return false;
    }
}
