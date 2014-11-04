package hr.fer.zemris.bool.fimpl;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.misc.BooleanSwissKnife;
import hr.fer.zemris.bool.opimpl.BooleanOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class that get's most of the different {@link BooleanFunction} implementations common
 * functionalities in one place and reduces the code duplication.
 * 
 * @author Antonio Paunovic
 */
public class BFGrandUnifier {

  private List<BooleanVariable> domain;
  private List<Integer> mintermIndexes;
  private List<Integer> maxtermIndexes;
  private List<Integer> dontCareIndexes;
  private boolean indexesAreMinterms;

  // Default first element of collections.
  private static final int START = 0;
  // Step used in range sequence used to get range of possible indices.
  private static final int STEP = 1;
  // Number of rows in domain's truth table.
  private int tableSize;
  // All indices that are not in property indexes.
  private List<Integer> otherIndexes;

  /**
   * Constructor for this class. Initializes all the needed values for further computations
   * 
   * @param domain variables.
   * @param indexes minterms or maxterms.
   * @param dontCares dont cares.
   * @param indexesAreMinterms are indexes minterms or maxterms?
   */
  public BFGrandUnifier(List<BooleanVariable> domain, List<Integer> indexes,
      List<Integer> dontCares, boolean indexesAreMinterms) {

    this.domain = BooleanSwissKnife.copyBooleanVariableList(domain);
    this.indexesAreMinterms = indexesAreMinterms;

    tableSize = BooleanSwissKnife.binaryVariationsCount(domain.size());
    otherIndexes = BooleanSwissKnife.range(START, tableSize, STEP);
    otherIndexes.removeAll(indexes);
    otherIndexes.removeAll(dontCares);

    if (indexesAreMinterms) {
      mintermIndexes = BooleanSwissKnife.copyIntList(indexes);
      maxtermIndexes = BooleanSwissKnife.copyIntList(otherIndexes);
    } else {
      mintermIndexes = BooleanSwissKnife.copyIntList(otherIndexes);
      maxtermIndexes = BooleanSwissKnife.copyIntList(indexes);
    }
    dontCareIndexes = dontCares;

    Collections.sort(mintermIndexes);
    Collections.sort(maxtermIndexes);
    Collections.sort(dontCareIndexes);
  }

  /**
   * Iterate over functions minterm values.
   * 
   * @return Iterable for minterm values.
   */
  public Iterable<Integer> mintermIterable() {
    return Collections.unmodifiableList(mintermIndexes);
  }

  /**
   * Iterate over functions maxterm values.
   * 
   * @return Iterable for maxterm values.
   */
  public Iterable<Integer> maxtermIterable() {
    return Collections.unmodifiableList(maxtermIndexes);
  }

  /**
   * Iterate over functions dontcare values.
   * 
   * @return Iterable for dontcare values.
   */
  public Iterable<Integer> dontcareIterable() {
    return Collections.unmodifiableList(dontCareIndexes);

  }

  /**
   * Size of logical tables constructed for the given domain.
   * 
   * @return int size of logical table.
   */
  public int getTableSize() {
    return tableSize;
  }

  /**
   * View of minterm indices.
   * 
   * @return integer list of minterm indices.
   */
  public List<Integer> getMintermIndexes() {
    return Collections.unmodifiableList(mintermIndexes);
  }

  /**
   * View of maxterm indices.
   * 
   * @return integer list of maxtermindices.
   */
  public List<Integer> getMaxtermIndexes() {
    return Collections.unmodifiableList(maxtermIndexes);
  }

  /**
   * View of dontcares indices.
   * 
   * @return integer list of dontcare indices.
   */
  public List<Integer> getDontCareIndexes() {
    return Collections.unmodifiableList(dontCareIndexes);
  }

  /**
   * Computes the value when function is applied to it's domain's values.
   */
  public BooleanValue getValue() {
    // There are two basic cases for computing value.
    // First, dontCares are empty so value is gained via index.
    // Else, dontCares are not empty so the function has to be computed
    // be it's formula.

    return (dontCareIndexes.isEmpty()) ? getValueNoDontCares() : getValueWithDontCares();
  }

  /*
   * Helper function to compute value of the function when there are no dontCares.
   */
  private BooleanValue getValueNoDontCares() {
    final int numOfVariables = domain.size();
    int bin = 0;

    for (int i = 0, weight = (int) Math.pow(2, numOfVariables - 1), domSize = domain.size(); i < domSize; i++, weight /=
        2) {

      if (domain.get(i).getValue() == BooleanValue.TRUE) {
        bin += weight;
      }
    }

    if ((indexesAreMinterms && hasMinterm(bin)) || (!indexesAreMinterms && hasMaxterm(bin))) {
      return BooleanValue.TRUE;
    } else if (hasDontCare(bin)) {
      return BooleanValue.DONT_CARE;
    } else {
      return BooleanValue.FALSE;
    }
  }

  /**
   * Check for existence of dontcare index.
   */
  public boolean hasDontCare(int index) {
    return dontCareIndexes.contains(index);
  }

  /**
   * Check for existence of maxterm index.
   */
  public boolean hasMaxterm(int index) {
    return maxtermIndexes.contains(index);
  }

  /**
   * Check for existence of minterm index.
   */
  public boolean hasMinterm(int index) {
    return mintermIndexes.contains(index);
  }

  /*
   * Helper function to compute value of the function when there exist dontCares. By default it
   * computes with minterms, but if maxterms were actually in indexes, it complements the result.
   */
  private BooleanValue getValueWithDontCares() {

    final int numOfIndexes = mintermIndexes.size();
    final int numOfVariables = domain.size();
    BooleanOperator[] products = new BooleanOperator[numOfIndexes];

    // Minterms are determined by indexes.
    for (int indexPos = 0; indexPos < numOfIndexes; indexPos++) {
      BooleanValue[] binIndex =
          BooleanSwissKnife.indexAsBinary(numOfVariables, mintermIndexes.get(indexPos));
      List<BooleanSource> vars = new ArrayList<>();

      // For every index, formula is determined by checking index's binary
      // value.
      // True variable are always the same, false are NOT.
      for (int i = 0; i < numOfVariables; i++) {

        if (binIndex[i] == BooleanValue.FALSE) {
          vars.add(BooleanOperators.not(domain.get(i)));
        } else {
          vars.add(domain.get(i));
        }
      }

      BooleanSource[] tmp = vars.toArray(new BooleanSource[numOfVariables]);
      products[indexPos] = BooleanOperators.and(tmp);
    }

    BooleanOperator sumOfProducts = BooleanOperators.or(products);
    BooleanValue computedResult = sumOfProducts.getValue();

    // If indexes are maxterms, complement the resulting value.
    return (indexesAreMinterms) ? computedResult : BooleanSwissKnife
        .complementBooleanValue(computedResult);
  }
}
