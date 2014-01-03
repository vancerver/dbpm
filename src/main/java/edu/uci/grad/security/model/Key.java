package edu.uci.grad.security.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Key {

    @Id
    private String user;
    private byte[] key;

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public byte[] getKey() {
	return key;
    }

    public void setKey(byte[] key) {
	this.key = key;
    }
}
