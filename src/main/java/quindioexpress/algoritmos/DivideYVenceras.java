package quindioexpress.algoritmos;

import java.util.List;
import quindioexpress.modelo.Paquete;

public class DivideYVenceras {

    public static Paquete mayorPeso(List<Paquete> paquetes) {
        return mayorPeso(paquetes, false);
    }

    public static Paquete mayorPeso(List<Paquete> paquetes, boolean traza) {
        if (paquetes.isEmpty()) {
            throw new RuntimeException("Conjunto vacío");
        }
        return mayorPeso(paquetes, 0, paquetes.size() - 1, traza, "");
    }

    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin, boolean traza, String sangria) {
       
        if (inicio == fin) {
            if (traza) {
                System.out.println(sangria + "base [" + inicio + "] -> " + paquetes.get(inicio));
            }
            return paquetes.get(inicio);
        }
      
        int mitad = (inicio + fin) / 2;
        if (traza) {
            System.out.println(sangria + "dividir [" + inicio + ".." + fin + "] en [" + inicio + ".." + mitad
                    + "] y [" + (mitad + 1) + ".." + fin + "]");
        }
        
        Paquete izquierdo = mayorPeso(paquetes, inicio, mitad, traza, sangria + "  ");
       
        Paquete derecho = mayorPeso(paquetes, mitad + 1, fin, traza, sangria + "  ");
       
        Paquete ganador = derecho;
        if (izquierdo.getPeso() >= derecho.getPeso()) {
            ganador = izquierdo;
        }
        if (traza) {
            System.out.println(sangria + "combinar " + izquierdo + " vs " + derecho + " -> " + ganador);
        }
        return ganador;
    }
}
