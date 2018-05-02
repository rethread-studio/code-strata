package list.maison;

/**
 * Created by nharrand on 02/05/17.
 */
import java.util.Arrays;

public class ArrayList<E> {
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;
    private Object elements[];

    public ArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public void add(E e) {
        if (size == elements.length) {
            ensureCapa();
        }
        elements[size++] = e;
    }

    public void remove(E e) {
        Object n[] = new Object[elements.length];
        int j = 0;
        for(int i = 0; i < size; i++) {
            if(!elements[i].equals(e)) {
                n[j] = elements[i];
                j++;
            }
        }
        elements = n;
        size = j-1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addAll(ArrayList<E> l) {
        for(int i = 0; i < l.size(); i++) {
            this.add(l.get(i));
        }
    }


    private void ensureCapa() {
        int newSize = elements.length * 2;
        elements = Arrays.copyOf(elements, newSize);
    }

    @SuppressWarnings("unchecked")
    public E get(int i) {
        if (i>= size || i <0) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size " + i );
        }
        return (E) elements[i];
    }
}