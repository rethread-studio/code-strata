/**
 * Created by baudry on 14/03/17.
 */

import org.apache.commons.collections.list.TreeList;

public class BoGoSort {

    void sort(TreeList arr)
    {
        int shuffle=1;
        for(;!isSorted(arr);shuffle++)
            shuffle(arr);
    }

    void shuffle(TreeList arr)
    {
        //Standard Fisher-Yates shuffle algorithm
        int i=arr.size()-1;
        while(i>0)
            swap(arr,i--,(int)(Math.random()*i));
    }

    void swap(TreeList arr,int i,int j) {
        Object temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    boolean isSorted(TreeList arr)
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
    void display1D(int[] arr)
    {
        for(int i=0;i<arr.length;i++)
            System.out.print(arr[i]+" ");
        System.out.println();
    }

}
