package hr.fer.zemris.bool.qmc.sorters;

import static org.junit.Assert.*;

import java.util.Random;

import hr.fer.zemris.java.sorters.QSortParallel2;

import org.junit.Before;
import org.junit.Test;

public class QSortParallelTest {

  private int[] data;
  
  @Before
  public void setUp() {
    // She arisen me from the ashes, and granted me a piece of luck
    // Then mercilessly to the ashes, she buried me again, right back
  }
  
  @Test
  public void testSortSingle() {
    data = new int[]  { 1 };
    QSortParallel2.sort(data);
    assertTrue("When you're single, there are no toubles.", data[0] == 1);
  }
  
  @Test (expected = IndexOutOfBoundsException.class) 
  public void testSortNone() {
    data = new int[]  { };
    QSortParallel2.sort(data);
    assertTrue("Faces look ugly when you're alone.", data[0] == 1);
  }
  
  @Test
  public void testSortSame() {
    data = new int[100];
    int[] otherdata = new int[100];
    for (int i = 0; i < data.length; i++) {
      data[i] = 666;
      otherdata[i] = 666;
    }
    assertTrue("Everybody's same, only I am special. The doctors say so.", QSortParallel2.isSorted(data));
    assertArrayEquals("Season of change... I feel so strange.", data, otherdata);
  }
  
  @Test
  public void testSortBig() {
    final int SIZE = 1300000;
    Random rand = new Random();
    data = new int[SIZE];
    for(int i = 0; i < data.length; i++) {
      data[i] = rand.nextInt();
    }
    QSortParallel2.sort(data);
    assertTrue("Chinaman are sorted out.", QSortParallel2.isSorted(data));
  }
  
  @Test
  public void testIsSorted() {
    final int SIZE = 1300000;
    Random rand = new Random();
    data = new int[SIZE];
    for(int i = 0; i < data.length; i++) {
      data[i] = rand.nextInt();
    }
    assertFalse("The hell it is!", QSortParallel2.isSorted(data));
    QSortParallel2.sort(data);
    assertTrue("Born to raise hell, Born to raise hell. We know how to do it and we do it real well", QSortParallel2.isSorted(data));
  }

}
