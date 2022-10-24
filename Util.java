//class Util 
import java.util.Random;

public class Util{
	// MEMBERS:
	//////////////////////////////////////////
		static int COUNT;
		static Random RG;
		 // 0=silence. Larger number is more verbose
		//  you can change this in the Tree class
		//  and flip it on and off like a switch
		static int verbosity=1;
		
	// METHODS:
	//////////////////////////////////////////
	static void dbug(String ss){ //no \n
		if (verbosity>0) System.out.print(ss); } 
	static void debug(String ss){
		if (verbosity>0) System.out.println(ss); } 
	static void debug(String ss, String ff){
		// E.g., debug("Testing", "\n =========== %s:\n");
		if (verbosity>0) System.out.printf(ff, ss); } 
	String tab (int n, char c){
		// returns a string of length n filled with c
		char[] chars = new char[n];
		java.util.Arrays.fill(chars, c);
		return String.valueOf(chars); }//tab
}
