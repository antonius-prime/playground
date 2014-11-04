package hr.fer.zemris.trisat.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.ISATAlgorithm;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;

/**
 * This class implements basic iterative search. It takes solution and searches it neighborhood. If
 * new best fitness is worse than the last one, it cancels the program.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class IterativeSearch implements ISATAlgorithm {

  final static int ITER_COUNT = 100000;

  @Override
  public BitVector solve(SATFormula formula) {

    final int numberOfVariables = formula.getNumberOfVariables();

    Random rand = new Random();
    BitVector currentSolution = new BitVector(rand, numberOfVariables);

    GeneralOptimalSolutionsSet solutions = new GeneralOptimalSolutionsSet();

    double currentFitness = fitness(formula, currentSolution);
    int iterationsToGo = ITER_COUNT;

    do {
      BitVectorNGenerator generator = new BitVectorNGenerator(currentSolution);
      MutableBitVector[] neighborhood = generator.createNeighbourhood();

      for (MutableBitVector assignment : neighborhood) {

        if (Double.compare(fitness(formula, assignment), solutions.getFitness()) > 0) {
          solutions.init(assignment, fitness(formula, assignment));
        } else if ((int) fitness(formula, assignment) == (int) solutions.getFitness()) {
          solutions.add(assignment, fitness(formula, assignment));
        }

      }

      if (Double.compare(solutions.getFitness(), currentFitness) < 0) {
        System.out.println("curr:\n\t" + currentFitness);
        System.out.println("best:\n\t" + solutions.getFitness());
        System.out.println("Captin, it seems we are stuck in the local optimum!");
        System.out.println("Abandon ship! Every man for himself...");
        System.exit(0);
      } else {
        currentFitness = solutions.getFitness();
        currentSolution = getRandomSolution(rand, solutions);
      }
      System.out.println("Iteration: " + (ITER_COUNT - iterationsToGo) + " has fitness: "
          + currentFitness);

    } while (iterationsToGo-- > 0);

    if (solutions.getFitness() == 91)
      for (BitVector bv : solutions.getSolutions()) {
        System.out.println(bv);
      }
    // Return the last one.
    return currentSolution;
  }

  /*
   * Extract random solution from the solution map.
   */
  private static BitVector getRandomSolution(Random rand, final GeneralOptimalSolutionsSet solutions) {

    final int index = rand.nextInt(solutions.size());

    return solutions.getSolution(index);
  }

  /**
   * Class implementing a collection of best solution for a formula. How good solutions are, depends
   * on the calculated fitness value of those solutions.
   * 
   * @author Antonio Paunovic
   * @version 0.1
   */
  public class GeneralOptimalSolutionsSet {

    protected double bestFitness;
    protected List<BitVector> solutions = new ArrayList<>();

    public void init(BitVector solution, double fitness) {
      solutions = new ArrayList<>();
      solutions.add(solution);
      bestFitness = fitness;
    }

    public void add(BitVector solution, double fitness) {

      if ((int) fitness > (int) bestFitness) {
        solutions = new ArrayList<>();
        solutions.add(solution);
        bestFitness = fitness;
      } else if ((int) fitness == (int) bestFitness) {
        solutions.add(solution);
      } else {
        // Given Solution sucks so it wont be included.
        return;
      }
    }

    /**
     * Return best fitness so far.
     * 
     * @return fitness
     */
    public double getFitness() {
      return bestFitness;
    }

    /**
     * Get solution indexed by argument.
     * @param index Inex of the solution
     * @return Indexed value
     */
    public BitVector getSolution(int index) {
      return solutions.get(index);
    }

    /**
     * Return view of the solution collection.
     * @return view of the solution collection.
     */
    public List<BitVector> getSolutions() {
      return Collections.unmodifiableList(solutions);
    }

    /**
     * Get the size of the solution set.
     * @return number of solutions stored.
     */
    public int size() {
      return solutions.size();
    }
  }

}
