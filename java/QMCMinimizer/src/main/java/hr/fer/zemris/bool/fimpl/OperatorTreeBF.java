package hr.fer.zemris.bool.fimpl;

import java.util.Collections;
import java.util.List;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.misc.BooleanSwissKnife;
import hr.fer.zemris.bool.misc.IIndexFilter;

/**
 * This class is used to construct a boolean function by specifying an operator tree.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */

public class OperatorTreeBF implements BooleanFunction {

  /**
   * Name of the function.
   */
  private String name;
  /**
   * Variables used in function.
   */
  private List<BooleanVariable> domain;
  /**
   * Tree representing expression consisting of operators and operands.
   */
  private BooleanOperator operatorTree;

  // Helper class for iterating through indexes.
  private BFGrandUnifier traversalHelper;

  /**
   * Constructor for operator tree boolean function. It also initializes traversal helper.
   * Preconditions:
   * <ul>
   * <li>None of the method parameters can be null reference.</li>
   * </ul>
   * 
   * @param name of the function.
   * @param domain is the list of variables on which function operates.
   * @param operatorTree whole function expression.
   * @throws {@link IllegalArgumentException};
   */
  public OperatorTreeBF(String name, List<BooleanVariable> domain, BooleanOperator operatorTree) {

    if (!preconditionsOperatorTreeBF(name, domain, operatorTree)) {
      throw new IllegalArgumentException("Precondtition violation: null pointers are not allowed.");
    }

    this.name = name;
    this.domain = BooleanSwissKnife.copyBooleanVariableList(domain);
    this.operatorTree = operatorTree;

    initTraversalHelper();
  }

  private boolean preconditionsOperatorTreeBF(String name, List<BooleanVariable> domain,
      BooleanOperator operatorTree) {

    if (name == null || domain == null || operatorTree == null) {
      return false;
    }
    return true;
  }

  /**
   * Helper object which stores lists of minterms, maxterms and dont cares.
   */
  private void initTraversalHelper() {
    final int START = 0;
    final int STEP = 1;
    final int tableLength = BooleanSwissKnife.binaryVariationsCount(domain.size());
    final List<Integer> tableRange = BooleanSwissKnife.range(START, tableLength, STEP);

    traversalHelper =
        new BFGrandUnifier(domain, getIndexes(tableRange), getDontCares(tableRange), true);
  }

  // Compute minterms.
  private List<Integer> getIndexes(List<Integer> tableRange) {
    IIndexFilter<Integer> predicate = new IIndexFilter<Integer>() {
      @Override
      public boolean accepts(Integer elem) {
        return hasMinterm(elem);
      }
    };

    return BooleanSwissKnife.filterInts(predicate, tableRange);
  }

  // Compute dontcares.
  private List<Integer> getDontCares(List<Integer> tableRange) {
    IIndexFilter<Integer> predicate = new IIndexFilter<Integer>() {
      @Override
      public boolean accepts(Integer elem) {
        return hasDontCare(elem);
      }
    };

    return BooleanSwissKnife.filterInts(predicate, tableRange);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BooleanValue getValue() {
    return operatorTree.getValue();
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
    return evaluateIndex(index) == BooleanValue.TRUE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMaxterm(int index) {
    return evaluateIndex(index) == BooleanValue.FALSE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasDontCare(int index) {
    return evaluateIndex(index) == BooleanValue.DONT_CARE;
  }

  /**
   * This method checks if for given index and an underlying operator tree, a positive value can be
   * calculated. If it can, function has minterm for that index.
   * 
   * @param index for which we calculate truth value.
   * @return true if {@code BooleanValue.TRUTH} is calculated, otherwise false.
   */
  private BooleanValue evaluateIndex(final int index) {
    // Store values to regenerate them after computations.
    List<BooleanVariable> originalDomain = BooleanSwissKnife.copyBooleanVariableList(domain);
    List<BooleanVariable> operatorDomain = operatorTree.getDomain();
    final int domainSize = operatorDomain.size();
    BooleanValue[] booleanIndex = BooleanSwissKnife.indexAsBinary(domainSize, index);

    for (int i = 0; i < booleanIndex.length; i++) {
      BooleanVariable var = domain.get(i);
      int operatorIndexOfVar = operatorDomain.indexOf(var); // index of element in operators domain
      BooleanVariable operatorVar = operatorDomain.get(operatorIndexOfVar);
      operatorVar.setValue(booleanIndex[i]); // set it to value determined by index
    }

    // Regenerate original variables.
    domain = originalDomain;

    return operatorTree.getValue();
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
