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

        titulo("6. Orden natural (Comparable) y Comparator");
        System.out.println("Natural (código):  " + codigos(centro.ordenadosNatural()));
        System.out.println("Prioridad desc:    " + codigos(centro.ordenados(new ComparadorPrioridadDesc())));
        System.out.println("Peso desc:         " + codigos(centro.ordenados(new ComparadorPesoDesc())));
        System.out.println("Tiempo asc:        " + codigos(centro.ordenados(new ComparadorTiempoAsc())));
        System.out.println("Registro intacto:  " + codigos(centro.getRegistro().subList(0, 5)) + " ...");

        titulo("7. Ejemplo de despacho del enunciado (PriorityQueue)");
        PriorityQueue<Paquete> ejemplo = new PriorityQueue<>(new ComparadorDespacho());
        ejemplo.add(new Paquete("PQ420", "Armenia", 1, 3, 30));
        ejemplo.add(new Paquete("PQ731", "Armenia", 1, 5, 45));
        ejemplo.add(new Paquete("PQ105", "Armenia", 1, 5, 25));
        ejemplo.add(new Paquete("PQ942", "Armenia", 1, 4, 20));
        String orden = "";
        while (!ejemplo.isEmpty()) {
            orden += ejemplo.poll().getCodigo() + " ";
        }
        System.out.println("Esperado PQ105 PQ731 PQ942 PQ420 -> " + orden.trim());

        titulo("8. Atención por orden de llegada (Queue) + asignación");
        for (int i = 0; i < 4; i++) {
            Paquete p = centro.asignarPorLlegada();
            if (p == null) {
                System.out.println("Sin repartidor disponible en la zona del siguiente paquete; queda en espera.");
                break;
            }
            System.out.println("Asignado " + p.getCodigo() + " (" + p.getDestino() + ") a " + p.getRepartidor());
        }
        System.out.println("Entregas en orden PQ105, PQ942, PQ731");
        centro.registrarEntrega("PQ105");
        centro.registrarEntrega("PQ942");
        centro.registrarEntrega("PQ731");
        try {
            centro.registrarEntrega("PQ731");
        } catch (RuntimeException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }

        titulo("9. Despacho prioritario (PriorityQueue) + asignación");
        for (int i = 0; i < 6; i++) {
            Paquete p = centro.asignarPorPrioridad();
            if (p == null) {
                Paquete sig = centro.siguientePorPrioridad();
                System.out.println("Bloqueado: " + sig + " no tiene repartidor disponible en " + sig.getDestino()
                        + ". Se habilita R03.");
                centro.buscarRepartidor("R03").setDisponible(true);
                continue;
            }
            System.out.println("Asignado " + p + " a " + p.getRepartidor().getIdentificacion());
            centro.registrarEntrega(p.getCodigo());
        }

        titulo("10. Historial de entregas (lista enlazada propia, recorrida con Iterator)");
        int n = 1;
        for (Paquete p : centro.getHistorial()) {
            System.out.println((n++) + ". " + p.getCodigo());
        }
        System.out.println("Total entregados: " + centro.cantidadEntregados());

        titulo("11. Casos borde de la lista propia (genérica, con String)");
        ColeccionPropia<String> lista = new ListaSimplementeEnlazada<>();
        System.out.println("Vacía: " + lista.estaVacia() + ", eliminar 'X' en vacía: " + lista.eliminar("X"));
        lista.agregar("A");
        System.out.println("Un elemento " + lista + " -> eliminar A: " + lista.eliminar("A") + " " + lista);
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.agregar("D");
        System.out.println("Lista " + lista + ", obtener(2) = " + lista.obtener(2));
        lista.eliminar("A");
        System.out.println("Eliminar primero A:     " + lista);
        lista.eliminar("C");
        System.out.println("Eliminar intermedio C:  " + lista);
        lista.eliminar("D");
        System.out.println("Eliminar último D:      " + lista);
        lista.agregar("E");
        System.out.println("Agregar E (cola ok):    " + lista);
        System.out.println("Eliminar inexistente Z: " + lista.eliminar("Z") + ", tamaño = " + lista.tamanio());
        try {
            lista.obtener(5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }

        titulo("12. Recursividad");
        List<Paquete> ejemploPeso = new ArrayList<>();
        ejemploPeso.add(new Paquete("PQ101", "Armenia", 4.5, 3, 10));
        ejemploPeso.add(new Paquete("PQ203", "Salento", 3.0, 3, 10));
        ejemploPeso.add(new Paquete("PQ415", "Armenia", 7.2, 3, 10));
        ejemploPeso.add(new Paquete("PQ730", "Armenia", 2.3, 3, 10));
        System.out.printf("Ejemplo enunciado, peso Armenia = %.1f kg (esperado 14.0)%n",
                AlgoritmoRecursivo.pesoTotalPorMunicipio(ejemploPeso, "Armenia"));
        System.out.println("Ejemplo enunciado, prioridad >= 4 en {5,2,4,1,5,3,4} = "
                + AlgoritmoRecursivo.contarPrioridadMinima(new int[]{5, 2, 4, 1, 5, 3, 4}, 4, 0) + " (esperado 4)");
        for (String m : centro.getMunicipiosOrdenados()) {
            System.out.printf("Peso total %s = %.1f kg%n", m,
                    AlgoritmoRecursivo.pesoTotalPorMunicipio(centro.getRegistro(), m));
        }
        System.out.println("Paquetes con prioridad >= 4: "
                + AlgoritmoRecursivo.contarPrioridadMinima(centro.getRegistro(), 4));

        titulo("13. Búsqueda binaria (precondición: ordenado por código)");
        List<Paquete> porCodigo = centro.ordenadosNatural();
        List<String> codigosOrdenados = new ArrayList<>();
        for (Paquete p : porCodigo) {
            codigosOrdenados.add(p.getCodigo());
        }
        System.out.println("Buscar PQ731:");
        int pos = BusquedaBinaria.buscar(codigosOrdenados, "PQ731", true);
        System.out.println("   posición " + pos + " -> " + porCodigo.get(pos));
        System.out.println("Buscar PQ999: posición " + BusquedaBinaria.buscar(codigosOrdenados, "PQ999"));
        List<String> desordenados = new ArrayList<>();
        for (Paquete p : centro.getRegistro()) {
            desordenados.add(p.getCodigo());
        }
        System.out.println("Sin cumplir la precondición (orden de registro), buscar PQ731: posición "
                + BusquedaBinaria.buscar(desordenados, "PQ731") + " (resultado no confiable)");

        titulo("14. Divide y vencerás: paquete de mayor peso");
        List<Paquete> ejemploDyV = new ArrayList<>();
        ejemploDyV.add(new Paquete("E0", "Armenia", 4.5, 3, 10));
        ejemploDyV.add(new Paquete("E1", "Armenia", 12.0, 3, 10));
        ejemploDyV.add(new Paquete("E2", "Armenia", 3.2, 3, 10));
        ejemploDyV.add(new Paquete("E3", "Armenia", 18.7, 3, 10));
        ejemploDyV.add(new Paquete("E4", "Armenia", 7.4, 3, 10));
        ejemploDyV.add(new Paquete("E5", "Armenia", 5.1, 3, 10));
        Paquete max = DivideYVenceras.mayorPeso(ejemploDyV, true);
        System.out.println("Resultado final: " + max.getPeso() + " kg (esperado 18.7)");
        Paquete maxJornada = DivideYVenceras.mayorPeso(centro.getRegistro());
        System.out.println("Mayor peso de la jornada: " + maxJornada);
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

