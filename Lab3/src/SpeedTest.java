

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;




public class SpeedTest {
	



	public static void DESTest(){
		String fileName = "ThreeLittlePig.txt";
		String password = "passcode";
		
		try {
			Path path = Paths.get(fileName);
			byte[] data = Files.readAllBytes(path);
			System.out.println("DES Encrypt/Decrypt");
			for (int i = 0; i <200; i++){
				DesEncrypter DES = new DesEncrypter(password, "PBEWithMD5AndDES" );
				data = DES.encrypt(data);
				data =  DES.decrypt(data);
			}
			FileOutputStream fos = new FileOutputStream("DesDecripted.txt");
			fos.write(data);
			fos.close();
	
		} 
		catch (Exception e) { System.err.println(e); }
	}
	
	
	
	
public static void TripleDesTest(){
		String fileName = "ThreeLittlePig.txt";
		String password = "passcode";
		
		try {
			Path path = Paths.get(fileName);
			byte[] data = Files.readAllBytes(path);
			System.out.println("Triple Des Encrypt/Decrypt");
			for (int i = 0; i <200; i++){
				DesEncrypter TripleDES = new DesEncrypter(password, "PBEWithMD5AndTripleDES" );
				data = TripleDES.encrypt(data);
				data =  TripleDES.decrypt(data);
			}
	
			FileOutputStream fos = new FileOutputStream("TripleDesDecripted.txt");
			fos.write(data);
			fos.close();
			
		} 
		catch (Exception e) { System.err.println(e); }
	}
	


	public static void main(String[] args){
		
		SpeedTest.DESTest();
		System.out.println();
		SpeedTest.TripleDesTest();
	}
	
}
