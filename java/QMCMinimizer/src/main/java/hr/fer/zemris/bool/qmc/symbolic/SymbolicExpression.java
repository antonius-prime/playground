package hr.fer.zemris.bool.qmc.symbolic;

import java.util.List;

/**
 * Interface for symbolic expressions used in QMC minimization algorithm.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public interface SymbolicExpression {
  /** Evaluate expressions to its semantical value. */
  SymbolicExpression evaluate();

  /**
   * Make expressions representation simpler, if possible.
   * 
   * @return simpler expression with defining equal semantics.
   */
  SymbolicExpression simplify();

  /**
   * Get operands from which expression is constituted.
   * 
   * @return list of operands.
   */
  List<SymbolicExpression> getOperands();
}
