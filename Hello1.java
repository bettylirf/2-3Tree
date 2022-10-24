// file: Hello1.java
//		We illustrate the use of random number generators:
//			WATCH CAREFULLY how we use "ss" as random seed!
//			
//		But feel free to add/modify for your own testing.
//
// -- Chee (Data Structure Class 2019)

import java.util.Random;

public class Hello1 {
	static void debug(String s){// helper method
		System.out.print(s);}
	static void debug(String s, int n){// helper method
		System.out.print(s + n);}
	// MAIN=========================:
    public static void main (String[] argv){
		int ss = argv.length>0? Integer.valueOf(argv[0]) : 111;
		int nn = argv.length>1? Integer.valueOf(argv[1]) : 10;
		int mm = argv.length>2? Integer.valueOf(argv[2]) : 1;
		Random rgen = (ss==0)? new Random(): new Random(ss);

		System.out.println("Hello, one world!");
		debug("=====================\n");
		System.out.print("Random integers between 0 and 99: \n"
				+ rgen.nextInt(100));
		if (nn>10) nn=10; // don't print too much!
		for (int i=1; i<nn; i++)
			System.out.print(", " + rgen.nextInt(100));
		debug("\n=====================\n");
    }//main
}//class
