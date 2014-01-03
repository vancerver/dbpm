package edu.uci.grad.security.model;

import javax.persistence.Entity;

@Entity
public class PasswordPolicy {

    private int minimumPasswordLength;
    private int maximumPasswordLength;
    private int maximumNumberOfDigits;
    private int minimumNumberUniqueCharacters;
    private int maximumNumberOfSymbols;
    private int maximumNumberOfLowercase;
    private int maximumNumberOfUppercase;
    private int amountOfCharRepeatsToAllow;
    private boolean policyActive;
    private boolean disallowAlphaSequence;
    private boolean disallowQwertySequence;
    private boolean disallowWhitespace;
    private boolean disallowCharRepeats;
    private boolean isDefault;

    public int getMinimumPasswordLength() {
	return minimumPasswordLength;
    }

    public void setMinimumPasswordLength(int minimumPasswordLength) {
	this.minimumPasswordLength = minimumPasswordLength;
    }

    public int getMaximumPasswordLength() {
	return maximumPasswordLength;
    }

    public void setMaximumPasswordLength(int maximumPasswordLength) {
	this.maximumPasswordLength = maximumPasswordLength;
    }

    public int getMinimumNumberUniqueCharacters() {
	return minimumNumberUniqueCharacters;
    }

    public void setMinimumNumberUniqueCharacters(
	    int minimumNumberUniqueCharacters) {
	this.minimumNumberUniqueCharacters = minimumNumberUniqueCharacters;
    }

    public boolean isPolicyActive() {
	return policyActive;
    }

    public void setPolicyActive(boolean policyActive) {
	this.policyActive = policyActive;
    }

    public int getMaximumNumberOfDigits() {
	return maximumNumberOfDigits;
    }

    public void setMaximumNumberOfDigits(int maximumNumberOfDigits) {
	this.maximumNumberOfDigits = maximumNumberOfDigits;
    }

    public int getMaximumNumberOfSymbols() {
	return maximumNumberOfSymbols;
    }

    public void setMaximumNumberOfSymbols(int maximumNumberOfSymbols) {
	this.maximumNumberOfSymbols = maximumNumberOfSymbols;
    }

    public int getMaximumNumberOfLowercase() {
	return maximumNumberOfLowercase;
    }

    public void setMaximumNumberOfLowercase(int maximumNumberOfLowercase) {
	this.maximumNumberOfLowercase = maximumNumberOfLowercase;
    }

    public int getMaximumNumberOfUppercase() {
	return maximumNumberOfUppercase;
    }

    public void setMaximumNumberOfUppercase(int maximumNumberOfUppercase) {
	this.maximumNumberOfUppercase = maximumNumberOfUppercase;
    }

    public boolean isDisallowAlphaSequence() {
	return disallowAlphaSequence;
    }

    public void setDisallowAlphaSequence(boolean disallowAlphaSequence) {
	this.disallowAlphaSequence = disallowAlphaSequence;
    }

    public boolean isDisallowQwertySequence() {
	return disallowQwertySequence;
    }

    public void setDisallowQwertySequence(boolean disallowQwertySequence) {
	this.disallowQwertySequence = disallowQwertySequence;
    }

    public boolean isDisallowWhitespace() {
	return disallowWhitespace;
    }

    public void setDisallowWhitespace(boolean disallowWhitespace) {
	this.disallowWhitespace = disallowWhitespace;
    }

    public boolean isDisallowCharRepeats() {
	return disallowCharRepeats;
    }

    public void setDisallowCharRepeats(boolean disallowCharRepeats) {
	this.disallowCharRepeats = disallowCharRepeats;
    }

    public int getAmountOfCharRepeatsToAllow() {
	return amountOfCharRepeatsToAllow;
    }

    public void setAmountOfCharRepeatsToAllow(int amountOfCharRepeatsToAllow) {
	this.amountOfCharRepeatsToAllow = amountOfCharRepeatsToAllow;
    }

    public boolean isDefault() {
	return isDefault;
    }

    public void setDefault(boolean isDefault) {
	this.isDefault = isDefault;
    }
}
