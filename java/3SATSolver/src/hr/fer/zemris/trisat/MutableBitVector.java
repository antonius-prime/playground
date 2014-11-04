package hr.fer.zemris.trisat;

/**
 * Class representing the boolean vector. It is used as a possible solution,
 * that is, an assignment of values for SAT solvers. 
 * This is mutable variant of {@link BitVector} class. Because of mutability it
 * is practical for adjustments or mutations of the possible solution.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class MutableBitVector extends BitVector {

  /**
   * Constructor with deterministic initialization.
   * 
   * @param bits boolean array of values.
   */
  public MutableBitVector(boolean... bits) {
    super(bits);
  }
  
  /**
   * Constructor with random initialization.
   * 
   * @param n length of the vector. 
   */
  public MutableBitVector(int n) {
    super(n);
  }

  /**
   * Write the given value to the vector cell indexed by given index. 
   * 
   * @param index
   * @param value
   */
  public void set(int index, boolean value) {
    bits[index] = value;
  }
}
