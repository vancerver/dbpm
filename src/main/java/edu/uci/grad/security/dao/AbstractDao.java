/*
 *  Copyright 2010. The Regents of the University of California. All Rights Reserved.
 *  Permission to use, copy, modify, and distribute any part of this software including any source code and documentation for educational, research, and non-profit purposes, without fee, and without a written agreement is hereby granted, provided that the above copyright notice, this paragraph and the following three paragraphs appear in all copies of the software and documentation.
 *  Those desiring to incorporate this software into commercial products or use for commercial purposes should contact Office of Technology Alliances, University of California, Irvine, 380 University Tower, Irvine, CA 92607-7700, Phone: (949) 824-7295, FAX: (949) 824-2899.
 *  IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING, WITHOUT LIMITATION, LOST PROFITS, CLAIMS OR DEMANDS, OR BUSINESS INTERRUPTION, ARISING OUT OF THE USE OF THIS SOFTWARE, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  THE SOFTWARE PROVIDED HEREIN IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS. THE UNIVERSITY OF CALIFORNIA MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES OF ANY KIND, EITHER IMPLIED OR EXPRESS, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE, OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, TRADEMARK OR OTHER RIGHTS.
 */
package edu.uci.grad.security.dao;

import static edu.uci.grad.security.SystemwideConstants.*;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

/**
 *
 * @author duongb
 */
public class AbstractDao {

    public static final String MASTER_DATABASE_FILE_NAME = "dbpm.odb";
    private static final ThreadLocal<EntityManager> threads = new ThreadLocal<EntityManager>() {
        @Override
        protected EntityManager initialValue() {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.user", OBJECT_DB_SERVER_USERNAME);
            properties.put("javax.persistence.jdbc.password", OBJECT_DB_SERVER_PASSWORD);
            String masterDatabaseUrl = System.getProperty(MAIN_DATABASE_SERVER_ROOT_URL_SYSTEM_PROPERTY_NAME) + MASTER_DATABASE_FILE_NAME;
            try {
                return Persistence.createEntityManagerFactory(masterDatabaseUrl, properties).createEntityManager();
            } catch (PersistenceException ex) {
                JOptionPane.showMessageDialog(null, "There was an error connecting to the database at URL " + masterDatabaseUrl, "Error Connecting To Password Server", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    };

    protected EntityManager getEntityManager() {
        return threads.get();
    }
}