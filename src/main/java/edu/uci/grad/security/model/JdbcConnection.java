package edu.uci.grad.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class JdbcConnection {

    @Id
    @GeneratedValue
    private Integer jdbcConnectionId;
    private String descriptiveName,
	    jdbcUrl,
	    jdbcDriverClass;

    public String getJdbcDriverClass() {
	return jdbcDriverClass;
    }

    public void setJdbcDriverClass(String jdbcDriverClass) {
	this.jdbcDriverClass = jdbcDriverClass;
    }
    private boolean defaultDatabase;

    public String getDescriptiveName() {
	return descriptiveName;
    }

    public void setDescriptiveName(String descriptiveName) {
	this.descriptiveName = descriptiveName;
    }

    public String getJdbcUrl() {
	return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
	this.jdbcUrl = jdbcUrl;
    }

    @Override
    public String toString() {
	return descriptiveName;
    }

    public boolean isDefaultDatabase() {
	return defaultDatabase;
    }

    public void setDefaultDatabase(boolean defaultDatabase) {
	this.defaultDatabase = defaultDatabase;
    }

    public Integer getJdbcConnectionId() {
	return jdbcConnectionId;
    }

    public void setJdbcConnectionId(Integer jdbcConnectionId) {
	this.jdbcConnectionId = jdbcConnectionId;
    }
}
