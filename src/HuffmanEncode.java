/**
 * HuffmanEncode.java
 * created by Clarence Cheung
 * from CS 367 Summer 2014
 * used in Huffman Coding
 * modified in 2017
 * 
 */

import java.io.*;
import java.util.*;
import java.nio.charset.Charset;

public class HuffmanEncode {

    /** fields **/
	private Map<String,Integer> mapCharCount;
    private Map<String, String> mapCharCode;
	private long totalFreqCnt, totalCodeCnt;
	private static final String DELIMITER=" ", NEWLINE="\r\n";
    private static final Charset CharsetUTF8= Charset.forName("UTF-8");
	
	/** constructor **/
	public HuffmanEncode() {  }
    
	
	/** methods **/
	
    /**
     *  runs the compression routine 
     * @param codeFile
     * @param inputFile
     * @throws IOException
     */
    public void encode(String inputFile,String outputFile,String codeFile) { //throws IOException { 
    	
        // create a map to count the frequency of a character
        // create a map to store the binary code of a character
        // create a Priority Queue to store Huffman nodes
    	mapCharCount= countCharacterFromFile(inputFile);
    	if (mapCharCount==null) { return; }
        if (mapCharCount.size()<1) { System.err.println("file is empty..."); return; }
    	mapCharCode= new TreeMap<String, String>();
        PriorityQueue<HuffmanNode> prioQueue= new PriorityQueue<HuffmanNode>();
        
        //for (String s: mapCharCount.keySet()) { System.out.println(s+": "+mapCharCount.get(s));  }
        
        // insert the character and frequency to PrioirtyQueue
        totalFreqCnt= 0;
        for (String key : mapCharCount.keySet()) {
            int charFreq= mapCharCount.get(key);
            prioQueue.insert(new HuffmanNode(key,charFreq));
            totalFreqCnt+= charFreq;
            //System.out.println(key+": "+charFreq);
        }
        //System.out.println("total count= "+totalFreqCnt);
        
        // build the Huffman Tree
        buildHuffmanTree(prioQueue);
        
        // build Huffman code from Huffman Tree
        buildHuffmanCode(prioQueue.removeTop(),"");
        
        // count the number of binary codes after the conversion
        totalCodeCnt= 0;
        int mapCharSize= 0;
        PriorityQueue<HuffmanNode> pqCodeCnt= new PriorityQueue<HuffmanNode>();
        for (String key : mapCharCode.keySet()) {
            String code= mapCharCode.get(key);
            // insert the binary code to a priority queue for efficient file output 
            pqCodeCnt.insert(new HuffmanNode(key,mapCharCount.get(key),code));
            totalCodeCnt+= code.length();
            mapCharSize++;
        }
        double avgCodeLen= totalCodeCnt*1.0/mapCharSize;
        //System.out.printf("average length of code: %.4f\n",avgCodeLen);
        
        exportCodesToFile(pqCodeCnt,inputFile,outputFile,codeFile);
        
    } // end of compression method
    
    
    /**
     * This method counts the frequency of each character in file
     * @param inputFile
     * @return Map<String,Integer> of Character count; null if file is empty
     * @throws IOException
     */
    private Map<String,Integer> countCharacterFromFile(String inputFile) { 
    	
        try {
            // read in file
        	FileInputStream fis= new FileInputStream(new File(inputFile));
            InputStreamReader inStream= new InputStreamReader(fis,CharsetUTF8);
            BufferedReader bufReader= new BufferedReader(inStream);
            Map<String,Integer> map= new TreeMap<String,Integer>();
            int thisChar;
            while ( (thisChar=bufReader.read()) != -1) {    
                // convert each Character to String
                char character= (char) thisChar;
                // if encountered spaces convert them to their expression
                String str= convertWhiteSpace(Character.toString(character));
                // create and/or increase frequency count for the Character Count TreeMap
                if (!map.containsKey(str)) map.put(str,0);
                map.put(str,map.get(str)+1);
            }
            bufReader.close(); // close the file
            return map; // return TreeMap
        } catch (IOException e) {
        	System.err.println("Error: " + e.getMessage());
        	return null;
        }
    } // end of countCharacterFromFile
    
    
    /**
     * write the codes to code table and transcribe the characters to Huffman Encoding
     * @param pqNode
     * @param outputFile
     * @param codeFile
     * @throws IOException
     */
    private void exportCodesToFile(PriorityQueue<HuffmanNode> pqNode,String inFile,String outFile,String codeFile) {
    	exportHuffmanCode(pqNode,codeFile); // export Huffman Code
    	exportHuffCodeInput(inFile,outFile); // export converted input file to binary Huffman Code
    } // end of exporting codes
    
    
    /**
     * export codes to code table
     * @param pqNode
     * @param codeFile
     */
    private void exportHuffmanCode(PriorityQueue<HuffmanNode> pqNode,String codeFile) {
    	try {
            // create Huffman Code output file
            OutputStreamWriter osw= new OutputStreamWriter(new FileOutputStream(codeFile),CharsetUTF8);
            Writer fstream= new BufferedWriter(osw);
		    BufferedWriter bufWriter = new BufferedWriter(fstream);
		    // loop thru the priority queue
		    while (!pqNode.isEmpty()) {
		        HuffmanNode node= pqNode.removeTop();
		        bufWriter.write(node.key+DELIMITER+node.code+NEWLINE);
		    }
		    bufWriter.close();
		
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    } // end of exportHuffmanCode 
    
    
    private void exportHuffCodeInput(String inputFile, String outputFile) {
        try {
        	// read in file
            InputStreamReader inStream= new InputStreamReader(new FileInputStream(new File(inputFile)),CharsetUTF8);
            BufferedReader bufReader= new BufferedReader(inStream);
            // create Huffman Code output file
            OutputStreamWriter osw= new OutputStreamWriter(new FileOutputStream(outputFile),CharsetUTF8);
            BufferedWriter bufWriter = new BufferedWriter(new BufferedWriter(osw));
		    
            int character;
            long totalBits= 0, totalCodeBits= 0;
            while ((character= bufReader.read()) != -1) {
                char c= (char) character;
                String str= convertWhiteSpace(Character.toString(c));
                String code= mapCharCode.get(str);
                bufWriter.write(code);
                totalCodeBits+= code.length();
                totalBits+= 8;
            }
		    bufReader.close();
		    bufWriter.close(); // close files
		    
		    //display statistics
		    double compression= (totalBits-totalCodeBits)*1.0/totalBits;
		    System.out.println("Total bits without compression= "+totalBits);
		    System.out.println("Total bits with compression= "+totalCodeBits);
		    System.out.printf("Compression ratio= %.4f%%\n",compression*100.0);
		
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    
    /**
     * Build the Huffman Tree
     * @param prioQ
     */
    private void buildHuffmanTree(PriorityQueue<HuffmanNode> prioQ) {
    	while (prioQ.size() > 1) {
    	    // get left and right children by removing the first element priority queue
    	    HuffmanNode left= prioQ.removeTop();
    	    HuffmanNode right= prioQ.removeTop();
    	    // insert new node to priority queue
    	    int totalFreq= left.frequency + right.frequency;
    	    prioQ.insert(new HuffmanNode("",totalFreq,left,right));
    	}
    } // end of buildHuffmanTree
    
    
    /**
     * Build the Huffman Encoding from the Huffman Tree
     * @param prioQ
     */
    private void buildHuffmanCode(HuffmanNode node,String code) {
    	node.code= code;
    	//PreOrderTraverseRecursive(node);
    	PreOrderTraverseIterative(node);
    }  // end of buildHuffmanCode
    
    
    /** PreOreder Tree Traversal (Iterative)
     * maybe slower but with no stack/heap issue
     * @param node
     * @param code
     */
    private void PreOrderTraverseIterative(HuffmanNode root) {
    	if (root==null) return; // return if root is null
    	Stack<HuffmanNode> stack= new Stack<>(); // create a stack for HuffmanNode
        stack.push(root); // push root to stack
        // while stack is not empty
        while (!stack.isEmpty() ) {
            HuffmanNode node= stack.pop();
            if (node.right!=null && node.left!=null) {
            	node.left.code= new StringBuilder(node.code+"0").toString();
        	    node.right.code= new StringBuilder(node.code+"1").toString();
        	    stack.push(node.right);
        	    stack.push(node.left);
            } else { 
            	mapCharCode.put(node.key,node.code);
        	    //System.out.println(node.key+": "+node.code); 
            }
        }
    } // end of PreOrder Iterative Traversal
    
    
    /** PreOreder Tree Traversal (Recursive)
     * maybe faster but prone to stack/heap limitation
     * @param node
     * @param code
     */
    private void PreOrderTraverseRecursive(HuffmanNode node) {
        if (node.left!=null && node.right!=null) {
        	node.left.code= new StringBuilder(node.code+"0").toString();
    	    node.right.code= new StringBuilder(node.code+"1").toString();
        	PreOrderTraverseRecursive(node.left);	
            PreOrderTraverseRecursive(node.right);
        } else { 
        	mapCharCode.put(node.key,node.code);
            //System.out.println(node.key+": "+node.code); 
        }
    } // end of PreOrder Recursive Traversal
    
    
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
	
}
