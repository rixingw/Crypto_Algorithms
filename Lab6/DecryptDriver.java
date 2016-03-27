
class DecryptDriver {

  public static void main(String[] args){
    if( args.length != 2 ) {
       System.err.println("Provide 2 filenames: encrypted output");
       System.exit(-1);
     }

       RSA_WIT rsa = new RSA_WIT("977447",  "649487", "6359");

       rsa.PrintParameters();

       System.out.println(rsa);

       rsa.DecryptFile(args[0], args[1]);

      System.out.println("FINISHED");

  }


}
