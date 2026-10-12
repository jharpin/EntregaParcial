package quindioexpress.estructura;

import java.util.Iterator;
import java.util.Objects;


public class ListaSimplementeEnlazada<T> implements ColeccionPropia<T> {

    private int size;
    private Nodo<T> nodoPrimero;
    private Nodo<T> nodoUltimo;

    public ListaSimplementeEnlazada() {
        size = 0;
        nodoPrimero = null;
        nodoUltimo = null;
    }

  
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            nodoPrimero = nuevoNodo;
        } else {
            nodoUltimo.setSiguiente(nuevoNodo);
        }
        nodoUltimo = nuevoNodo;
        size++;
    }

    
    @Override
    public boolean eliminar(T elemento) {
        if (estaVacia()) {
            return false;
        }
        if (Objects.equals(nodoPrimero.getDato(), elemento)) {      
            nodoPrimero = nodoPrimero.getSiguiente();
            if (nodoPrimero == null) {                               
                nodoUltimo = null;
            }
            size--;
            return true;
        }
        Nodo<T> anterior = nodoPrimero;
        while (anterior.getSiguiente() != null
                && !Objects.equals(anterior.getSiguiente().getDato(), elemento)) {
            anterior = anterior.getSiguiente();
        }
        if (anterior.getSiguiente() == null) {                   
            return false;
        }
        if (anterior.getSiguiente() == nodoUltimo) {               
            nodoUltimo = anterior;
        }
        anterior.setSiguiente(anterior.getSiguiente().getSiguiente()); 
        size--;
        return true;
    }

   
    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= size) {
            throw new IndexOutOfBoundsException("Posición " + posicion + " fuera de rango [0," + size + ")");
        }
        Nodo<T> auxiliar = nodoPrimero;
        for (int i = 0; i < posicion; i++) {
            auxiliar = auxiliar.getSiguiente();
        }
        return auxiliar.getDato();
    }

    @Override
    public int tamanio() {
        return size;
    }

    @Override
    public boolean estaVacia() {
        return size == 0;
    }


    @Override
    public Iterator<T> iterator() {
        return new ListaIterator<>(nodoPrimero);
    }

    @Override
    public String toString() {
        String texto = "[";
        Nodo<T> auxiliar = nodoPrimero;
        while (auxiliar != null) {
            texto += auxiliar.getDato();
            if (auxiliar.getSiguiente() != null) {
                texto += " -> ";
            }
            auxiliar = auxiliar.getSiguiente();
        }
        return texto + "]";
    }
}
