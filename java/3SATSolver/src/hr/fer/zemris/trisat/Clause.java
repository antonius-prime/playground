package hr.fer.zemris.trisat;

import java.util.Arrays;

/**
 * This class represents a clause of a logical formula which is itself
 * in the conjunctive normal form (CNF). It is constituted by the three
 * disjunct variables.
 *  
 * @author Antonio Paunovic
 * @version 0.1
 */
public class Clause {
  
  private int[] literals;
  
  /**
   * Constructor which initializes instance using provided indices.
   * 
   * @param indices of the variables.
   */
  public Clause(int[] indices) {
    this.literals = Arrays.copyOf(indices, indices.length);
  }
  
  /**
   * Return the number of the literals.
   * 
   * @return number of the literals
   */
  public int getSize() {
    return literals.length;
  }
  
  /**
   * Return the indexed literal.
   * 
   * @param index of the literal.
   * @return indexed literal, if exist.
   * @throws IllegalArgumentException
   */
  public int getLiteral(int index) {
    
    if (index < 0 || index >= literals.length) {
      throw new IllegalArgumentException("Clause: Bad index given "+index);
    }
    
    return literals[index]; 
  }
  
  /**
   * Check whether the assigned values satisfy the clause.
   * 
   * @param assignment of values to variables.
   * @return {@code true} if satisfies, otherwise {@code false}.
   */
  public boolean isSatisfied(BitVector assignment) {

    boolean satisfied = false;
    
    for (int literal : literals) {
      
      boolean val = assignment.get(Math.abs(literal)-1);

      satisfied = (literal > 0 && val == true) || (literal < 0 && val == false);
      
      if (satisfied) {
        break;
      }
          
    }
    
    return satisfied;
  }
    
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb =
        new StringBuilder(literals.length);
    
    for (int i = 0; i < literals.length; i++) {
      int literal = literals[i];

      if (literal < 0) sb.append("-");
      
      sb.append("x"+Math.abs(literal));
      
      if (i < literals.length - 1) {
        sb.append('+');
      }
    }
    
    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(literals);
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
    Clause other = (Clause) obj;
    if (!Arrays.equals(literals, other.literals))
      return false;
    return true;
  }

}
