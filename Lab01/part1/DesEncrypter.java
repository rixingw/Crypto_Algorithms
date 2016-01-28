import java.io.*;
import java.security.spec.AlgorithmParameterSpec; 
import java.security.spec.KeySpec;
import javax.crypto.spec.*;
//import java.security.Provider;


import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import javax.crypto.*;

public class DesEncrypter {
	private Cipher ecipher;
	private Cipher dcipher;
	
        // Create an 8-byte initialization vector
        byte[] iv = new byte[]{
        	(byte)0x8E, (byte)0x12, (byte)0x39, (byte)0x9C,
            	(byte)0x07, (byte)0x72, (byte)0x6F, (byte)0x5A
        	};


	



	DesEncrypter(String passPhrase, String ALG) {
            	// Prepare the parameter to the ciphers
    		int iterationCount = 19; // Iteration count
            	AlgorithmParameterSpec paramSpec = new PBEParameterSpec(iv, iterationCount);


 		try {
            		// Create the key
            		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), iv, iterationCount);
            		SecretKey key = SecretKeyFactory.getInstance(ALG).generateSecret(keySpec);	


            		ecipher = Cipher.getInstance(key.getAlgorithm());
            		dcipher = Cipher.getInstance(key.getAlgorithm());


            		// Create the ciphers
            		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);


        	} 
		catch (java.security.InvalidAlgorithmParameterException e) { System.err.println(e); } 
		catch (java.security.spec.InvalidKeySpecException e) 	   { System.err.println(e); } 
		catch (javax.crypto.NoSuchPaddingException e) 		   { System.err.println(e); } 
		catch (java.security.NoSuchAlgorithmException e) 	   { System.err.println(e); } 
		catch (java.security.InvalidKeyException e) 		   { System.err.println(e); }
	}


	/*
    	 * Create a key like: SecretKey key = KeyGenerator.getInstance("DES").generateKey();
	 * and pass the key to this constructor.
	 */
	/*
	DesEncrypter(SecretKey key) {

        	AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        	try {
            		ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            		dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            		// CBC requires an initialization vector
            		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        	} 
		catch (java.security.InvalidAlgorithmParameterException e) { System.err.println(e); } 
		catch (javax.crypto.NoSuchPaddingException e) 		   { System.err.println(e); } 
		catch (java.security.NoSuchAlgorithmException e) 	   { System.err.println(e); } 
		catch (java.security.InvalidKeyException e) 		   { System.err.println(e); }
    	}
	*/




    	public void encrypt(InputStream in, OutputStream out) {
		byte[] buf = new byte[1024];
        	try {
            		// Bytes written to out will be encrypted
            		out = new CipherOutputStream(out, ecipher);

            		// Read in the cleartext bytes and write to out to encrypt
            		int numRead = 0;
            		while ((numRead = in.read(buf)) >= 0) {
                		out.write(buf, 0, numRead);
            		}
            		out.close();
        	} 
		catch (java.io.IOException e) { System.err.println("encrypt: " + e);}
    	}



    	public void decrypt(InputStream in, OutputStream out) {
		byte[] buf = new byte[1024];
        	try {
            		// Bytes read from in will be decrypted
            		in = new CipherInputStream(in, dcipher);

            		// Read in the decrypted bytes and write the cleartext to out
            		int numRead = 0;
            		while ((numRead = in.read(buf)) >= 0) {
                		out.write(buf, 0, numRead);
            		}
            		out.close();
        	} 
		catch (java.io.IOException e) { System.err.println("decrypt: " + e);}
    	}


	//
	// This method is implemented in Listing All Available Cryptographic Services
	//
	private static void printCryptoImpls(String serviceType) {
		String[] names;
        	Set<String> result = new HashSet<String>();

        	// All providers
        	Provider[] providers = Security.getProviders();
        	for (int i=0; i<providers.length; i++) {

            		// Get services provided by each provider
            		Set keys = providers[i].keySet();


            		/*for (Iterator it=keys.iterator(); it.hasNext(); ) {
                		String key = (String)it.next();
				System.out.println(key);
			}*/

            		for (Iterator it=keys.iterator(); it.hasNext(); ) {

                		String key = (String)it.next();

                		key = key.split(" ")[0];

                		if (key.startsWith(serviceType + ".")) {
                    			result.add(key.substring(serviceType.length() + 1));
                		} 
				else if (key.startsWith("Alg.Alias." + serviceType + ".")) {
                    			// This is an alias
                    			result.add(key.substring(serviceType.length() + 11));
                		}
            		}
        	}

        	names =  (String[])result.toArray(new String[result.size()]);
		for(int i=0; i < names.length; i++)
			System.out.println("\t"+names[i]);
    	}



	public static void main(String [] args) {

		try {
			/*
			System.out.println("Crypto Implementations:");
			printCryptoImpls("Cipher");
			System.out.println("Key Generators:");
			printCryptoImpls("KeyGenerator");
			*/
			
            		/*
			 * Pass it either "PBEWithMD5AndDES" or "PBEWithMD5AndTripleDES" for DES or 3DES respectively
			 */
    			DesEncrypter DES = new DesEncrypter("smilepass", "PBEWithMD5AndDES" );
    			DES.encrypt(new FileInputStream("smile.png"), new FileOutputStream("smile_encripted"));
    			DES.decrypt(new FileInputStream("smile_encripted"), new FileOutputStream("smileDecripted.png"));

    			DesEncrypter TDES = new DesEncrypter("smilepass", "PBEWithMD5AndTripleDES" );
    			TDES.encrypt(new FileInputStream("smile.png"), new FileOutputStream("smile_3DESencripted"));
    			TDES.decrypt(new FileInputStream("smile_3DESencripted"), new FileOutputStream("smile3DESDecripted"));
		} 
		catch (Exception e) { System.err.println(e); }
	}
}
