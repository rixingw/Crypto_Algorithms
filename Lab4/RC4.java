


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;


public class RC4 {


	/*
		Initialize state (from - 255)
		Scramble state randomly based on key
	*/
	public void ksa(byte state[], byte key[], int len) {
   		int i,j=0; 
		byte t;
   
   		for (i=0; i < 256; ++i)
      			state[i] = (byte) i; 
   		for (i=0; i < 256; ++i) {
      			j = (j + state[i] + key[i % len]) % 256; 
      			// now swap
			i &=0xff;
			j &=0xff;
      			t = state[i]; 
      			state[i] = state[j]; 
      			state[j] = t; 
   		}   
	}



int i=0,j=0; 

public byte prga(byte[] state) { 
   	
   		byte key; 
		byte t;
      	i = (i + 1) % 256; 
      	j = (j + state[i]) % 256; 
		i &=0xff;
		j &=0xff;

		// now swap
		t = state[i]; 
		state[i] = state[j]; 
		state[j] = t; 

		return state[ (((state[i] + state[j]) % 256) &0xff)];

	}  



	public static void main(String args[]) {
		try{
		///////////////////////
		String fileName = args[0];
		Path path = Paths.get(fileName);
		byte[] data = Files.readAllBytes(path);
		String password = args[1];
		///////////////////////

		byte MSG[] = data;
   		int MSG_size = MSG.length;

   		byte key[]=password.getBytes();
   		int PASS_size = key.length;

		RC4 rc4 = new RC4();


   		byte state[] = new byte[256];

   		byte prga_output[] = new byte[MSG_size];
   		byte encrypted[]   = new byte[MSG_size];	   
   		byte decrypted[]   = new byte[MSG_size];	   


   		rc4.ksa(state, key, PASS_size);
   		
   		// Change Every byte
   		for (int b = 0; b < MSG_size; b++){
   			prga_output[b] = rc4.prga(state);
   		}

	
    // ENCRYPTING
	//	System.out.println("\n- Encrypting message: |"+new String(MSG)+"|");
   		for( int i=0; i < MSG_size; i++) {
   			encrypted[i] = (byte) (MSG[i] ^ prga_output[i]);
   		}

		System.out.print("- Below is the encrypted message (in hex):\n\t");
   		for( int i=0; i < MSG_size; i++) {
			String str = String.format("%02X", encrypted[i]);
			System.out.print(str + " ");
		}
		System.out.println();


	


		///////////////////////	 Decryption  Simulation /////////////////	

		// Reset i, j, 
   		rc4.i=0;
   		rc4.j=0;

   		int mSize = encrypted.length;

		byte prga_b[] = new byte[mSize];  
		byte state_b[] = new byte[256];
		rc4.ksa(state_b, key, PASS_size);

		for (int b = 0; b < MSG_size; b++){
   			prga_b[b] = rc4.prga(state_b);
   		}

     	 // DECRYPTING
   		for(int i=0; i < MSG_size; i++) {
   			decrypted[i] = (byte)(encrypted[i] ^ prga_b[i]);	
   		}


		String[] fileEx = fileName.split("\\.(?=[^\\.]+$)");
		String outputFile = fileEx[0] + "Output." + fileEx[1];
		FileOutputStream fos = new FileOutputStream(outputFile);
		fos.write(decrypted);
		fos.close();
		System.out.println(outputFile + " created.");
		///////////////////////
		
	}
		catch (Exception e) { System.err.println(e); }


	}
}
