/**
 * HuffmanNode.java
 * created by Clarence Cheung
 * from CS 367 Summer 2014
 * used in Huffman Coding
 * modified in 2017
 * 
 */

public class HuffmanNode implements Comparable<HuffmanNode> {

    /** fields **/
    String key, character, code;
    int frequency;
    HuffmanNode left, right, node;
	
    
    /** constructor(s) **/
    public HuffmanNode(String key, int freq) {
        this.key= key;
        this.frequency= freq;
    }

    public HuffmanNode(String key, int freq, String code) {
        this.key= key;
        this.frequency= freq;
        this.code= code;
    }

    public HuffmanNode(String key, int freq, HuffmanNode leftChild, HuffmanNode rightChild) {
        this.key= key;
        this.frequency= freq;
        this.left= leftChild;
        this.right= rightChild;
    }

    public HuffmanNode(HuffmanNode node, String character) {
        this.node = node;
        this.character= character;
    }
    
    
    /** methods **/
    
    /** compareTo frequency **/
    public int compareTo(HuffmanNode hfn) { return this.frequency - hfn.frequency; }
    
    /** compare the code alphabetically **/
    public int compare(HuffmanNode hfn) { 
        if (this.code.compareTo(hfn.code) > 0) return -1;
        return 1;
    }
    
}
