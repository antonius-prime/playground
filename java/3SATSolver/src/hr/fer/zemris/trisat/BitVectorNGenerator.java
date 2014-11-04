package hr.fer.zemris.trisat;

import java.util.Iterator;

/**
 * This class is used as a neighborhood function. It is initialized with some {@link BitVector} 
 * solution and it offers functionality of traversing possible solutions from the neighborhood of the given one.
 * That is, the solutions which are semantically close to it.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class BitVectorNGenerator implements Iterable<MutableBitVector> {

  private BitVector assignment;
  private int neighborhoodSize;

  /**
   * Constructor. Object is initialized by the bit vector.
   * @param assignment
   */
  public BitVectorNGenerator(BitVector assignment) {

    this.assignment = assignment;
    this.neighborhoodSize = assignment.getSize();
  }

  /**
   * Return solution's neighborhood iterator. 
   */
  @Override
  public Iterator<MutableBitVector> iterator() {
    return new MutableBitVectorIterator(this, neighborhoodSize);
  }

  /**
   * Generate finite neighborhood for the solution.
   * 
   * @return array of solutions.
   */
  public MutableBitVector[] createNeighbourhood() {

    MutableBitVector[] mvectors = new MutableBitVector[assignment.getSize()];

    for (int index = 0; index < neighborhoodSize; index++) {
      MutableBitVector mvb = assignment.copy();

      // Flip value on index position.
      mvb.set(index, mvb.get(index) == true ? false : true);

      // Add to collection of vectors.
      mvectors[index] = mvb;
    }

    return mvectors;
  }

  /**
   * Implementation of the iterator for the neighborhood traversal.
   * 
   * @author Antonio Paunovic
   * @version 0.1
   */
  private static class MutableBitVectorIterator implements Iterator<MutableBitVector> {

    private BitVectorNGenerator generator;
    private int pos = 0;
    private final int size;

    public MutableBitVectorIterator(BitVectorNGenerator bvng, final int size) {
      this.generator = bvng;
      this.size = size;
    }

    @Override
    public boolean hasNext() {
      return pos < size;
    }

    @Override
    public MutableBitVector next() {

      MutableBitVector nextNeighbor = generator.assignment.copy();
      nextNeighbor.set(pos, generator.assignment.get(pos) ? false : true);
      
      pos += 1;
      
      return nextNeighbor;
    }

  }

}
