package hr.fer.zemris.bool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is an abstraction over boolean operators. It has private list of sources based on
 * which final result is calculated. Operators determine their domain by inspecting domains of given
 * sources and producing an union
 * 
 * @author Antonio Paunovic
 * @version 0.1
 */
public class BooleanOperator implements BooleanSource {

  /**
   * Given boolean sources upon which operator is applied.
   */
  protected List<BooleanSource> sources;

  /**
   * Construct the operator with sources's domains as operands.
   * 
   * @param sources
   */
  protected BooleanOperator(List<BooleanSource> sources) {
    this.sources = sources;
  }

  /**
   * This class represents an abstraction upon concrete operators, so getting value is unsupported.
   * 
   * @throws UnsupportedOperationException
   */
  @Override
  public BooleanValue getValue() {
    throw new UnsupportedOperationException(
        "This class is conceptual abstraction. Thus, it is of no value.");
  }

  /**
   * Operators domain is determined from the union of sources domains.
   * 
   * @return operators domain.
   */
  @Override
  public List<BooleanVariable> getDomain() {
    Set<BooleanVariable> domainUniq = new HashSet<>();

    for (BooleanSource src : sources) {
      domainUniq.addAll(src.getDomain());
    }

    List<BooleanVariable> domain = new ArrayList<>(domainUniq);

    return domain;
  }

  /**
   * Getter for sources property. Returns operands from which operator tree is constituted.
   * 
   * @return operands
   */
  protected List<BooleanSource> getSources() {
    return sources;
  }

}
