package hr.fer.zemris.bool.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;

/**
 * Helper class which is actually a list of functions for manipulation with objects from
 * hr.fer.zemris.bool package.
 * 
 * @author Antonio Paunovic
 * @version 0.4
 */
public class BooleanSwissKnife {

  /**
   * Filter leaves all elements from the list, which satisfy filter paramterer, in list.
   * 
   * @param filter predicate for filtering
   * @param elements list of elements to filter
   * @return new list with elements that satisfy.
   */
  public static <T> List<T> filterInts(IIndexFilter<T> filter, List<T> elements) { // NOT YET
    List<T> tmpRecords = new ArrayList<>();
    for (T elem : elements) {
      if (filter.accepts(elem)) {
        tmpRecords.add(elem);
      }
    }

    return tmpRecords;
  }

  /**
   * Checks if any element satisfies the given filter's predicate.
   * 
   * @param filter filter predicate
   * @param coll list of element to check
   * @return boolean true if at least one element satisfies, otherwise false.
   */
  public static <T> boolean any(IIndexFilter<T> filter, List<T> coll) {
    for (T elem : coll) {
      if (filter.accepts(elem)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if all elements satisfy the given filter's predicate.
   * 
   * @param filter filter predicate
   * @param coll list of element to check
   * @return boolean true if all elements satisfy, otherwise false.
   */
  public static <T> boolean all(IIndexFilter<T> filter, List<T> coll) {
    for (T i : coll) {
      if (!filter.accepts(i)) {
        return false;
      }
    }

    return true;
  }

  /**
   * For a given index, an array of {@link BooleanValue} is returned. This array represents how
   * logically index looks if zeroes are false and ones are true.
   * 
   * @param numberLength length of resulting number
   * @param index to turn binary and then to propositional logic values.
   * @return array of boolean values representing an index as logical value.
   */
  public static BooleanValue[] indexAsBinary(int numberLength, int index) {
    BooleanValue[] booleanIndex = new BooleanValue[numberLength];

    for (int bitMask = 1, bitPosition = 0; bitPosition < numberLength; bitMask *= 2, bitPosition++) {

      if ((index & bitMask) == 0) {
        booleanIndex[numberLength - 1 - bitPosition] = BooleanValue.FALSE;
      } else {
        booleanIndex[numberLength - 1 - bitPosition] = BooleanValue.TRUE;
      }
    }

    return booleanIndex;
  }

  /**
   * Complements value of {@link BooleanValue}. FALSE becomes TRUE and vice-versa. We don't care for
   * DONT_CARE.
   * 
   * @param oldVal value to complement
   * @return complemented value
   */
  public static BooleanValue complementBooleanValue(BooleanValue oldVal) {
    BooleanValue newVal;
    switch (oldVal) {
      case TRUE:
        newVal = BooleanValue.FALSE;
        break;
      case FALSE:
        newVal = BooleanValue.TRUE;
        break;
      default:
        newVal = BooleanValue.DONT_CARE;
    }

    return newVal;
  }

  /**
   * Returns a list consisting of integers beginning with {@code start} and ending with {@code end}.
   * Integers are at {@code step} distance from each other.
   * 
   * @param start of sequnece
   * @param end of sequence
   * @param step
   * @return list of integers starting from start and ending with end (exclusive).
   */
  public static List<Integer> range(int start, int end, int step) {
    List<Integer> range = new ArrayList<>();
    for (int i = start; i < end; i += step) {
      range.add(i);
    }

    return range;
  }

  /**
   * Number of variations for set of two elements.
   * 
   * @param n number of variables.
   * @return count of variations
   */
  public static int binaryVariationsCount(int n) {
    int count = 1;
    for (int i = 0; i < n; i++) {
      count *= 2;
    }

    return count;
  }

  /**
   * Find all common elements for two lists.
   * 
   * @param coll1 first list
   * @param coll2 second list
   * @return intersection of two lists.
   */
  public static <T> List<T> listIntersection(Collection<T> coll1, Collection<T> coll2) {
    List<T> intersect = new ArrayList<>();

    for (T t : coll1) {
      if (coll2.contains(t)) {
        intersect.add(t);
      }
    }

    return intersect;
  }

  /**
   * Just printouts the list.
   * 
   * @param l list to print.
   */
  public static <T> void printList(List<T> l) {
    System.out.println(l);

    for (T elem : l) {
      System.out.print(elem + " ");
    }
    System.out.println();
  }

  /**
   * Copy a list of {@link Integer}.
   * 
   * @param ls incoming list to copy.
   * @return copy of incoming list.
   */
  public static List<Integer> copyIntList(List<Integer> ls) {
    List<Integer> newLs = new ArrayList<>();

    for (Integer i : ls) {
      newLs.add(i);
    }

    return newLs;
  }

  /**
   * Make a deep copy of list of {@link BooleanVariable}.
   * 
   * @param ls incoming list to copy.
   * @return copy of incoming list.
   */
  public static List<BooleanVariable> copyBooleanVariableList(List<BooleanVariable> ls) {
    List<BooleanVariable> newLs = new ArrayList<>();

    for (BooleanVariable var : ls) {
      newLs.add(var.clone());
    }

    return newLs;
  }

  /**
   * Make a deep copy of list of {@link Mask}.
   * 
   * @param ls incoming list to copy.
   * @return copy of incoming list.
   */
  public static List<Mask> copyMaskList(List<Mask> ls) {
    List<Mask> newLs = new ArrayList<>();

    for (Mask var : ls) {
      newLs.add(var.clone());
    }

    return newLs;
  }

}
