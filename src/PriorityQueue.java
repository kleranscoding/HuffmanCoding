/**
 * PriorityQueue.java
 * created by Clarence Cheung
 * from CS 367 Summer 2014
 * used in Huffman Coding
 * modified in 2017
 * 
 */

import java.util.NoSuchElementException;

public class PriorityQueue<T extends Comparable<T>> implements PriorityQueueInterface<T>  {
    
	/** fields **/
	private static final int CAPACITY= 200;
	private Heap<T> heap;
	private int nextLoc, numItems;
	
	/** constructor **/
	public PriorityQueue() {
	    heap= new Heap<T>(CAPACITY);
	    numItems= 0;
	    nextLoc= 1;
	}
	
	/** main methods **/
	
	/** insert item **/
    public void insert(T newItem) {
        heap.insert(newItem);
        numItems++;
        nextLoc++;
    }
    
    /** remove and return max element from queue **/
    public T removeTop() throws NoSuchElementException { 
        if (this.isEmpty()) {
            if (heap.isEmpty()) throw new NoSuchElementException();
        }
        numItems--;
        nextLoc--;
        return heap.remove();
    }
    
    /** return max element from queue **/
    public T peek() throws NoSuchElementException { 
    	if (this.isEmpty()) throw new NoSuchElementException();
        return heap.get(1);
    }
    
    /** determine if queue is empty **/
    public boolean isEmpty() { 
        if (numItems==0) return true;
        return false;
    }
    
    /** aux methods **/
    
    /** print PriorityQueue **/
    public void printQueue() { heap.printMaxHeap(); }
    
    /** get size of PriorityQueue **/
    public int size() { return heap.size(); }
    
}
