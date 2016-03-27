
import java.io.*;


public class A {

   
   	public A(String filename) {
      		FileOutputStream out = null;
      		try {
      			out = new FileOutputStream(new File(filename));

			byte buffer[] = new byte[8];
			for(int i=0;i<8;i++) buffer[i] = (byte) i;

			out.write(buffer, 0, 8);	// dump 8 bytes in the file

			out.write(buffer, 0, 2);	// now dump 2 more bytes which are
							//  byte 1:   (byte) 0
							//  byte 2:   (byte) 1


      			out.close();
      		}
      		catch(IOException e) {
	      		System.err.println(e);
	      		System.exit(-1);
      		}		
   	}

   


   	public static void main(String[] args) {
		if( args.length != 1 ) {
			System.err.println("Provide a filename.");
			System.exit(-1);
		}
		new A(args[0]);
   	}
}
