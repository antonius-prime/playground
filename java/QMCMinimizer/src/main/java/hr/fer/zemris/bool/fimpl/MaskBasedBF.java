package hr.fer.zemris.bool.fimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;
import hr.fer.zemris.bool.MaskValue;
import hr.fer.zemris.bool.misc.BooleanSwissKnife;

/**
 * This class is used to define function by string boolean masks. Depending on constructor
 * parameter, ones mark the minterms or maxterms.
 * 
 * @author Antonio Paunovic
 * @version 0.3
 */

public class MaskBasedBF implements BooleanFunction {

  private String name;
  private List<BooleanVariable> domain;
  private boolean masksAreMinterms;
  private List<Mask> masks;
  private List<Mask> dontCareMasks;

  BFGrandUnifier traversalHelper;

  /**
   * Constructor for {@link MaskBasedBF} instance. Preconditions:
   * <ul>
   * <li>None of the arguments can be null.</li>
   * </ul>
   * 
   * @param name of the function.
   * @param domain a list of variables {@link BooleanVariable}.
   * @param masksAreMinterms flag which indicates whether masks are minterms (true if they are).
   *        Otherwise they are minterms.
   * @param masks list of minterm/maxterm masks which define the function.
   * @param dontCareMasks list of dontcare masks which define the function.
   * @throws IllegalArgumentException on precondition violation.
   */
  public MaskBasedBF(String name, List<BooleanVariable> domain, boolean masksAreMinterms,
      List<Mask> masks, List<Mask> dontCareMasks) {
    if (!preconditionsMaskBasedBf(name, domain, masksAreMinterms, masks, dontCareMasks)) {
      throw new IllegalArgumentException("Precondition violation: no null arguments are allowed.");
    }

    this.name = name;
    this.domain = BooleanSwissKnife.copyBooleanVariableList(domain);
    this.masksAreMinterms = masksAreMinterms;
    this.masks = BooleanSwissKnife.copyMaskList(masks);
    this.dontCareMasks = BooleanSwissKnife.copyMaskList(dontCareMasks);

    traversalHelper =
        new BFGrandUnifier(domain, indexesFromMasks(this.masks),
            indexesFromMasks(this.dontCareMasks), masksAreMinterms);
  }

  /** Precondition check for constructor. */
  private boolean preconditionsMaskBasedBf(String name, List<BooleanVariable> domain,
      boolean masksAreMinterms, List<Mask> masks, List<Mask> dontCareMasks) {
    return name != null && domain != null && masks != null && dontCareMasks != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BooleanValue getValue() {
    return traversalHelper.getValue();
  }

  /**
   * Creates list of indexes from given list of masks.
   * 
   * @param masks list of masks to be converted.
   * @return list of integers representing indexes.
   */
  private List<Integer> indexesFromMasks(List<Mask> masks) {
    List<Integer> indexes = new ArrayList<>();
    for (Mask m : masks) {
      for (List<MaskValue> listMask : possibleMasks(maskAsList(m))) {
        indexes.add(listMaskToIndex(listMask));
      }
    }

    return indexes;
  }

  /**
   * Creates a list of list of all possible mask values for some mask. That is it takes into account
   * all unknown values of masks represented by an 'x' and extrapolates all other possible values.
   * 
   * @param maskList list of masks to extrapolate
   * @return list of list of mask values that represent possible masks.
   */
  private List<List<MaskValue>> possibleMasks(List<MaskValue> maskList) {
    // End of recursion: last element of mask, be it 'x' or empty.
    if (maskList == null || !maskList.contains(MaskValue.DONT_CARE)) {
      List<List<MaskValue>> ret = new ArrayList<>();
      ret.add(maskList);
      return ret;
    }
    // List to store all possible values.
    List<List<MaskValue>> possible = new ArrayList<>();
    // List for temporary computations.
    List<MaskValue> newMaskValues = new ArrayList<>();

    for (int i = 0, maskLen = maskList.size(); i < maskLen; i++) {

      // If value is don't care, make a copy of list with that element
      // changed with one and zero values and then enters the recursion
      // searching for other don't cares.
      if (maskList.get(i) == MaskValue.DONT_CARE) {

        List<MaskValue> additionalMaskList = copyMaskValueList(newMaskValues);
        additionalMaskList.add(MaskValue.ONE);
        additionalMaskList.addAll(copyMaskValueList(maskList.subList(i + 1, maskLen)));

        // Enter the recursion with one as changed element.
        possible.addAll(possibleMasks(additionalMaskList));

        additionalMaskList = copyMaskValueList(newMaskValues);
        additionalMaskList.add(MaskValue.ZERO);
        additionalMaskList.addAll(copyMaskValueList(maskList.subList(i + 1, maskLen)));

        // Enter the recursion with zero as changed element.
        possible.addAll(possibleMasks(additionalMaskList));

        return possible;
      } else {
        newMaskValues.add(maskList.get(i));
      }

    }
    return null;
  }

  /**
   * For a given list of {@link MaskValue} return index describing it's binary value.
   * 
   * @param listMask mask
   * @return index integer representing mask's value as index.
   */
  private Integer listMaskToIndex(List<MaskValue> listMask) {
    final int numOfVariables = domain.size();
    int bin = 0;

    for (int i = 0, weight = (int) Math.pow(2, numOfVariables - 1), domLen = domain.size(); i < domLen; i++, weight /=
        2) {

      if (listMask.get(i) == MaskValue.ONE) {
        bin += weight;
      }
    }

    return bin;
  }

  /**
   * Takes mask an returns it as list of mask values.
   * 
   * @param m mask to transform.
   * @return list of mask value representing mask.
   */
  private List<MaskValue> maskAsList(Mask m) {
    List<MaskValue> mList = new ArrayList<>();
    for (int i = 0, maskLen = m.getSize(); i < maskLen; i++) {

      mList.add(m.getValue(i));
    }

    return mList;
  }

  /**
   * Copies a list of {@link MaskValue}.
   * 
   * @param ml list of mask values representing mask.
   * @return a copy of parameter {@code ml}.
   */
  private List<MaskValue> copyMaskValueList(List<MaskValue> ml) {
    List<MaskValue> newList = new ArrayList<>();
    for (MaskValue mv : ml) {
      newList.add(mv);
    }

    return newList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BooleanVariable> getDomain() {
    return Collections.unmodifiableList(domain);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMinterm(int index) {
    return traversalHelper.hasMinterm(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMaxterm(int index) {
    return traversalHelper.hasMaxterm(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasDontCare(int index) {
    return traversalHelper.hasDontCare(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Integer> mintermIterable() {
    return traversalHelper.mintermIterable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Integer> maxtermIterable() {
    return traversalHelper.maxtermIterable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Integer> dontcareIterable() {
    return traversalHelper.dontcareIterable();
  }

  /**
   * List of masks getter.
   * 
   * @return list of masks.
   */
  public List<Mask> getMasks() {
    return Collections.unmodifiableList(masks);
  }

  /**
   * List of dont care masks getter.
   * 
   * @return list of dont care masks.
   */
  public List<Mask> getDontCareMasks() {
    return Collections.unmodifiableList(dontCareMasks);
  }

  /**
   * Method returns flag telling whether are masks minterms.
   * 
   * @return {@code true} if they are, otherwise {@code false}.
   */
  public boolean areMasksProducts() {
    return masksAreMinterms;
  }
}
