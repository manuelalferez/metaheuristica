package meta;

import java.io.*;

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

    static void escribirMovimientoEnFichero(int entorno, Vecino vecino, int coste, int iteraciones) {
        Main.contenidoLog += "\nEntorno:             " + entorno;
        Main.contenidoLog += "\nMovimiento:          " + vecino.getPrimeraPosicion() + " " + vecino.getSegundaPosicion();
        Main.contenidoLog += "\nCoste:               " + coste;
        Main.contenidoLog += "\nIteración:           " + iteraciones;
        Main.contenidoLog += "\n";
    }

    static void escribirSolucionInicial(Solucion solucionInicial, int iteracion) {
        Main.contenidoLog += "\nSolución inicial:    ";
        for (int i : solucionInicial.solucion) {
            Main.contenidoLog += i + " ";
        }

        Main.contenidoLog += "\nCoste:               " + solucionInicial.coste;
        Main.contenidoLog += "\nIteración:           " + iteracion;
        Main.contenidoLog += "\n";
    }
    static void escribirIndividuo(Solucion solucionInicial, int generacion, int parentesco) {
        if (parentesco == Genetico.PADRES)
                Main.contenidoLog += "Antes:          |"+ generacion + "|    ";
            else
                Main.contenidoLog += "Despues:        |"+ generacion + "|    ";

        for (int i : solucionInicial.solucion) {
            Main.contenidoLog += i + " ";
        }
        Main.contenidoLog += " \n";
    }


    static void realizarMovimiento(int[] v, Vecino vecino) {
        int aux = v[vecino.getPrimeraPosicion()];
        v[vecino.getPrimeraPosicion()] = v[vecino.getSegundaPosicion()];
        v[vecino.getSegundaPosicion()] = aux;
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
     * Calcula el coste del movimiento antes de intercambiar las puertas y después. Después se restan a coste total
     * para calcular el coste final asociado al movimiento
     */
    static int calcularCosteParametrizado(Solucion solucion, Vecino vecino) {
        int costeAntes = 0, costeDespues = 0;
        costeAntes = calcularCosteMovimiento(solucion.solucion, vecino);
        realizarMovimiento(solucion.solucion, vecino);
        costeDespues = calcularCosteMovimiento(solucion.solucion, vecino);
        // Deshacemos el intercambio
        realizarMovimiento(solucion.solucion, vecino);

        return solucion.coste + costeDespues - costeAntes;
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
                if (i != r)
                    coste += 2 * Main.aeropuertoActual.flujos[r][i] * Main.aeropuertoActual.distancias[solucion[r]][solucion[i]];
                if (i != s)
                    coste += 2 * Main.aeropuertoActual.flujos[s][i] * Main.aeropuertoActual.distancias[solucion[s]][solucion[i]];
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

    static Solucion generarSolucionInicial(int tamSolucion) {
        Solucion solucionInicial = new Solucion(tamSolucion);
        int[] posiciones = new int[tamSolucion];
        int tamLogico = tamSolucion;
        for (int i = 0; i < tamSolucion; i++) posiciones[i] = i;

        while (tamLogico != 0) {
            int number = Main.random.nextInt(tamLogico);
            solucionInicial.solucion[tamSolucion - tamLogico] = posiciones[number];
            posiciones[number] = posiciones[tamLogico - 1];
            tamLogico--;
        }

        return solucionInicial;
    }
}
