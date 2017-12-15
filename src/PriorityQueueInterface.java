/**
 * PriorityQueueInterface.java
 * created by Clarence Cheung
 * from CS 367 Summer 2014
 * used in Huffman Coding
 * modified in 2017
 * 
 */


import java.util.NoSuchElementException;

public interface PriorityQueueInterface<T> {
    
	/** insert item at the end of the queue **/
    void insert(T item);
    
    /** removes and returns the front item in the queue **/
    T removeTop() throws NoSuchElementException;
    
    /** returns the front item in the queue **/
    T peek() throws NoSuchElementException;
    
    /** check if the queue is empty **/
    boolean isEmpty();
    
}
