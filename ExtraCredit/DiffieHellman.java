import java.io.PrintStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

class DiffieHellman {
    private BigInteger privateKey = DiffieHellman.getRandomPrime(500);

    public BigInteger computePublicKey(BigInteger q, BigInteger a) {
        BigInteger y = a.modPow(this.privateKey, q);
        return y;
    }

    public BigInteger computeMasterKey(BigInteger y, BigInteger q) {
        BigInteger k = y.modPow(this.privateKey, q);
        return k;
    }

    public static BigInteger getPrimitiveRoot() {
        BigInteger a = DiffieHellman.getRandomPrime(101);
        return a;
    }

    public static BigInteger getModulous() {
        BigInteger q = DiffieHellman.getRandomPrime(600);
        return q;
    }

    private static BigInteger getRandomPrime(int n) {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger bi = BigInteger.probablePrime(n, secureRandom);
        return bi;
    }

    public static String createHashString(BigInteger bigInteger) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        }
        catch (Exception e) {
            System.out.println("Error:" + e);
        }
        byte[] arrby = bigInteger.toByteArray();
        messageDigest.update(arrby);
        byte[] arrby2 = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte by : arrby2) {
            stringBuffer.append(Integer.toString((by & 255) + 256, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
