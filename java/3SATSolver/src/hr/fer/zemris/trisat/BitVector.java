package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Random;

/**
 * Class representing the boolean vector. It is used as a possible solution for the problem,
 * that is, an assignment of values for SAT solvers.
 * 
 * @author Antonio Paunovic
 * @version 0.3
 */
public class BitVector {

  protected boolean[] bits;
  
  /**
   * Constructor which, given a random number generator and a length, constructs
   * classes internal bit array.
   * 
   * @param rand Random number generator.
   * @param numberOfBits Size of the solution vector.
   */
  public BitVector(Random rand, int numberOfBits) {
    bits = new boolean[numberOfBits];
    
    for (int i = 0; i < numberOfBits; i++) {
      bits[i] = rand.nextBoolean();
    }
        
  }
  
  /**
   * Constructor which initializes the bit array using the given array of bits.
   * 
   * @param bits Array of bits to use.
   */
  public BitVector(boolean ... bits) {
    this.bits = Arrays.copyOf(bits, bits.length);
  }
  
  /**
   * Constructor which creates an bit array given an integer.
   * 
   * @param n Integer representing vector of bits.
   */
  public BitVector(int n) {
    this.bits = intToBoolean(n);
  }
  
  /**
   * Return the indexed cell of the bit string.
   * 
   * @param index Index of the value. 
   * @return {@code boolean} Vector value at given index.
   */
  public boolean get(final int index) {
    return bits[index];
  }
  
  /**
   * Return the size of the stored bit string.
   * 
   * @return {@code int} Size of the bit string. 
   */
  public int getSize() {
    return bits.length;
  }

  /**
   * String representation of bit vector in which false and true are represented as 0 and 1. 
   */
  @Override
  public String toString() {
    
    char[] bitstring = new char[bits.length];
    
    for (int i = 0; i < bits.length; i++) {
      bitstring[i] = bits[i] ? '1' : '0'; 
    }
    
    return String.valueOf(bitstring);
  }

  /** 
   * Return mutable copy of this {@link BitVector} instance.
   * 
   * @return {@link MutableBitVector} copy.
   */
  public MutableBitVector copy() {
    return new MutableBitVector(bits);
  }
  
  /*
   * Helper method which, given an integer, turns it to a array of boolean values.
   * False corresponds to '0' and true to '1'.
   */
  private static boolean[] intToBoolean(final int n) {
    // Length of the bit string.
    int len = 0;
    for (int i = n; i >0; i /= 2) {
      len++;
    }
    
    // Convert to boolean array of the calculated length.
    boolean[] bools = new boolean[len];  
    
    for (int i = len-1; i >= 0; i--) {
      bools[len-1-i] = (n & (1 << i)) != 0;
    }
    
    return bools;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(bits);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BitVector other = (BitVector) obj;
    if (!Arrays.equals(bits, other.bits))
      return false;
    return true;
  }
 
  
}
