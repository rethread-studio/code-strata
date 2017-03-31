import org.apache.commons.collections.list.TreeList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by baudry on 31/03/17.
 */
public class QuickSortTest {
    @Test
    public void quickSort() throws Exception {
        QuickSort bgs = new QuickSort();
        TreeList list = new TreeList();
        list.add(0,(Integer)1);
        list.add(1,(Integer)4);
        list.add(2,(Integer)2);
        list.add(3,(Integer)7);
        assertFalse(bgs.isSorted(list));
        bgs.sort(list);
        assertTrue(bgs.isSorted(list));
    }

}