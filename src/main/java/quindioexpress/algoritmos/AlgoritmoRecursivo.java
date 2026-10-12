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

}
