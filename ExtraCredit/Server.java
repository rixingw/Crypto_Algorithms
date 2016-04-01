import java.lang.*;
import java.io.*;
import java.net.*;
import java.math.BigInteger;
import javax.crypto.*;
import javax.crypto.spec.*;



public class Server extends Thread {

  private ServerSocket servSock = null;

	private void printInfo(Socket s) {
		InetAddress ia;
	 	System.out.println("\tLocal Port : " + s.getLocalPort());
	 	System.out.println("\tRemote Port: " + s.getPort());

	 	ia = s.getInetAddress();		// REMOTE
	 	System.out.println("\t==> Remote IP: " +ia.getHostAddress());
	 	System.out.println("\t==> Remote Name: " +ia.getHostName());
	 	System.out.println("\t==> Remote DNS Name: " +ia.getCanonicalHostName());

	 	ia = s.getLocalAddress();		// LOCAL
	 	System.out.println("\t==> Local IP: " +ia.getHostAddress());
	 	System.out.println("\t==> Local Name: " +ia.getHostName());
	 	System.out.println("\t==> Local DNS Name: " +ia.getCanonicalHostName());
	}


	public Server(int port) {
		try {
        		servSock = new ServerSocket(port, 5);
	 		      System.out.println("Listening on port " + port);
		}
		catch(Exception e) {
			System.err.println("[ERROR] + " + e);
		}
		this.start();
	}

	public void run() {
		while(true) {
			try {
				System.out.println("Waiting for connections...");
   			Socket s = servSock.accept();
   			System.out.println("Server accepted connection from: " + s.getInetAddress().getHostAddress());

        // Start DiffieHellman
        BigInteger q = DiffieHellman.getModulous();
        BigInteger a = DiffieHellman.getPrimitiveRoot();

				new ClientHandler(s,q,a).start();
			}
			catch(Exception e) {
				System.err.println("[ERROR] + " + e);
			}
		}

	}


	public static void main(String args[]) {
		new Server(1234);
   }

}
