package fr.inria.DataStructure;

import java.util.List;

/**
 * Created by nharrand on 23/03/17.
 */
public interface OrderedTree<T> {
    T getEl();
    List<? extends OrderedTree<T>> getChildren();


}
