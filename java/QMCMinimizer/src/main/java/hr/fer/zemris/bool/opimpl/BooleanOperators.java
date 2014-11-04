package hr.fer.zemris.bool.opimpl;

import java.util.Arrays;

import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;

/**
 * Factory class for {@link BooleanOperator} implementations
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class BooleanOperators {

  private BooleanOperators() {}

  /**
   * Create and operator from given {@link BooleanSource} array.
   * 
   * @param sources representing an logical expression
   * @return boolean and operator
   */
  public static BooleanOperator and(BooleanSource... sources) {
    return new BooleanOperatorAND(Arrays.asList(sources));
  }

  /**
   * Create and operator from given {@link BooleanSource} array.
   * 
   * @param sources representing an logical expression
   * @return boolean or operator
   */
  public static BooleanOperator or(BooleanSource... sources) {
    return new BooleanOperatorOR(Arrays.asList(sources));
  }

  /**
   * Create and operator from given {@link BooleanSource} array.
   * 
   * @param sources representing an logical expression
   * @return boolean not operator
   * @throws IllegalArgumentException if more than one expression are given
   */
  public static BooleanOperator not(BooleanSource... sources) {
    if (sources.length != 1) {
      throw new IllegalArgumentException("Not relation operates on a signle expresssion.");
    }
    return new BooleanOperatorNOT(Arrays.asList(sources));
  }
}
