package quindioexpress.estructura;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class ListaIterator<T> implements Iterator<T> {

    private Nodo<T> actual;

    public ListaIterator(Nodo<T> nodoPrimero) {
        this.actual = nodoPrimero;
    }

    @Override
    public boolean hasNext() {
        return actual != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("NO hay elementos");
        }
        T dato = actual.getDato();
        actual = actual.getSiguiente();
        return dato;
    }
}
