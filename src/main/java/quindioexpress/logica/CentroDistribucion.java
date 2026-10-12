package quindioexpress.logica;import java.util.*;
import quindioexpress.estructura.ColeccionPropia;
import quindioexpress.estructura.ListaSimplementeEnlazada;
import quindioexpress.modelo.*;
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
}