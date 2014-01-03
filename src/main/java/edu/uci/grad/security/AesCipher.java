/*
 *  Copyright 2010. The Regents of the University of California. All Rights Reserved.
 *  Permission to use, copy, modify, and distribute any part of this software including any source code and documentation for educational, research, and non-profit purposes, without fee, and without a written agreement is hereby granted, provided that the above copyright notice, this paragraph and the following three paragraphs appear in all copies of the software and documentation.
 *  Those desiring to incorporate this software into commercial products or use for commercial purposes should contact Office of Technology Alliances, University of California, Irvine, 380 University Tower, Irvine, CA 92607-7700, Phone: (949) 824-7295, FAX: (949) 824-2899.
 *  IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING, WITHOUT LIMITATION, LOST PROFITS, CLAIMS OR DEMANDS, OR BUSINESS INTERRUPTION, ARISING OUT OF THE USE OF THIS SOFTWARE, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  THE SOFTWARE PROVIDED HEREIN IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS. THE UNIVERSITY OF CALIFORNIA MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES OF ANY KIND, EITHER IMPLIED OR EXPRESS, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE, OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, TRADEMARK OR OTHER RIGHTS.
 */
package edu.uci.grad.security;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encapsulates operations against a AES cipher
 *
 * @author duongb
 * @since 1.1
 */
public class AesCipher {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final Cipher decryptor;
    private final Cipher encryptor;

    public AesCipher(byte[] key, String iv) throws RuntimeException {

	try {

	    Key keySpec = new SecretKeySpec(key, "AES");
//			LOGGER.debug("Created key.");

	    AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
//			LOGGER.debug("Created initialization vector.");

	    decryptor = Cipher.getInstance(TRANSFORMATION);
	    decryptor.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//			LOGGER.debug("Initalizied decryption cipher.");

	    encryptor = Cipher.getInstance(TRANSFORMATION);
	    encryptor.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
//			LOGGER.debug("Initalizied encryption cipher.");

	} catch (Exception ex) {
	    throw new RuntimeException(ex);
	}
    }

    public String decrypt(String text) throws GeneralSecurityException {
	byte[] cryptic = text.getBytes();
	byte[] plain = decryptor.doFinal(cryptic);
	return new String(plain);
    }

    public char[] decrypt(byte[] cryptic) throws GeneralSecurityException {
	byte[] plain = decryptor.doFinal(cryptic);
	return new String(plain).toCharArray();
    }

    public String encrypt(String text) throws GeneralSecurityException {
	byte[] plain = text.getBytes();
	byte[] cryptic = encryptor.doFinal(plain);
	return new String(cryptic);
    }

    public byte[] encrypt(char[] password) throws GeneralSecurityException {
	String text = new String(password);
	byte[] plain = text.getBytes();
	return encryptor.doFinal(plain);
    }

    public static Key generateKey(int keySize) throws GeneralSecurityException {
	try {
	    KeyGenerator keygen = KeyGenerator.getInstance("AES", "SunJCE");
	    keygen.init(keySize);
	    return keygen.generateKey();
	} catch (NoSuchAlgorithmException ex) {
	    throw new GeneralSecurityException(ex);
	}
    }
}
