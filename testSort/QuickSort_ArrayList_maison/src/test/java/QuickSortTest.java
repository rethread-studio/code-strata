import list.maison.ArrayList;
import org.junit.Test;



import static org.junit.Assert.*;

/**
 * Created by baudry on 31/03/17.
 */
public class QuickSortTest {
    @Test
    public void quickSort() throws Exception {
        QuickSort bgs = new QuickSort();
        ArrayList list = new ArrayList();
        list.add((Integer)1);
        list.add((Integer)4);
        list.add((Integer)2);
        list.add((Integer)7);
        list.add((Integer)9);
        list.add((Integer)3);
        list.add((Integer)7);
        list.add((Integer)4);
        list.add((Integer)0);
        assertFalse(bgs.isSorted(list));
        list = bgs.sort(list);
        assertTrue(bgs.isSorted(list));
    }

}
