package quindioexpress.modelo;
import java.util.Comparator;
public class ComparadorPrioridadDesc implements Comparator<Paquete> {
        @Override
    public int compare(Paquete o1, Paquete o2) {
        return o2.getPrioridad() - o1.getPrioridad();
    }
}
