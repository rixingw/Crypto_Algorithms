

/*
        Rixing Wu
        1/26/2015
        Lab 1
        COMP3571
*/

import java.io.*;

class Driver{
    public static void main(String[] args){
        try {
            DesEncrypter DES = new DesEncrypter("passcode", "PBEWithMD5AndDES" );
            DES.encrypt(new FileInputStream("smile.png"), new FileOutputStream("smile_DES"));
            
            DesEncrypter TDES = new DesEncrypter("passcode", "PBEWithMD5AndTripleDES" );
            TDES.decrypt(new FileInputStream("smile_DES"), new FileOutputStream("smile_decrypted"));

        }
        catch (Exception e) { System.err.println(e); }
    }
}