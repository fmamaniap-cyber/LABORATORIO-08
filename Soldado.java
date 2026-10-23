import java.util.Random;

public class Soldado {
    private static int totalCreados = 0;

    private String nombre;
    private int nivelAtaque;
    private int nivelDefensa;
    private int nivelVida;
    private int vidaActual;
    private int velocidad;
    private String actitud;
    private boolean vive;

    public Soldado() {
        this("Soldado" + totalCreados, aleatorio(), aleatorio(), aleatorio());
    }

    public Soldado(String nombre, int vida, int ataque, int defensa) {
        this.nombre = nombre;
        nivelVida = vida;
        vidaActual = vida;
        nivelAtaque = ataque;
        nivelDefensa = defensa;
        velocidad = 0;
        actitud = "defensiva";
        vive = true;
        totalCreados++;
    }

    public Soldado(String nombre, int vida) {
        this(nombre, vida, aleatorio(), aleatorio());
    }

    public Soldado(Soldado otro) {
        this(otro.nombre, otro.nivelVida, otro.nivelAtaque, otro.nivelDefensa);
        vidaActual = otro.vidaActual;
        velocidad = otro.velocidad;
        actitud = otro.actitud;
        vive = otro.vive;
    }

    private static int aleatorio() {
        return new Random().nextInt(5) + 1;
    }

    public void atacar() {
        if (vive) {
            actitud = "ofensiva";
            avanzar();
        }
    }

    public void defender() {
        if (vive) {
            actitud = "defensiva";
            velocidad = 0;
        }
    }

    public void avanzar() {
        if (vive) velocidad++;
    }

    public void retroceder() {
        if (vive) {
            if (velocidad > 0) {
                velocidad = 0;
                actitud = "defensiva";
            } else {
                velocidad--;
            }
        }
    }

    public void huir() {
        if (vive) {
            actitud = "fuga";
            velocidad += 2;
        }
    }

    public void serAtacado(int danio) {
        if (vive) {
            vidaActual -= danio;
            if (vidaActual <= 0) morir();
        }
    }

    public void morir() {
        vidaActual = 0;
        vive = false;
    }

    public Soldado sumar(Soldado otro) {
        Soldado suma = new Soldado("Suma", nivelVida + otro.nivelVida,
                nivelAtaque + otro.nivelAtaque, nivelDefensa + otro.nivelDefensa);
        suma.velocidad = velocidad + otro.velocidad;
        suma.vidaActual = vidaActual + otro.vidaActual;
        return suma;
    }

    public void setVidaActual(int vida) {
        vidaActual = vida;
        if (vidaActual <= 0) morir();
        else vive = true;
    }

    public String getNombre() { return nombre; }
    public int getNivelAtaque() { return nivelAtaque; }
    public int getNivelDefensa() { return nivelDefensa; }
    public int getNivelVida() { return nivelVida; }
    public int getVidaActual() { return vidaActual; }
    public int getVelocidad() { return velocidad; }
    public String getActitud() { return actitud; }
    public boolean getVive() { return vive; }
    public static int getTotalCreados() { return totalCreados; }

    public void setNivelAtaque(int valor) { nivelAtaque = valor; }
    public void setNivelDefensa(int valor) { nivelDefensa = valor; }

    public boolean iguales(Soldado otro) {
        return nombre.equals(otro.nombre) &&
               nivelAtaque == otro.nivelAtaque &&
               nivelDefensa == otro.nivelDefensa &&
               vidaActual == otro.vidaActual && vive == otro.vive;
    }

    public String toString() {
        return nombre + " | vida actual: " + vidaActual +
               " | ataque: " + nivelAtaque + " | defensa: " + nivelDefensa +
               " | velocidad: " + velocidad + " | actitud: " + actitud +
               " | vive: " + vive;
    }
}
