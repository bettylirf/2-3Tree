//  file: Hello.java
//		This program should be in every folder for testing!
//			-- It illustrates the processing of the standard
//				command line arguments (ss, nn, mm).
//			-- Feel free to add to this program for your own testing.
// -- Chee (Basic Algorithms, Fall 202)

public class Hello {
	// METHODS======================:
	static void debug(String s){// helper method
		System.out.print(s);}

	// MAIN=========================:
    public static void main (String[] argv){
		int ss = argv.length>0? Integer.valueOf(argv[0]) : 1111;
		int nn = argv.length>1? Integer.valueOf(argv[1]) : 100;
		int mm = argv.length>2? Integer.valueOf(argv[2]) : 10;
      System.out.println("Hello, world!");
	  debug("====================\n");
	  debug(" ss = " + String.valueOf(ss) +"\n");
	  debug(" nn = " + String.valueOf(nn) +"\n");
	  debug(" mm = " + String.valueOf(mm) +"\n");
	  debug(" ====================\n");
    }//main
}//class
