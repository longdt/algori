/**
 * 
 */
package com.solt.hash;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author thienlong
 * 
 */
public class HashFunction {
	private MessageDigest messageDigest;

	/**
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public HashFunction(String hashType) throws NoSuchAlgorithmException {
		messageDigest = MessageDigest.getInstance(hashType);
	}

	/**
	 * @param string
	 * @return
	 * @throws DigestException
	 */
	public Integer hash(Object object) {
		try {
			byte[] objectBytes = object.toString().getBytes();
			return messageDigest.digest(objectBytes, 0, objectBytes.length);
		} catch (Exception e) {
			return null;
		}
	}

}
