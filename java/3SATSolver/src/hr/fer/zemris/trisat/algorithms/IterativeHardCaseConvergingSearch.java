package hr.fer.zemris.trisat.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.ISATAlgorithm;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SATFormulaStats;

/**
 * Implementation of the {@link ISATAlgorithm} for solving 3SAT problems.
 * This concrete implementation uses a heuristic for better space searching.
 * The heuristics rewards assignments which satisfy those clauses which are 
 * rarely satisfied and penalizes dearly those who fail to do so. 
 * It also rewards little those which satisfy the clauses which are easy to satisfy.
 * This way search converges to the solutions better than with genereal
 * iterative algorigm {@link IterativeSearch}.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class IterativeHardCaseConvergingSearch implements ISATAlgorithm {

  /* Number of iterations. */
  private final static int ITER_COUNT = 10000;

  /* Number of assignments stored in the map of possible solutions. */
  private final int numberOfBest = 10;
  
  @Override
  public BitVector solve(SATFormula formula) {
   
    final int numberOfVariables = formula.getNumberOfVariables();
    Random rand = new Random();
    BitVector currentSolution = new BitVector(rand, numberOfVariables);
    SATFormulaStats formulaStats = new SATFormulaStats(formula); 
    Map<BitVector, Double> solutions = new HashMap<>();
    Set<BitVector> valid = new HashSet<>();

    int iterationsToGo = ITER_COUNT;
    
    do {
      
      double currentFitness;
      solutions = new HashMap<>();
      
      formulaStats.setAssignment(currentSolution, false);
      
      BitVectorNGenerator generator = new BitVectorNGenerator(currentSolution);

      for (MutableBitVector assignment : generator) {
        
        formulaStats.setAssignment(assignment, true);        
        currentFitness = formulaStats.getPercentageBonus();
        
        if (formulaStats.isSatisfied()) {
          valid.add(assignment);
        }
        
        addSolution(solutions, assignment, currentFitness);
      }
      
      currentSolution = getRandomSolution(rand, solutions);
      
    } while (iterationsToGo-- > 0);

    // Printout found solutions to the STDOUT.
    for (BitVector bitVector : valid) {
      System.out.println(bitVector);
    }
    
    return currentSolution;
  }
  
  /*
   * This method tries to add the possible solution to the existing solutions.
   * It is added instead of the first existing solution from which it 
   * has better fitness value.  
   */
  private void addSolution(Map<BitVector, Double> solutions, BitVector solution, double fitness) {
    
    int solutionCnt = solutions.size();
    
    // Case: Possible solutions not filled yet.
    if (solutionCnt < numberOfBest) {
      solutions.put(solution, fitness);     
    
    } else {  // Case: See if it is better than some existing.
      
      // Note: perhaps should sort first in ascending order, or use sorted map. 
      for (Entry<BitVector, Double> entry : solutions.entrySet()) {
       
        if (Double.compare(fitness, entry.getValue()) > 0) {
          solutions.remove(entry.getKey());
          solutions.put(solution, fitness);
          break;
        }
      }
    }
  }
  
  /*
   * Extract random solution from the solution map.
   */
  private BitVector getRandomSolution(Random rand, Map<BitVector, Double> solutions) {
   
    List<BitVector> keysAsArray = new ArrayList<>(solutions.keySet());
   
    return keysAsArray.get(rand.nextInt(keysAsArray.size()));
  }
  
  
}
