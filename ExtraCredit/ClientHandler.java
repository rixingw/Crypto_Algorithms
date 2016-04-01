
import java.lang.*;
import java.io.*;
import java.net.*;
import java.math.BigInteger;
import javax.crypto.*;
import java.util.Scanner;

public class ClientHandler extends Thread {
	private Socket s = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois  = null;
	private BigInteger q = null;
	private BigInteger a = null;

	public ClientHandler(Socket s, BigInteger modulus, BigInteger pRoot) {
		this.s = s;
		this.q = modulus;
		this.a = pRoot;
	try {
	 		oos  = new ObjectOutputStream(s.getOutputStream());
	 		ois  = new ObjectInputStream(s.getInputStream());
		}
		catch (Exception e) {
			System.err.println("Exception: " + e);
		}
	}

	public void run() {
		try {
			System.out.println("Server: Initializing Diffie Hellman protocol...");

        oos.writeObject(q);
        System.out.println("Server: sending modulus (p)...");

        oos.writeObject(a);
        System.out.println("Server: sending prime root (a)...");

        DiffieHellman dh = new DiffieHellman();
        BigInteger y = dh.computePublicKey(q,a);
				System.out.println("Server: Computing public key (Ya)... ");

      	oos.writeObject(y);
        System.out.println("Server: Sending Ya...");

				System.out.println("Client: Waiting for Client's public key (Yb)...");
      	BigInteger yb = (BigInteger)ois.readObject();
      	System.out.println("Server: Recieved Yb.");

				System.out.println("Server: Creating symmetric Key...");
      	BigInteger mkey = dh.computeMasterKey(yb,q);

				String keyHash = DiffieHellman.createHashString(mkey);
				System.out.println("Server: Symmetric Key Created.  \n");
				System.out.println("Hash:" + keyHash);

			 	System.out.println("--------- Secured Communication Initialized -----------");
				// AES aes = new AES(keyHash);
				// SealedObject sealedObj = (SealedObject)ois.readObject();
				// INFO info = (INFO)aes.decrypt(sealedObj);
				// System.out.println(info);
				Scanner scanner = new Scanner(System.in);
				 MListener ml = new MListener(ois, oos, keyHash);
				 ml.setDaemon(false);
				 ml.start();
				 IMAGE_OBJ im = new IMAGE_OBJ("./IMAGE_TO_SEND.png");
				 INFO imgInfo = new INFO("Server", "IMAGE");
				 imgInfo.setData(im);
				 ml.sendMessage(imgInfo);

				 while (scanner.hasNext()){
							 String message = scanner.nextLine();
							 INFO info = new INFO("Server", message);
							 // SealedObject sb = aes.encrypt(info);
							 // oos.writeObject(sb);
							 ml.sendMessage(info);
					 }

				 //ml.sendMessage(new INFO("Server", "Poopie pants"));
			// /*
			//  * (1) SEND a BigInteger, which is Serializable
			//  */
      // s.writeObject(new BigInteger("123456788"));
      //
			// /*
			//  * (2) RECEIVE a BigInteger, which is Serializable
			//  */
      // gInteger bi = (BigInteger) ois.readObject();
      // stem.out.println("Client sent to me: " + bi);
      //
			// /*
			//  * (3) SEND a custom object (** must be Serializable **)
			//  */
      // FO info = new INFO("LEON", new BigInteger("112233445566778899"));
			// for(int i=0; i < info.getArray().length; i++) {
			// 	info.getArray()[i] = i;
			// }
      // s.writeObject(info);
      //
			// /*
			//  * (4) SEND a String, which is Serializable
			//  */
      // s.writeObject(new String("This is a String"));
      //
      //
      //
			// System.out.println("--------- ENCRYPTED SEND/RECEIVE -----------");
      //
      //
			// /*
			//  * Create Key
			//  */
			// 		String password = "12345678";
    	// 		byte key[] = password.getBytes();
    	// 		DESKeySpec desKeySpec = new DESKeySpec(key);
    	// 		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    	// 		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
      //
			// /*
			//  * Create Cipher
			//  */
    	// 		Cipher desCipher_encr = Cipher.getInstance("DES/ECB/PKCS5Padding");
    	// 		Cipher desCipher_decr = Cipher.getInstance("DES/ECB/PKCS5Padding");
			//
			// /*
			//  * Initialize Cipher
			//  */
    	// 		desCipher_encr.init(Cipher.ENCRYPT_MODE, secretKey);
    	// 		desCipher_decr.init(Cipher.DECRYPT_MODE, secretKey);
      //
      //
      //
			// /*
			//  * SEND a String (encrypted)
			//  */
			// String str = new String("Encrypted String :)");
			// SealedObject sealedObj = new SealedObject(str, desCipher_encr);
			// oos.writeObject(sealedObj);
      //
      //
			// /*
			//  * RECEIVE a custom Object (encrypted)
			//  */
			// SealedObject sobj = (SealedObject) ois.readObject();
			// INFO info_en = (INFO) sobj.getObject(desCipher_decr);
			// System.out.println("INFO object (encrypted) sent by client: " );
      // fo_en.printInfo();
      // r(int i=0; i < info_en.getArray().length; i++) {
			// 	System.out.println("\t["+ i + "] " + info_en.getArray()[i]);
      //
      //
			// /*
			//  * SEND IMAGE
			//  */
			// IMAGE_OBJ im = new IMAGE_OBJ("./IMAGE_TO_SEND.png");
			// SealedObject si = new SealedObject(im, desCipher_encr);
			// oos.writeObject(si);
      //





			/*
			 * Close stream
			 */
		//	System.out.println("IO Stream closed.");
	 	//	oos.close();
	 	//	ois.close();

	 		/*
	  		 * Close connection
	  		 */
         	//	s.close();
		}
		catch(Exception e) {
			System.err.println("Exception: " + e);
		}
	}


}
