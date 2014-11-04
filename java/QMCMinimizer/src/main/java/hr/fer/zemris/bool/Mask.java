package hr.fer.zemris.bool;

import java.util.Arrays;

/**
 * @author Antonio Paunovic
 * @version 0.2
 */
public class Mask implements Cloneable {

  private MaskValue[] mask;

  /**
   * Mask constructor.
   * 
   * @param mask an array of {@link MaskValue} representing mask.
   */
  public Mask(MaskValue[] mask) {
    this.mask = Arrays.copyOf(mask, mask.length);
  }

  /**
   * Construct {@link Mask} from index that determines boolean values of masks by it's binary number
   * representation.
   * 
   * @param domainSize number of the variables.
   * @param index integer from which is mask generated.
   * @return Mask determined by index's binary number representation.
   */
  public static Mask fromIndex(int domainSize, int index) {
    MaskValue[] maskFromIndex = new MaskValue[domainSize];

    for (int bitMask = 1, bitPosition = 0; bitPosition < domainSize; bitMask *= 2, bitPosition++) {

      if ((index & bitMask) == 0) {
        maskFromIndex[domainSize - 1 - bitPosition] = MaskValue.ZERO;
      } else {
        maskFromIndex[domainSize - 1 - bitPosition] = MaskValue.ONE;
      }
    }

    return new Mask(maskFromIndex);
  }

  /**
   * Generate {@link Mask} from string that presumably represents a boolean value.
   * 
   * @param stringMask string consisting of {@code 0 1 or x} characters.
   * @return Mask object determined by string.
   */
  public static Mask parse(String stringMask) {

    final int maskLength = stringMask.length();
    MaskValue[] maskFromString = new MaskValue[maskLength];
    char[] charMask = stringMask.toCharArray();

    for (int bitPosition = 0; bitPosition < maskLength; bitPosition++) {
      switch (charMask[bitPosition]) {
        case '0':
          maskFromString[bitPosition] = MaskValue.ZERO;
          break;
        case '1':
          maskFromString[bitPosition] = MaskValue.ONE;
          break;
        case 'x':
          maskFromString[bitPosition] = MaskValue.DONT_CARE;
          break;
        default:
          throw new IllegalArgumentException("String mask representation is not valid");
      }
    }

    return new Mask(maskFromString);
  }

  /**
   * Method that returns value of mask at given index.
   * 
   * @param index of mask value to return.
   * @return MaskValue at given index.
   */
  public MaskValue getValue(int index) {
    if (index >= mask.length || index < 0) {
      throw new IllegalArgumentException();
    }

    return mask[index];
  }

  /**
   * Check if this mask is more general than the other mask. Assuming masks are of the same size,
   * the one that has more 'don't care' values is more general because it can be extrapolated with
   * more values. Preconditions:
   * <ul>
   * <li>masks have to be of same length</li>
   * </ul>
   * 
   * @param m other mask.
   * @return true if this mask is more general, otherwise false.
   * @throws IllegalArgumentException on precondition violations.
   */
  public boolean isMoreGeneral(Mask m) {
    final int maskLen = m.getSize();

    if (maskLen != getSize()) {
      throw new IllegalArgumentException(
          "Precondition violation: Masks differ in their length thus can't be compared.");
    }

    final int undefinedThis = maskLen - getNumberOfOnes() - getNumberOfZeros();
    final int undefinedOther = maskLen - m.getNumberOfOnes() - m.getNumberOfZeros();

    return undefinedThis > undefinedOther;
  }

  /**
   * Some mask can be combined if from the mask generated by combining we can generate backwards the
   * same values that it was generated from and none else. This method creates combined mask if
   * possible. Preconditions:
   * <ul>
   * <li>Masks have to be non-null Mask objects of equal sizes.</li>
   * </ul>
   * 
   * @param m1 first mask
   * @param m2 second mask
   * @return combination of given masks if it exist, otherwise {@code null}.
   * @throws IllegalArgumentException on preconditions violation.
   */
  public static Mask combine(Mask m1, Mask m2) {
    if (!preconditionsCombine(m1, m2)) {
      throw new IllegalArgumentException("Not combinable masks: " + m1 + " and " + m2);
    }

    if (m1.isMoreGeneral(m2) || m2.isMoreGeneral(m1)) {
      return null;
    }

    final int maskLen = m1.getSize();
    MaskValue[] combinedMask = new MaskValue[maskLen];
    boolean isCombinable = false;
    int undefiedCount = 0;
    int difference = 0;

    // General idea is that no more than one DONT_CARE value can exist.
    // So on the positions where two masks differ by 0 or 1 a 'x' can
    // be inserted. There can be only one different value (0 and 1).
    for (int bitPosition = 0; bitPosition < maskLen; bitPosition++) {
      MaskValue valueM1 = m1.getValue(bitPosition);
      MaskValue valueM2 = m2.getValue(bitPosition);

      if (valueM1 == valueM2 && valueM1 == MaskValue.DONT_CARE) {
        undefiedCount++;
        if (undefiedCount > 1)
          return null;
      }

      if (valueM1 != valueM2) {
        difference += 1;
        if (difference > 1) {
          return null;
        }
        combinedMask[bitPosition] = MaskValue.DONT_CARE;
        isCombinable = true;
      } else {
        combinedMask[bitPosition] = valueM1;
      }
    }

    return isCombinable ? new Mask(combinedMask) : null;
  }

  private static boolean preconditionsCombine(Mask m1, Mask m2) {
    return (m1 != null && m2 != null) && (m1.getSize() == m2.getSize());
  }

  /**
   * Method returns number of {@link MaskValue} ZERO values.
   * 
   * @return number of zeroes in mask.
   */
  public int getNumberOfZeros() {
    return getNumberOf(MaskValue.ZERO);

  }

  /**
   * Method returns number of {@link MaskValue} ONE value.
   * 
   * @return number of ones in masku.
   */
  public int getNumberOfOnes() {
    return getNumberOf(MaskValue.ONE);
  }

  /**
   * Helper method that gets the number of occurrences of a value from {@link Mask}.
   * 
   * @param what a kind of value to be counted.
   * @return number of occurrences of mask value.
   */
  private <T> int getNumberOf(T what) {
    int cnt = 0;
    for (int i = 0, maskLen = getSize(); i < maskLen; i++) {
      if (mask[i] == what) {
        cnt++;
      }
    }

    return cnt;
  }

  /**
   * Returns length of the mask instance;
   * 
   * @return number of mask values constituting the mask.
   */
  public int getSize() {
    return mask.length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(mask);
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
    Mask other = (Mask) obj;
    if (!Arrays.equals(mask, other.mask))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(mask.length);
    for (MaskValue mv : mask) {
      if (mv == MaskValue.DONT_CARE) {
        sb.append("x");
      }
      if (mv == MaskValue.ONE) {
        sb.append("1");
      }
      if (mv == MaskValue.ZERO) {
        sb.append("0");
      }
    }
    // return "Mask " + Arrays.toString(mask);
    return sb.toString();
  }

  /**
   * Method used for deep copying of object.
   */
  @Override
  public Mask clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    Mask newMask = new Mask(this.mask);
    return newMask;
  }
}