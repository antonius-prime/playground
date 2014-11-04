package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithms.ExhaustiveSearch;
import hr.fer.zemris.trisat.algorithms.IterativeHardCaseConvergingSearch;
import hr.fer.zemris.trisat.algorithms.IterativeSearch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Program for solving 3SAT problem.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class TriSATSolver {

  /*
   * Class method which, given a file path, extracts information for SAT formula
   * initialization. 
   */
  private static SATFormula readFormulaFromFile(String filename) {
    
    int numberOfVariables = 0;
    int numberOfClauses = 0;
    
    List<int[]> clausesIndices = new ArrayList<>();
    
    boolean definition = false;
    
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {

      for(String line; (line = br.readLine().trim()) != null; ) {
        
        if (line.startsWith("c")) continue;
        if (line.startsWith("%")) break;
        
        if (line.startsWith("p")) { 
          String[] lineSplit = line.split("\\s+");
          numberOfVariables = Integer.parseInt(lineSplit[2]);
          numberOfClauses = Integer.parseInt(lineSplit[3]);
          definition = true;
          continue;
        }
        
        if (!definition) throw new RuntimeException("Malformed input file.");
        
        String[] nums = line.split("\\s+");
        int[] indices = new int[3];

        for (int index = 0; index < 3; index++) {
          try {
            indices[index] = Integer.parseInt(nums[index]);
          } catch (NumberFormatException e) {
            throw new RuntimeException("Malformed input file.");
          }
        }
        
        clausesIndices.add(indices);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    Clause[] clauses = new Clause[numberOfClauses];
    
    int index = 0;
    for (int[] is : clausesIndices) {
      clauses[index++] = new Clause(is);
    }
    
    return new SATFormula(numberOfVariables, clauses);    
  }
  
  /**
   * Main program 3SAT solving.  
   * 
   * @param args first argument is number of the algorithm (1,2,3) and second path to the file. 
   */
  public static void main(String[] args) {
    
    if (args.length != 2) {
      System.err.println("Please provide two arguments: <number_of_algorithm> <input_file_name> ");
      System.exit(0);
    }
    
    String algorithm = args[0];
    String filename = args[1];
    
    ISATAlgorithm alg = new IterativeHardCaseConvergingSearch();
    
    switch (algorithm) {
      case "1" : alg = new ExhaustiveSearch(); break;
      case "2" : alg = new IterativeSearch(); break;
      case "3" : alg = new IterativeHardCaseConvergingSearch(); break;
      default  : alg = new ExhaustiveSearch();
    }
    
    SATFormula formula = readFormulaFromFile(filename);
    
    alg.solve(formula);
    
  }
}
