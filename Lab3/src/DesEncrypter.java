
import java.security.spec.AlgorithmParameterSpec; 
import java.security.spec.KeySpec;
import javax.crypto.spec.*;
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

	
	

 public byte[] encrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
		
		long startTime = System.nanoTime();
		
	 	for (int t = 0; t < 100; t++){
		             data = ecipher.doFinal(data);
		} 	
	 	
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println((double)elapsedTime/100.0);
		return data;
    	}
    	
public byte[] decrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
			long startTime = System.nanoTime();
			for (int t = 0; t < 100; t++){
	               data = dcipher.doFinal(data);
			}
			long elapsedTime = System.nanoTime() - startTime;
			System.out.println((double)elapsedTime/100.0);
			return  data;
	}

}