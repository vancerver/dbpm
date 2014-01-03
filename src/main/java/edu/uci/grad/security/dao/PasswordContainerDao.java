package edu.uci.grad.security.dao;

import edu.uci.grad.security.model.PasswordContainer;
import java.util.List;

public class PasswordContainerDao extends AbstractDao {

    private static final String QUERY_TO_GET_ALL_PASSWORDFILE = "SELECT pf FROM PasswordContainer pf";
    private static final String QUERY_TO_GET_PASSWORDFILE_BY_ID = "SELECT pf FROM PasswordContainer pf WHERE pf.passwordContainerId = %s";

    /**
     * Method getAllPasswordContainers() will return all PasswordContainers that
     * are on the database.
     *
     * @return List<PasswordContainer> all PasswordContainer on the database.
     */
    public List<PasswordContainer> getAllPasswordContainers() {
        return getEntityManager().createQuery(QUERY_TO_GET_ALL_PASSWORDFILE, PasswordContainer.class).getResultList();
    }

    /**
     * Method addPasswordContainer() will add a new PasswordContainer to the
     * database with the given name and database location.
     *
     * @param groupName
     * @param odbLocation
     */
    public void addPasswordContainer(String groupName, String odbLocation) {
        PasswordContainer newGroup = new PasswordContainer();
        newGroup.setGroupName(groupName);
        newGroup.setDatabaseFileUrl(odbLocation);
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(newGroup);
        getEntityManager().getTransaction().commit();
    }

    /**
     * Method removePasswordContainer will remove the provided PasswordContainer
     * from the database.
     *
     * @param selectedItem the PasswordContainer to remove.
     */
    public void removePasswordContainer(PasswordContainer passwordContainerToRemove) {
        Integer passwordContainerId = passwordContainerToRemove.getPasswordContainerId();
        if (passwordContainerId > 1) {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(getPasswordContainerById(passwordContainerId));
            getEntityManager().getTransaction().commit();
        }
    }

    public PasswordContainer getPasswordContainerById(Integer passwordContainerId) {
        return getEntityManager().createQuery(
                String.format(
                        QUERY_TO_GET_PASSWORDFILE_BY_ID,
                        passwordContainerId), PasswordContainer.class).getResultList().get(0);
    }

    public void setExistingPasswordContainer(PasswordContainer selected) {
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(selected);
        getEntityManager().getTransaction().commit();
    }
}
