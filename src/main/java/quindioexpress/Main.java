package quindioexpress;

import java.util.*;
import quindioexpress.algoritmos.*;
import quindioexpress.estructura.*;
import quindioexpress.logica.CentroDistribucion;
import quindioexpress.modelo.*;

public class Main {

    public static void main(String[] args) {
        CentroDistribucion centro = new CentroDistribucion();
        cargarDatos(centro);

        titulo("1. Registro y validaciones");
        System.out.println("Paquetes registrados: " + centro.getRegistro().size()
                + " | Repartidores: " + centro.getRepartidores().size());
        intentarRegistrarPaquete(centro, "PQ731", "Armenia", 1.0, 3, 10);
        intentarRegistrarPaquete(centro, "PQ001", "Armenia", 0, 3, 10);
        intentarRegistrarPaquete(centro, "PQ002", "Armenia", 2.0, 8, 10);
        intentarRegistrarPaquete(centro, "PQ003", "", 2.0, 3, 10);
        intentarRegistrarPaquete(centro, "PQ004", "Salento", 2.0, 3, 0);
        try {
            centro.registrarRepartidor(new Repartidor("R01", "Otro", "Armenia", true));
        } catch (RuntimeException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }

        titulo("2. Consulta por código (HashMap) y de repartidores");
        System.out.println("PQ942 -> " + centro.buscarPaquete("PQ942"));
        System.out.println("¿Existe PQ999? " + centro.existePaquete("PQ999"));
        System.out.println("R04 -> " + centro.buscarRepartidor("R04"));
        System.out.println("Repartidores: " + centro.getRepartidores());

        titulo("3. Orden de registro (auditoría)");
        System.out.println(codigos(centro.getRegistro()));

        titulo("4. Municipios");
        System.out.println("Sin repetir (HashSet): " + centro.getMunicipios());
        System.out.println("Ordenados (TreeSet):   " + centro.getMunicipiosOrdenados());

        titulo("5. Agrupación por destino (Map<String, List<Paquete>>)");
        for (String m : centro.getMunicipiosOrdenados()) {
            System.out.println(m + " -> " + codigos(centro.paquetesDe(m)));
        }
    }
    private static List<String> codigos(Collection<Paquete> ps) {
        List<String> r = new ArrayList<>();
        for (Paquete p : ps) {
            r.add(p.getCodigo());
        }
        return r;
    }

    private static void titulo(String t) {
        System.out.println("\n=== " + t + " ===");
    }

   
    private static void intentarRegistrarPaquete(CentroDistribucion centro, String codigo, String destino,
                                                 double peso, int prioridad, int tiempo) {
        try {
            centro.registrarPaquete(new Paquete(codigo, destino, peso, prioridad, tiempo));
        } catch (RuntimeException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }
    }
}
