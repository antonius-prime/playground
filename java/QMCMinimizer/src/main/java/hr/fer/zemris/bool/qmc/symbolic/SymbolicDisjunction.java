package hr.fer.zemris.bool.qmc.symbolic;

import java.util.Iterator;
import java.util.List;

/**
 * This class is an implementation of {@link SymbolicOperation}. It represents logical operation of
 * disjunction.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class SymbolicDisjunction extends SymbolicOperation {

  /**
   * Constructor for disjunction operation. Precondition:
   * <ul>
   * <li>there must be at least one operand.</li>
   * </ul>
   * 
   * @param operands list of operands.
   * @throws IllegalArgumentException on precondition violation.
   */
  public SymbolicDisjunction(List<SymbolicExpression> operands) {
    super(operands);
    if (!preconditionSymbolicDisjunction(operands)) {
      throw new IllegalArgumentException(
          "Precondition violation: provide at least one arguments to constructor. Provided: "
              + operands.size());
    }
  }

  /** Constructor preconditions. */
  private boolean preconditionSymbolicDisjunction(List<SymbolicExpression> operands) {
    return operands.size() >= 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SymbolicExpression evaluate() {
    // operands = simplify(operands);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SymbolicExpression simplify() {
    return new SymbolicDisjunction(removeIrrelevant(operands));
  }

  /**
   * Remove all irrelevant clause from expression. It is irrelevant if the same expression can be proved by
   * an existing, simpler expression. That is if there exists an disjunction expression that is real
   * subset of given expression. 
   * 
   * @param rest existing expressions.
   * @return original list with irrelevant clauses removed.
   */
  private List<SymbolicExpression> removeIrrelevant(List<SymbolicExpression> rest) {
    
    Iterator<SymbolicExpression> fstIter = rest.iterator();
    
    while (fstIter.hasNext()) {
    
      SymbolicExpression fst = fstIter.next();
      Iterator<SymbolicExpression> sndIter = rest.iterator();
      
      while (sndIter.hasNext()) {
        SymbolicExpression snd = sndIter.next();
      
        if (fst== snd) {
          continue;
        }
        
        if (snd instanceof SymbolicConjunction || fst instanceof SymbolicConjunction) {
          if (fst.getOperands().containsAll(snd.getOperands())) {
            fstIter.remove();
            break;
          }
        }
      }
    }
    
    return rest;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (operands.size() == 1) {
      return operands.get(0).toString();
    }
    StringBuilder sb = new StringBuilder(operands.size());
    sb.append("(");
    for (SymbolicExpression expr : operands.subList(0, operands.size() - 1)) {
      sb.append(expr.toString() + "+");
    }
    sb.append(operands.get(operands.size() - 1));
    sb.append(")");
    return sb.toString();
  }
  
}
