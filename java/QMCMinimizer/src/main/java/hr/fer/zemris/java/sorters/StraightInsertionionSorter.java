package hr.fer.zemris.java.sorters;

/**
 * This class is used to sort an array of primitive integers using binary insertion sort algorithm.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class StraightInsertionionSorter {

  /**
   * Insertion sort algorithm.
   */
  public static void sort(int[] array) {
    sort(array, 0, array.length);
  }

  /**
   * Insertion sort algorithm, sorts from {@code startIndex} to {@code endIndex}.
   * 
   * @param array to sort.
   */
  public static void sort(int[] array, int startIndex, int endIndex) {

    for (int i = startIndex; i < endIndex; ++i) {
      for (int j = i; j > 0 && array[j - 1] > array[j]; --j) {
        swap(array, j - 1, j);
      }
    }
  }

  /**
   * Helper method which swaps two array elements at given indices.
   * 
   * @param array to mutate.
   * @param i first index to swap.
   * @param j second index to swap.
   */
  private static void swap(int[] array, int i, int j) {
    final int tmp = array[i];
    array[i] = array[j];
    array[j] = tmp;
  }

}
