package hr.fer.zemris.bool;

/**
 * Interface that defines boolean functions.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public interface BooleanFunction extends NamedBooleanSource {

  /**
   * Check if function has minterm for given index.
   * 
   * @param index to check.
   * @return true if there is minterm on that index.
   */
  boolean hasMinterm(int index);

  /**
   * Check if function has maxterm for given index.
   * 
   * @param index to check.
   * @return true if there is maxterm on that index.
   */
  boolean hasMaxterm(int index);

  /**
   * Check if function has don't care for given index.
   * 
   * @param index to check.
   * @return true if there is don't cae on that index.
   */
  boolean hasDontCare(int index);

  /**
   * This method is used for iterating minterms.
   * 
   * @return iterable collection of minterm indexes.
   */
  Iterable<Integer> mintermIterable();

  /**
   * This method is used for iterating maxterms.
   * 
   * @return iterable collection of maxterm indexes.
   */
  Iterable<Integer> maxtermIterable();

  /**
   * This method is used for iterating don't cares.
   * 
   * @return iterable collection of don't care indexes.
   */
  Iterable<Integer> dontcareIterable();
}
