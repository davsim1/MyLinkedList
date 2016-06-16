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

public class MyLinkedList<E> extends AbstractSequentialList<E>
		implements Serializable, Cloneable, Iterable<E>, Collection<E>, Deque<E>, List<E>, Queue<E> {
	private static final long serialVersionUID = 1L;
	private Node<E> head = null;
	private Node<E> tail = null;
	private int size = 0;

	private class Node<T> {
		public T data;
		public Node<T> prev;
		public Node<T> next;

		public Node(T data) {
			this.data = data;
		}

	}

	public MyLinkedList() {
		super();
	}

	public MyLinkedList(Collection<? extends E> c) {
		super();
		this.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		return super.addAll(index, c);
	}

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

	private void insertNode(Node<E> first, Node<E> newNode, Node<E> last) {
		link(first, newNode);
		link(newNode, last);
	}

	public boolean add(E e) {
		add(size, e);
		return true;
	}

	@Override
	public ListIterator<E> listIterator(int i) {
		return new ListIterator<E>() {
			Node<E> prev = null;
			Node<E> curr = head;
			Node<E> next = head;
			boolean canAlter = false;
			boolean firstNext = true;
			boolean firstPrev = true;
			int index = 0;
			int expectedModCount = modCount;

			@Override
			public void add(E e) {
				canAlter = false;
				Node<E> newNode = new Node<E>(e);
				if (head == null) {
					head = newNode;
					tail = head;
					next = null;
				} else {
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

			@Override
			public boolean hasNext() {
				return (curr != null && curr.next != null) || (curr == head && curr != null && firstNext);
			}

			@Override
			public boolean hasPrevious() {
				return (curr != null && curr.prev != null) || (curr == tail && curr != null && firstPrev);
			}

			@Override
			public E next() {
				E result;
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				if (expectedModCount != modCount) {
					throw new ConcurrentModificationException();
				}
				if (curr == head && curr != null && firstNext) {
					result = curr.data;
					prev = curr;
					firstNext = false;
					firstPrev = true;
				} else {
					result = curr.next.data;
					prev = curr;
					curr = curr.next;
					firstNext = true;
				}
				next = curr.next;
				index++;
				canAlter = true;
				return result;
			}

			@Override
			public int nextIndex() {
				return index;
			}

			@Override
			public E previous() {
				E result;
				if (!hasPrevious()) {
					throw new NoSuchElementException();
				}
				if (expectedModCount != modCount) {
					throw new ConcurrentModificationException();
				}
				if (curr == tail && curr != null && firstPrev) {
					result = curr.data;
					prev = curr;
					firstPrev = false;
					firstNext = true;
				} else {
					result = curr.prev.data;
					prev = curr;
					curr = curr.prev;
					firstPrev = true;
				}
				next = curr.next;
				index--;
				canAlter = true;
				return result;
			}

			@Override
			public int previousIndex() {
				return index - 1;
			}

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

			@Override
			public void set(E e) {
				if (!canAlter) {
					throw new IllegalStateException();
				}
				curr.data = e;
			}

			private ListIterator<E> goToIndex(int i) {
				if (i < 0 || i > size) {
					throw new IndexOutOfBoundsException();
				}
				while (i != 0 && hasNext() && nextIndex() < i) {
					next();
				}
				return this;
			}

		}.goToIndex(i);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void addFirst(E e) {
		add(0, e);
	}

	@Override
	public void addLast(E e) {
		add(e);

	}

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

	@Override
	public E element() {
		if (head == null) {
			throw new NoSuchElementException();
		}
		return head.data;
	}

	@Override
	public E getFirst() {
		return element();
	}

	@Override
	public E getLast() {
		if (tail == null) {
			throw new NoSuchElementException();
		}
		return tail.data;
	}

	@Override
	public boolean offer(E e) {
		add(e);
		return true;
	}

	@Override
	public boolean offerFirst(E e) {
		add(0, e);
		return true;
	}

	@Override
	public boolean offerLast(E e) {
		return offer(e);
	}

	@Override
	public E peek() {
		return head == null ? null : head.data;
	}

	@Override
	public E peekFirst() {
		return peek();
	}

	@Override
	public E peekLast() {
		return tail == null ? null : tail.data;
	}

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

	@Override
	public E pollFirst() {
		return poll();
	}

	@Override
	public E pollLast() {
		E result = peekLast();
		if (result != null) {
			remove(size - 1);
		}
		return result;
	}

	@Override
	public E pop() {
		return removeFirst();
	}

	@Override
	public void push(E e) {
		addFirst(e);
	}

	@Override
	public E remove() {
		return removeFirst();
	}

	@Override
	public E removeFirst() {
		if (head == null) {
			throw new NoSuchElementException();
		}
		return poll();
	}

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

	@Override
	public E removeLast() {
		if (tail == null) {
			throw new NoSuchElementException();
		}
		return pollLast();
	}

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

	@Override
	public Object clone() {
		MyLinkedList<E> result = new MyLinkedList<E>();
		for (E e : this) {
			result.add(e);
		}
		return result;
	}

}
