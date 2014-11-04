package hr.fer.zemris.bool.opimpl;

import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanValue;

import java.util.List;

/**
 * Class representing logical or value.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class BooleanOperatorOR extends BooleanOperator {

  /**
   * Construct operator from given list of sources.
   * 
   * @param sources list of that operator usees.
   */
  public BooleanOperatorOR(List<BooleanSource> sources) {
    super(sources);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BooleanValue getValue() {
    final int FIRST_ELEMENT = 0;

    BooleanSource first = sources.get(FIRST_ELEMENT);
    BooleanValue result = first.getValue();

    for (int i = 1; i < sources.size(); i++) {
      result = or(result, sources.get(i).getValue());
    }

    return result;
  }

  /**
   * Compute boolean value for sources that this operator relates.
   * 
   * @param first value
   * @param second value
   * @return propositional logical outcome of and operation upon given values.
   */
  private BooleanValue or(BooleanValue first, BooleanValue second) {

    if (first.equals(BooleanValue.TRUE) || second.equals(BooleanValue.TRUE)) {
      return BooleanValue.TRUE;
    } else if (first.equals(BooleanValue.DONT_CARE) || second.equals(BooleanValue.DONT_CARE)) {
      return BooleanValue.DONT_CARE;
    } else {
      return BooleanValue.FALSE;
    }
  }

}
