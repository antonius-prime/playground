package hr.fer.zemris.bool.opimpl;

import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanValue;

import java.util.List;

/**
 * Class representing boolean not relation.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class BooleanOperatorNOT extends BooleanOperator {

  public BooleanOperatorNOT(List<BooleanSource> sources) {
    super(sources);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BooleanValue getValue() {
    BooleanValue result;
    // not operates on one expression only
    switch (sources.get(0).getValue()) {
      case TRUE:
        result = BooleanValue.FALSE;
        break;
      case FALSE:
        result = BooleanValue.TRUE;
        break;
      default:
        result = BooleanValue.DONT_CARE;
    }

    return result;
  }



}
