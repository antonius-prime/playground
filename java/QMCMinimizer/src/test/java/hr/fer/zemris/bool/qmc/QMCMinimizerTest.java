package hr.fer.zemris.bool.qmc;

import static org.junit.Assert.*;
import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;
import hr.fer.zemris.bool.fimpl.IndexedBF;
import hr.fer.zemris.bool.fimpl.MaskBasedBF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class QMCMinimizerTest {

  private BooleanVariable varA;
  private BooleanVariable varB;
  private BooleanVariable varC;
  private BooleanVariable varD;
  private BooleanFunction f1;
  private MaskBasedBF[] fje;
  private List<BooleanVariable> domain;
  
  private long t0,t1; 

  @Before
  public void setUp() {
    varA = new BooleanVariable("A");
    varB = new BooleanVariable("B");
    varC = new BooleanVariable("C");
    varD = new BooleanVariable("D");
    domain = new ArrayList<>(Arrays.asList(varA,varB,varC,varD));
  }

  @Test
  public void testMinimize() {
    testMinimize(new IndexedBF("f1", 
        domain,
        true, Arrays.asList(0, 1, 4, 5, 11, 15), 
        new ArrayList<Integer>()),
        new MaskBasedBF[] {
        new MaskBasedBF("", domain, true, 
            Arrays.asList(Mask.parse("0x0x"), Mask.parse("1x11")), new ArrayList<Mask>()),
        });
    
    testMinimize(new IndexedBF("f2", 
        domain,
        true, Arrays.asList(0,1,4,5,9,11,15), 
        new ArrayList<Integer>()), 
        new MaskBasedBF[] {
          new MaskBasedBF("",
            domain, true,
            Arrays.asList(Mask.parse("0x0x"), Mask.parse("1x11"), Mask.parse("10x1")), new ArrayList<Mask>()),
           new MaskBasedBF("",
            Arrays.asList(varA, varB, varC, varD), true,
            Arrays.asList(Mask.parse("0x0x"), Mask.parse("1x11"), Mask.parse("x001")), new ArrayList<Mask>()),
    });
    
    testMinimize(new IndexedBF("f3", 
        domain,
        true, Arrays.asList(0,1,4,5,9,15), 
        Arrays.asList(11)), 
        new MaskBasedBF[] {
          new MaskBasedBF("",
            domain, true,
            Arrays.asList(Mask.parse("0x0x"), Mask.parse("1x11"), Mask.parse("10x1")), new ArrayList<Mask>()),
           new MaskBasedBF("",
            Arrays.asList(varA, varB, varC, varD), true,
            Arrays.asList(Mask.parse("0x0x"), Mask.parse("1x11"), Mask.parse("x001")), new ArrayList<Mask>()),
    });
    
    testMinimize(new IndexedBF("f6", 
        domain,
        true, Arrays.asList(0, 1, 4, 5, 11, 15), 
        Arrays.asList(2, 6, 10)),
        new MaskBasedBF[] {
        new MaskBasedBF("", domain, true, 
            Arrays.asList(Mask.parse("0x0x"), Mask.parse("1x11")), new ArrayList<Mask>()),
        });

  }
  
  private void testMinimize(BooleanFunction f, MaskBasedBF[] expectMasksFuncs) {
    f1 = f;
    fje = QMCMinimizer.minimize(f1);
    assertTrue("Wrong number of possible minimal terms for minimization of: " + f1 + "function.",
        fje.length == expectMasksFuncs.length);
    
    // Fill mask lists to sets so order won't matter.
    Set<Set<Mask>> actual = new HashSet<>();
    Set<Set<Mask>> expected = new HashSet<>();
    for (int i = 0; i < fje.length; i++) {
      MaskBasedBF actualF = fje[i];
      MaskBasedBF expectedF = expectMasksFuncs[i];
      actual.add(new HashSet<>(actualF.getMasks()));
      expected.add(new HashSet<>(expectedF.getMasks()));
    }
      
    assertEquals("Minimal clauses don't match.", expected, actual);
  }
  
  @Test
  public void testMinimizeSize() {
    f1 = new IndexedBF("f1", 
        domain,
        true, Arrays.asList(4,5,6,7,8,9,10,11,13,14), 
        new ArrayList<Integer>());
    fje = QMCMinimizer.minimize(f1);
    assertTrue("Wrong number of possible minimal terms for minimization of: " + f1 + "function.",
        fje.length == 4);
    
    f1 = new IndexedBF("f3", 
        domain,
        true, Arrays.asList(4,5,7,9,10,11,13,14), 
        Arrays.asList(6,8));
    
    fje = QMCMinimizer.minimize(f1);
    assertTrue("Wrong number of possible minimal terms for minimization of: " + f1 + "function.",
        fje.length == 4);

  }
  
  @Test
  public void testMinimizeTime() {
    testMinimizeTime(new IndexedBF("f1", 
        domain,
        true, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14), 
        new ArrayList<Integer>()));
    
    testMinimizeTime(new IndexedBF("f2", 
        domain,
        true, Arrays.asList(0, 1, 2, 3, 4, 6, 7, 8, 9, 11, 12, 13, 14, 15),
        new ArrayList<Integer>()));
    
    testMinimizeTime(new IndexedBF("f3", 
        domain,
        true, Arrays.asList(0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14),
        new ArrayList<Integer>()));
    
    testMinimizeTime(new IndexedBF("f4", 
        domain,
        true, Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15),
        new ArrayList<Integer>()));
    
    testMinimizeTime(new IndexedBF("f5", 
        domain,
        true, Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
        new ArrayList<Integer>()));
    
    testMinimizeTime(new IndexedBF("f6", 
        domain,
        true, Arrays.asList(1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
        new ArrayList<Integer>()));
    
  }
  
  private void testMinimizeTime(BooleanFunction f) {
    t0 = System.currentTimeMillis();
    QMCMinimizer.minimize(f);
    t1 = System.currentTimeMillis();
    System.out.println("TIME: "+(t1-t0)+"ms, for function "+ f.getName());
    assertTrue("It took too long to minimize:"+(t1-t0)+"ms.", (t1-t0) <= 1000);
  }
  
}
