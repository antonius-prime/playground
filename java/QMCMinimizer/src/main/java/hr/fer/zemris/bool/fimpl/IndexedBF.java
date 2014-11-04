package hr.fer.zemris.bool.fimpl;

import java.util.Collections;
import java.util.List;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.misc.BooleanSwissKnife;
import hr.fer.zemris.bool.misc.IIndexFilter;

/**
 * This represents a boolean function which is defined by specifying indexes of minterms (or
 * maxterms) and indexes of don't cares.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class IndexedBF implements BooleanFunction {

  /**
   * Name of the function.
   */
  private String name;
  /**
   * Variables used in function.
   */
  private List<BooleanVariable> domain;
  /**
   * Flag if indexes are minterms.
   */
//  private boolean indexesAreMinterms;
  /**
   * List of indexes that determine the function. They can be minterms or maxterms depending on
   * {@code indexerAreMinterms}.
   */
//  private List<Integer> indexes;
  /**
   * List of indexes representing the dontCares values.
   */
//  private List<Integer> dontCares;

  // Helper class for iterating through indexes.
  BFGrandUnifier traversalHelper;

  /**
   * Constructs a named boolean function. See parameters:
   * 
   * Preconditions:
   * <ul>
   * <li>Given indexes mustn't have any common elements with dontCares.</li>
   * <li>indexes and dontCares must be a numbers between 0 and {@code tableSize}.</li>
   * 
   * @param name of function
   * @param domain all variables that constitute the function
   * @param indexesAreMinterms true if indexes re minterms, if false they are maxterms.
   * @param indexes Integer list of indexes whic represent minterms or maxterms depending on
   *        {@code indexesAreMinterms}
   * @param dontCares Integer list of don't care values.
   * @throws IllegalArgumentException if preconditions are violated.
   */
  public IndexedBF(String name, List<BooleanVariable> domain, boolean indexesAreMinterms,
      List<Integer> indexes, List<Integer> dontCares) {

    // tableSize = BooleanSwissKnife.binaryVariationsCount(domain.size());
    // tableRange = BooleanSwissKnife.range(START, tableSize, STEP);
    // otherIndexes = BooleanSwissKnife.listIntersection(tableRange,
    // indexes);
    // otherIndexes = BooleanSwissKnife.listIntersection(otherIndexes,
    // dontCares);

    this.name = name;
    this.domain = BooleanSwissKnife.copyBooleanVariableList(domain);
//    this.indexesAreMinterms = indexesAreMinterms;
//    this.indexes = indexes;
//    this.dontCares = dontCares;

    traversalHelper = new BFGrandUnifier(this.domain, indexes, dontCares, indexesAreMinterms);

    if (!preconditionsIndexedBF(indexes, dontCares)) {
      throw new IllegalArgumentException(
          "Precondition violation: check your indexes and dontCares.");
    }
  }

  // Precondition checker.
  private boolean preconditionsIndexedBF(List<Integer> indexes, List<Integer> dontCares) {
    // Predicate which is sent to functions below.
    // See javadoc constructor preconditions for clarifications.
    IIndexFilter<Integer> outOfBounds = new IIndexFilter<Integer>() {
      @Override
      public boolean accepts(Integer elem) {
        return elem < 0 || elem >= traversalHelper.getTableSize();
      }
    };

    if (BooleanSwissKnife.any(outOfBounds, indexes))
      return false;
    if (BooleanSwissKnife.any(outOfBounds, dontCares))
      return false;
    if (!Collections.disjoint(indexes, dontCares))
      return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Computes the value when function is applied to it's domain's values.
   */
  @Override
  public BooleanValue getValue() {
    return traversalHelper.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BooleanVariable> getDomain() {
    return Collections.unmodifiableList(domain);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMinterm(int index) {
    return traversalHelper.hasMinterm(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMaxterm(int index) {
    return traversalHelper.hasMaxterm(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasDontCare(int index) {
    return traversalHelper.hasDontCare(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Integer> mintermIterable() {
    return traversalHelper.mintermIterable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Integer> maxtermIterable() {
    return traversalHelper.maxtermIterable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Integer> dontcareIterable() {
    return traversalHelper.dontcareIterable();
  }

}
