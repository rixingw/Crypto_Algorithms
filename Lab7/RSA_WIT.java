
import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.*;

/**
 * An implementation of the RSA algorithm.
 * It encrypts and decrypts a file.  To be used an a demonstration program.
 * It can be split to only encrypt or only decrypt (now does it both).
 * <P>
 * If only the compiled code of this implementation is given to you, you'll need to write a driver program
 * that looks like this:
 * <pre>
 *
 */
public class RSA_WIT {
	public static void main(String[] args) {

		if( args.length != 3 ) {
			System.err.println("Provide 3 filenames: plain encrypted decrypted.");
			System.exit(-1);
		}

      		RSA_WIT rsa = new RSA_WIT("977447",  "649487", "6359");		// see the constructor for details.
					//  p            q     publicKey


		rsa.PrintParameters();

      		System.out.println(rsa);

		//rsa.EncryptDecrypt(args[0], args[2]);	// encrypt/decrypt on the fly

		rsa.EncryptFile(args[0], args[1]);	// Encrypt file
      		rsa.DecryptFile(args[1], args[2]);	// Decrypt file

		System.out.println("FINISHED");
   	}


	private final int NB = 4;			// block size for encryption


	private final static BigInteger one      = new BigInteger("1");
   	private final static SecureRandom random = new SecureRandom();

   	private BigInteger privateKey;
   	private BigInteger publicKey;
   	private BigInteger modulus;
      	private BigInteger p = null;
      	private BigInteger q = null;
      	private BigInteger phi = null;


	/**
	 * Perform encryption
	 */
   	private BigInteger encrypt(BigInteger message) {
      		return message.modPow(publicKey, modulus);			// 2N-bit number
   	}

	/**
	 * Perform decryption
	 */
   	private BigInteger decrypt(BigInteger encrypted) {
      		return encrypted.modPow(privateKey, modulus);			// 2N-bit number
   	}

	/**
	 * Formats the public and private keys as well as the modulus.
	 * @return returns a formated string of containing the public, private and modulus.
	 */
   	public String toString() {
      		String s = "";
      		s += "+--------------------------------------\n";
      		s += "|  public  = " + publicKey  + "\n";
      		s += "|  private = " + privateKey + "\n";
      		s += "|  modulus = " + modulus + "\n";
      		s += "+--------------------------------------";
      		return s;
   	}

	/**
	 * Print all parameters of of the algorithm used.
	 */
	public void PrintParameters() {
      		System.out.println("+--------------------------------------");
      		System.out.println("|p:       (" + p.bitLength() +          "-bits) " + p);
      		System.out.println("|q:       (" + q.bitLength() +          "-bits) " + q);
      		System.out.println("|phi:     (" + phi.bitLength() +        "-bits) " + phi);
      		System.out.println("|modulus: (" + modulus.bitLength() +    "-bits) " + modulus);
      		System.out.println("|PU key:  (" + publicKey.bitLength() +  "-bits) " + publicKey);
      		System.out.println("|PR key:  (" + privateKey.bitLength() + "-bits) " + privateKey);
      		System.out.println("+--------------------------------------");
	}

   	/**
    	 * Sets up the public, private and modulus.
    	 *
	 * <P>
	 * A brief review on exponents:
	 * <pre>
     	 * 0<=x<2^n
    	 * 0<=y<2^n
    	 * 0<= x*y < 2^n * 2^n which is 0<= x*y < 2^(2n)
    	 *
    	 * a1 = 2^(2n)
    	 * b1 = 2^m
    	 * a1 * b1 = 2^(2n) * 2^m = 2^(2n+m)
	 * </pre>
	 * <P>
	 * To generate a (let's say) 20-bit prime number write an execute the following program:
	 * <pre>
	 * import java.math.BigInteger;
	 * import java.util.Random;
	 * import java.security.SecureRandom;

	 * public class RandomPrime {
    	 * 	public static void main(String[] args) {
         * 		SecureRandom random = new SecureRandom();
         * 		BigInteger prime = BigInteger.probablePrime(20, random);	// for a 20-bit random prime number
         *		System.out.println(prime);					// See your number.
     	 *	}
	 * }
	 * </pre>
    	 *
    	 */
   	public RSA_WIT(String the_p, String the_q, String the_public_key) {
      		p = new BigInteger(the_p);					// N-bit number  (20 bits)
      		q = new BigInteger(the_q);					// N-bit number  (20 bits)
      		phi = (p.subtract(one)).multiply(q.subtract(one));		// 2N-bit number

      		modulus    = p.multiply(q);                                  	// 2N-bit number (common value in practice = 2^16 + 1)
      		publicKey  = new BigInteger(the_public_key);   			// (13 bits)
      		privateKey = publicKey.modInverse(phi);				// 2N-bit number (same as phi)
   	}

   	/*
    	 * A 40 bit BigInteger is represented as byte[5] (5*8=40bits).
    	 * However, toByteArray() needs an additional bit for the sign when converting the BigInteger into a
    	 * byte[].  That means that a 40-bit BigInteger needs 6 bytes to be stored.  So, we are dumping into the file
    	 * 6-byte BigIntegers (or 40/8 + 1 = 6).
    	 */
   	private void Write_BigInteger_to_file(FileOutputStream out, BigInteger bi) {
	      int ss = modulus.bitLength()/8 + 1;

	      byte[] d = bi.toByteArray();
	      byte[] dd = new byte[ss];


	      if( d.length > ss ) {	// don't know why we would ever get this!
		      System.err.println("SIZE IS: " + d.length);
		      System.exit(-1);
	      }

	      // initialize our array to 0s
	      for( int y=0; y< dd.length; y++) {
		      dd[y] = (byte) 0;
	      }

	      // convert d array (which has size NB+1 or less into an NB+1 sized array
	      for( int y=dd.length-1, x=d.length-1;    x >= 0;     x--, y--){
		      dd[y] = d[x];
	      }


	      // dump data into the file
      	      try {
		      out.write(dd);
		      out.flush();
	      }
      	      catch(IOException ee) {
	      	System.err.println(ee);
	      	System.exit(-1);
      	      }
   	}


	/*
	 * Entry point of application that expect 3 file names.
	 * <OL>
	 * 	<LI> plaintext file name (could be a binary file as well) MUST EXIST!
	 * 	<LI> encrypted file name (encrypted content of the plaintext file)
	 * 	<LI> decrypted file name (decrypt the encrypted file yielding the plaintext's contents)
	 * </OL>
	 */
	/*
   	public static void main(String[] args) {

		if( args.length != 3 ) {
			System.err.println("Provide 3 filenames: plain encrypted decrypted.");
			System.exit(-1);
		}

      		RSA_WIT rsa = new RSA_WIT("977447",  "649487", "6359");


		rsa.PrintParameters();

      		System.out.println(rsa);

		//rsa.EncryptDecrypt(args[0], args[2]);	// encrypt/decrypt on the fly

		rsa.EncryptFile(args[0], args[1]);	// Encrypt file
      		rsa.DecryptFile(args[1], args[2]);	// Decrypt file

		System.out.println("FINISHED");
   	}
	*/



	/**
	 * Encrypt/decrypt an input file on the fly.
	 * Encrypt the plaintext by a block and then decrypt that block and dump it in the output file.
	 * So, the input and the output file are identical.
	 * @param inf the name of the plaintext file.
	 * @param outf the name of the file that will be produced and will have the same content as the plaintext file.
	 */
	public void EncryptDecrypt(String inf, String outf) {
		FileOutputStream out = null;
      		FileInputStream  in  = null;
      		try {
      			out = new FileOutputStream(new File(outf));
      			in  = new FileInputStream(new File(inf));
      		}
      		catch(IOException e) {
	      		System.err.println(e);
	      		System.exit(-1);
      		}

		int w = 0;
		byte[] buffer = new byte[NB];
		try {
			while( (w = in.read(buffer)) != 0 ) {	// read a block of data at a time
				if( w == NB ) {
      	      				BigInteger message = new BigInteger(1, buffer);		// convert message into a number from byte[], without the 1, gives negative numbers.
              				BigInteger encrypt = encrypt(message);			// encrypt
              				BigInteger decrypt = decrypt(encrypt);			// decrypt right after we encrypt.



					// System.out.println(message.toByteArray().length + " " + encrypt.toByteArray().length + " " + decrypt.toByteArray().length + " " + message + " " + encrypt + " " + decrypt);

					byte[] dd = decrypt.toByteArray();	// get bytes of the decrypted message


					if( dd.length > NB ) {					// message was originally NB number of bytes
						byte[] gg = new byte[NB];			// if more than NB, get the last NB bytes only.
						for(int r=0; r<NB; r++)	gg[r] = (byte)0;	// init array to 0s

						for(int t=NB-1, q=dd.length-1; t>=0 && q>=0; q--, t--){	// grab only the last NB bytes
							gg[t] = dd[q];
						}
						out.write(gg);					// dump the NB bytes into the output file
					}
					else if( dd.length == NB ) {
						out.write(decrypt.toByteArray());		// correct size.
					}
					else {							// need to dump NB number of byte (pre-pend 0s first)
						byte[] gg = new byte[NB];
						for(int r=0; r<NB; r++)	gg[r] = (byte)0;

						for(int c=NB-1, t=dd.length-1; c >= 0 && t >= 0; c--, t--){
							gg[c] = dd[t];
						}
						out.write(gg);
					}

				}
				else if( w == -1 ) {
					// no more data, processed entire file
					break;
				}
				/*
				 * The loop checks for 0 (that we never get).
				 * We'll get -1 at the end of the file - and we break
				 * We'll get less than NB and the else part above will handle it.
				 *
				else {	// why in the world whould I ever get here? (input file is in blocks of NB...)
      	      				BigInteger message = new BigInteger(1, buffer);
              				BigInteger encrypt = encrypt(message);
              				BigInteger decrypt = decrypt(encrypt);

					out.write(decrypt.toByteArray());

					break;
				}
				*/
			}
		}
		catch(IOException er) {
			System.err.println(er);
			System.exit(-1);
		}



		/*
		 * Close files
		 */
      		try {
      			out.close();
			in.close();
      		}
      		catch(IOException ee) {
	      		System.err.println(ee);
	      		System.exit(-1);
      		}
	}



	/**
	 * Encrypts an input file.
	 * @param inf is the plaintext
	 * @param outf is the encrypted file
	 */
	public void EncryptFile(String inf, String outf) {

      		FileOutputStream out = null;
      		FileInputStream  in  = null;
      		try {
      			out = new FileOutputStream(new File(outf));
      			in  = new FileInputStream(new File(inf));
      		}
      		catch(IOException e) {
	      		System.err.println(e);
	      		System.exit(-1);
      		}

		int w = 0;
		byte[] buffer = new byte[NB];
		try {

			//************************************************************************///
			// Ecrypt Header information
			byte lbs = (byte)(in.available() % NB);
			byte[] lbbytes = new byte[NB];
			for (int i = 0; i < NB; i++){
				lbbytes[i] = (byte)0;
			}
			lbbytes[0] = lbs;
			BigInteger header = new BigInteger(1, lbbytes);
			BigInteger e_header = encrypt(header);
			Write_BigInteger_to_file(out,e_header);

			//************************************************************************///

			while( (w = in.read(buffer)) != 0 ) {
				if( w == NB ) {
  	      				BigInteger message = new BigInteger(1, buffer);
          				BigInteger encrypt = encrypt(message);
					/*
              				BigInteger decrypt = decrypt(encrypt);

	      				System.out.println("\t------------------------------------");
              				System.out.println("\t (" + message.bitLength() + " bits) message   = " + message);
              				System.out.println("\t (" + encrypt.bitLength() + " bits) encrpyted = " + encrypt);
              				System.out.println("\t (" + decrypt.bitLength() + " bits) decrypted = " + decrypt);
	      				System.out.println();
					*/

	      				Write_BigInteger_to_file(out, encrypt);
				}
				else if( w == -1 ) {
					// no more data, processed entire file
					break;
				}
				else {
       		 			// In case we did not encrypt everything, because the last block was not NB size.
					/*
	       		 	 	 * copy leftover from data array into t[] -  (pad goes in front)
	       		 	 	 */
	      				int j = 0;
								byte [] t = new byte[NB];

	      				//int padSize = NB -  (bytes.length % NB); // size of pad
	      				int padSize = NB -  w; 			 // size of pad
	      				for(j=0; j < padSize; j++)  {		 // insert the pad
		      				t[j] = (byte) 0;
	      				}


	      				for(int i=0; i < w; i++, j++) {	// copy data into t[]
		        		//	System.out.println("To assign in t[" + j + "] <- buffer["+i+"]");
	      					t[j] = buffer[i];
	      				}


      	      				BigInteger message = new BigInteger(1, t);
              				BigInteger encrypt = encrypt(message);
					/*
              				BigInteger decrypt = decrypt(encrypt);

	      				System.out.println("\t------------------------------------");
              				System.out.println("\t (" + message.bitLength() + " bits) message   = " + message);
              				System.out.println("\t (" + encrypt.bitLength() + " bits) encrpyted = " + encrypt);
              				System.out.println("\t (" + decrypt.bitLength() + " bits) decrypted = " + decrypt);
	      				System.out.println();
					*/


	      				Write_BigInteger_to_file(out, encrypt);

					break;
				}
			}
		}
		catch(IOException er) {
			System.err.println(er);
			System.exit(-1);
		}



		/*
		 * Close files
		 */
      		try {
      			out.close();
			in.close();
      		}
      		catch(IOException ee) {
	      		System.err.println(ee);
	      		System.exit(-1);
      		}

	}


   	/**
	 * Decrypt a file and place it in another file.
	 * @param fname encrypted file name
	 * @param outfile file containing the produced (decrypted) content.
	 */
	public void DecryptFile(String fname, String outfile) {
		FileOutputStream out = null;		// OUTPUT FILE (encrypted file)
      		FileInputStream in = null;		// INPUT FILE (decrypted file)

		//
		// Open the input and output files
		//
      		try {
							out = new FileOutputStream(new File(outfile));
      				in = new FileInputStream(new File(fname));
      		}
      		catch(IOException e) {
	      		System.err.println(e);
	      		System.exit(-1);
      		}




		int ss = modulus.bitLength()/8 + 1;	// calculate how many BYTES the module is (ss).
      		byte [] tr = new byte[ss];		// modulus is 5 bytes (40 bits) - need one more byte
							// tr will hold (ss) number of bytes from the encrypted file.
      		try {
						//System.out.println("available : " + in.available());
						//************************************************************************///
						// Decrypt Header information
						byte[] fblock = new byte[ss];
						int fsize = in.read(fblock);
						BigInteger e_header = new BigInteger(fblock);
						BigInteger d_header = decrypt(e_header);
						fblock = d_header.toByteArray();
						int  fmd = (int)fblock[0];
						//System.out.println("Remining : " + fmd);
						//************************************************************************///
					//	System.out.println("available : " + in.available());

						int ret = -1;
      			while ( (ret = in.read(tr)) == ss ) {	// go in a loop and read (ss) number of bytes from the encrypted file.
					      			BigInteger enc = new BigInteger(tr);	// convert the encrypted bytes into a BigInteger
				              BigInteger decrypt = decrypt(enc);	// Decrypt data
											byte[] data = decrypt.toByteArray();	// Convert the decrypted BigInteger into an array of bytes
										if( data.length > NB ) {				// dump only the last NB bytes
															byte[] gg = new byte[NB];			// we encrypted the file using NB number of bytes
															// (now we ended up with data.length number of bytes)
															// We will use only the last NB number of bytes.
															// *****  TODO *****
															//
															// Copy the last NB number of bytes from the data[] into the gg[]
															for(int r=0; r<NB; r++)	gg[r] = (byte)0;	// initialize array to all 0s
															int j = NB-1;
															for (int i = data.length-1; j >= 0; i--){
																	gg[j] = data[i];
																	j--;
															}

												out.write(gg);					// dump gg[] into the output file (these are the decrypted bytes.

											}
											else if( data.length == NB ) {				// dump all data
												out.write(data);
											}
											else {
																if( in.available() > 0 ) {	// if there is more data in the encrypted file,
																	//
																	// *****  TODO *****
																	//
																	// Create an array that can hold NB number of bytes
																	// Initialize the array with 0s
																	// Copy the last NB number of bytes from data[] into the newly created array.
																	// Dump the newly created array into the file.

																	 byte[] gg = new byte[NB];
																	for(int r=0; r<NB; r++)	gg[r] = (byte)0;	// initialize array to all 0s
																		int j = data.length-1;
																		for (int i = NB-1; j >= 0; i--){
																			gg[i] = data[j];
																			j--;
																		}


																	out.write(gg);
																}
																else {
																//
																 byte[] gg = new byte[fmd];
																for (int r = 0; r < fmd; r++) {
																	gg[r] = (byte)0;
																}
																//	System.out.println("fmd:" + fmd);
																//	System.out.println("data " + data.length);
																	int j = data.length - 1;
																for (int i = fmd-1; i>=0; i--){
																				gg[i] = data[j];
																 				System.out.println("gg [" + i + "] <-- data [" + j + "]" );
																				j--;
																				if (j < 0){break;}
																}

																	out.write(gg);	// this is last thing we read
													}
											}
      			}
      		}
      		catch(IOException ioe) {
	      		System.err.println(ioe);
	      		System.exit(-1);
      		}
      		finally {
	      		try {
      				if( in != null)  in.close();		// Close the inputfile
      				if( out != null) out.close();		// Close the outputfile

	      		}
	      		catch(IOException e) {}
      		}
   	}
}
