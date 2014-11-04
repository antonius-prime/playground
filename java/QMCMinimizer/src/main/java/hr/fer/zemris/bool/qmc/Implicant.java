package hr.fer.zemris.bool.qmc;

import hr.fer.zemris.bool.Mask;

import java.util.Collection;
import java.util.Set;

/**
 * This class represents implicant in the table of implicants used by the {@link QMCMinimizer}.
 * Every implicant contains a mask, a set of minterms, and an alias. Alias is used during symbolic
 * manipulation of the Pyne-McCluskey algorithm for finding minimal set of implicants. Conceptually,
 * it is substitution of some implicant by symbolic string value.
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class Implicant {

  /** Minterms associated with this implicant. */
  private Set<Integer> minterms;
  /** Mask associated with this implicant. */
  private Mask mask;
  /** Alias associated with this implicant. */
  private String alias;

  /**
   * Constructor for implicant. Initializes minterm set and mask.
   * 
   * @param minterms set of minterms.
   * @param mask 
   */
  public Implicant(Set<Integer> minterms, Mask mask) {
    this.minterms = minterms;
    this.mask = mask;
  }

  /**
   * Get minterms associated with this implicant.
   * 
   * @return set of minterms for this implicant.
   */
  public Set<Integer> getMinterms() {
    return minterms;
  }

  /**
   * Get mask associated with this implicant.
   * 
   * @return implicants mask
   */
  public Mask getMask() {
    return mask;
  }

  /**
   * Get implicants alias.
   * 
   * @return alias.
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Set implicants alias to the given argument.
   * 
   * @param alias
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * When client knows alias of some implicant in collection of implicants, he can access its mask.
   * 
   * @param implicants collection of implicants.
   * @param otherAlias alias to search for
   * @return mask of given alias if it exists, otherwise {@code null}.
   */
  public static Mask getMaskByAlias(Collection<Implicant> implicants, String otherAlias) {
    for (Implicant implicant : implicants) {
      if (implicant.getAlias().equals(otherAlias)) {
        return implicant.getMask();
      }
    }
    // Not found in collection.
    return null;
  }

  /**
   * Decent string representation of the {@link Implicant} class instance.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append((alias) == null ? "" : alias);
    sb.append(" ( ");
    for (Integer mint : minterms) {
      sb.append(mint + " ");
    }
    sb.append(") ");
    sb.append(mask);

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((alias == null) ? 0 : alias.hashCode());
    result = prime * result + ((mask == null) ? 0 : mask.hashCode());
    result = prime * result + ((minterms == null) ? 0 : minterms.hashCode());
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
    Implicant other = (Implicant) obj;
    if (alias == null) {
      if (other.alias != null)
        return false;
    } else if (!alias.equals(other.alias))
      return false;
    if (mask == null) {
      if (other.mask != null)
        return false;
    } else if (!mask.equals(other.mask))
      return false;
    if (minterms == null) {
      if (other.minterms != null)
        return false;
    } else if (!minterms.equals(other.minterms))
      return false;
    return true;
  }


}
