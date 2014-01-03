package edu.uci.grad.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PasswordContainer {

    @Id
    @GeneratedValue
    private Integer passwordContainerId;
    private String groupName;
    private String databaseFileUrl;
    private Boolean cryptoJCE;

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public String getDatabaseFileUrl() {
	return databaseFileUrl;
    }

    public void setDatabaseFileUrl(String databaseFileUrl) {
	this.databaseFileUrl = databaseFileUrl;
    }

    @Override
    public String toString() {
	return groupName;
    }

    public Integer getPasswordContainerId() {
	return passwordContainerId;
    }

    public void setPasswordContainerId(Integer passwordContainerId) {
	this.passwordContainerId = passwordContainerId;
    }

    public Boolean getCryptoJCE() {
        return cryptoJCE;
    }

    public void setCryptoJCE(Boolean cryptoJCE) {
        this.cryptoJCE = cryptoJCE;
    }
    
    
}
