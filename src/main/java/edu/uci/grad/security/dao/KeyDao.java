package edu.uci.grad.security.dao;

import edu.uci.grad.security.AesCipher;
import edu.uci.grad.security.model.Key;
import java.security.GeneralSecurityException;
import java.util.List;

public class KeyDao extends AbstractDao {

    public static final String QUERY_TO_GET_KEY_FOR_USER = "SELECT k FROM Key k WHERE k.user = '"; // Username + "'" is inserted after this String.

    /**
     * Method getKeyForUser will get the AES encryption key for the specified
     * user.
     *
     * @param user
     * @return the byte[] key for the user.
     */
    public byte[] getKeyForUser(String user) {
        List<Key> keyList = getEntityManager().createQuery(QUERY_TO_GET_KEY_FOR_USER + user + "'", Key.class).getResultList();

        byte[] encodedKey = null;
        if (keyList.size() == 0) {
            try {
                java.security.Key newKey = AesCipher.generateKey(128);
                Key newKeyEntity = new Key();
                newKeyEntity.setKey(newKey.getEncoded());
                newKeyEntity.setUser(user);
                getEntityManager().getTransaction().begin();
                getEntityManager().persist(newKeyEntity);
                getEntityManager().getTransaction().commit();
                encodedKey = newKeyEntity.getKey();
            } catch (GeneralSecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            encodedKey = keyList.get(0).getKey();
        }
        return encodedKey;
    }
}
