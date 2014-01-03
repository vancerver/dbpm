package edu.uci.grad.security.util;

import edu.uci.grad.security.AesCipher;
import edu.uci.grad.security.SystemwideConstants;
import edu.uci.grad.security.model.PasswordPolicy;
import edu.vt.middleware.password.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

public class PasswordUtils {

    public static final String MESSAGE_TITLE_FOR_PASSWORD_NOT_MEETING_POLICY_POPUP = "Invalid password:\n";
    public static final String IV = "1a7fae981ca5e3e0";

    public static String passwordMeetsPolicy(char[] newPassword, PasswordPolicy policy) {
	PasswordValidator validator = getPasswordValidator(policy);
	PasswordData passwordData = new PasswordData();
	passwordData.setPassword(new edu.vt.middleware.password.Password(new String(newPassword)));
	RuleResult result = validator.validate(passwordData);
	String resultString = "";
	if (result.isValid()) {
	    resultString = "Valid password";
	} else {
	    resultString = MESSAGE_TITLE_FOR_PASSWORD_NOT_MEETING_POLICY_POPUP;
	    for (String msg : validator.getMessages(result)) {
		resultString += msg + "\n";
	    }
	}
	return resultString;
    }

    private static PasswordValidator getPasswordValidator(PasswordPolicy policy) {
//		PasswordPolicy policy = PasswordsDatabaseUtil.getCurrentPasswordPolicy();

	List<Rule> ruleList = getRuleList(policy);

	PasswordValidator validator = new PasswordValidator(ruleList);

	return validator;
    }

    private static List<Rule> getRuleList(PasswordPolicy policy) {
	// group all rules together in a List
	List<Rule> ruleList = new ArrayList<Rule>();

	// control allowed characters
	if ((policy.getMaximumNumberOfLowercase() > 0)
		|| (policy.getMaximumNumberOfSymbols() > 0)
		|| (policy.getMaximumNumberOfUppercase() > 0)
		|| (policy.getMaximumNumberOfLowercase() > 0)) {
	    CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
	    if (policy.getMaximumNumberOfDigits() > 0) {
		charRule.getRules().add(new DigitCharacterRule(policy.getMaximumNumberOfDigits()));
	    }
	    if (policy.getMaximumNumberOfSymbols() > 0) {
		charRule.getRules().add(new NonAlphanumericCharacterRule(policy.getMaximumNumberOfSymbols()));
	    }
	    if (policy.getMaximumNumberOfUppercase() > 0) {
		charRule.getRules().add(new UppercaseCharacterRule(policy.getMaximumNumberOfUppercase()));
	    }
	    if (policy.getMaximumNumberOfLowercase() > 0) {
		charRule.getRules().add(new LowercaseCharacterRule(policy.getMaximumNumberOfLowercase()));
	    }
	    charRule.setNumberOfCharacteristics(charRule.getRules().size());
	    ruleList.add(charRule);
	}
	if (policy.isDisallowAlphaSequence()) {
	    AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();
	    ruleList.add(alphaSeqRule);
	}
	if (policy.isDisallowQwertySequence()) {
	    QwertySequenceRule qwertySeqRule = new QwertySequenceRule();
	    ruleList.add(qwertySeqRule);
	}
	if (policy.isDisallowCharRepeats()) {
	    RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(policy.getAmountOfCharRepeatsToAllow());
	    ruleList.add(repeatRule);
	}
	LengthRule lengthRule = new LengthRule(policy.getMinimumPasswordLength(), policy.getMaximumPasswordLength());
	ruleList.add(lengthRule);
	if (policy.isDisallowWhitespace()) {
	    WhitespaceRule whitespaceRule = new WhitespaceRule();
	    ruleList.add(whitespaceRule);
	}

	return ruleList;
    }

    public static char[] generateNewPassword(PasswordPolicy policy) {
	PasswordGenerator generator = new PasswordGenerator();

	if (policy.getMaximumNumberOfDigits() > 0
		|| policy.getMaximumNumberOfSymbols() > 0
		|| policy.getMaximumNumberOfUppercase() > 0
		|| policy.getMaximumNumberOfLowercase() > 0) {
	    // create character rules to generate passwords with
	    List<CharacterRule> rules = new ArrayList<CharacterRule>();
	    if (policy.getMaximumNumberOfDigits() > 0) {
		rules.add(new DigitCharacterRule(policy.getMaximumNumberOfDigits()));
	    }
	    if (policy.getMaximumNumberOfSymbols() > 0) {
		rules.add(new NonAlphanumericCharacterRule(policy.getMaximumNumberOfSymbols()));
	    }
	    if (policy.getMaximumNumberOfUppercase() > 0) {
		rules.add(new UppercaseCharacterRule(policy.getMaximumNumberOfUppercase()));
	    }
	    if (policy.getMaximumNumberOfLowercase() > 0) {
		rules.add(new LowercaseCharacterRule(policy.getMaximumNumberOfLowercase()));
	    }
	    String password = generator.generatePassword(getRandomPasswordLength(policy), rules);
	    return password.toCharArray();
	} else {
	    JOptionPane.showMessageDialog(null, "You may not generate a password when all character rules are set to 0");
	    return new char[0];
	}
    }

    private static int getRandomPasswordLength(PasswordPolicy policy) {
	return new Random().nextInt(policy.getMaximumPasswordLength()
		- policy.getMinimumPasswordLength() + 1)
		+ policy.getMinimumPasswordLength();
    }

    public static String getDecryptedPasswordForUser(String username, byte[] encryptedPassword, byte[] key) {
	return new String(decrypt(encryptedPassword, username, key));
    }

    public static byte[] encrypt(char[] newPassword, byte[] key) {
	AesCipher cipher = new AesCipher(key, getIv());
	try {
	    return cipher.encrypt(newPassword);
	} catch (GeneralSecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    public static char[] decrypt(byte[] encryptedPassword, String user, byte[] key) {
	AesCipher cipher = new AesCipher(key, getIv());
	try {
	    return cipher.decrypt(encryptedPassword);
	} catch (GeneralSecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    private static String getIv() {
	return IV;
    }
}
