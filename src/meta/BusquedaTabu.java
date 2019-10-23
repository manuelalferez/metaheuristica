package meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BusquedaTabu {
    private static boolean DIVERSIFICAR = false; // Si no diversificamos, intensificamos
    private static final int MAX_ITERACIONES = 10000;
    private static final int MAX_INTENTOS = 100;
    private static final int NUM_VECINOS = 10;
    private static int intentos = 0;
    private static int iteraciones = 0;
    private static int entorno = 0;

    private static Solucion solucionActual = new Solucion();
    private static Vecino mejorVecino = new Vecino();
    private static int costeMejorVecino = Integer.MAX_VALUE;

    private Random random;
    private int tamSolucion;

    private Solucion mejorSolucion;

    private List<Vecino> listaTabues;
    private int[][] memoriaLargoPlazo;

    BusquedaTabu(int seed, int tam) {
        random = new Random(seed);
        tamSolucion = tam;
        listaTabues = new ArrayList<>();
        memoriaLargoPlazo = new int[tam][tam];
    }

    void algoritmoTabu() {
        generarSolucionInicial();
        mejorSolucion = new Solucion(solucionActual);
        do {
            generarMejorVecino();
            realizarMovimiento();
            actualizaMemoriaLargoPlazo(mejorVecino);

            //Utils.escribirMovimiento(entorno, mejorVecino, solucionActual.coste, iteraciones);
            iteraciones++;

            if (solucionActual.coste < mejorSolucion.coste) {
                mejorSolucion.copiar(solucionActual);
                intentos = 0;
            } else intentos++;

            if (intentos == MAX_INTENTOS) {
                calcularEstrategia();
                generarEntorno();
                intentos = 0;
                entorno++;
            }
            listaTabues.clear();
        } while (iteraciones < MAX_ITERACIONES);
    }

    /**
     * Elegimos entre diversificar o intensificar
     */
    private void calcularEstrategia() {
        double probabilidad = random.nextDouble();
        DIVERSIFICAR = probabilidad < 0.5;
    }

    private void realizarMovimiento(){
        Utils.realizarMovimiento(solucionActual.solucion, mejorVecino);
        solucionActual.coste = costeMejorVecino;
    }

    private void generarMejorVecino(){
        Vecino vecinoActual;
        costeMejorVecino = Integer.MAX_VALUE;
        int costeVecinoActual;
        // Generamos los 10 vecinos
        for (int i = 0; i < NUM_VECINOS; i++) {
            vecinoActual = generarVecino();
            costeVecinoActual = Utils.calcularCosteParametrizado(solucionActual.solucion, solucionActual.coste, vecinoActual);

            if (costeVecinoActual < costeMejorVecino) {
                mejorVecino.copiarVecino(vecinoActual);
                costeMejorVecino = costeVecinoActual;
            }
        }
    }

    private void generarSolucionInicial() {
        int[] posicionesGeneradas = new int[tamSolucion];
        int tamLogico = tamSolucion;
        for (int i = 0; i < tamSolucion; i++) posicionesGeneradas[i] = i;
        solucionActual = new Solucion(tamSolucion);

        while (tamLogico != 0) {
            int number = random.nextInt(tamLogico);
            solucionActual.solucion[tamSolucion - tamLogico] = posicionesGeneradas[number];
            posicionesGeneradas[number] = posicionesGeneradas[tamLogico - 1];
            tamLogico--;
        }

        solucionActual.coste = Utils.calcularCoste(solucionActual.solucion);
        Utils.escribirSolucionInicial(solucionActual.solucion, solucionActual.coste, iteraciones);
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

    /**
     * Se copia dos veces, hace más simple la búsqueda de los mejores valores en la generación del entorno
     */
    private void actualizaMemoriaLargoPlazo(Vecino vecino) {
        memoriaLargoPlazo[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] += 1;
        memoriaLargoPlazo[vecino.getSegundaPosicion()][vecino.getPrimeraPosicion()] += 1;
    }

    /**
     * Genera el entorno teniendo en cuenta que la memoria a largo plazo es una matriz triangular superior. Empezamos
     * a recorrerla desde abajo hacia arriba, de menos elementos en la fila a más elementos
     */
    private void generarEntorno() {
        solucionActual = new Solucion(tamSolucion); // La situación actual cambiará por el nuevo entorno
        int mejorValor, posicionMejor;
        boolean esMejor;

        for (int i = 0; i < memoriaLargoPlazo.length; i++) {
            mejorValor = reiniciarMejorValor();
            posicionMejor = 0;
            for (int j = 0; j < memoriaLargoPlazo.length; j++) {
                if (i != j) {
                    esMejor = calcularSiEsMejor(i, j, mejorValor);

                    if (esMejor && valorNoEsta(solucionActual.solucion, i, j)) {  // j = valor a introducir
                        mejorValor = memoriaLargoPlazo[i][j];
                        posicionMejor = j;
                    }
                }
            }
            solucionActual.solucion[i] = posicionMejor;
        }
        solucionActual.coste = Utils.calcularCoste(solucionActual.solucion);
    }

    private boolean calcularSiEsMejor(int fila, int columna, int mejorValor) {
        if (DIVERSIFICAR) return memoriaLargoPlazo[fila][columna] < mejorValor;
        else return memoriaLargoPlazo[fila][columna] > mejorValor;
    }

    private int reiniciarMejorValor() {
        if (DIVERSIFICAR) return Integer.MAX_VALUE;
        else return Integer.MIN_VALUE;
    }

    /**
     * El entorno se va generando desde el inicio. La cotaSuperior mejora la eficiencia
     */
    private boolean valorNoEsta(int[] entorno, int cotaSuperior, int valor) {
        for (int i = 0; i < cotaSuperior; i++) if (entorno[i] == valor) return false;
        return true;
    }

    int[] getSolucion() {
        return this.mejorSolucion.solucion;
    }

    int getCosteSolucion() {
        return this.mejorSolucion.coste;
    }
}
