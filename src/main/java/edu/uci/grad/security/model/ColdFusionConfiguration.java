package edu.uci.grad.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ColdFusionConfiguration {

    @Id
    @GeneratedValue
    private Integer coldFusionConfigurationId;
    private String resetCfScriptUrl,
	    cfAdminUsername,
	    cfAdminPassword,
	    databaseName,
	    dsnName,
	    databaseUserName,
	    databaseHost,
	    databasePort, coldFusionServerName;

    public String getColdFusionServerName() {
	return coldFusionServerName;
    }

    public void setColdFusionServerName(String coldFusionServerName) {
	this.coldFusionServerName = coldFusionServerName;
    }

    public String getResetCfScriptUrl() {
	return resetCfScriptUrl;
    }

    public void setResetCfScriptUrl(String resetCfScriptUrl) {
	this.resetCfScriptUrl = resetCfScriptUrl;
    }

    public String getCfAdminUsername() {
	return cfAdminUsername;
    }

    public void setCfAdminUsername(String cfAdminUsername) {
	this.cfAdminUsername = cfAdminUsername;
    }

    public String getCfAdminPassword() {
	return cfAdminPassword;
    }

    public void setCfAdminPassword(String cfAdminPassword) {
	this.cfAdminPassword = cfAdminPassword;
    }

    public String getDatabaseName() {
	return databaseName;
    }

    public void setDatabaseName(String databaseName) {
	this.databaseName = databaseName;
    }

    public String getDsnName() {
	return dsnName;
    }

    public void setDsnName(String dsnName) {
	this.dsnName = dsnName;
    }

    public String getDatabaseUserName() {
	return databaseUserName;
    }

    public void setDatabaseUserName(String databaseUserName) {
	this.databaseUserName = databaseUserName;
    }

    public String getDatabaseHost() {
	return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
	this.databaseHost = databaseHost;
    }

    public String getDatabasePort() {
	return databasePort;
    }

    public void setDatabasePort(String databasePort) {
	this.databasePort = databasePort;
    }

    public Integer getColdFusionConfigurationId() {
	return coldFusionConfigurationId;
    }

    public void setColdFusionConfigurationId(Integer coldFusionConfigurationId) {
	this.coldFusionConfigurationId = coldFusionConfigurationId;
    }

    @Override
    public String toString() {
	return coldFusionServerName;
    }
}
