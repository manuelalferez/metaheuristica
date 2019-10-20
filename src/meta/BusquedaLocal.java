package meta;

import java.util.Random;

class BusquedaLocal {
    private final static int VECINO_SELECCIONADO = 1;

    String contenidoLog = "";
    private Random random;
    private int[][] vecinosGenerados;
    private int tamSolucion;

    private int[] situacionActual;
    private int costeSituacionActual;

    BusquedaLocal(int seed, int tam) {
        random = new Random(seed);
        this.tamSolucion = tam;
        vecinosGenerados = new int[tam][tam];
    }

    private void generarSolucionInicial() {
        int[] posiciones = new int[tamSolucion];
        int tamLogico = tamSolucion;
        for (int i = 0; i < tamSolucion; i++) posiciones[i] = i;
        situacionActual = new int[tamSolucion];

        while (tamLogico != 0) {
            int number = random.nextInt(tamLogico);
            situacionActual[tamSolucion - tamLogico] = posiciones[number];
            posiciones[number] = posiciones[tamLogico - 1];
            tamLogico--;
        }
    } //generar_solucion_inicial()

    private void limpiarMatriz() {
        for (int i = 0; i < tamSolucion; i++)
            for (int j = i + 1; j < tamSolucion; j++)
                vecinosGenerados[i][j] = 0;
    }

    private boolean vecinoSeleccionado(Vecino vecino) {
        return vecinosGenerados[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] == VECINO_SELECCIONADO;
    }

    private void anadirVecino(Vecino vecino) {
        vecinosGenerados[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] = VECINO_SELECCIONADO;
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

    public void escribirMovimiento(int entorno, Vecino vecino, int coste, int evaluaciones) {
        contenidoLog += "\nEntorno:             " + entorno;
        contenidoLog += "\nMovimiento:          " + vecino.getPrimeraPosicion() + " " + vecino.getSegundaPosicion();
        contenidoLog += "\nCoste:               " + coste;
        contenidoLog += "\nIteración:           " + evaluaciones;
        contenidoLog += "\n----------------------------------------";
    }

    public void escribirSolucionInicial(int _iteracion) {
        contenidoLog += "\n-------------------------------------------------------------------------------";
        contenidoLog += "\nSolución inicial:    ";
        for (int i : situacionActual) {
            contenidoLog += i + " ";
        }

        contenidoLog += "\nCoste:               " + costeSituacionActual;
        contenidoLog += "\nIteración:           " + _iteracion;
        contenidoLog += "\n-------------------------------------------------------------------------------";
    }

    void algoritmoBusquedaLocal(Aeropuerto aeropuerto) {
        generarSolucionInicial();
        costeSituacionActual = Main.calcularCoste(situacionActual, aeropuerto);

        int costeMejorVecino = costeSituacionActual;
        Vecino mejorVecino = new Vecino();
        int costeVecinoActual;
        Vecino vecinoActual = new Vecino();
        int evaluaciones = 0, intentos = 0, entorno = 0;

        int MAX_EVALUACIONES = 50000;
        int MAX_INTENTOS = 100;
        int NUM_VECINOS = 10;


        escribirSolucionInicial(evaluaciones);
        do {
            for (int i = 0; i < NUM_VECINOS; i++) {
                vecinoActual = generarVecino();
                costeVecinoActual = Main.calcularCosteParametrizado(situacionActual, costeSituacionActual, aeropuerto,
                        vecinoActual.getPrimeraPosicion(), vecinoActual.getSegundaPosicion());

                if (costeVecinoActual < costeMejorVecino) {
                    mejorVecino.copiarVecino(vecinoActual);
                    costeMejorVecino = costeVecinoActual;
                }
            }
            if (costeMejorVecino < costeSituacionActual) {
                Main.realizarMovimiento(situacionActual, mejorVecino.getPrimeraPosicion(), mejorVecino.getSegundaPosicion());
                costeSituacionActual = costeMejorVecino;
                evaluaciones++;
                intentos = 0;
                escribirMovimiento(entorno, mejorVecino, costeSituacionActual, evaluaciones);
            } else {
                intentos ++;
            }
            entorno++;
            limpiarMatriz();
        } while (evaluaciones < MAX_EVALUACIONES && intentos != MAX_INTENTOS);
    }

    public int[] getSolucion() {
        return this.situacionActual;
    }

    public int getCosteSolucion() {
        return this.costeSituacionActual;
    }
}
