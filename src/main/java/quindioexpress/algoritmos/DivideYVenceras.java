package quindioexpress.algoritmos;

import quindioexpress.modelo.Paquete;

import java.util.List;

public class DivideYVenceras {

    public static Paquete mayorPeso(List<Paquete> paquetes) {

        if (paquetes.isEmpty()) {
            throw new RuntimeException("No hay paquetes");
        }

        return mayorPeso(paquetes, 0, paquetes.size() - 1);
    }

    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin) {

        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        int mitad = (inicio + fin) / 2;

        Paquete izquierdo = mayorPeso(paquetes, inicio, mitad);

        Paquete derecho = mayorPeso(paquetes, mitad + 1, fin);

        if (izquierdo.getPeso() >= derecho.getPeso()) {
            return izquierdo;
        } else {
            return derecho;
        }
    }

    public static Paquete mayorPeso(List<Paquete> ejemploDyV, boolean b) {
        return null;
    }
}