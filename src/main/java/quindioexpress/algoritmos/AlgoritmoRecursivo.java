package quindioexpress.algoritmos;
import java.util.List;
import quindioexpress.modelo.Paquete;

public class AlgoritmoRecursivo {
    private AlgoritmoRecursivo(){}

    public static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        return pesoTotal(paquetes, municipio, 0);
    }

    private static double pesoTotal(List<Paquete> paquetes, String municipio, int i) {
        if (i == paquetes.size()) return 0.0;
        Paquete p = paquetes.get(i);
        double actual = 0.0;

        if (p.getDestino().equalsIgnoreCase(municipio)) {
            actual = p.getPeso();
        } else {
            actual = 0.0;
        }return actual + pesoTotal(paquetes, municipio, i + 1);
    }
    private static int contar(List<Paquete> paquetes, int minima, int i) {
        if (i == paquetes.size()) {
            return 0;
        }
        int actual = 0;
        if (paquetes.get(i).getPrioridad() >= minima) {
            actual = 1;
        } else {
            actual = 0;
        }
        return actual + contar(paquetes, minima, i + 1);
    }
    public static int contarPrioridadMinima(List<Paquete> paquetes, int minima) {
        return minima;
    }
    public static int contarPrioridadMinima(int[] prioridades, int minima, int i) {
        if (i == prioridades.length) {
            return 0;
        }
        int actual = 0;
        if (prioridades[i] >= minima) {
            actual = 1;
        } else {
            actual = 0;
        }
        return actual + contarPrioridadMinima(prioridades, minima, i + 1);
    }

}
