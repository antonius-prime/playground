package hr.fer.zemris.bool;

import java.util.List;

/**
 * This class represents any source capable of producing legal BooleanValue.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public interface BooleanSource {

  /**
   * Contractor is capable of producing {@link BooleanValue}.
   * 
   * @return value of implementation
   */
  BooleanValue getValue();

  /**
   * Contractor can provide an information based on which variables is this value produced.
   * 
   * @return list of variables which constitute source.
   */
  List<BooleanVariable> getDomain();
}
