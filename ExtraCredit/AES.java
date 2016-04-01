import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.SealedObject;
import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Security;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;

public class AES
{
  byte[] iv = new byte[]
  {
    0x00, 0x01, 0x02, 0x03,
    0x04, 0x05, 0x06, 0x07,
    0x08, 0x09,0x0a, 0x0b,
    0x0c, 0x0d, 0x0e, 0x0f
  };

  private byte[] salt = "WENTWORTH_INSTITUTE".getBytes();
  private Cipher cipher_encr;
  private Cipher cipher_decr;

  public AES(String passphrase){
    try{
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      PBEKeySpec pbeKeySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 65536, 256);
      SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
      SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

      cipher_encr = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher_encr.init(Cipher.ENCRYPT_MODE, secret , new IvParameterSpec(iv));
      cipher_decr = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher_decr.init(Cipher.DECRYPT_MODE, secret , new IvParameterSpec(iv));

    }catch(Exception e){
      System.out.println("Error!" + e);
      System.exit(-1);
    }
  }


  /**     AES encrypton
  *  @param data  Object to Encrypt
  *  @return an encrypted Object
  */
  public SealedObject encrypt(Serializable data)
  throws Exception{
      return new SealedObject(data, cipher_encr);
  }

  /**
  *   - AES decrypton
  *  @param sealedObj Encrypted Object
  *  @return an Serializable Object
  */
  public Serializable decrypt(SealedObject sealedObj)
   throws Exception{
      return (Serializable)sealedObj.getObject(cipher_decr);
  }


}
