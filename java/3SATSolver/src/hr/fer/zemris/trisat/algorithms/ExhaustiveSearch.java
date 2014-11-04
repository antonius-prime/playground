package hr.fer.zemris.trisat.algorithms;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.ISATAlgorithm;
import hr.fer.zemris.trisat.SATFormula;

/**
 * This class implements the algorithm of exhaustive satisfiability checks
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class ExhaustiveSearch implements ISATAlgorithm {

  /**
   * Solve the problem searching through the whole solution space. Brute-force it.
   */
  @Override
  public BitVector solve(SATFormula formula) {

    final int numberOfVariables = formula.getNumberOfVariables();
    
    List<boolean[]> models = new ArrayList<>();
    
    for (boolean[] vec : booleanCombinations(numberOfVariables)) {
      if (formula.isSatisfied(new BitVector(vec))) {
        System.out.println(stringArray(vec));
        models.add(vec);  
      }
    }
    
    BitVector solution = new BitVector(
        models.isEmpty() ? null : models.get(0)); 
    
    return solution;
  }
  
  /* Helper function for creating combinations of a solution. */
  private List<boolean[]> booleanCombinations(int n) {
    
    List<boolean[]> ret = new ArrayList<>();
    
    for (List<Boolean> lb : booleanCombinationsH(n)) {
      ret.add(listToPrimitiveArray(lb));
      
    }
    
    return ret;
  }
  
  /** Helper function which recursively generates combinations. */
  private ArrayList<ArrayList<Boolean>> booleanCombinationsH(int n) {
    
    // Base case.
    if (n == 1) {
      ArrayList<ArrayList<Boolean>> ls = new ArrayList<>();
      ArrayList<Boolean> t = new ArrayList<>(); t.add(true);
      ArrayList<Boolean> f = new ArrayList<>(); f.add(false);
      
      ls.add(t); ls.add(f);
      
      return ls;
    }
        
    // General case.
    ArrayList<ArrayList<Boolean>> trues = booleanCombinationsH(n-1);
    trues.stream().forEach(l -> l.add(true));

    ArrayList<ArrayList<Boolean>> falses = booleanCombinationsH(n-1);
    falses.stream().forEach(l -> l.add(false));
    
    trues.addAll(falses);

    return trues;
  }
  
  /* Helper function which turns list to primitive array. */
  private static boolean[] listToPrimitiveArray(List<Boolean> list) {

    boolean[] primitives = new boolean[list.size()];

    int i = 0;
    for (Boolean b : list) {
      primitives[i] = b; //  list.get(i) can have poor performance depending on the List implementation
      i++;
    }
    
    return primitives;
  }
  
  /* 
   * Show array of booleans as array of ones and zeros where
   * 'true' maps to '1' and 'false' to '0'  
   */
  private static String stringArray(boolean[] arr) {
    
    StringBuilder sb = new StringBuilder(arr.length);

    for (boolean elem : arr) {
      sb.append(elem == true ? 1 : 0);        
    }
    
    return sb.toString();
  }
}


