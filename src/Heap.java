/**
 * Heap.java
 * created by Clarence Cheung
 * from CS 367 Summer 2014
 * used in Huffman Coding
 * modified in 2017
 * 
 */

import java.util.NoSuchElementException;

public class Heap<T extends Comparable<T>> {
    
	/** fields **/
    private T[] items;
    private int nextLoc, numItems; 
    
    /** constructor:  **/
    public Heap(int INIT_SIZE) {
        if (INIT_SIZE < 1) throw new IllegalArgumentException();
        items= (T[]) new Comparable[INIT_SIZE+1];
        numItems= 0;
        nextLoc= 1;
    }
    
    /** methods **/
    
    /** 
     * insert the element at the next available position
     * restore the heap property (if necessary)
     */
    public void insert(T newItem) {
    	// expand storage if needed
        if (numItems== items.length-1) { expandStorage(); }
        
        items[nextLoc++]= newItem;
        
        /** restore the heap property **/
        // index of inserted item and its parent index
        int child= nextLoc-1, parent= child/2;
        // swap values in Parent_Index and Child_Index
        while (parent!=0 && items[child].compareTo(items[parent]) < 0) {
            T temp = items[parent];
            items[parent]= items[child];
            items[child]= temp;
            child= parent;
            parent= child/2;
        }
        numItems++;
    }
    
    /** 
     * remove the root element in the heap
     * add the last node to the root
     * restore the heap property (if necessary) by swapping with the larger of the children
     */
    public T remove() throws NoSuchElementException {
        if (this.isEmpty()) throw new NoSuchElementException();
        T toRemove = items[1];
        items[nextLoc] = null;
        items[1] = items[nextLoc-1];
        nextLoc--;
        
        /** restore the heap property **/
        // swap the parent with the larger child
        int parent= 1, leftChild= 2*parent, rightChild= leftChild+1;
        while ((leftChild<=nextLoc-1 || rightChild<=nextLoc-1) && 
               (items[leftChild].compareTo(items[parent])<0 || items[rightChild].compareTo(items[parent])<0)) {
            
        	if (items[leftChild].compareTo(items[rightChild])<0) {
        	    // swap leftChild and parent because priority of leftChild >= rightChild
        	    T temp= items[parent];
        	    items[parent]= items[leftChild];
        	    items[leftChild]= temp;
        	    parent= leftChild;
            } else {
                // swap rightChild and parent because priority of leftChild < rightChild
        	    T temp= items[parent];
        	    items[parent]= items[rightChild];
        	    items[rightChild]= temp;
        	    parent= rightChild;
            }
        	leftChild= 2*parent;
        	rightChild= leftChild+1;
        }
        numItems--;
        return toRemove;
    }
    
    /** 
     * check if heap is empty
     */
    public boolean isEmpty() {
        if (numItems==0) return true;
        return false;
    }
    
    
    /** 
     * expand storage to handle internal capacity issues
     * new capacity is twice the old capacity
     */
    private void expandStorage() {
        T[] oldArray= items;
        items = (T[]) new Comparable[2*items.length];
        for (int i = 0; i < oldArray.length; i++) { items[i] = oldArray[i]; }
    }
    
    
    /** aux methods **/
    
    
    /** get size of heap **/
    public int size() { return numItems; }
    
    /** get element at index **/
    public T get(int index) throws NoSuchElementException {
        if (index==0 || index>=nextLoc || isEmpty()) throw new NoSuchElementException();
        return items[index];
    }
    
    /** print the heap **/
    public void printMaxHeap() {
    	System.out.print("Items= ");
        for (int i= 1; i< nextLoc ; i++) { if (items[i]!=null ) System.out.print(items[i]+","); }
        System.out.println();
    }
    
}
