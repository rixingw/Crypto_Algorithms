

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
            DesEncrypter DES = new DesEncrypter("test passphrase", "PBEWithMD5AndDES" );
            DES.encrypt(new FileInputStream("smile.png"), new FileOutputStream("smile_encripted"));
            DES.encrypt(new FileInputStream("smile_encripted"), new FileOutputStream("smile_double_encripted"));
            
            DES.decrypt(new FileInputStream("smile_double_encripted"), new FileOutputStream("smile_decripted"));
            DES.decrypt(new FileInputStream("smile_decripted"), new FileOutputStream("smile_original.png"));

        } 
        catch (Exception e) { System.err.println(e); }
    }
}