import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class JuegoEstrategicoTactico {
    private static final int TAM_TABLERO = 10;
    private static final String[] NOMBRES_REINOS = {
        "Inglaterra", "Francia", "Sacro Imperio", "Castilla-Aragon", "Moros"
    };
    private ArrayList<Reino> reinos;
    private ArrayList<Ejercito> ejercitos;
    private Scanner sc;
    private Random azar;

    public JuegoEstrategicoTactico() {
        reinos = new ArrayList<Reino>();
        ejercitos = new ArrayList<Ejercito>();
        sc = new Scanner(System.in);
        azar = new Random();
    }

    public static void main(String[] args) {
        JuegoEstrategicoTactico juego = new JuegoEstrategicoTactico();
        juego.iniciar();
    }

    public void iniciar() {
        generarMundo();
        int opcion;
        do {
            System.out.println("JUEGO ESTRATEGICO Y TACTICO ");
            System.out.println("1. Ver tablero estratégico");
            System.out.println("2. Ver datos de reinos y ejércitos");
            System.out.println("3. Jugar");
            System.out.println("4. Ver contadores de soldados");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1: mostrarTablero(); break;
                case 2: mostrarDatos(); break;
                case 3: jugar(); break;
                case 4: mostrarContadores(); break;
                case 5: System.out.println("Fin del programa."); break;
                default: System.out.println("Opción incorrecta.");
            }
        } while (opcion != 5);
    }

    private void generarMundo() {
        // dos reinos diferentes 
        int r1 = azar.nextInt(NOMBRES_REINOS.length);
        int r2;
        do { r2 = azar.nextInt(NOMBRES_REINOS.length); } while (r1 == r2);
        Reino reino1 = new Reino(NOMBRES_REINOS[r1]);
        Reino reino2 = new Reino(NOMBRES_REINOS[r2]);
        reinos.add(reino1);
        reinos.add(reino2);

        int cantidad1 = azar.nextInt(10) + 1;
        int cantidad2 = azar.nextInt(10) + 1;
        for (int i = 0; i < cantidad1; i++) {
            int[] pos = buscarPosicionLibre();
            Ejercito e = new Ejercito("Ejercito" + (i + 1) + "R1", reino1, pos[0], pos[1]);
            e.generarSoldados();
            reino1.agregarEjercito(e);
            ejercitos.add(e);
        }
        for (int i = 0; i < cantidad2; i++) {
            int[] pos = buscarPosicionLibre();
            Ejercito e = new Ejercito("Ejercito" + (i + 1) + "R2", reino2, pos[0], pos[1]);
            e.generarSoldados();
            reino2.agregarEjercito(e);
            ejercitos.add(e);
        }
    }

    private int[] buscarPosicionLibre() {
        int f, c;
        boolean ocupada;
        do {
            f = azar.nextInt(TAM_TABLERO);
            c = azar.nextInt(TAM_TABLERO);
            ocupada = false;
            for (Ejercito e : ejercitos)
                if (e.getFila() == f && e.getColumna() == c) ocupada = true;
        } while (ocupada);
        return new int[]{f, c};
    }

    private void mostrarTablero() {
        System.out.println("\\nTABLERO ESTRATEGICO (R1/R2 indican el reino)");
        for (int f = 0; f < TAM_TABLERO; f++) {
            for (int c = 0; c < TAM_TABLERO; c++) {
                String marca = " . ";
                for (Ejercito e : ejercitos) {
                    if (e.getFila() == f && e.getColumna() == c && e.cantidadSoldados() > 0) {
                        marca = e.getNombre().contains("R1") ? " R1" : " R2";
                    }
                }
                System.out.print("|" + marca);
            }
            System.out.println("|");
        }
        System.out.println("R1 = " + reinos.get(0).getNombre() + " | R2 = " + reinos.get(1).getNombre());
    }

    private void mostrarDatos() {
        for (Reino r : reinos) {
            System.out.println("\\nREINO: " + r.getNombre());
            for (Ejercito e : r.getEjercitos()) {
                System.out.println(e);
                for (Soldado s : e.getSoldados()) System.out.println("  " + s);
            }
        }
    }

    private void mostrarContadores() {
        System.out.println("Objetos Soldado creados desde que inició el programa: " + Soldado.getTotalCreados());
        for (Ejercito e : ejercitos) {
            System.out.println(e.getNombre() + ": creados=" + Ejercito.getCreados(e.getId()) +
                    ", vivos=" + e.cantidadSoldados());
        }
    }

    private void jugar() {
        boolean continuar = true;
        while (continuar && !juegoTerminado()) {
            mostrarTablero();
            System.out.println("\\nEjércitos disponibles:");
            for (int i = 0; i < ejercitos.size(); i++) {
                Ejercito e = ejercitos.get(i);
                if (e.cantidadSoldados() > 0)
                    System.out.println(i + ". " + e);
            }
            System.out.print("Jugador, elija índice del ejército a mover (-1 para volver): ");
            int indice = sc.nextInt();
            if (indice == -1) break;
            if (indice < 0 || indice >= ejercitos.size() || ejercitos.get(indice).cantidadSoldados() == 0) {
                System.out.println("Ejército no válido.");
                continue;
            }
            Ejercito elegido = ejercitos.get(indice);
            System.out.println("Direcciones: 1 arriba, 2 abajo, 3 izquierda, 4 derecha");
            System.out.print("Dirección: ");
            int dir = sc.nextInt();
            int nf = elegido.getFila(), nc = elegido.getColumna();
            if (dir == 1) nf--;
            else if (dir == 2) nf++;
            else if (dir == 3) nc--;
            else if (dir == 4) nc++;
            else { System.out.println("Dirección incorrecta."); continue; }
            if (nf < 0 || nf >= TAM_TABLERO || nc < 0 || nc >= TAM_TABLERO) {
                System.out.println("Movimiento fuera del tablero.");
                continue;
            }
            Ejercito rival = buscarEjercito(nf, nc);
            if (rival != null) {
                if (elegido.getReino().getNombre().equals(rival.getReino().getNombre())) {
                    System.out.println("No se permite una guerra civil. Movimiento cancelado.");
                    continue;
                }
                elegido.mover(nf, nc);
                resolverEncuentro(elegido, rival);
            } else {
                elegido.mover(nf, nc);
            }
            mostrarContadores();
            if (juegoTerminado()) {
                System.out.println("¡La guerra terminó!");
                mostrarGanador();
                continuar = false;
            }
        }
    }

    private Ejercito buscarEjercito(int f, int c) {
        for (Ejercito e : ejercitos)
            if (e.getFila() == f && e.getColumna() == c && e.cantidadSoldados() > 0) return e;
        return null;
    }

    private void resolverEncuentro(Ejercito a, Ejercito b) {
        System.out.println("\\n¡Se encontraron " + a.getNombre() + " y " + b.getNombre() + "!");
        System.out.println("1. Resolver batalla automáticamente");
        System.out.println("2. Batalla táctica por turnos");
        System.out.print("Opción: ");
        int opcion = sc.nextInt();
        // El tablero táctico se crea con soldados de ambos ejércitos.
        ArrayList<Soldado> sa = a.getSoldados();
        ArrayList<Soldado> sb = b.getSoldados();
        if (opcion == 2) batallaTactica(sa, sb);
        else batallaAutomatica(sa, sb);
        Ejercito ganador = sa.size() > 0 ? a : (sb.size() > 0 ? b : null);
        if (ganador != null) {
            ganador.mover(a.getFila(), a.getColumna());
            // El ejército vencedor ocupa la posición del encuentro.
            if (ganador == b) ganador.mover(a.getFila(), a.getColumna());
            System.out.println("Ejército que queda en el tablero: " + ganador.getNombre());
        }
        Ejercito.actualizarVivos(a.getId(), sa.size());
        Ejercito.actualizarVivos(b.getId(), sb.size());
    }

    private void batallaAutomatica(ArrayList<Soldado> a, ArrayList<Soldado> b) {
        int poderA = poder(a), poderB = poder(b);
        System.out.println("Poder total " + a.size() + " soldados: " + poderA);
        System.out.println("Poder total " + b.size() + " soldados: " + poderB);
        // La métrica es la suma de vida actual más ataque y defensa de cada soldado.
        if (poderA >= poderB) {
            System.out.println("Gana el primer ejército porque su poder total es mayor o igual.");
            b.clear();
        } else {
            System.out.println("Gana el segundo ejército porque su poder total es mayor.");
            a.clear();
        }
    }

    private int poder(ArrayList<Soldado> soldados) {
        int total = 0;
        for (Soldado s : soldados)
            total += s.getVidaActual() + s.getNivelAtaque() + s.getNivelDefensa();
        return total;
    }

    private void batallaTactica(ArrayList<Soldado> a, ArrayList<Soldado> b) {
        // Cada ronda se enfrenta el primer soldado de cada ejército.
        while (!a.isEmpty() && !b.isEmpty()) {
            Soldado s1 = a.get(0), s2 = b.get(0);
            System.out.println("\\nDuelo: " + s1);
            System.out.println("Contra: " + s2);
            System.out.println("1. Atacar con el primer soldado");
            System.out.println("2. Atacar con el segundo soldado");
            System.out.print("Elija quién ataca (1 o 2): ");
            int atacante = sc.nextInt();
            if (atacante == 1) {
                int danio = Math.max(1, s1.getNivelAtaque() - s2.getNivelDefensa() + 1);
                s2.serAtacado(danio);
                System.out.println(s1.getNombre() + " causa " + danio + " de daño.");
                if (!s2.getVive()) { b.remove(0); System.out.println(s2.getNombre() + " fue derrotado."); }
            } else {
                int danio = Math.max(1, s2.getNivelAtaque() - s1.getNivelDefensa() + 1);
                s1.serAtacado(danio);
                System.out.println(s2.getNombre() + " causa " + danio + " de daño.");
                if (!s1.getVive()) { a.remove(0); System.out.println(s1.getNombre() + " fue derrotado."); }
            }
        }
        if (a.isEmpty()) System.out.println("Ganó el segundo ejército en la batalla táctica.");
        if (b.isEmpty()) System.out.println("Ganó el primer ejército en la batalla táctica.");
    }

    private boolean juegoTerminado() {
        boolean reino1Vivo = false, reino2Vivo = false;
        for (Ejercito e : reinos.get(0).getEjercitos())
            if (e.cantidadSoldados() > 0) reino1Vivo = true;
        for (Ejercito e : reinos.get(1).getEjercitos())
            if (e.cantidadSoldados() > 0) reino2Vivo = true;
        return !reino1Vivo || !reino2Vivo;
    }

    private void mostrarGanador() {
        boolean vivo1 = false, vivo2 = false;
        for (Ejercito e : reinos.get(0).getEjercitos()) if (e.cantidadSoldados() > 0) vivo1 = true;
        for (Ejercito e : reinos.get(1).getEjercitos()) if (e.cantidadSoldados() > 0) vivo2 = true;
        if (vivo1 && !vivo2) System.out.println("Ganó el reino " + reinos.get(0).getNombre());
        else if (vivo2 && !vivo1) System.out.println("Ganó el reino " + reinos.get(1).getNombre());
        else System.out.println("No hay un ganador.");
    }
}
