package meta;

import java.util.Random;

class BusquedaLocal {
    private final static int VECINO_SELECCIONADO = 1;

    private Random random;
    private int[][] vecinosGenerados;
    private int tamSolucion;

    private Solucion situacionActual;
    private int costeSituacionActual;

    BusquedaLocal(int seed, int tam) {
        random = new Random(seed);
        this.tamSolucion = tam;
        vecinosGenerados = new int[tam][tam];
    }

    void algoritmoBusquedaLocal() {
        generarSolucionInicial();
        costeSituacionActual = Utils.calcularCoste(situacionActual);

        int costeMejorVecino = costeSituacionActual;
        Vecino mejorVecino = new Vecino();
        int costeVecinoActual;
        Vecino vecinoActual;
        int evaluaciones = 0, intentos = 0, entorno = 0;

        int MAX_EVALUACIONES = 50000;
        int MAX_INTENTOS = 100;
        int NUM_VECINOS = 10;

        Utils.escribirSolucionInicial(situacionActual, costeSituacionActual, evaluaciones);
        do {
            for (int i = 0; i < NUM_VECINOS; i++) {
                vecinoActual = generarVecino();
                costeVecinoActual = Utils.calcularCosteParametrizado(situacionActual, costeSituacionActual, vecinoActual);

                if (costeVecinoActual < costeMejorVecino) {
                    mejorVecino.copiarVecino(vecinoActual);
                    costeMejorVecino = costeVecinoActual;
                }
            }
            if (costeMejorVecino < costeSituacionActual) {
                Utils.realizarMovimiento(situacionActual, mejorVecino);
                costeSituacionActual = costeMejorVecino;
                evaluaciones++;
                intentos = 0;
                Utils.escribirMovimiento(entorno, mejorVecino, costeSituacionActual, evaluaciones);
            } else {
                intentos++;
            }
            entorno++;
            limpiarMatriz();
        } while (evaluaciones < MAX_EVALUACIONES && intentos != MAX_INTENTOS);
    }

    private void generarSolucionInicial() {
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
    }

    private Vecino generarVecino() {
        Vecino nuevoVecino = new Vecino();
        do {
            nuevoVecino.setPrimeraPosicion(random.nextInt(tamSolucion));
            nuevoVecino.setSegundaPosicion(random.nextInt(tamSolucion));
            if (nuevoVecino.getPrimeraPosicion() > nuevoVecino.getSegundaPosicion()) {
                nuevoVecino.intercambiarPosiciones();
            }
        } while (vecinoSeleccionado(nuevoVecino) || nuevoVecino.tienePosicionesIguales());
        anadirVecino(nuevoVecino);

        return nuevoVecino;
    }

    private void limpiarMatriz() {
        for (int i = 0; i < tamSolucion; i++)
            for (int j = i + 1; j < tamSolucion; j++)
                vecinosGenerados[i][j] = 0; // Limpia la diagonal superior
    }

    private boolean vecinoSeleccionado(Vecino vecino) {
        return vecinosGenerados[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] == VECINO_SELECCIONADO;
    }

    private void anadirVecino(Vecino vecino) {
        vecinosGenerados[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] = VECINO_SELECCIONADO;
    }

    Solucion getSolucion() {
        return situacionActual;
    }

    int getCosteSolucion() {
        return this.costeSituacionActual;
    }
}
