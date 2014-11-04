package hr.fer.zemris.bool;

import java.util.Arrays;
import java.util.List;

/**
 * Class used as factory form {@link Mask} objects.
 * 
 * @author Antonio Paunovic
 * @version 0.2
 */
public class Masks {

  /**
   * Create mask from one or more given indices.
   * 
   * @param domainSize number of variables.
   * @param indexes integer values of indices for mask creations.
   * @return list of masks generated from indices.
   * @see Mask fromIndex method
   */
  public static List<Mask> fromIndexes(final int domainSize, final int... indexes) {

    final int indexesCount = indexes.length;
    Mask[] masks = new Mask[indexesCount];

    for (int index = 0; index < indexesCount; index++) {
      masks[index] = Mask.fromIndex(domainSize, indexes[index]);
    }

    return Arrays.asList(masks);
  }

  /**
   * Create mask from one or more given string boolean values.
   * 
   * @param strings strings of boolean values for mask creations.
   * @return list of masks generated from strings..
   * @see Mask parse method
   * @throws IllegalArgumentException if string is of no good.
   */
  public static List<Mask> fromStrings(String... strings) throws IllegalArgumentException {

    final int stringsCount = strings.length;
    Mask[] masks = new Mask[stringsCount];
    for (int index = 0; index < stringsCount; index++) {
      masks[index] = Mask.parse(strings[index]);
    }

    return Arrays.asList(masks);
  }
}
