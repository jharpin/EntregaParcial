package quindioexpress.algoritmos;

import java.util.List;

public class BusquedaBinaria {

   
    private BusquedaBinaria() {}

   
    public static <T extends Comparable<T>> int buscar(List<T> ordenada, T buscado) {
        return buscar(ordenada, buscado, 0, ordenada.size() - 1, false);
    }

   
    public static <T extends Comparable<T>> int buscar(List<T> ordenada, T buscado, boolean traza) {
        return buscar(ordenada, buscado, 0, ordenada.size() - 1, traza);
    }


    private static <T extends Comparable<T>> int buscar(
            List<T> ordenada, T buscado, int inicio, int fin, boolean traza) {

       
        if (inicio > fin) {
            return -1;
        }

        int mitad = (inicio + fin) / 2;

     
        if (traza) {
            System.out.println("   izq=" + inicio + " der=" + fin + " medio=" + mitad + " -> " + ordenada.get(mitad));
        }

        int comparacion = ordenada.get(mitad).compareTo(buscado);

       
        if (comparacion == 0) {
            return mitad;
        } 
      
        else if (comparacion < 0) {
            return buscar(ordenada, buscado, mitad + 1, fin, traza);
        } 
      
        else {
            return buscar(ordenada, buscado, inicio, mitad - 1, traza);
        }
    }
}

