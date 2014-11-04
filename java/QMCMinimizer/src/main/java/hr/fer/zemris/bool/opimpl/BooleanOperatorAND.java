package hr.fer.zemris.bool.opimpl;

import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanValue;

import java.util.List;

/**
 * Class defines boolean and operator
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class BooleanOperatorAND extends BooleanOperator {

  /**
   * Construct from list of {@link BooleanSource}
   * 
   * @param sources
   */
  public BooleanOperatorAND(List<BooleanSource> sources) {
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
      result = and(result, sources.get(i).getValue());
    }

    return result;
  }

  /**
   * Compute boolean value for sources that this operator relates.
   * 
   * @param first value
   * @param second value
   * @return propositional logical outcome of or operation upon given values.
   */
  private BooleanValue and(BooleanValue first, BooleanValue second) {

    if (first.equals(BooleanValue.FALSE) || second.equals(BooleanValue.FALSE)) {
      return BooleanValue.FALSE;
    } else if (first.equals(BooleanValue.DONT_CARE) || second.equals(BooleanValue.DONT_CARE)) {
      return BooleanValue.DONT_CARE;
    } else {
      return BooleanValue.TRUE;
    }
  }

}
