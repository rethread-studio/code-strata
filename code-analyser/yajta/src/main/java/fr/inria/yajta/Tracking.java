package fr.inria.yajta;

/**
 * Created by nharrand on 11/07/17.
 */
public interface Tracking {
    void stepIn(String thread, String method);
    void stepOut(String thread);
    void flush();
}
