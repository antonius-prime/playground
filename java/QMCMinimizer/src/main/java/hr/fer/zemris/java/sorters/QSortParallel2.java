package hr.fer.zemris.java.sorters;

public class QSortParallel2 {

  /**
   * Prag koji govori koliko elemenata u podpolju minimalno mora biti da bi se sortiranje nastavilo
   * paralelno; ako elemenata ima manje, algoritam prelazi na klasično rekurzivno (slijedno)
   * sortiranje.
   */
  private final static int P_THRESHOLD = 6000;
  /**
   * Prag za prekid rekurzije. Ako elemenata ima više od ovoga, quicksort nastavlja rekurzivnu
   * dekompoziciju. U suprotnom ostatak sortira algoritmom umetanja.
   */
  private final static int CUT_OFF = 5;

  /**
   * Sučelje prema klijentu: prima polje i vraća se tek kada je polje sortirano. Primjenjujući
   * gornje pragove najprije posao paralelizira a kada posao postane dovoljno mali, rješava ga
   * slijedno.
   * 
   * @param array polje koje treba sortirati
   */
  public static void sort(int[] array) {
    new QSortJob(array, 0, array.length - 1).run();
  }

  /**
   * Model posla sortiranja podpolja čiji su elementi na pozicijama koje su veće ili jednake
   * <code>startIndex</code> i manje ili jednake <code>endIndex</code>.
   */
  static class QSortJob implements Runnable {

    private int[] array;
    private int startIndex;
    private int endIndex;

    public QSortJob(int[] array, int startIndex, int endIndex) {
      super();
      this.array = array;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

    @Override
    public void run() {
      if (endIndex - startIndex + 1 > CUT_OFF) {
        boolean doInParallel = endIndex - startIndex + 1 > P_THRESHOLD;
        // pronađi pivot, razdijeli polje u lijevi i desni dio
        final int p = selectPivot();
        swap(array, p, endIndex);
        int pivot = array[endIndex];
        int i = startIndex;
        int j = endIndex - 1;

        for (;;) {
          while (i < j && array[i] < pivot) {
            i++;
          }
          while (i < j && array[j] > pivot) {
            j--;
          }
          if (i >= j) {

            break;
          }
          swap(array, i, j);
          i++;;
        }

        if (array[i] > pivot) {
          swap(array, i, endIndex);
        }

        // Neka je ovdje 'i' konačna lokacija na kojoj je završio
        // pivot
        Thread t1 = null;
        if (startIndex < i) {
          QSortJob job = new QSortJob(array, startIndex, i - 1);
          t1 = executeJob(doInParallel, job);
        }
        Thread t2 = null;
        if (endIndex > i) {
          QSortJob job = new QSortJob(array, i + 1, endIndex);
          t2 = executeJob(doInParallel, job);
        }
        // ako su t1/t2 pokrenuti, dočekaj ih da završe
        if (t1 != null) {
          try {
            t1.join();
          } catch (InterruptedException e) {
            System.err.println("Thread trouble!");
            e.printStackTrace();
          }
        }
        if (t2 != null) {
          try {
            t2.join();
          } catch (InterruptedException e) {
            System.err.println("Thread trouble!");
            e.printStackTrace();
          }
        }

      } else {
        StraightInsertionionSorter.sort(array, startIndex, endIndex + 1);
      }
    }

    /**
     * Direktno izvodi zadani posao pozivom run() i tada vraća <code>null</code> ili pak stvara novu
     * dretvu, njoj daje taj posao i pokreće je te vraća referencu na stvorenu dretvu (u tom slučaju
     * ne čeka da posao završi).
     * 
     * @param doInParallel treba li posao pokrenuti u novoj dretvi
     * @param job posao
     * @return <code>null</code> ili referencu na pokrenutu dretvu
     */
    private Thread executeJob(boolean doInParallel, QSortJob job) {
      if (doInParallel) {
        Thread t = new Thread(job);
        t.start();
        return t;
      } else {
        job.run();
        return null;
      }

    }

    /**
     * Odabir pivota metodom medijan-od-tri u dijelu polja <code>array</code> koje je ograđeno
     * indeksima <code>startIndex</code> i <code>endIndex</code> (oba uključena).
     * 
     * @return vraća indeks na kojem se nalazi odabrani pivot
     */
    public int selectPivot() {
      final int midIndex = (startIndex + endIndex) >>> 1;

      if (array[startIndex] > array[midIndex]) {
        swap(array, startIndex, midIndex);
      }
      if (array[startIndex] > array[endIndex]) {
        swap(array, startIndex, endIndex);
      }
      if (array[midIndex] > array[endIndex]) {
        swap(array, midIndex, endIndex);
      }

      return midIndex;
    }

    /**
     * U predanom polju <code>array</code> zamjenjuje elemente na pozicijama <code>i</code> i
     * <code>j</code>.
     * 
     * @param array polje u kojem treba zamijeniti elemente
     * @param i prvi indeks
     * @param j drugi indeks
     */
    public static void swap(int[] array, int i, int j) {
      final int tmp = array[i];
      array[i] = array[j];
      array[j] = tmp;
    }
  }

  /**
   * Pomoćna metoda koja provjerava je li predano polje doista sortirano uzlazno.
   * 
   * @param array polje * @return <code>true</code> ako je, <code>false</code> inače
   */
  public static boolean isSorted(int[] array) {
    for (int i = 1; i < array.length; i++) {
      if (array[i - 1] > array[i]) {
        return false;
      }
    }

    return true;
  }

}
