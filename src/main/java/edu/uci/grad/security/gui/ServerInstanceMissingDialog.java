/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uci.grad.security.gui;

import edu.uci.grad.security.util.JavaProcess;
import edu.uci.grad.security.util.ServerStarter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vcagle
 */
public class ServerInstanceMissingDialog extends javax.swing.JDialog {

    /**
     * Creates new form ServerInstanceMissingDialog
     */
    public ServerInstanceMissingDialog(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        welcomeLabel = new javax.swing.JLabel();
        problemInfoLabel = new javax.swing.JLabel();
        startAndUseNewServerButton = new javax.swing.JButton();
        objectDbServerUrlTextField = new javax.swing.JTextField();
        serverUrlLabel = new javax.swing.JLabel();
        connectToServerButton = new javax.swing.JButton();
        helpWithServerConnectionsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Initial Startup - No Database URL Provided");
        setLocationByPlatform(true);

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        welcomeLabel.setText("Welcome to Database Password Manager!");

        problemInfoLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        problemInfoLabel.setText("<html>No master database URL was provided at startup.<br>\nA master database URL is required.<br>\nPlease select one of the following options to continue:</html>");

        startAndUseNewServerButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        startAndUseNewServerButton.setText("Startup & Use A New ObjectDB Server Instance @ localhost");
        startAndUseNewServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startAndUseNewServerButtonActionPerformed(evt);
            }
        });

        objectDbServerUrlTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        objectDbServerUrlTextField.setText("objectdb://localhost:6136/");

        serverUrlLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        serverUrlLabel.setText("Use the active ObjectDB Server instance at the following URL:");

        connectToServerButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        connectToServerButton.setText("Connect");
        connectToServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectToServerButtonActionPerformed(evt);
            }
        });

        helpWithServerConnectionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(objectDbServerUrlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectToServerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(problemInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(helpWithServerConnectionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(welcomeLabel)
                            .addComponent(serverUrlLabel)
                            .addComponent(startAndUseNewServerButton))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(problemInfoLabel)
                    .addComponent(helpWithServerConnectionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startAndUseNewServerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(serverUrlLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(objectDbServerUrlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectToServerButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startAndUseNewServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startAndUseNewServerButtonActionPerformed
	try {
	    JavaProcess.exec(ServerStarter.class);
	    objectDbServerUrlTextField.setText("objectdb://localhost:6136/");
	    this.setVisible(false);
	} catch (IOException ex) {
	    Logger.getLogger(ServerInstanceMissingDialog.class.getName()).log(Level.SEVERE, null, ex);
	} catch (InterruptedException ex) {
	    Logger.getLogger(ServerInstanceMissingDialog.class.getName()).log(Level.SEVERE, null, ex);
	}
    }//GEN-LAST:event_startAndUseNewServerButtonActionPerformed

    private void connectToServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToServerButtonActionPerformed
	this.setVisible(false);
    }//GEN-LAST:event_connectToServerButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectToServerButton;
    private javax.swing.JButton helpWithServerConnectionsButton;
    private javax.swing.JTextField objectDbServerUrlTextField;
    private javax.swing.JLabel problemInfoLabel;
    private javax.swing.JLabel serverUrlLabel;
    private javax.swing.JButton startAndUseNewServerButton;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables

    public String getSelectedServerUrl() {
	return objectDbServerUrlTextField.getText();
    }
}
