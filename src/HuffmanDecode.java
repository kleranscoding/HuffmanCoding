/**
 * HuffmanDecode.java
 * created by Clarence Cheung
 * from CS 367 Summer 2014
 * used in Huffman Coding
 * modified in 2017
 * 
 */

import java.io.*;
import java.util.*;
import java.nio.charset.Charset;

public class HuffmanDecode {

	/** fields **/
	private static final String DELIMITER=" ";
    private static final Charset CharsetUTF8= Charset.forName("UTF-8");
    private HuffmanNode root;
	
    
	/** constructor **/
	public HuffmanDecode() { }
    
    
    /** methods **/
    
	/** runs the decompression routine **/
    public void decode(String inputFile, String outputFile,String codeFile) { 
        // create root node
    	HuffmanNode node= new HuffmanNode("",'\0',null,null);
    	root= rebuildHuffmanTree(codeFile,node);
    	if (root==null) return;
    	decodeFileFromHuffTree(inputFile,outputFile);
    } // end of decode

	
    /**
     * rebuild Huffman Tree from code file 
     * @param codeFile
     * @param root
     * @return root
     */
    private HuffmanNode rebuildHuffmanTree(String codeFile,HuffmanNode root) {
        
        try {
        	BufferedReader bufReader= new BufferedReader(new InputStreamReader(new FileInputStream(codeFile),CharsetUTF8));
			String eachLine= "";
        	while ((eachLine=bufReader.readLine()) != null) {
			    String[] lineArr= eachLine.split(DELIMITER);
        		if (lineArr.length==2) {
        		    String character= lineArr[0];
    			    char[] code= lineArr[1].toCharArray();
    			    root= constructHuffmanTree(root,character,code,0);
			    } else if (lineArr.length==3) {
			    	root= constructHuffmanTree(root," ",lineArr[2].toCharArray(),0); 
			    }
			}
        	bufReader.close();
        	return root;
        	
        } catch (Exception e) {
        	System.err.println("Error: " + e.getMessage());
            return null;
        }
    } // end of rebuildHuffmanTree
    
    
    /**
     * helper function for rebuilding the Huffman Tree
     * @param node
     * @param character
     * @param code
     * @param index
     * @return node
     */
    private HuffmanNode constructHuffmanTree(HuffmanNode node,String character,char[] code, int index) {
        // base case: reaches end of code and creates a leaf node
        if (index>=code.length) { return new HuffmanNode(character,'\0'); }
        // creates a new node if current node is null
    	if (node==null) { node= new HuffmanNode(null,'\0',null,null); }
    	// create left and right children
    	if (code[index]-'0'==0) {
    	    node.left= constructHuffmanTree(node.left,character,code,index+1);
    	} else if (code[index]-'0'==1) {
    	    node.right= constructHuffmanTree(node.right,character,code,index+1);
    	} else throw new IllegalArgumentException();
    	// return the node
    	return node;
    } // end of constructHuffmanTree
    
    
    /**
     * decode file using Huffman Tree
     * @param inputFile
     * @param outputFile
     */
    private void decodeFileFromHuffTree(String inputFile, String outputFile) {
        try {
        	BufferedReader bufReader= new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),CharsetUTF8));
        	BufferedWriter bufWriter= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),CharsetUTF8));
            
        	HuffmanNode node= root;
            int character;
        	while ((character= bufReader.read())!= -1) {
        	    char c= (char) character;
        	    // decode
        	    node= decodeCharacter(node,c);
        	    // if reaches the leaf node 
        	    if (node.key!=null) { 
        	    	String restoredChar= revertWhiteSpace(node.key);
        	        bufWriter.write(restoredChar);
        	        node= root; // reset current node to root
        	    }
        	}
            bufReader.close();
            bufWriter.close();
            
            System.out.println("Successfully decoded to "+outputFile);
        	
        } catch (Exception e) {
        	System.err.println("Error: "+e.getMessage());
        	return;
        }
    } // end of decodeFileFromHuffTree
    
    
    /**
     * decode each character by traversing the Huffman Tree
     * @param node
     * @param c character
     * @return 
     */
    private HuffmanNode decodeCharacter(HuffmanNode node, char c) {
    	HuffmanNode base= node;
    	// move left or right
    	// throw exception other than 0,1
    	if (c-'0'==0) {
    		base= base.left;
    	} else if (c-'0'==1) {
    		base= base.right;	
    	} else throw new IllegalArgumentException();
    	return base;
    }
    
    
    
    private void TraverseTree(HuffmanNode node,String dir) {
        if (node==null) { return; }
        if (node.key!="") System.out.println(node.key+DELIMITER+dir);
        TraverseTree(node.left,dir+"0");
        TraverseTree(node.right,dir+"1");
    }
    
    
    /**
     * convert whitespace to its expression
     * @param space
     * @return
     */
    private String convertWhiteSpace(String space) {
    	String character= space;
        if (character.equals("\n")) { character= new StringBuilder("\\n").toString(); }
        //else if (character.equals(" ")) { character= new StringBuilder("space").toString(); } 
        else if (character.equals("\r")) { character= new StringBuilder("\\r").toString(); } 
        else if (character.equals("\t")) { character= new StringBuilder("\\t").toString(); }
        return character;
    } // end of convertWhiteSpace
    
    
    /**
     * convert whitespace to its expression
     * @param space
     * @return
     */
    private String revertWhiteSpace(String space) {
    	String character= space;
        if (character.equals("\\n")) { character= new StringBuilder("\n").toString(); } 
        //else if (character.equals("space")) { character= new StringBuilder(" ").toString(); } 
        else if (character.equals("\\r")) { character= new StringBuilder("\r").toString(); } 
        else if (character.equals("\\t")) { character= new StringBuilder("\t").toString(); }
        return character;
    } // end of revertWhiteSpace
	
}
