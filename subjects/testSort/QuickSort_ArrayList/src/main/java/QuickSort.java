/**
 * Created by baudry on 31/03/17.
 */

//import org.apache.commons.collections.list.TreeList;
import java.util.List;
import java.util.ArrayList;

public class QuickSort {

    public static List sort(List arr) {
        if (!arr.isEmpty()) {
            int pivot = 0; //This pivot can change to get faster results
            //int pivot = (int) Math.floor(Math.random() * ((double) arr.size())); //This pivot can change to get faster results

            List less = new ArrayList();
            List pivotList = new ArrayList();
            List more = new ArrayList();

            // Partition
            for(int i=1;i<arr.size();i++)  {
                if ((Integer)arr.get(i) < ((Integer)arr.get(pivot))) {
                    less.add(arr.get(i));
                }
                else if ((Integer)arr.get(i) > ((Integer)arr.get(pivot)))
                    more.add(arr.get(i));
                else
                    pivotList.add(arr.get(i));
            }
            pivotList.add(arr.get(pivot));
            // Recursively sort sublists
            less = sort(less);
            more = sort(more);

            // Concatenate results
            less.addAll(pivotList);
            less.addAll(more);
            return less;
        }
        return arr;

    }

    static boolean isSorted(List arr)
    {

        for(int i=1;i<arr.size();i++) {
            Object current = arr.get(i);
            Integer currenti = (Integer)current;
            Object previous = arr.get(i-1);
            Integer previousi = (Integer)previous;
            if (currenti < previousi) return false;
        }
        return true;
    }


}
