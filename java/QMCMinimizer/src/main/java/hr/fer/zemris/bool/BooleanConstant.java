package hr.fer.zemris.bool;

import java.util.Collections;
import java.util.List;

/**
 * This class is used to represent boolean constants. Possible values are {@link BooleanValue}.
 * Namely, {@code TRUE} and {@code FALSE}.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class BooleanConstant implements BooleanSource {

  /**
   * TRUE constant.
   */
  public final static BooleanConstant TRUE = new BooleanConstant(BooleanValue.TRUE);
  /**
   * FALSE constant.
   */
  public final static BooleanConstant FALSE = new BooleanConstant(BooleanValue.FALSE);

  /**
   * Read-only property which stores value of this boolean expression, that is constants TRUE or
   * FALSE.
   */
  private BooleanValue value;

  /**
   * Initialize instance's {@code value}. Though by type it could also be {@code DONT_CARE}, since
   * constructor is private, it will only be {@code TRUE} or {@code FALSE};
   * 
   * @param value {@code TRUE, FALSE}.
   */
  private BooleanConstant(BooleanValue value) {
    this.value = value;
  }

  /**
   * Returns instance's value.
   * 
   * @return value TRUE or FALSE.
   */
  @Override
  public BooleanValue getValue() {
    return value;

  }

  /**
   * Constant's domain of variables is empty.
   * 
   * @return empty list.
   */
  @Override
  public List<BooleanVariable> getDomain() {
    return Collections.emptyList();
  }

}
