package hr.fer.zemris.bool.misc;

/**
 * Defines an interface for filtering implementations.
 * 
 * @author Antonio Paunovic
 * @param <T>
 * @version 0.1
 */
public interface IIndexFilter<T> {
  public boolean accepts(T elem);
}
