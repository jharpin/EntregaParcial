package quindioexpress.modelo;

import java.util.Comparator;

public class ComparadorDespacho implements Comparator<Paquete> {

    @Override
    public int compare(Paquete o1, Paquete o2) {
        int resultado = o2.getPrioridad() - o1.getPrioridad();
        if (resultado == 0) {
            resultado = o1.getTiempoEstimado() - o2.getTiempoEstimado();
        }
        if (resultado == 0) {
            resultado = o1.compareTo(o2);
        }
        return resultado;
    }
}
