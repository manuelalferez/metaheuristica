package meta;

import java.io.*;
import java.util.Random;

class Utils {

    static void escribirFichero(String nombreFichero, String mensaje) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(nombreFichero, false);
            pw = new PrintWriter(fichero);
            pw.println(mensaje);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Para asegurarnos que se cierra el fichero
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Calcula el coste de una solución, cada posición con todas (n^2)
     */
    static int calcularCoste(int[] solucion) {
        int coste = 0;

        if (Main.aeropuertoActual.getEsSimetrica()) {
            for (int i = 0; i < solucion.length; i++)
                for (int j = i + 1; j < solucion.length; j++)
                    coste += 2 * (Main.aeropuertoActual.flujos[i][j] * Main.aeropuertoActual.distancias[solucion[i]][solucion[j]]);
        } else {
            for (int i = 0; i < solucion.length; i++)
                for (int j = 0; j < solucion.length; j++)
                    if (i != j)
                        coste += Main.aeropuertoActual.flujos[i][j] * Main.aeropuertoActual.distancias[solucion[i]][solucion[j]];
        }
        return coste;
    }

    /**
     * Calcula el coste que implica un movimiento, después de realizar una permutación.
     * La forma de calcularlo es obteniendo el coste asociado de una puerta intercambiada con todas. Las puertas
     * no intercambiadas se obvia el cálculo (n)
     */
    private static int calcularCosteMovimiento(int[] solucion, Vecino vecino) {
        int coste = 0;
        int r = vecino.getPrimeraPosicion();
        int s = vecino.getSegundaPosicion();
        if (Main.aeropuertoActual.getEsSimetrica()) {
            for (int i = 0; i < solucion.length; i++) {
                if (i != r) coste += 2 * Main.aeropuertoActual.flujos[r][i] * Main.aeropuertoActual.distancias[solucion[r]][solucion[i]];
                if (i != s) coste += 2 * Main.aeropuertoActual.flujos[s][i] * Main.aeropuertoActual.distancias[solucion[s]][solucion[i]];
            }
        } else {
            for (int i = 0; i < solucion.length; i++) {
                if (i != r) {
                    coste += Main.aeropuertoActual.flujos[r][i] * Main.aeropuertoActual.distancias[solucion[r]][solucion[i]];
                    coste += Main.aeropuertoActual.flujos[i][r] * Main.aeropuertoActual.distancias[solucion[i]][solucion[r]];
                }
                if (i != s) {
                    coste += Main.aeropuertoActual.flujos[s][i] * Main.aeropuertoActual.distancias[solucion[s]][solucion[i]];
                    coste += Main.aeropuertoActual.flujos[i][s] * Main.aeropuertoActual.distancias[solucion[i]][solucion[s]];
                }
            }
        }
        return coste;
    }

    static void realizarMovimiento(int[] v, Vecino vecino) {
        int aux = v[vecino.getPrimeraPosicion()];
        v[vecino.getPrimeraPosicion()] = v[vecino.getSegundaPosicion()];
        v[vecino.getSegundaPosicion()] = aux;
    }

    /**
     * Calcula el coste del movimiento antes de intercambiar las puertas y después. Después se restan a coste total
     * para calcular el coste final asociado al movimiento
     */
    static int calcularCosteParametrizado(int[] permutacion, int coste, Vecino vecino) {
        int costeAntes = 0, costeDespues = 0;
        costeAntes = calcularCosteMovimiento(permutacion, vecino);
        realizarMovimiento(permutacion, vecino);
        costeDespues = calcularCosteMovimiento(permutacion, vecino);
        // Deshacemos el intercambio
        realizarMovimiento(permutacion, vecino);

        return coste + costeDespues - costeAntes;
    }

    static void escribirMovimiento(int entorno, Vecino vecino, int coste, int iteraciones) {
        Main.contenidoLog += "\nEntorno:             " + entorno;
        Main.contenidoLog += "\nMovimiento:          " + vecino.getPrimeraPosicion() + " " + vecino.getSegundaPosicion();
        Main.contenidoLog += "\nCoste:               " + coste;
        Main.contenidoLog += "\nIteración:           " + iteraciones;
        Main.contenidoLog += "\n";
    }

    static void escribirSolucionInicial(int[] situacionActual, int costeSituacionActual, int iteracion) {
        Main.contenidoLog += "\n\n";
        Main.contenidoLog += "\nSolución inicial:    ";
        for (int i : situacionActual) {
            Main.contenidoLog += i + " ";
        }

        Main.contenidoLog += "\nCoste:               " + costeSituacionActual;
        Main.contenidoLog += "\nIteración:           " + iteracion;
        Main.contenidoLog += "\n\n";
    }


    static Solucion generarSolucionInicial(int tamSolucion, Random random) {
        Solucion situacionActual = new Solucion();
        int[] posiciones = new int[tamSolucion];
        int tamLogico = tamSolucion;
        for (int i = 0; i < tamSolucion; i++) posiciones[i] = i;
        situacionActual = new Solucion(tamSolucion);

        while (tamLogico != 0) {
            int number = random.nextInt(tamLogico);
            situacionActual.solucion[tamSolucion - tamLogico] = posiciones[number];
            posiciones[number] = posiciones[tamLogico - 1];
            tamLogico--;
        }
        situacionActual.coste = Utils.calcularCoste(situacionActual.solucion);
        //Utils.escribirSolucionInicial(solucionActual.solucion, solucionActual.coste, iteraciones);
        return situacionActual;
    }
}
