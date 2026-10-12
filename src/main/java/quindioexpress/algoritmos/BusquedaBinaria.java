package quindioexpress.algoritmos;

import java.util.List;

public class BusquedaBinaria {

    public static <T extends Comparable<T>> int buscar(List<T> ordenada, T buscado) {
        return buscar(ordenada, buscado, 0, ordenada.size() - 1);
    }

    private static <T extends Comparable<T>> int buscar(
            List<T> ordenada, T buscado, int inicio, int fin) {

        if (inicio > fin) {
            return -1;
        }

        int mitad = (inicio + fin) / 2;

        int comparacion = ordenada.get(mitad).compareTo(buscado);

        if (comparacion == 0) {
            return mitad;
        } else if (comparacion < 0) {
            return buscar(ordenada, buscado, mitad + 1, fin);
        } else {
            return buscar(ordenada, buscado, inicio, mitad - 1);
        }
    }
}
