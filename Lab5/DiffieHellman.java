
/*
	Rixing Wu
	Lab 4 DiffieHellman
*/
import java.math.BigInteger;
import java.util.Random;
import java.security.SecureRandom;


public class DiffieHellman{

	public BigInteger getPrimeSizeOf(int size){
		 SecureRandom random = new SecureRandom();
       	 BigInteger prime = BigInteger.probablePrime(size, random);
       	 return prime;
	}

	public static void main(String[] args){
		DiffieHellman dh = new DiffieHellman();
		 BigInteger q = dh.getPrimeSizeOf(80);
		 BigInteger a = new BigInteger("3");
		User userA = new User(a, q);
		User userB = new User(a, q);
		BigInteger YA = userA.computeY();
		BigInteger YB = userB.computeY();

		BigInteger comKA = userA.computeCommonKey(YB);
		BigInteger comKB = userB.computeCommonKey(YA);
		System.out.println("UserA Common Key: " + comKA);
		System.out.println("UserB Common Key: " + comKB);
	}
}


class User{
	private BigInteger x;
	public BigInteger a;
	public BigInteger q;

	public User(BigInteger a, BigInteger q){
		 SecureRandom random = new SecureRandom();
       	 this.x = BigInteger.probablePrime(50, random);
       	 this.a = a;
       	 this.q = q;
	}


	public BigInteger computeY(){
		BigInteger y = a.modPow(x, q);
		return y;
	}

	BigInteger computeCommonKey(BigInteger otherY){
		BigInteger commonKey = otherY.modPow(x, q);
		return commonKey;
	}
}
