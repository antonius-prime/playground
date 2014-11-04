package hr.fer.zemris.bool.qmc.symbolic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is an implementation of {@link SymbolicOperation}. It represents logical operation of
 * conjunction.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class SymbolicConjunction extends SymbolicOperation {

  /**
   * Constructor for conjunction operation. Precondition:
   * <ul>
   * <li>there must be at least one operand.</li>
   * </ul>
   * 
   * @param operands list of operands.
   * @throws IllegalArgumentException on precondition violation.
   */
  public SymbolicConjunction(List<SymbolicExpression> operands) {
    super(operands);
    if (!preconditionSymbolicConjunction(operands)) {
      throw new IllegalArgumentException(
          "Precondition violation: provide at least one arguments to constructor. Provided: "
              + operands.size());
    }
  }

  /** Constructor preconditions. */
  private boolean preconditionSymbolicConjunction(List<SymbolicExpression> operands) {
    return operands.size() >= 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SymbolicExpression evaluate() {
    return distribute(operands).get(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SymbolicExpression simplify() {
    List<SymbolicExpression> res = new ArrayList<>();
    for (int i = 0, opLen = operands.size(); i < opLen; i++) {
      SymbolicExpression fst = operands.get(i);
      if (!res.containsAll(fst.getOperands())) {
        res.addAll(fst.getOperands());
      }
    }

    return new SymbolicConjunction(res);
  }

  /**
   * Distribution rule of logic. For a conjunction of disjunctions, it creates a disjunction of
   * conjunctions.
   * 
   * @param operands to distribute.
   * @return disjunction of conjunctions.
   */
  public List<SymbolicExpression> distribute(List<SymbolicExpression> operands) {
    final int operandLen = operands.size();
    if (operandLen == 1) {
      return operands;
    }
    List<SymbolicExpression> res =
        new ArrayList<>(distribute(operands.get(0), operands.subList(1, operandLen)));

    return res;
  }

  /**
   * Implementation of distribution method. It reduces expression from left to right into one
   * disjunction of conjunctions.
   * 
   * @param operands to distribute.
   * @return disjunction of conjunctions.
   */
  public List<SymbolicExpression> distribute(SymbolicExpression start,
      List<SymbolicExpression> operands) {
    final int operandLen = operands.size();
    if (operandLen == 0) {
      return Collections.singletonList(start);
    }

    List<SymbolicExpression> disjOperands = new ArrayList<>();
    List<SymbolicExpression> ops1 = start.getOperands();
    List<SymbolicExpression> ops2 = operands.get(0).getOperands();

    for (int j = 0, ops1Len = ops1.size(); j < ops1Len; j++) {
      SymbolicExpression fst = ops1.get(j);
      for (int k = 0, ops2Len = ops2.size(); k < ops2Len; k++) {
        SymbolicExpression snd = ops2.get(k);
        disjOperands.add(new SymbolicConjunction(Arrays.asList(fst, snd)).simplify());
      }
    }

    SymbolicExpression disj = new SymbolicDisjunction(disjOperands).simplify();

    return distribute(disj, operands.subList(1, operandLen));
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(operands.size());
    for (SymbolicExpression expr : operands) {
      sb.append(expr.toString());
    }
    return sb.toString();
  }
}
