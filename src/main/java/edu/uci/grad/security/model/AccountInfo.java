package edu.uci.grad.security.model;

import javax.persistence.*;

@Entity
public class AccountInfo {

    @Id
    @GeneratedValue
    private Long id;
    private String userName,
	    databaseName,
	    dsnName;
    @OneToOne
    private ColdFusionConfiguration coldFusionConfiguration;
    @OneToOne
    private JdbcConnection jdbcConnection;
    @ManyToOne
    private PasswordContainer passwordContainer;

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

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    @Override
    public String toString() {
	return userName;
    }

    public ColdFusionConfiguration getColdFusionConfiguration() {
	return coldFusionConfiguration;
    }

    public void setColdFusionConfiguration(ColdFusionConfiguration coldFusionConfiguration) {
	this.coldFusionConfiguration = coldFusionConfiguration;
    }

    public JdbcConnection getJdbcConnection() {
	return jdbcConnection;
    }

    public void setJdbcConnection(JdbcConnection jdbcConnection) {
	this.jdbcConnection = jdbcConnection;
    }

    public PasswordContainer getPasswordContainer() {
	return passwordContainer;
    }

    public void setPasswordContainer(PasswordContainer passwordContainer) {
	this.passwordContainer = passwordContainer;
    }
}
