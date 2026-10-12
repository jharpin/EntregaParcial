package quindioexpress.logica;import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import quindioexpress.estructura.ColeccionPropia;
import quindioexpress.estructura.ListaSimplementeEnlazada;
import quindioexpress.modelo.ComparadorDespacho;
import quindioexpress.modelo.EstadoPaquete;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;
public class CentroDistribucion {

    private final List<Paquete> registro = new ArrayList<>();
    private final Map<String, Paquete> paquetesPorCodigo = new HashMap<>();
    private final Queue<Paquete> colaLlegada = new ArrayDeque<>();
    private final PriorityQueue<Paquete> colaPrioridad = new PriorityQueue<>(new ComparadorDespacho());
    private final Set<String> municipios = new HashSet<>();
    private final TreeSet<String> municipiosOrdenados = new TreeSet<>();
    private final Map<String, List<Paquete>> paquetesPorMunicipio = new HashMap<>();
    private final Map<String, Repartidor> repartidoresPorId = new LinkedHashMap<>();
    private final Map<String, List<Repartidor>> repartidoresPorZona = new HashMap<>();
    private final ColeccionPropia<Paquete> historial = new ListaSimplementeEnlazada<>();

    public void registrarPaquete(Paquete p) {
        if (paquetesPorCodigo.containsKey(p.getCodigo())) {
            throw new RuntimeException("Ya existe un paquete con código " + p.getCodigo());
        }
        paquetesPorCodigo.put(p.getCodigo(), p);
        registro.add(p);
        colaLlegada.offer(p);
        colaPrioridad.offer(p);
        municipios.add(p.getDestino());
        municipiosOrdenados.add(p.getDestino());

        List<Paquete> delMunicipio = paquetesPorMunicipio.get(p.getDestino());
        if (delMunicipio == null) {
            delMunicipio = new ArrayList<>();
            paquetesPorMunicipio.put(p.getDestino(), delMunicipio);
        }
        delMunicipio.add(p);
    }

    public Paquete buscarPaquete(String codigo) {
        return paquetesPorCodigo.get(codigo);
    }

    public boolean existePaquete(String codigo) {
        return paquetesPorCodigo.containsKey(codigo);
    }

    public List<Paquete> getRegistro() {
        return Collections.unmodifiableList(registro);
    }

    public Set<String> getMunicipios() {
        return Collections.unmodifiableSet(municipios);
    }

    public Set<String> getMunicipiosOrdenados() {
        return Collections.unmodifiableSet(municipiosOrdenados);
    }

    public List<Paquete> paquetesDe(String municipio) {
        List<Paquete> lista = paquetesPorMunicipio.get(municipio);
        if (lista == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(lista);
    }

    public List<Paquete> ordenadosNatural() {
        List<Paquete> copia = new ArrayList<>(registro);
        Collections.sort(copia);
        return copia;
    }

    public List<Paquete> ordenados(Comparator<Paquete> criterio) {
        List<Paquete> copia = new ArrayList<>(registro);
        Collections.sort(copia, criterio);
        return copia;
    }
    public void registrarRepartidor(Repartidor r) {
        if (repartidoresPorId.containsKey(r.getIdentificacion())) {
            throw new RuntimeException("Ya existe un repartidor con identificación " + r.getIdentificacion());
        }
        repartidoresPorId.put(r.getIdentificacion(), r);

        List<Repartidor> deLaZona = repartidoresPorZona.get(r.getZona());
        if (deLaZona == null) {
            deLaZona = new ArrayList<>();
            repartidoresPorZona.put(r.getZona(), deLaZona);
        }
        deLaZona.add(r);
    }

    public Repartidor buscarRepartidor(String id) { return repartidoresPorId.get(id); }

    public Collection<Repartidor> getRepartidores() { return Collections.unmodifiableCollection(repartidoresPorId.values()); }

   
    private Repartidor repartidorDisponiblePara(String destino) {
        List<Repartidor> deLaZona = repartidoresPorZona.get(destino);
        if (deLaZona == null) {
            return null;
        }
        for (Repartidor r : deLaZona) {
            if (r.isDisponible()) return r;
        }
        return null;
    }


    public Paquete asignarPorLlegada() { return asignarDesde(colaLlegada); }

  
    public Paquete asignarPorPrioridad() { return asignarDesde(colaPrioridad); }

  
    public Paquete siguientePorPrioridad() { return colaPrioridad.peek(); }


    private Paquete asignarDesde(Queue<Paquete> cola) {
        Paquete p = cola.peek();
        if (p == null) return null;
        Repartidor r = repartidorDisponiblePara(p.getDestino());
        if (r == null) return null;
        colaLlegada.remove(p);
        colaPrioridad.remove(p);
        p.asignar(r);
        r.setDisponible(false);
        return p;
    }

   
    public void registrarEntrega(String codigo) {
        Paquete p = paquetesPorCodigo.get(codigo);
        if (p == null) throw new RuntimeException("No existe el paquete " + codigo);
        if (p.getEstado() != EstadoPaquete.ASIGNADO) {
            throw new RuntimeException("El paquete " + codigo + " no está asignado (" + p.getEstado() + ")");
        }
        p.marcarEntregado();
        p.getRepartidor().setDisponible(true);
        historial.agregar(p);
    }

    public Iterable<Paquete> getHistorial() { return historial; }

    public int cantidadEntregados() { return historial.tamanio(); }
}

