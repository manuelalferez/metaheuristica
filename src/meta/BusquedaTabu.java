package meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BusquedaTabu {
    private static boolean DIVERSIFICAR = false; // Si no diversificamos, intensificamos

    private Random random;
    private int tamSolucion;

    private int[] mejorSolucion;
    private int costeMejorSolucion;

    private List<Vecino> listaTabues;
    private int[][] memoriaLargoPlazo;

    BusquedaTabu(int seed, int tam) {
        random = new Random(seed);
        this.tamSolucion = tam;
        listaTabues = new ArrayList<Vecino>();
        memoriaLargoPlazo = new int[tam][tam];
    }

    void algoritmoTabu(Aeropuerto aeropuerto) {
        int[] solucionActual = generarSolucionInicial();
        mejorSolucion = solucionActual;
        int costeSolucionActual = Utils.calcularCoste(mejorSolucion, aeropuerto);
        costeMejorSolucion = costeSolucionActual;

        int costeMejorVecino = costeSolucionActual;
        Vecino mejorVecino = new Vecino();
        int costeVecinoActual;
        Vecino vecinoActual;
        int iteraciones = 0, intentos = 0, entorno = 0;

        int MAX_ITERACIONES = 50000;
        int MAX_INTENTOS = 100;
        int NUM_VECINOS = 10;

        Utils.escribirSolucionInicial(mejorSolucion, costeSolucionActual, iteraciones);
        do {
            // Generamos los 10 vecinos
            for (int i = 0; i < NUM_VECINOS; i++) {
                vecinoActual = generarVecino();
                costeVecinoActual = Utils.calcularCosteParametrizado(solucionActual, costeSolucionActual, aeropuerto,
                        vecinoActual);

                if (costeVecinoActual < costeMejorVecino) {
                    mejorVecino.copiarVecino(vecinoActual);
                    costeMejorVecino = costeVecinoActual;
                }
            }

            Utils.realizarMovimiento(solucionActual, mejorVecino);
            costeSolucionActual = costeMejorVecino;
            actualizaMemoriaLargoPlazo(mejorVecino);

            //Utils.escribirMovimiento(entorno, mejorVecino, costeSolucionActual, iteraciones);
            iteraciones++;

            if (costeSolucionActual < costeMejorSolucion) {
                mejorSolucion = solucionActual;
                costeMejorSolucion = costeSolucionActual;
                intentos = 0;
            } else {
                intentos++;
            }

            if (intentos == MAX_INTENTOS) {
                calcularEstrategia();
                solucionActual = generarEntorno();
                intentos = 0;
                entorno++;
                costeSolucionActual= Utils.calcularCoste(solucionActual, aeropuerto);
            }

            listaTabues.clear();
            costeMejorVecino = Integer.MAX_VALUE;
        } while (iteraciones < MAX_ITERACIONES);
    }

    /**
     * Elegimos entre diversificar o intensificar
     */
    private void calcularEstrategia() {
        double probabilidad = random.nextDouble();
        DIVERSIFICAR = probabilidad < 0.5;
    }

    private int[] generarSolucionInicial() {
        int[] posicionesGeneradas = new int[tamSolucion];
        int tamLogico = tamSolucion;
        for (int i = 0; i < tamSolucion; i++) posicionesGeneradas[i] = i;
        int[] solucionInicial = new int[tamSolucion];

        while (tamLogico != 0) {
            int number = random.nextInt(tamLogico);
            solucionInicial[tamSolucion - tamLogico] = posicionesGeneradas[number];
            posicionesGeneradas[number] = posicionesGeneradas[tamLogico - 1];
            tamLogico--;
        }
        return solucionInicial;
    }

    private Vecino generarVecino() {
        Vecino nuevoVecino = new Vecino();
        do {
            nuevoVecino.setPrimeraPosicion(random.nextInt(tamSolucion));
            nuevoVecino.setSegundaPosicion(random.nextInt(tamSolucion));
            if (nuevoVecino.getPrimeraPosicion() > nuevoVecino.getSegundaPosicion()) {
                nuevoVecino.intercambiarPosiciones();
            }
        } while (esVecinoTabu(nuevoVecino) || nuevoVecino.tienePosicionesIguales());
        anadirVecinoTabu(nuevoVecino);

        return nuevoVecino;
    }

    private boolean esVecinoTabu(Vecino vecino) {
        if (listaTabues.size() == 0) return false;
        for (Vecino v : listaTabues) {
            if (v.sonIguales(vecino)) return true;
        }
        return false;
    }

    /**
     * Agrega un vecino a la lista. Si la lista está llena, se elimina por el principio , y siempre se añade
     * por el final
     */
    private void anadirVecinoTabu(Vecino vecino) {
        int tenenciaTabu = 5;
        if (listaTabues.size() == tenenciaTabu) {
            listaTabues.remove(0);
            listaTabues.add(vecino);
        } else {
            listaTabues.add(vecino);
        }
    }

    private void actualizaMemoriaLargoPlazo(Vecino vecino) {
        memoriaLargoPlazo[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] += 1;
        memoriaLargoPlazo[vecino.getSegundaPosicion()][vecino.getPrimeraPosicion()] += 1;
    }

    /**
     * Genera el entorno teniendo en cuenta que la memoria a largo plazo es una matriz triangular superior. Empezamos
     * a recorrerla desde abajo hacia arriba, de menos elementos en la fila a más elementos
     */
    private int[] generarEntorno() {
        int[] nuevoEntorno = new int[tamSolucion];
        int mejorValor, posicionMejor = 0;
        boolean esMejor;

        for (int i = 0; i < memoriaLargoPlazo.length; i++) {
            mejorValor = reiniciarMejorValor();
            posicionMejor = 0;
            for (int j = 0; j < memoriaLargoPlazo.length; j++) {
                if (i != j) {
                    esMejor = calcularSiEsMejor(i, j, mejorValor);

                    if (esMejor && noEsta(nuevoEntorno, i, j)) {
                        mejorValor = memoriaLargoPlazo[i][j];
                        posicionMejor = j;
                    }
                }
            }
            nuevoEntorno[i] = posicionMejor;
        }

        return nuevoEntorno;
    }

    private boolean calcularSiEsMejor(int fila, int columna, int mejorValor) {
        if (DIVERSIFICAR) return memoriaLargoPlazo[fila][columna] < mejorValor;
        else return memoriaLargoPlazo[fila][columna] > mejorValor;
    }

    private int reiniciarMejorValor() {
        if (DIVERSIFICAR) return Integer.MAX_VALUE;
        else return Integer.MIN_VALUE;
    }

    private boolean noEsta(int[] vector, int cotaSuperior, int valor) {
        for (int i = 0; i < cotaSuperior; i++) if (vector[i] == valor) return false;
        return true;
    }

    int[] getSolucion() {
        return this.mejorSolucion;
    }

    int getCosteSolucion() {
        return this.costeMejorSolucion;
    }
}
