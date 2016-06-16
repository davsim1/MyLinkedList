package list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

public class Tester {
	public static MyLinkedList<Integer> myList = new MyLinkedList<Integer>();
	public static LinkedList<Integer> list = new LinkedList<Integer>();
	public static Random rand = new Random();

	public static void main(String[] args) throws Exception {
		rand.setSeed(System.currentTimeMillis());
		test1();
		populate();
		doMethod(2);
		System.out.println("TESTS PASSED");
	}

	public static void assertTrue(boolean b) throws Exception {
		if (!b) {
			throw new Exception("tests failed");
		}
	}

	public static void populate() throws Exception {
		int size = rand.nextInt(10);
		ArrayList<Integer> all = new ArrayList<Integer>();

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

	public static void test1() throws Exception {
		myList.add(1);
		list.add(1);
		myList.add(2);
		list.add(2);
		myList.add(3);
		list.add(3);

		assertTrue(myList.toString().equals(list.toString()));
	}

	public static void doMethod(int num) throws Exception {
		Integer m = Integer.MIN_VALUE;
		switch (num) {
		case 1:
			try {
				m = myList.getFirst();
			} catch (NoSuchElementException e) {
				try {
					list.getFirst();
					assertTrue(false);
				} catch (NoSuchElementException e2) {
					// test passes
				}
			}
			assertTrue(m.equals(list.getFirst()));
			break;
		case 2:
			try {
				m = myList.getLast();
			} catch (NoSuchElementException e) {
				try {
					list.getLast();
					assertTrue(false);
				} catch (NoSuchElementException e2) {
					// test passes
				}
			}
			assertTrue(m.equals(list.getLast()));
			break;
		}
	}
}
