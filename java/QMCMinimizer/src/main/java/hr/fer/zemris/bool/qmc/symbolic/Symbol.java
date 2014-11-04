package hr.fer.zemris.bool.qmc.symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a simple symbol in a {@link SymbolicExpression} hierarchy.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class Symbol implements SymbolicExpression {

  /** Read only property. Symbols value or representation or identity or ...blah */
  private String value;
  /** List of operands associated with this symbol. */
  private List<SymbolicExpression> operands = new ArrayList<>();

  /**
   * Symbols constructor. Initializes its value by given string parameter
   * 
   * @param value symbols string representation
   */
  public Symbol(String value) {
    this.value = value;
    operands.add(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SymbolicExpression evaluate() {
    return this;
  }

  /**
   * Getter for symbols value property.
   * 
   * @return string representing symbols value.
   */
  public String getValue() {
    return value;
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
  public String toString() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SymbolicExpression simplify() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((operands == null) ? 0 : operands.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
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
    Symbol other = (Symbol) obj;
    if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }
}
