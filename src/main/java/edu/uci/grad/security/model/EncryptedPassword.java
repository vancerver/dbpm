package edu.uci.grad.security.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EncryptedPassword {

    @Id
    private String userName;
    private byte[] encryptedPassword;
    private byte[] iv;

    public byte[] getEncryptedPassword() {
	return encryptedPassword;
    }

    public void setEncryptedPassword(byte[] encryptedPassword) {
	this.encryptedPassword = encryptedPassword;
    }

    public byte[] getIv() {
	return iv;
    }

    public void setIv(byte[] iv) {
	this.iv = iv;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }
}
