package meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BusquedaTabu {
    private final static int DIVERSIFICAR = 1;
    private final static int INTENSIFICAR = 0;

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
                double probabilidad = random.nextDouble();
                double cincuentaPorCiento = 0.5;
                if (probabilidad < cincuentaPorCiento) generarEntorno(DIVERSIFICAR);
                else generarEntorno(INTENSIFICAR);
                intentos = 0;
                entorno++;
            }
            System.out.printf("%d\n",iteraciones);
            listaTabues.clear();
            costeMejorVecino = Integer.MAX_VALUE;
        } while (iteraciones < MAX_ITERACIONES);
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
        if(listaTabues.size()==0) return false;
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
    }

    /**
     * La solución se genera desde la última fila y columna de la matriz, hasta terminar en
     * la fila=0. Esta técnica se usa porque son las posiciones de la matriz que tienen menos
     * elementos para buscar entre mayores valores
     */
    private int[] generarEntorno(int opcion){
        int[] nuevoEntorno = new int[tamSolucion];
        int mejorValor, posicionMejor= 0;
        boolean esMejor;

        mejorValor = reiniciarMejorValor(opcion);

        int penultimaFila = memoriaLargoPlazo.length-2; // La última fila no tiene elementos
        int ultimaColumna = memoriaLargoPlazo.length-1;

        /*
         * Rellenamos la última posición del nuevo entorno, debido a que al tener una matriz con la
         * diagonal principal vacía no podemos generar la posición i=tamanio-1 y j=tamanio-1
         */
        for(int k = 0;k<memoriaLargoPlazo.length;k++){
            esMejor = calcularSiEsMejor(opcion, k, ultimaColumna, mejorValor);

            if (opcion == INTENSIFICAR && esMejor) { // Intensificamos
                if (!estaValor(nuevoEntorno, k, ultimaColumna)) {
                    mejorValor = memoriaLargoPlazo[k][ultimaColumna];
                    posicionMejor = k;
                }
            }else if (esMejor){ // Diversificamos
                if (!estaValor(nuevoEntorno, k, ultimaColumna)) {
                    mejorValor = memoriaLargoPlazo[k][ultimaColumna];
                    posicionMejor = k;
                }
            }
        }
        nuevoEntorno[ultimaColumna] = posicionMejor;

        // Generamos lo restante del entorno
        for (int i = penultimaFila; i >= 0; i--) {
            mejorValor = reiniciarMejorValor(opcion);
            posicionMejor = 0;

            for (int j = i + 1; j < memoriaLargoPlazo.length; j++) {
                esMejor = calcularSiEsMejor(opcion, i, j, mejorValor);

                if (opcion == INTENSIFICAR && esMejor) { // Intensificamos
                    if (!estaValor(nuevoEntorno, j, i)) {
                        mejorValor = memoriaLargoPlazo[i][j];
                        posicionMejor = j;
                    }
                }else if (esMejor){ // Diversificamos
                    if (!estaValor(nuevoEntorno, j, i)) {
                        mejorValor = memoriaLargoPlazo[i][j];
                        posicionMejor = j;
                    }
                }
            }
            nuevoEntorno[i] = posicionMejor;
        }

        return nuevoEntorno;
    }

    private boolean calcularSiEsMejor(int opcion, int fila, int columna, int mejorValor){
        if(opcion==INTENSIFICAR) return  memoriaLargoPlazo[fila][columna] > mejorValor;
        else return memoriaLargoPlazo[fila][columna] < mejorValor;
    }

    private int reiniciarMejorValor(int opcion){
        if(opcion==INTENSIFICAR) return Integer.MIN_VALUE;
        else return Integer.MAX_VALUE;
    }

    private boolean estaValor(int[] vector, int valor, int cotaSuperior) {
        for (int i = 0; i < cotaSuperior; i++) {
            if (vector[i] == valor) return true;
        }
        return false;
    }

    public int[] getSolucion() {
        return this.mejorSolucion;
    }

    public int getCosteSolucion() {
        return this.costeMejorSolucion;
    }
}
