package hr.fer.zemris.bool.qmc.symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing symbolic operations. Symbolic operations are are actually basic logical
 * operations like conjucntion and disjunction.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public abstract class SymbolicOperation implements SymbolicExpression {

  /** List of operands associated with this operations. */
  protected List<SymbolicExpression> operands;

  /**
   * Constructor for expression. It initializes the operands of this instance. Preconditions:
   * <ul>
   * <li>operands mustn't be {@code null}.</li>
   * <li>operands mustn't be empty list.</li>
   * </ul>
   * 
   * @param operands list.
   * @throws IllegalArgumentException on preconditions violation.
   */
  protected SymbolicOperation(List<SymbolicExpression> operands) {
    if (!preconditionSymbolicOperation(operands)) {
      throw new IllegalArgumentException("Precondition violation.");
    }
    this.operands = new ArrayList<>(operands);
  }

  /** Constructor preconditions. */
  private boolean preconditionSymbolicOperation(List<SymbolicExpression> operands) {
    return operands != null && !operands.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SymbolicExpression> getOperands() {
    return Collections.unmodifiableList(operands);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((operands == null) ? 0 : operands.hashCode());
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
    SymbolicOperation other = (SymbolicOperation) obj;
    if (operands == null) {
      if (other.operands != null)
        return false;
    } else if (!operands.equals(other.operands))
      return false;
    return true;
  }
}
