package meta;

class BusquedaLocal {
    private final static int VECINO_SELECCIONADO = 1;
    private static final int MAX_ITERACIONES = 50000;
    private static final int MAX_INTENTOS = 100;
    private static final int NUM_VECINOS = 10;

    private static int intentos = 0;
    private static int iteraciones = 0;
    private static int entorno = 0;

    private static Solucion solucionActual = new Solucion();
    private static Vecino mejorVecino = new Vecino();
    private static int costeMejorVecino = Integer.MAX_VALUE;

    private int[][] vecinosGenerados; // Usada para no repetir vecinos cuando estamos generando los 10
    private int tamSolucion;

    BusquedaLocal() {
        this.tamSolucion = Main.aeropuertoActual.numPuertas;
        vecinosGenerados = new int[Main.aeropuertoActual.numPuertas][Main.aeropuertoActual.numPuertas];
    }

    void algoritmoBusquedaLocal() {
        solucionActual = Utils.generarSolucionInicial(tamSolucion);
        solucionActual.coste = Utils.calcularCoste(solucionActual.solucion);
        Utils.escribirSolucionInicial(solucionActual.solucion, solucionActual.coste, iteraciones);

        do {
            generarMejorVecino();
            if (costeMejorVecino < solucionActual.coste) {
                Utils.realizarMovimiento(solucionActual.solucion, mejorVecino);
                solucionActual.coste = costeMejorVecino;
                iteraciones++;
                intentos = 0;
                Utils.escribirMovimientoEnFichero(entorno, mejorVecino, solucionActual.coste, iteraciones);
            } else intentos++;
            entorno++;
            limpiarMatriz();
        } while (iteraciones < MAX_ITERACIONES && intentos != MAX_INTENTOS);
    }

    private void generarMejorVecino(){
        Vecino vecinoActual;
        costeMejorVecino = Integer.MAX_VALUE;
        int costeVecinoActual;

        for (int i = 0; i < NUM_VECINOS; i++) {
            vecinoActual = generarVecino();
            costeVecinoActual = Utils.calcularCosteParametrizado(solucionActual.solucion, solucionActual.coste, vecinoActual);

            if (costeVecinoActual < costeMejorVecino) {
                mejorVecino.copiarVecino(vecinoActual);
                costeMejorVecino = costeVecinoActual;
            }
        }
    }

    private Vecino generarVecino() {
        Vecino nuevoVecino = new Vecino();
        do {
            nuevoVecino.setPrimeraPosicion(Main.random.nextInt(tamSolucion));
            nuevoVecino.setSegundaPosicion(Main.random.nextInt(tamSolucion));
            if (nuevoVecino.getPrimeraPosicion() > nuevoVecino.getSegundaPosicion()) {
                nuevoVecino.intercambiarPosiciones();
            }
        } while (esVecinoSeleccionado(nuevoVecino) || nuevoVecino.tienePosicionesIguales());
        anadirVecino(nuevoVecino);

        return nuevoVecino;
    }

    private void limpiarMatriz() {
        for (int i = 0; i < tamSolucion; i++)
            for (int j = i + 1; j < tamSolucion; j++)
                vecinosGenerados[i][j] = 0; // Limpia la diagonal superior
    }

    private boolean esVecinoSeleccionado(Vecino vecino) {
        return vecinosGenerados[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] == VECINO_SELECCIONADO;
    }

    private void anadirVecino(Vecino vecino) {
        vecinosGenerados[vecino.getPrimeraPosicion()][vecino.getSegundaPosicion()] = VECINO_SELECCIONADO;
    }

    Solucion getSolucion() {
        return solucionActual;
    }
}
