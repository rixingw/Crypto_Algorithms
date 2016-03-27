

/*
        Rixing Wu
        1/26/2015
        Lab 03
        COMP3571
*/

import java.io.*;

class Driver{
    public static void main(String[] args){
        try {
            DesEncrypter DES = new DesEncrypter("smilepass", "PBEWithMD5AndDES" );
            DES.encrypt(new FileInputStream("smile.png"), new FileOutputStream("smile_encripted"));
            DES.decrypt(new FileInputStream("smile_encripted"), new FileOutputStream("smileDecripted.png"));
            
            DesEncrypter TDES = new DesEncrypter("smilepass", "PBEWithMD5AndTripleDES" );
            TDES.encrypt(new FileInputStream("smile.png"), new FileOutputStream("smile_3DESencripted"));
            TDES.decrypt(new FileInputStream("smile_3DESencripted"), new FileOutputStream("smile3DESDecripted"));
        } 
        catch (Exception e) { System.err.println(e); }
    }
}
