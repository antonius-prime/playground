package hr.fer.zemris.trisat;

import java.util.Arrays;

/**
 * Class implementing a boolean formula. 
 * Formula is a conjunction of disjunction of three-variable clauses.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class SATFormula {

  private Clause[] clauses;
  private final int numberOfVariables;
  
  /**
   * Constructor for the formula. Initialize the formula with given clauses.
   * 
   * @param numberOfVariables of the forumla
   * @param clauses array of clauses which will constitute the formula.
   */
  public SATFormula(int numberOfVariables, Clause[] clauses) {
    this.clauses = clauses;
    this.numberOfVariables = numberOfVariables;
  }

  /**
   * Access the indexed clause.
   *
   * @param index of the clause.
   * @return indexed clause.
   * @throws IllegalArgumentException on bad index.
   */
  public Clause getClause(final int index) {
    if (index < 0 || index >= clauses.length) 
      throw new IllegalArgumentException("Formula: Bad index given "+index);
    
    return clauses[index];
  }
  
  /**
   * Return the number of constituting clauses.
   * 
   * @return number of clauses.
   */
  public int getNumberOfClauses() {
    return clauses.length;
  }

  /**
   * Return the number of variables.
   * 
   * @return number of variables.
   */
  public int getNumberOfVariables() {
    return numberOfVariables;
  }

  /**
   * Check whether the assigned values satisfy the formula.
   * 
   * @param assignment of values to variables.
   * @return {@code true} if satisfies, otherwise {@code false}.
   */
  public boolean isSatisfied(BitVector assignment) {
    
    boolean satisfied = false;
    
    for (Clause clause : clauses) {
      
      satisfied = clause.isSatisfied(assignment);
      
      if (!satisfied) {
        break;
      }
    }
    
    return satisfied;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(clauses);
    result = prime * result + numberOfVariables;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SATFormula other = (SATFormula) obj;
    if (!Arrays.equals(clauses, other.clauses))
      return false;
    if (numberOfVariables != other.numberOfVariables)
      return false;
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    
    StringBuilder sb = new StringBuilder(clauses.length);
    
    for (Clause clause : clauses) {
      sb.append("("+clause+")");
    }
    
    return sb.toString();
  }

}
