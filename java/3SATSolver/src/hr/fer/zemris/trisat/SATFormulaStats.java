package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithms.IterativeHardCaseConvergingSearch;

/**
 * Instances of this class calculate and store data about some {@link SATFormula}.
 * The calculations are started via assignments of some {@link BitVector}.
 * Object itself is used by the {@link IterativeHardCaseConvergingSearch} algorithm.
 *  
 * @author Antonio Paunovic
 * @version 0.3
 */
public class SATFormulaStats {
  
  private SATFormula formula;
  private double[] post;
  private int clausesSatisfied;
  private boolean formulaSatisfied;
  private double percentageBonus;
  
  private final double percentageConstantUp = 0.01;
  private final double percentageConstantDown = 0.1;
  private final double percentageUnitAmount= 50;
  
  /**
   * Constructor which initializes the instance with for formula. 
   * 
   * @param formula to be analyzed.
   */
  public SATFormulaStats(SATFormula formula) {
    this.formula = formula;
    this.post = new double[formula.getNumberOfClauses()];
  }

  /**
   * Provides the assignment for which the formula is analyzed. All relevant
   * data is stored in object fields accessible interface of the {@link SATFormulaStats} object.
   * 
   * @param assignment for which the formula is analyzed.
   * @param updatePercentages {@code true} for update, {@code false} for initialization.
   */
  public void setAssignment(BitVector assignment, boolean updatePercentages) {
    this.formulaSatisfied = this.formula.isSatisfied(assignment);
    numberOfSatisfied(assignment);
    
    if (updatePercentages) {
      updatePost(assignment);
    } else {
      initPost(assignment);
    }
  }  

  /* 
   * Method for formula statistics initialization.
   */
  private void initPost(BitVector assignment) {
    
    for (int i = 0, clausesCnt = formula.getNumberOfClauses();
        i < clausesCnt; 
        i++) {
      
      if (formula.getClause(i).isSatisfied(assignment)) {
        post[i] = (1 - post[i]) * percentageConstantUp;
      } else {
        post[i] = (0 - post[i]) * percentageConstantDown;
      }
    }
  }
  
  /* 
   * Method for updating the bonuses (fitness) for some assignment.
   */
  private void updatePost(BitVector assignment) {
    
    double Z = this.clausesSatisfied;
    
    for (int i = 0, clausesCnt = formula.getNumberOfClauses();
        i < clausesCnt; 
        i++) {
      
      if (formula.getClause(i).isSatisfied(assignment)) {
        Z += percentageUnitAmount * (1 - post[i]);
      } else {
        Z += -percentageUnitAmount * (1 - post[i]);
      }
    }
    
    this.percentageBonus = Z;
  }

  /**
   * Return the number of satisfied clauses.
   * 
   * @return number of satisfied clauses
   */
  public int getNumberOfSatisfied() {
    return clausesSatisfied;
  }
  
  /*
   * Helper which calculates and updates number of satisfied clauses. 
   */
  private void numberOfSatisfied(BitVector assignment) {
    
    this.clausesSatisfied= 0;
    
    for (int i = 0, clausesCnt = formula.getNumberOfClauses();
        i < clausesCnt; 
        i++) {
      
      if (formula.getClause(i).isSatisfied(assignment)) {
        this.clausesSatisfied++;
      } 
    }
  }

  /**
   * Informs the caller whether the formula is satisfied by the current assignment.
   * 
   * @return {@code true} if satisfied, otherwise {@code false};
   */
  public boolean isSatisfied() {
    return formulaSatisfied;
  }

  /**
   * This method returns the calculated percentage bonus. The calculated percentage bonus is the 
   * sum of the corrections for the clauses. That counts as fitness.
   * 
   * @return sum of the corrections for the clauses.
   */
  public double getPercentageBonus() {
    return percentageBonus;
  }

  /**
   * Returns the calculated percentages for the clause.
   * 
   * @param index of the clause for which percentage is returned.
   * @return percentage index by given index argument.
   * @throws IllegalArgumentException on invalid index.
   */
  public double getPercentage(int index) {
    
    if (index < 0 || index >= post.length)
      throw new IllegalArgumentException("Formula statistics: bad index given "+index);
    
    return post[index];
  }
}
