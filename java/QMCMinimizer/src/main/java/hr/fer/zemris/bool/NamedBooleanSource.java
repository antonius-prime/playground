package hr.fer.zemris.bool;

/**
 * Interface NamedBooleanSource represents a source with associated name.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public interface NamedBooleanSource extends BooleanSource {

  /**
   * Get's the name of the {@link BooleanSource}.
   * 
   * @return name
   */
  String getName();
}
