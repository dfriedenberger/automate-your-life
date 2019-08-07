package de.frittenburger.aylive.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class MD5Calculator {

	
	
	public static String calculate(InputStream is) throws GeneralSecurityException, IOException
	{
	    byte[] buffer = new byte[0xffff];

		int numRead;
		MessageDigest md = MessageDigest.getInstance("MD5");

		 do {
	           numRead = is.read(buffer);
	           if (numRead > 0) {
	        	   md.update(buffer, 0, numRead);
	           }
	     } while (numRead != -1);

		byte[] hash = md.digest();
		
		StringBuilder sb = new StringBuilder(2*hash.length); 
		for(byte b : hash) { 
			sb.append(String.format("%02x", b&0xff)); 
		} 
		return sb.toString();
		
	}

	
}
