package list;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A doubly linked list with head and tail pointers.
 * <p>
 * MyLinkedList imitates Java's implementation of LinkedList as specified in
 * https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html. I coded
 * it only looking at the specification and not the original implementation for
 * practice using Java features. This implementation relies on listIterator and
 * AbstractSequentialList to minimize code repetition for operations like add,
 * get, and remove.
 * </p>
 * 
 * @author David Simmons (GitHub: davsim1) Date: 6/2016
 * 
 * @param <E>
 *            The type of the elements to be stored in the linked list
 */
public class MyLinkedList<E> extends AbstractSequentialList<E>
		implements Serializable, Cloneable, Iterable<E>, Collection<E>, Deque<E>, List<E>, Queue<E> {
	/* Inner Classes */
	/**
	 * A simple linked list Node to hold data, link to previous element, and
	 * link to next element.
	 * 
	 * @author David Simmons
	 *
	 * @param <T>
	 *            The type of the elements to be stored in the Node
	 */
	public class Node<T> {
		public T data;
		public Node<T> prev;
		public Node<T> next;

		public Node(T data) {
			this.data = data;
		}

	}

	/**
	 * An iterator to handle linked list operations.
	 * <p>
	 * Implementation of ListIterator which MyLinkedList uses through
	 * AbstractSequentialList for most of the linked list operations. Methods in
	 * MyLinkedList rely on this instead of using repeated code. Made this a
	 * regular inner class as opposed to an anonymous class from the
	 * listIterator method so it would show up in the Javadoc.
	 * </p>
	 * 
	 * @author David Simmons
	 */
	public class MyListIterator implements ListIterator<E> {
		/* Fields */
		Node<E> curr = head;
		Node<E> next = head;
		// Store if remove or set can be used
		boolean canAlter = false;
		// Store if this is the first return by next() to avoid repeatedly
		// returning head
		boolean firstNext = true;
		// Store if this is the first return by previous() to avoid
		// repeatedly returning tail
		boolean firstPrev = true;
		int index = 0;
		int expectedModCount = modCount;

		/* Constructor */
		/**
		 * Constructor to start the iterator at index i by calling next() until
		 * i is the next index to be returned.
		 * 
		 * @param i
		 *            The index to star the iterator at.
		 */
		public MyListIterator(int i) {
			goToIndex(i);
		}

		/* Methods */
		/**
		 * Adds e to the list before so that it would be the next element
		 * returned by {@link MyLinkedList.MyListIterator#previous}.
		 * 
		 * @see java.util.ListIterator#add(java.lang.Object)
		 * @param e
		 *            The element to add.
		 */
		@Override
		public void add(E e) {
			canAlter = false;
			Node<E> newNode = new Node<E>(e);
			if (head == null) {
				head = newNode;
				tail = head;
				next = null;
			} else {
				// Insert the element before the next one to be returned
				if (next == null || next.prev == tail) {
					link(tail, newNode);
				} else if (next == head) {
					link(newNode, head);
				} else {
					insertNode(next.prev, newNode, next);
				}
				index++;
			}
			size++;
			expectedModCount++;
			modCount++;
		}

		/**
		 * Return true while the list has more data to iterator over in the
		 * forwards direction.
		 * 
		 * @see java.util.ListIterator#hasNext()
		 * @return boolean if a {@link MyLinkedList.MyListIterator#next()} call
		 *         would be valid and return and element.
		 */
		@Override
		public boolean hasNext() {
			return (curr != null && curr.next != null) || (curr == head && curr != null && firstNext);
		}

		/**
		 * Return true while the list has more data to iterator over in the
		 * backwards direction.
		 * 
		 * @see java.util.ListIterator#hasPrevious()
		 * @return boolean if a {@link MyLinkedList.MyListIterator#previous}
		 *         call would be valid and return and element.
		 */
		@Override
		public boolean hasPrevious() {
			return (curr != null && curr.prev != null) || (curr == tail && curr != null && firstPrev);
		}

		/**
		 * Returns the next element from the list in the forwards direction.
		 * 
		 * @see java.util.ListIterator#next()
		 * @return E The next element.
		 * @throws NoSuchElementException
		 *             if {@link MyLinkedList.MyListIterator#hasNext} is false.
		 * @throws ConcurrentModificationException
		 *             if data in the list is added, removed, or changed by
		 *             another iterator while this iterator is in scope.
		 */
		@Override
		public E next() {
			E result;
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			if (expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			// Head is a special case because there is no node where
			// curr.next is head.
			if (curr == head && curr != null && firstNext) {
				result = curr.data;
				firstNext = false;
				firstPrev = true;
			} else {
				result = curr.next.data;
				curr = curr.next;
				firstNext = true;
			}
			next = curr.next;
			index++;
			canAlter = true;
			return result;
		}

		/**
		 * @see java.util.ListIterator#nextIndex()
		 * @return int The index of the element that a call to next() would
		 *         have.
		 */
		@Override
		public int nextIndex() {
			return index;
		}

		/**
		 * Returns the previous element from the list in the backwards
		 * direction.
		 * 
		 * @see java.util.ListIterator#previous()
		 * @return E The previous element.
		 * @throws NoSuchElementException
		 *             if {@link MyLinkedList.MyListIterator#hasPrevious} is
		 *             false.
		 * @throws ConcurrentModificationException
		 *             if data in the list is added, removed, or changed by
		 *             another iterator while this iterator is in scope.
		 */
		@Override
		public E previous() {
			E result;
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			// Tail is a special case because there is no node where
			// curr.prev is tail.
			if (curr == tail && curr != null && firstPrev) {
				result = curr.data;
				firstPrev = false;
				firstNext = true;
			} else {
				result = curr.prev.data;
				curr = curr.prev;
				firstPrev = true;
			}
			next = curr.next;
			index--;
			canAlter = true;
			return result;
		}

		/**
		 * @see java.util.ListIterator#previousIndex()
		 * @return int The index of the element that a call to previous() would
		 *         have.
		 */
		@Override
		public int previousIndex() {
			return index - 1;
		}

		/**
		 * Removes the last element returned by
		 * {@link MyLinkedList.MyListIterator#previous} or
		 * {@link MyLinkedList.MyListIterator#next()} as long as
		 * {@link MyLinkedList.MyListIterator#add} has not been called
		 * afterwards. This can only happen once per
		 * {@link MyLinkedList.MyListIterator#previous} or
		 * {@link MyLinkedList.MyListIterator#next()} call.
		 * 
		 * @see java.util.ListIterator#remove()
		 * @throws IllegalStateException
		 *             if {@link MyLinkedList.MyListIterator#previous} or
		 *             {@link MyLinkedList.MyListIterator#next()} have not been
		 *             called or if {@link MyLinkedList.MyListIterator#add} or
		 *             {@link MyLinkedList.MyListIterator#remove} have been
		 *             called after the last next/previous call.
		 */
		@Override
		public void remove() {
			if (!canAlter) {
				throw new IllegalStateException();
			}
			canAlter = false;

			if (curr == tail) {
				tail = curr.prev;
			}
			if (curr == head) {
				head = curr.next;
			}

			link(curr.prev, curr.next);

			size--;
			index--;
			expectedModCount++;
			modCount++;
		}

		/**
		 * Sets the last element returned by
		 * {@link MyLinkedList.MyListIterator#previous} or
		 * {@link MyLinkedList.MyListIterator#next()} as long as
		 * {@link MyLinkedList.MyListIterator#add} or
		 * {@link MyLinkedList.MyListIterator#remove} have not been called
		 * afterwards.
		 * 
		 * @see java.util.ListIterator#set(java.lang.Object)
		 * @throws IllegalStateException
		 *             if {@link MyLinkedList.MyListIterator#previous} or
		 *             {@link MyLinkedList.MyListIterator#next()} have not been
		 *             called or if {@link MyLinkedList.MyListIterator#add} or
		 *             {@link MyLinkedList.MyListIterator#remove} have been
		 *             called after the last next/previous call.
		 */
		@Override
		public void set(E e) {
			if (!canAlter) {
				throw new IllegalStateException();
			}
			curr.data = e;
		}

		/**
		 * Wind this iterator until next() will return element at index i.
		 * 
		 * @param i
		 * @return ListIterator<E> this
		 */
		private ListIterator<E> goToIndex(int i) {
			if (i < 0 || i > size) {
				throw new IndexOutOfBoundsException();
			}
			while (i != 0 && hasNext() && nextIndex() < i) {
				next();
			}
			return this;
		}

	}

	/* Fields */
	private static final long serialVersionUID = 1L;
	private Node<E> head = null;
	private Node<E> tail = null;
	private int size = 0;

	/* Constructors */
	/**
	 * Default constructor.
	 */
	public MyLinkedList() {
		super();
	}

	/**
	 * Constructor which adds all of c to the new list.
	 * 
	 * @param c
	 *            Collection to add to list
	 */
	public MyLinkedList(Collection<? extends E> c) {
		super();
		this.addAll(c);
	}

	/* Methods */
	/**
	 * Add all elements in c to this list at the index. This does error checking
	 * before using AbstractSequentialList's addAll().
	 * 
	 * @see java.util.AbstractSequentialList#addAll(int, java.util.Collection)
	 * @param index
	 *            The index to start adding at.
	 * @param c
	 *            A Collection of elements to add.
	 * @throws IndexOutOfBoundsException
	 *             if index is less than 0 or more than size.
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		return super.addAll(index, c);
	}

	/**
	 * Create the two-way link between first and second and update head and
	 * tail, if necessary.
	 * 
	 * @param first
	 * @param second
	 */
	private void link(Node<E> first, Node<E> second) {
		if (first != null) {
			if (second == head) {
				head = first;
			}
			first.next = second;
		}
		if (second != null) {
			if (first == tail) {
				tail = second;
			}
			second.prev = first;
		}
	}

	/**
	 * Insert newNode into the list by linking newNode to the elements before
	 * and after where it should be inserted.
	 * 
	 * @param first
	 * @param newNode
	 * @param last
	 */
	private void insertNode(Node<E> first, Node<E> newNode, Node<E> last) {
		link(first, newNode);
		link(newNode, last);
	}

	/**
	 * Add an element to the end of the list.
	 * 
	 * @see java.util.AbstractList#add(java.lang.Object)
	 * @param e
	 *            The element to add
	 * @return true
	 */
	public boolean add(E e) {
		add(size, e);
		return true;
	}

	/**
	 * Makes a ListIterator and moves its current position to i.
	 * 
	 * @see java.util.AbstractSequentialList#listIterator(int)
	 * @param i
	 *            The index in the list to start the iterator at.
	 * @return ListIterator The iterator starting at index i.
	 */
	@Override
	public ListIterator<E> listIterator(int i) {
		return new MyListIterator(i);
	}

	/**
	 * @see java.util.AbstractCollection#size()
	 * @return The size of this list.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Adds an element at index 0 using AbstractSequentialList's add at index.
	 * 
	 * @see java.util.Deque#addFirst(java.lang.Object)
	 * @param e
	 *            The element to add.
	 */
	@Override
	public void addFirst(E e) {
		add(0, e);
	}

	/**
	 * Alias for {@link MyLinkedList#add}.
	 * 
	 * @see java.util.Deque#addLast(java.lang.Object)
	 * @param e
	 *            The element to add.
	 */
	@Override
	public void addLast(E e) {
		add(e);

	}

	/**
	 * Returns a wrapper for listIterator() that iterates backwards from the end
	 * of the list.
	 * 
	 * @see java.util.Deque#descendingIterator()
	 * @return Iterator Traverses the list backwards.
	 */
	@Override
	public Iterator<E> descendingIterator() {
		return new Iterator<E>() {
			ListIterator<E> iter = listIterator(size);

			@Override
			public boolean hasNext() {
				return iter.hasPrevious();
			}

			@Override
			public E next() {
				return iter.previous();
			}

			@Override
			public void remove() {
				iter.remove();
			}
		};
	}

	/**
	 * Returns the head node's data.
	 * 
	 * @see java.util.Deque#element()
	 * @return E The head of the list.
	 * @throws NoSuchElementException
	 *             if this list is empty.
	 */
	@Override
	public E element() {
		if (head == null) {
			throw new NoSuchElementException();
		}
		return head.data;
	}

	/**
	 * Alias for {@link MyLinkedList#element}.
	 * 
	 * @see java.util.Deque#getFirst()
	 * @return E The head of the list.
	 */
	@Override
	public E getFirst() {
		return element();
	}

	/**
	 * Returns the tail node's data.
	 * 
	 * @see java.util.Deque#getLast()
	 * @return E The tail of the list.
	 * @throws NoSuchElementException
	 *             if this list is empty.
	 */
	@Override
	public E getLast() {
		if (tail == null) {
			throw new NoSuchElementException();
		}
		return tail.data;
	}

	/**
	 * Alias for {@link MyLinkedList#add}.
	 * 
	 * @see java.util.Deque#offer(java.lang.Object)
	 * @param e
	 *            The element to add.
	 * @return true.
	 */
	@Override
	public boolean offer(E e) {
		add(e);
		return true;
	}

	/**
	 * Alias for {@link MyLinkedList#addFirst}.
	 * 
	 * @see java.util.Deque#offerFirst(java.lang.Object)
	 * @param e
	 *            The element to add.
	 * @return true.
	 */
	@Override
	public boolean offerFirst(E e) {
		addFirst(e);
		return true;
	}

	/**
	 * Alias for {@link MyLinkedList#offer}.
	 * 
	 * @see java.util.Deque#offerLast(java.lang.Object)
	 * @param e
	 *            The element to add.
	 * @return true.
	 */
	@Override
	public boolean offerLast(E e) {
		return offer(e);
	}

	/**
	 * Returns the head's data or null if the list is empty.
	 * 
	 * @see java.util.Deque#peek()
	 * @return E The head of the list.
	 */
	@Override
	public E peek() {
		return head == null ? null : head.data;
	}

	/**
	 * Alias for {@link MyLinkedList#peek}.
	 * 
	 * @see java.util.Deque#peekFirst()
	 * @return E The head of the list.
	 */
	@Override
	public E peekFirst() {
		return peek();
	}

	/**
	 * Returns the tail's data or null if the list is empty.
	 * 
	 * @see java.util.Deque#peekLast()
	 * @return E The tail of the list.
	 */
	@Override
	public E peekLast() {
		return tail == null ? null : tail.data;
	}

	/**
	 * Returns the head's data then removes head or returns null if the list is
	 * empty.
	 * 
	 * @see java.util.Deque#poll()
	 * @return E The head of the list.
	 */
	@Override
	public E poll() {
		if (size == 0)
			return null;
		E result = peek();
		if (result != null) {
			remove(0);
		}
		return result;
	}

	/**
	 * Alias for {@link MyLinkedList#poll()}.
	 * 
	 * @see java.util.Deque#pollFirst()
	 * @return E The head of the list.
	 */
	@Override
	public E pollFirst() {
		return poll();
	}

	/**
	 * Returns the tail's data then removes tail if it is not null.
	 * 
	 * @see java.util.Deque#pollLast()
	 * @return E The tail of the list.
	 */
	@Override
	public E pollLast() {
		E result = peekLast();
		if (result != null) {
			remove(size - 1);
		}
		return result;
	}

	/**
	 * Alias for {@link MyLinkedList#removeFirst}.
	 * 
	 * @see java.util.Deque#pop()
	 * @return E The head of the list.
	 */
	@Override
	public E pop() {
		return removeFirst();
	}

	/**
	 * Alias for {@link MyLinkedList#addFirst}.
	 * 
	 * @see java.util.Deque#push(java.lang.Object)
	 * @param e
	 *            The element to add.
	 */
	@Override
	public void push(E e) {
		addFirst(e);
	}

	/**
	 * Alias for {@link MyLinkedList#removeFirst}.
	 * 
	 * @see java.util.Deque#remove()
	 * @return E The head of the list.
	 */
	@Override
	public E remove() {
		return removeFirst();
	}

	/**
	 * Throws exception if head is null, otherwise this is an alias for
	 * {@link MyLinkedList#poll}.
	 * 
	 * @see java.util.Deque#removeFirst()
	 * @return E The head of the list.
	 */
	@Override
	public E removeFirst() {
		if (head == null) {
			throw new NoSuchElementException();
		}
		return poll();
	}

	/**
	 * Removes the first element that equals o.
	 * 
	 * @see java.util.Deque#removeFirstOccurrence(java.lang.Object)
	 * @param o
	 *            The element to remove.
	 * @return boolean If the element was found and removed.
	 */
	@Override
	public boolean removeFirstOccurrence(Object o) {
		ListIterator<E> iter = listIterator();
		while (iter.hasNext()) {
			if (iter.next().equals(o)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Throws an exception if the list is empty, otherwise it is an alias for
	 * {@link MyLinkedList#pollLast}.
	 * 
	 * @see java.util.Deque#removeLast()
	 * @return E The tail of the list.
	 * @throws NoSuchElementException
	 *             if the list is empty.
	 */
	@Override
	public E removeLast() {
		if (tail == null) {
			throw new NoSuchElementException();
		}
		return pollLast();
	}

	/**
	 * Removes the last element that equals o (the first while iterating
	 * backwards from the tail).
	 * 
	 * @see java.util.Deque#removeLastOccurrence(java.lang.Object)
	 * @param o
	 *            The element to remove.
	 * @return boolean If the element was found and removed.
	 */
	@Override
	public boolean removeLastOccurrence(Object o) {
		Iterator<E> iter = descendingIterator();
		while (iter.hasNext()) {
			if (iter.next().equals(o)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Does a shallow copy of this list into a new instance of MyLinkedList.
	 * 
	 * @see java.lang.Object#clone()
	 * @return Object The new instance which is the clone.
	 */
	@Override
	public Object clone() {
		MyLinkedList<E> result = new MyLinkedList<E>();
		for (E e : this) {
			result.add(e);
		}
		return result;
	}

}
