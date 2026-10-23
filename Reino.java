import java.util.ArrayList;

public class Reino {
    private String nombre;
    private ArrayList<Ejercito> ejercitos;

    public Reino(String nombre) {
        this.nombre = nombre;
        ejercitos = new ArrayList<Ejercito>();
    }

    public void agregarEjercito(Ejercito e) { ejercitos.add(e); }
    public String getNombre() { return nombre; }
    public ArrayList<Ejercito> getEjercitos() { return ejercitos; }

    public String toString() {
        return nombre + " - cantidad de ejércitos: " + ejercitos.size();
    }
}
