package list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class Tester {
	public static MyLinkedList<Integer> myList = new MyLinkedList<Integer>();
	public static LinkedList<Integer> list = new LinkedList<Integer>();
	public static Random rand = new Random();

	public static void main(String[] args) throws Exception {
		rand.setSeed(System.currentTimeMillis());
		LinkedList<Integer> trace = new LinkedList<Integer>();
		Object origList = null;
		Object myOrigList = null;
		Object prevList = null;
		Object myPrevList = null;
		int randTest;
		test1();
		test2();
		test3();
		test4();
		test5();
		
		for (int i = 0; i < 100; i++) {
			trace.clear();
			populate();
			System.out.println("   populate My list: " + myList);
			System.out.println("populate Other list: " + list);
			for (int j = 1; j <= 39; j++) {
				doMethod(j);
			}
			populate();
			System.out.println("   populate My list: " + myList);
			System.out.println("populate Other list: " + list);
			for (int j = 0; j < 10; j++) {
				randTest = rand.nextInt(38) + 1;
				trace.push(randTest);
				myPrevList = myOrigList;
				prevList = origList;
				myOrigList = myList.clone();
				origList = list.clone();
				try {
					doMethod(randTest);
				} catch (Exception e) {
					System.out.println("Test sequence" + trace);
					System.out.println("prev my list    " + (MyLinkedList<Integer>)myPrevList);
					System.out.println("prev other list " + (LinkedList<Integer>)prevList);
					System.out.println("original my list    " + (MyLinkedList<Integer>)myOrigList);
					System.out.println("original other list " + (LinkedList<Integer>)origList);
					System.out.println("   My list: " + myList);
					System.out.println("Other list: " + list);
					throw e;
				}
			}
		}

		System.out.println("TESTS PASSED");
	}

	public static void assertTrue(boolean b) throws Exception {
		if (!b) {
			throw new Exception("tests failed");
		}
	}

	public static void assertSame(Collection<?> a, Collection<?> b) throws Exception {
		assertTrue(a.toString().equals(b.toString()));
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));

	}

	public static void test1() throws Exception {
		myList.add(1);
		list.add(1);
		myList.add(2);
		list.add(2);
		myList.add(3);
		list.add(3);
		assertSame(myList, list);
		myList.clear();
		list.clear();
	}

	public static void test2() throws Exception {
		myList.add(1);
		list.add(1);
		ListIterator<Integer> e1 = myList.listIterator(list.size());
		ListIterator<Integer> e2 = list.listIterator(list.size());
		ArrayList<Integer> l1 = new ArrayList<Integer>();
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		while (e1.hasPrevious())
			l1.add(e1.previous());
		while (e2.hasPrevious())
			l2.add(e2.previous());

		assertTrue(l1.equals(l2));

	}

	public static void test3() throws Exception {
		LinkedList<Character> charList = new LinkedList<Character>();
		MyLinkedList<Character> myCharList = new MyLinkedList<Character>();
		charList.add('A');
		myCharList.add('A');
		ListIterator<Character> iter = charList.listIterator();
		ListIterator<Character> myIter = myCharList.listIterator();

		for (int i = 0; i < 10; i++) {
			assertTrue(iter.next().equals('A'));
			assertTrue(iter.previous().equals('A'));
			assertTrue(myIter.next().equals('A'));
			assertTrue(myIter.previous().equals('A'));
		}
	}

	public static void test4() throws Exception {
		LinkedList<String> strList = new LinkedList<String>();
		MyLinkedList<String> myStrList = new MyLinkedList<String>();
		ArrayList<String> all = new ArrayList<String>();
		all.add("beta");
		all.add("gamma");
		all.add("delta");
		strList.add("alpha");
		myStrList.add("alpha");
		strList.add("beta");
		myStrList.add("beta");
		assertSame(strList, myStrList);
		strList.removeLast();
		myStrList.removeLast();
		assertSame(strList, myStrList);
		strList.addAll(0, all);
		myStrList.addAll(0, all);
		System.out.println("my " + myStrList);
		System.out.println("th " + strList);
		assertSame(strList, myStrList);
	}

	public static void test5() throws Exception {
		LinkedList<Double> doubleList = new LinkedList<Double>();
		MyLinkedList<Double> myDoubleList = new MyLinkedList<Double>();
		ArrayList<Double> all = new ArrayList<Double>();
		for (double i = 0.0; i < 10; i += 0.5) {
			all.add(i);
		}
		doubleList.addAll(all);
		myDoubleList.addAll(all);
		assertSame(doubleList, myDoubleList);
		
	}
	
	public static void populate() throws Exception {
		int size = rand.nextInt(10);
		ArrayList<Integer> all = new ArrayList<Integer>();

		myList.clear();
		list.clear();

		for (int i = 0; i < size; i++) {
			int num = rand.nextInt(10);
			myList.add(num);
			list.add(num);
			all.add(rand.nextInt(10));
		}

		myList.addAll(all);
		list.addAll(all);

		assertTrue(myList.toString().equals(list.toString()));

	}

	public static void doMethod(int num) throws Exception {
		Integer m = Integer.MIN_VALUE;
		boolean flag = true;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int idx;
		Integer t;
		temp.add(11);
		temp.add(12);
		temp.add(13);

		switch (num) {
		case 1:
			try {
				m = myList.getFirst();
			} catch (NoSuchElementException e) {
				try {
					flag = false;
					list.getFirst();
					assertTrue(false);
				} catch (NoSuchElementException e2) {
					// test passes
				}
			}
			if (flag) {
				assertTrue(m.equals(list.getFirst()));
			}
			assertSame(myList, list);
			flag = true;
			break;
		case 2:
			try {
				m = myList.getLast();
			} catch (NoSuchElementException e) {
				try {
					flag = false;
					list.getLast();
					assertTrue(false);
				} catch (NoSuchElementException e2) {
					// test passes
				}
			}
			if (flag) {
				assertTrue(m.equals(list.getLast()));
			}
			assertSame(myList, list);
			flag = true;
			break;
		case 3:
			try {
				m = myList.removeFirst();
			} catch (NoSuchElementException e) {
				try {
					flag = false;
					list.removeFirst();
					assertTrue(false);
				} catch (NoSuchElementException e2) {
					// test passes
				}
			}
			if (flag) {
				assertTrue(m.equals(list.removeFirst()));
			}
			assertSame(myList, list);
			flag = true;
			break;
		case 4:
			try {
				m = myList.removeLast();
			} catch (NoSuchElementException e) {
				try {
					flag = false;
					list.removeLast();
					assertTrue(false);
				} catch (NoSuchElementException e2) {
					// test passes
				}
			}
			if (flag) {
				assertTrue(m.equals(list.removeLast()));
			}
			assertSame(myList, list);
			flag = true;
			break;
		case 5:
			myList.addFirst(20);
			list.addFirst(20);
			assertSame(myList, list);
			break;
		case 6:
			myList.addLast(25);
			list.addLast(25);
			assertSame(myList, list);
			break;
		case 7:
			int n = rand.nextInt(10);
			assertTrue(myList.contains(n) == list.contains(n));
			break;
		case 8:
			assertTrue(myList.size() == list.size());
			break;
		case 9:
			assertTrue(myList.add(30) == list.add(30));
			assertSame(myList, list);
			break;
		case 10:
			Integer x = rand.nextInt(10);
			assertTrue(myList.remove(x) == list.remove(x));
			assertSame(myList, list);
			break;
		case 11:
			assertTrue(myList.addAll(temp) == list.addAll(temp));
			assertSame(myList, list);
			try {
				myList.addAll(null);
				assertTrue(false);
			} catch (NullPointerException e) {
				// pass
			}
			break;
		case 12:
			idx = list.size() == 0 ? 0 : rand.nextInt(list.size());
			assertTrue(myList.addAll(idx, temp) == list.addAll(idx, temp));
			try {
				assertSame(myList, list);
			} catch (Exception e) {
				assertSame(myList, list);
				throw e;
			}
			try {
				myList.addAll(myList.size() + 1, temp);
				assertTrue(false);
			} catch (IndexOutOfBoundsException e) {
				// pass
			}
			try {
				list.addAll(list.size() + 1, temp);
				assertTrue(false);
			} catch (IndexOutOfBoundsException e) {
				// pass
			}
			try {
				myList.addAll(null);
				assertTrue(false);
			} catch (NullPointerException e) {
				// pass
			}
			break;
		case 13:
			myList.clear();
			assertTrue(myList.size() == 0);
			assertTrue(myList.toString() == "[]");
			assertTrue(myList.equals(new ArrayList<Integer>()));
			myList.addAll(list);
			assertSame(myList, list);
			break;
		case 14:
			if (list.size() > 0) {
				idx = rand.nextInt(list.size());
				assertTrue(myList.get(idx).equals(list.get(idx)));
			}
			try {
				myList.get(-1);
				assertTrue(false);
			} catch (IndexOutOfBoundsException e) {
				// pass
			}
			try {
				myList.get(list.size() + 1);
				assertTrue(false);
			} catch (IndexOutOfBoundsException e) {
				// pass
			}
			break;
		case 15:
			if (list.size() == 0) {
				try {
					myList.set(0, 999);
					assertTrue(false);
				} catch (IndexOutOfBoundsException e) {
					// pass
				}
			} else {
				idx = rand.nextInt(list.size());
				assertTrue(myList.set(idx, myList.get(idx) + 1).equals(list.set(idx, list.get(idx) + 1)));
				assertSame(myList, list);
			}
			break;
		case 16:
			idx = list.size() == 0 ? 0 : rand.nextInt(list.size());
			myList.add(idx, 50);
			list.add(idx, 50);
			assertSame(myList, list);
			break;
		case 17:
			if (list.size() == 0) {
				try {
					myList.remove(0);
					assertTrue(false);
				} catch (IndexOutOfBoundsException e) {
					// pass
				}
			} else {
				idx = rand.nextInt(list.size());
				assertTrue(myList.remove(idx).equals(list.remove(idx)));
				assertSame(myList, list);
			}
			break;
		case 18:
			m = rand.nextInt(10);
			assertTrue(myList.indexOf((Integer) m) == list.indexOf((Integer) m));
			break;
		case 19:
			m = rand.nextInt(
					10);/*
						 * System.out.println(myList); System.out.println(list);
						 * System.out.println("m " + m);
						 * System.out.println(myList.lastIndexOf((Integer)m));
						 * System.out.println(list.lastIndexOf((Integer)m));
						 */
			assertTrue(myList.lastIndexOf((Integer) m) == list.lastIndexOf((Integer) m));
			break;
		case 20:
			if (myList.peek() == null) {
				assertTrue(list.peek() == null);
			} else {
				assertTrue(myList.peek().equals(list.peek()));
			}
			break;
		case 21:
			if (myList.isEmpty()) {
				try {
					myList.element();
					assertTrue(false);
				} catch (NoSuchElementException e) {
					// pass
				}
			} else {
				assertTrue(myList.element().equals(list.element()));
			}
			break;
		case 22:
			if ((t = myList.poll()) == null) {
				assertTrue(list.poll() == null);
			} else {
				assertTrue(t.equals(list.poll()));
			}
			assertSame(myList, list);
			break;
		case 23:
			if (list.size() == 0) {
				try {
					myList.remove();
					assertTrue(false);
				} catch (NoSuchElementException e) {
					// pass
				}
			} else {
				assertTrue(myList.remove().equals(list.remove()));
			}
			assertSame(myList, list);
			break;
		case 24:
			assertTrue(myList.offer(40) == (list.offer(40)));
			assertSame(myList, list);
			break;
		case 25:
			assertTrue(myList.offerFirst(41) == (list.offerFirst(41)));
			assertSame(myList, list);
			break;
		case 26:
			assertTrue(myList.offerLast(42) == (list.offerLast(42)));
			assertSame(myList, list);
			break;
		case 27:
			assertTrue(myList.peekFirst() == list.peekFirst() || myList.peekFirst().equals(list.peekFirst()));
			break;
		case 28:
			assertTrue(myList.peekLast() == list.peekLast() || myList.peekLast().equals(list.peekLast()));
			break;
		case 29:
			if ((t = myList.pollFirst()) == null) {
				assertTrue(list.pollFirst() == null);
			} else {
				assertTrue(t.equals(list.pollFirst()));
			}
			assertSame(myList, list);
			break;
		case 30:
			if ((t = myList.pollLast()) == null) {
				assertTrue(list.pollLast() == null);
			} else {
				assertTrue(t.equals(list.pollLast()));
			}
			assertSame(myList, list);
			break;
		case 31:
			m = rand.nextInt(10);
			myList.push(m);
			list.push(m);
			assertSame(myList, list);
			break;
		case 32:
			try {
				m = myList.pop();
			} catch (NoSuchElementException e) {
				try {
					flag = false;
					list.pop();
					assertTrue(false);
				} catch (NoSuchElementException e2) {
					// test passes
				}
			}
			if (flag) {
				assertTrue(m.equals(list.pop()));
			}
			flag = true;
			assertSame(myList, list);
			break;
		case 33:
			m = rand.nextInt(10);
			assertTrue(myList.removeFirstOccurrence((Integer) m) == (list.removeFirstOccurrence((Integer) m)));
			assertSame(myList, list);
			break;
		case 34:
			m = rand.nextInt(10);
			assertTrue(myList.removeLastOccurrence((Integer) m) == (list.removeLastOccurrence((Integer) m)));
			assertSame(myList, list);
			break;
		case 35:
			// Verify forwards and backwards iteration
			ListIterator<Integer> e1 = myList.listIterator(list.size());
			ListIterator<Integer> e2 = list.listIterator(list.size());
			ArrayList<Integer> l1 = new ArrayList<Integer>();
			ArrayList<Integer> l2 = new ArrayList<Integer>();
			while (e1.hasPrevious())
				l1.add(e1.previous());
			while (e2.hasPrevious())
				l2.add(e2.previous());
			if (!l1.equals(l2)) {
				System.out.println("my asc list:    " + l1);
				System.out.println("other asc list: " + l2);
			}
			assertTrue(l1.equals(l2));

			while (e1.hasNext())
				l1.add(e1.next());
			while (e2.hasNext())
				l2.add(e2.next());
			if (!l1.equals(l2)) {
				System.out.println("my mix list:    " + l1);
				System.out.println("other mix list: " + l2);
			}
			assertTrue(l1.equals(l2));
			assertSame(myList, list);
			// Verify errors
			if (myList.size() > 2) {
				try {

					for (Integer i : myList) {
						myList.remove(myList.peek());
					}

					assertTrue(false);
				} catch (ConcurrentModificationException c) {
					// pass
				}
				try {

					for (Integer i : myList) {
						myList.push(myList.getLast());
					}
					assertTrue(false);
				} catch (ConcurrentModificationException c) {
					// pass
				}
			}
			Iterator<Integer> e3 = myList.iterator();
			try {
				int k = 0;
				while (k <= myList.size() + 1) {
					e3.next();
					k++;
				}
				assertTrue(false);
			} catch (NoSuchElementException e) {
				// pass
			}

			// reset
			myList.clear();
			myList.addAll(list);
			break;
		case 36:
			Iterator<Integer> desc1 = myList.descendingIterator();
			Iterator<Integer> desc2 = list.descendingIterator();
			ArrayList<Integer> lst1 = new ArrayList<Integer>();
			ArrayList<Integer> lst2 = new ArrayList<Integer>();
			while (desc1.hasNext())
				lst1.add(desc1.next());
			while (desc2.hasNext())
				lst2.add(desc2.next());
			if (!lst1.equals(lst2)) {
				System.out.println("my tail " + myList.getLast());
				System.out.println("my desc list:    " + lst1);
				System.out.println("other desc list: " + lst2);
			}
			assertTrue(lst1.equals(lst2));
			break;
		case 37:
			MyLinkedList<Integer> myClone = (MyLinkedList<Integer>) myList.clone();
			LinkedList<Integer> clone = (LinkedList<Integer>) list.clone();
			assertTrue(myClone.equals(clone));
			assertTrue(clone != list);
			assertTrue(myClone != myList);
			break;
		case 38:
			Object[] myArr = myList.toArray();
			Object[] arr = list.toArray();
			assertTrue(myArr.length == arr.length);
			for (int i = 0; i < arr.length; i++) {
				assertTrue(myArr[i].equals(arr[i]));
			}
			break;
		case 39:
			Integer[] myIntArr = myList.toArray(new Integer[0]);
			Integer[] intarr = list.toArray(new Integer[0]);
			assertTrue(myIntArr.length == intarr.length);
			for (int i = 0; i < intarr.length; i++) {
				assertTrue(myIntArr[i].equals(intarr[i]));
			}
			break;
		default:
			throw new Exception("unknown method");
		}
	}
}
