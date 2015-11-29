package doublylinkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;

import mylistpackage.MyList;

/**
 * Abstract doubly linked list contains common operations that sorted doubly linked lists
 * and non sorted doubly linked lists have. 
 * @author Letian Sun	
 * @version 1/18/2015
 *
 * @param <Type> can be any object type
 */
public abstract class AbstractDLinkedMyList<Type> implements MyList<Type> {
	
	/**
	 * Reference to the first node in the list.
	 */
	protected ListNode<Type> front;

	/**
	 * Reference to the back node in the list.
	 */
	protected ListNode<Type> back;
	
	/**
	 * Current size of linked list
	 */
	protected int size;

	/**
	 * Constructs an empty list
	 */
	public AbstractDLinkedMyList() {
		size = 0;
		front = back = null;
	}

	 /**
     * Return the size of the list
     * @see mylistpackage.MyList#getSize()
     * @return the size of the list
     */
    public int getSize() {
        return size;
    }

	/**
	 * Return if the list is currently empty
	 * @see mylistpackage.MyList#isEmpty()
	 * @return if the list is empty
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns whether or not list contains a specific element
	 * @param value- value to search for
	 * @see mylistpackage.MyList#contains(Object)
	 * @return if list contains value
	 */
	@Override
	public boolean contains(Type value) {
		return getIndex(value) >= 0;
	}

	/**
	 * Inserts an element in the front of the list
	 * @param value to insert at the front of the list
	 * @see mylistpackage.MyList#insertFront(Object)
	 */
	@Override
	public void insertFront(Type value) {
		size ++;
		front = new ListNode<Type>(value, front, null);
		if (front.next != null) {
			front.next.previous = front;
		} else {
			// only one element, adjust back pointer
			back = front;
		}
	}

	/**
	 * Remove a value at a specific index in the linked list
	 * @param index - index of the element to remove.
	 * @see mylistpackage.MyList#removeAtIndex(int)
	 * @throws IndexOutOfBoundsException if index < 0 or index >= size, not a valid index in the list
	 */
	@Override
	public void removeAtIndex(int index) {
		checkInBoundsOfArray(index);
		 if (index == 0) {
	          front = front.next;
	          //remove previous pointer, if list is not empty 
	          if(front!= null) {
	        	  front.previous = null;
	          }
	          //if size was one
	          if (size == 1)
	              back = null;
	      }
	      else {
	    	  //go to one before
	          ListNode<Type> current = nodeAt(index - 1);
	          current.next = current.next.next;
	          if (current.next == null) {
	              back = current;
	          } else {
	        	  //adjust previous pointer
	        	  current.next.previous = current;
	          }
	      }
	      size--;
	}

	/**
	 * Clear the contents of the list.
	 * @see mylistpackage.MyList#clear()
	 */
	@Override
	public void clear() {
		front = back = null;
		size = 0;
	}

	/**
	 * Inserts the given value at the given index.
	 * @param index <= size() and index >= 0
	 * @param value to insert
	 * @throws IndexOutOfBoundsException if 0 < index > size
	 */
	public void insertAtIndex(int index, Type value) {
		checkOkToInsertAtIndex(index);
		if (size == 0) {
			ListNode<Type> newNode = new ListNode<Type>(value, null, null);
			front = back = newNode;
		} else {
			if (index == 0) {
				ListNode<Type> newNode = new ListNode<Type>(value, front, null);
				//assign previous reference
				newNode.next.previous = newNode;
				front = newNode;
			} else {
				// one before insertion point
				ListNode<Type> current = nodeAt(index - 1);
				// create the node, previous link should be current, next should be current's next
				ListNode<Type> newNode = new ListNode<Type>(value,
						current.next, current);
				current.next = newNode;
				if (index == size) {
					back = newNode;
				} else {
					//assign back reference of next
					current.next.previous = current;
				}
			}
		}
		size++;
	}

	/**
	 * Returns the node at a specific index.
	 * @param index where 0 <= index < size()
	 * @return reference to the node at a specific index
	 */
	protected ListNode<Type> nodeAt(int index) {
		//Calling this ourself, don't have to worry about index out of bounds
		ListNode<Type> current;
		if (index > size / 2) {
			// optimize better to start from back
			current = back;
			for (int i = 1; i < size - index; i++) {
				current = current.previous;
			}
		} else {
			current = front;
			for (int i = 1; i <= index; i++) {
				current = current.next;
			}
		}
		return current;
	}

	/**
	* Replaces the value at the given index with the given value.
	* @param index < size and index >= 0
	* @param value is assigned
	* @throws IndexOutOfBoundsException if index < 0 or index >= size
	*/
	@Override
	public void set(int index, Type value) {
		checkInBoundsOfArray(index);
		nodeAt(index).data = value;
	}
	
	/**
     * Remove the first occurrence of an element in unsorted doubly linked list.
     * @param value the value to remove the first occurrence of.
     * @see mylistpackage.MyList#remove(java.lang.Object)
    */
	@Override
	public void remove(Type value) {
		int indexOf = getIndex(value);
		if(indexOf >= 0) {
			removeAtIndex(indexOf);
		}
	}


	 /**
     * Ensures that the given index is a valid index to work with in the doubly linked list
     * @param index to check against in the doubly linked list.
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     */
    protected void checkInBoundsOfArray(int index) {
    	if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }   
    }
    
    /**
     * Ensures that the given index is a valid insertion index in the doubly linked list
     * @param index, index to check for valid insertion point
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     */
    protected void checkOkToInsertAtIndex(int index) {
    	if (index < 0 || index > size) {
    		throw new IndexOutOfBoundsException();
    	}
    }
    

	/**
	 * Returns the value at the given index in the list.
	 * @param index < size and index >= 0
	 * @throws IndexOutOfBoundsException if index < 0 or index >= size
	 * @return the value at the given index in the list.
	 */
	@Override
	public Type get(int index) {
		checkInBoundsOfArray(index);
		return nodeAt(index).data;
	}

	/**
	 * Returns an iterator for this list.
	 * 
	 * @return an iterator for the list.
	 */
	@Override
	public Iterator<Type> iterator() {
		return new LinkedIterator();
	}

	/**
	 * Represents a list node.
	 * @author Letian Sun
	 * @param <E> is of any object type
	 */
	protected static class ListNode<Type> {

		/**
		 * Data stored in this node.
		 */
		public Type data;

		/**
		 * Link to previous node in the list
		 */
		public ListNode<Type> previous;
		
		/**
		 * Link to next node in the list.
		 */
		public ListNode<Type> next;

		/**
		 * Constructs a node with given data and two null links. 
		 * @param data assigned
		 */
		public ListNode(Type data) {
			this(data, null, null);
		}

		/**
		 * Constructs a node with given data and given links.
		 * @param data assigned
		 * @param next assigned
		 * @param previous assigned
		 */
		public ListNode(Type data, ListNode<Type> next, ListNode<Type> previous) {
			this.data = data;
			this.next = next;
			this.previous = previous;
		}
	}

	/**
	 * Represents an iterator for the list. 
	 * @author Letian Sun
	 * @version 1/19/2015
	 * @param <Type> can be any object type
	 */
	private class LinkedIterator implements Iterator<Type> {

		/**
		 * Location of current value to return.
		 */
		private ListNode<Type> current;

		/**
		 * flag that indicates whether list element can be removed.
		 */
		private boolean removeOK;

		/**
		 * Location of the prior value in case of removal.
		 */
		private ListNode<Type> prior;

		/**
		 * Constructs an iterator for the given list.
		 */
		public LinkedIterator() {
			reset();
		}

		/**
		 * Resets the iterator the first list element.
         */
		public final void reset() {
			current = front;
			removeOK = false;
			prior = null;
		}

		/**
		 * Returns whether there are more list elements.
		 * @return true if there are more elements left, false otherwise
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return current != null;
		}

		/**
		 * Returns the next element in the iteration.
		 * 
		 * @throws NoSuchElementException
		 *             if no more elements.
		 * @return the next element in the iteration.
		 * @see java.util.Iterator#next()
		 */
		public Type next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			prior = current;
			Type result = current.data;
			current = current.next;
			removeOK = true;
			return result;
		}

		/**
		 * Removes the last element returned by the iterator.
		 * 
		 * @throws IllegalStateException
		 *             if a call to next has not been made efore call to remove.
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			if (!removeOK) {
				throw new IllegalStateException();
			}
			AbstractDLinkedMyList.this.remove(prior.data);
			removeOK = false;
		}
	}
}
