import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Ejercito {
    public static final int MAX_SOLDADOS = 10;
    private static int siguienteId = 1;
    private static HashMap<Integer, Integer> soldadosCreadosPorEjercito = new HashMap<Integer, Integer>();
    private static HashMap<Integer, Integer> soldadosVivosPorEjercito = new HashMap<Integer, Integer>();

    private int id;
    private String nombre;
    private Reino reino;
    private int fila;
    private int columna;
    private ArrayList<Soldado> soldados;

    public Ejercito(String nombre, Reino reino, int fila, int columna) {
        id = siguienteId++;
        this.nombre = nombre;
        this.reino = reino;
        this.fila = fila;
        this.columna = columna;
        soldados = new ArrayList<Soldado>();
        soldadosCreadosPorEjercito.put(id, 0);
        soldadosVivosPorEjercito.put(id, 0);
    }

    public void generarSoldados() {
        Random azar = new Random();
        int cantidad = azar.nextInt(MAX_SOLDADOS) + 1;
        for (int i = 0; i < cantidad; i++) {
            Soldado s = new Soldado("Soldado" + i + "E" + id,
                    azar.nextInt(5) + 1, azar.nextInt(5) + 1, azar.nextInt(5) + 1);
            soldados.add(s);
            soldadosCreadosPorEjercito.put(id, soldadosCreadosPorEjercito.get(id) + 1);
            soldadosVivosPorEjercito.put(id, soldadosVivosPorEjercito.get(id) + 1);
        }
    }

    public boolean agregarSoldado(Soldado s) {
        if (soldados.size() >= MAX_SOLDADOS) return false;
        soldados.add(s);
        soldadosCreadosPorEjercito.put(id, soldadosCreadosPorEjercito.get(id) + 1);
        soldadosVivosPorEjercito.put(id, soldadosVivosPorEjercito.get(id) + 1);
        return true;
    }

    public void quitarSoldado(int indice) {
        if (indice >= 0 && indice < soldados.size()) {
            soldados.remove(indice);
            soldadosVivosPorEjercito.put(id, soldados.size());
        }
    }

    public ArrayList<Soldado> getSoldados() { return soldados; }
    public String getNombre() { return nombre; }
    public Reino getReino() { return reino; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public int getId() { return id; }
    public void mover(int f, int c) { fila = f; columna = c; }
    public int cantidadSoldados() { return soldados.size(); }
    public static int getCreados(int id) { return soldadosCreadosPorEjercito.get(id); }
    public static int getVivos(int id) { return soldadosVivosPorEjercito.get(id); }
    public static void actualizarVivos(int id, int cantidad) { soldadosVivosPorEjercito.put(id, cantidad); }

    public String toString() {
        return nombre + " (" + reino.getNombre() + ") en [" + fila + "," + columna +
               "], soldados vivos: " + soldados.size();
    }
}
