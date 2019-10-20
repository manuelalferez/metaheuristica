package meta;

/**
 * Esta clase representa el intercambio de dos posiciones, realizadas sobre una solución del problema y que conforma
 * la identidad de un vecino
 */
public class Vecino {
    // Posiciones que intercambiamos
    private final static int PRIMERA_POSICION = 0;
    private final static int SEGUNDA_POSICION = 1;

    int[] posiciones; // Posiciones que generan al vecino;

    Vecino() {
        posiciones = new int[2]; // Almacenaremos las dos posiciones intercambiadas
    }

    void setPrimeraPosicion(int valor) {
        posiciones[PRIMERA_POSICION] = valor;
    }

    void setSegundaPosicion(int valor) {
        posiciones[SEGUNDA_POSICION] = valor;
    }

    int getPrimeraPosicion() {
        return posiciones[PRIMERA_POSICION];
    }

    int getSegundaPosicion() {
        return posiciones[SEGUNDA_POSICION];
    }

    void copiarVecino(Vecino copia) {
        posiciones[PRIMERA_POSICION] = copia.posiciones[PRIMERA_POSICION];
        posiciones[SEGUNDA_POSICION] = copia.posiciones[SEGUNDA_POSICION];
    }

    void intercambiarPosiciones() {
        int aux = posiciones[PRIMERA_POSICION];
        posiciones[PRIMERA_POSICION] = posiciones[SEGUNDA_POSICION];
        posiciones[SEGUNDA_POSICION] = aux;
    }

    boolean tienePosicionesIguales() {
        return posiciones[PRIMERA_POSICION] == posiciones[SEGUNDA_POSICION];
    }
}
