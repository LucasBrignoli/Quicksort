package test;

import java.util.Arrays;

import algoritmos.Secuencial;

public class Test {

	public static void main(String[] args) {
		int[] data = { 8, 7, 2, 1, 0, 9, 6, 4, 3, 11};
	    System.out.println("Unsorted Array");
	    System.out.println(Arrays.toString(data));

	    int size = data.length;

	    // call quicksort() on array data
	    Secuencial.quickSort(data, 0, size - 1);

	    System.out.println("Sorted Array in Ascending Order: ");
	    System.out.println(Arrays.toString(data));
	}

}
