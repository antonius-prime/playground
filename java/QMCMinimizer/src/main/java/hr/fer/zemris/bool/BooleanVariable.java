package hr.fer.zemris.bool;

import java.util.Collections;
import java.util.List;

/**
 * This class represents named boolean variable. It can be used as a symbol or a placeholder in more
 * complex boolean expressions.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class BooleanVariable implements NamedBooleanSource, Cloneable {

  /**
   * Variable's value. Has getter and setter.
   */
  private BooleanValue value;
  /**
   * Variable's name. Read-only property.
   */
  private String name;

  // Initial value of variable is false.
  {
    value = BooleanValue.FALSE;
  }

  /**
   * Create variable with {@code name}.
   * 
   * @param name of the new variable.
   */
  public BooleanVariable(String name) {
    this.name = name;
  }

  /**
   * Returns the value of variable.
   * 
   * @return value {@link BooleanValue} {@code TRUE, FALSE, DONT_CARE}.
   */
  @Override
  public BooleanValue getValue() {
    return value;
  }

  /**
   * The domain of the variable is the variable itself.
   * 
   * @return singleton list containing this instance of variable.
   */
  @Override
  public List<BooleanVariable> getDomain() {
    return Collections.singletonList(this);
  }

  /**
   * Get's the variable's name.
   * 
   * @return name of the variable.x
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Set the value to some {@link BooleanValue}.
   * 
   * @param value {@code TRUE, FALSE, DONT_CARE}.
   */
  public void setValue(BooleanValue value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  /**
   * Variables are equal if their names are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BooleanVariable other = (BooleanVariable) obj;
    if (name == null || other.name == null) {
      return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "BooleanVariable [name=" + name + ", value=" + value + "]";
  }

  /**
   * Method used for deep copying of object.
   */
  @Override
  public BooleanVariable clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    BooleanVariable newVar = new BooleanVariable(this.name);
    newVar.setValue(this.getValue());

    return newVar;
  }

}
