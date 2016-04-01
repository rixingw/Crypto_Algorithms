
import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import javax.crypto.*;

public class MListener extends Thread {

  private ObjectInputStream ois = null;
  private ObjectOutputStream oos = null;

  private AES aes = null;

  public MListener(ObjectInputStream in, ObjectOutputStream out, String passphrase){
        ois  = in;
        oos = out;
        aes = new AES(passphrase);
  }

  public void sendMessage(Serializable info){
  try{
  //  System.out.println(info);
    SealedObject sb = aes.encrypt(info);
    oos.writeObject(sb);
  }catch(Exception e){
    System.out.println("Error transimitting:" + e);
  }
  }

  @Override
  public void run(){
    try {
        while(true){
          SealedObject sealedObj = (SealedObject)ois.readObject();
          INFO info = (INFO)aes.decrypt(sealedObj);
          if (info.getMessage().equals("IMAGE")){
              IMAGE_OBJ imgObj = (IMAGE_OBJ)info.getData();
              imgObj.SaveImage("./ImageReceived.png");
          }
            System.out.println(info);
      }
    }catch(Exception e) {
  			System.err.println("Exception: " + e);
  	}
  }

}
