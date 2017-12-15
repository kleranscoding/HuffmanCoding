/**
 * HuffmanEncode.java
 * created by Clarence Cheung
 * from CS 367 Summer 2014
 * used in Huffman Coding
 * modified in 2017
 * 
 */

import java.io.*;

public class HuffmanCode {
    
    private static final int argsLen= 4;	

	/** main driver program **/
	public static void main(String[] args) throws IOException {
        
    	// check if enough argument
    	checkArgs(args);
		
    	// check if correct option is supplied
    	menu(args);
        
    } // end of main
	
	
	/** check if correct option **/
    private static void menu(String[] args) {
        // assign arguments to variables
        String option= args[0];
        String inputFile= args[1], outputFile= args[2], codeFile= args[3];
        
        // incorrect option
        correctOption(option);
        
        if (option.equals("c")) {
        	// do compression
        	HuffmanEncode hfencode= new HuffmanEncode();
            hfencode.encode(inputFile,outputFile,codeFile);
        } else if (option.equals("d")) {
            // do decompression
        	HuffmanDecode hfdecode= new HuffmanDecode();
            hfdecode.decode(inputFile,outputFile,codeFile);
        }
    }
    
    
	/** check if enough arguments are supplied **/
    private static void checkArgs(String[] args) {
        if (args.length!=argsLen) {
            usage();
            System.exit(1);
        }
    }
    
    /** prompt correct usage **/
    private static void usage() { System.out.println("usage: java HuffmanCode <c|d> <inputFile> <outputFile> <codeFile>"); }
    
    
    /** check if correct option **/
    private static void correctOption(String option) {
        if (!(option.equals("c") || option.equals("d"))) {
        	System.out.println("your option: "+option);
            System.out.println("valid options are 'c' & 'd'");
            System.exit(1);
        } 
    }
    
}
