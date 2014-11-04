package hr.fer.zemris.trisat;

/**
 * An interface for implementing 3SAT solving algorithms.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public interface ISATAlgorithm {

  /**
   * Solve the given formula.
   * 
   * @param formula to solve
   * @return a solution that satisfies
   */
  BitVector solve(SATFormula formula);
  
  /**
   * Default fitness value is a number of how much formulas clauses does 
   * the given assignment satisfy.
   * 
   * @param formula to model
   * @param assignment possible solution
   * @return number of clauses satisfied
   */
  default double fitness(SATFormula formula, BitVector assignment) { 
    
    double fitness = 0;
    final int formulaSize = formula.getNumberOfClauses();
    
    for (int index = 0; index < formulaSize; index++) {
      if (formula.getClause(index).isSatisfied(assignment)) {
        fitness++;
      }
    }
    
    return fitness;
  }
}
