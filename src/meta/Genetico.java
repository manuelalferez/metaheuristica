package meta;

public class Genetico {
    Poblacion poblacion;
    private Poblacion poblacionDescendiente;
    private int TAM_TORNEO = 2;
    private static int TAM_POBLACION = 50;
    private static int NUM_POSICIONES_INTERCAMBIADAS = 3;
    private static int NUM_EVALUACIONES = 100;
    private static int iteraciones;
    static int generacion;
    private Reproduccion nuevaReproduccion;
    static int PADRES = 0;
    static int HIJOS = 1;
    private int[] posIntercambio;

    Genetico() {
    }

    void algoritmoGenetico() {
        iteraciones=0;
        generacion = 0;
        inicializarPoblacion();
        poblacion.evaluar();
        while (iteraciones < NUM_EVALUACIONES) {
            crearPoblacionDescendientes();
            seleccionar();
            recombinar();
            mutar();
            poblacion.calcularElites();
            poblacionDescendiente.evaluar();
            reemplazar();
            generacion++;
        }
    }

    private void inicializarPoblacion() {
        poblacion = new Poblacion(TAM_POBLACION, Main.aeropuertoActual.numPuertas);
        poblacion.inicializar();
    }

    private void crearPoblacionDescendientes() {
        poblacionDescendiente = new Poblacion(TAM_POBLACION, Main.aeropuertoActual.numPuertas);
    }


    private void seleccionar() {
        int posIndividuoGanador;
        for (int i = 0; i < poblacion.getTam(); i++) {
            posIndividuoGanador = torneoBinario();
            poblacionDescendiente.incluirIndividuo(poblacion.individuos[posIndividuoGanador]);
        }
    }

    private int torneoBinario() {
        int posPrimerIndividuo = Main.random.nextInt(poblacion.individuos.length);
        int posSegundoIndividuo = generarPosDistintaPrimerIndividuo(posPrimerIndividuo);
        return mejorIndividuo(posPrimerIndividuo, posSegundoIndividuo);
    }

    private int generarPosDistintaPrimerIndividuo(int posPrimerIndividuo) {
        int posSegundoIndividuo;
        do {
            posSegundoIndividuo = Main.random.nextInt(poblacion.individuos.length);
        } while (posPrimerIndividuo == posSegundoIndividuo);
        return posSegundoIndividuo;
    }

    private int mejorIndividuo(int primer, int segundo) {
        if (poblacion.individuos[primer].coste < poblacion.individuos[segundo].coste)
            return primer;
        else return segundo;
    }

    private void recombinar() {
        int i = 0;
        while (i < poblacion.getTam()) {
            double probabilidad = Main.random.nextDouble();
            if (probabilidad < 0.7) {
                escribirLogIndividuos(i, PADRES, generacion);//Individuos Antes de Mutar
                realizarCruce(i);
                copiarIndividuosCruzados(i);
                escribirLogIndividuos(i, HIJOS, generacion+1);//Individuos Despues de Mutar
                poblacionDescendiente.individuos[i].marcarComoModificado();
                poblacionDescendiente.individuos[i + 1].marcarComoModificado();
            }
            i += TAM_TORNEO;
        }
    }

    private void realizarCruce(int posPrimerProgenitor) {
        nuevaReproduccion = new Reproduccion(poblacion.individuos[posPrimerProgenitor],
                poblacion.individuos[posPrimerProgenitor + 1]);
        if (Main.esCruceMOC()) nuevaReproduccion.cruceMOC();
        else nuevaReproduccion.cruceOX2();
    }

    private void copiarIndividuosCruzados(int posPrimerProgenitor) {
        poblacionDescendiente.individuos[posPrimerProgenitor].copiar(nuevaReproduccion.getPrimerProgenitor());
        poblacionDescendiente.individuos[posPrimerProgenitor + 1].copiar(nuevaReproduccion.getSegundoProgenitor());
    }


    private void escribirLogIndividuos(int posicion, int parentesco, int generacion){
        Utils.escribirIndividuo(poblacionDescendiente.individuos[posicion], generacion, parentesco);
        Utils.escribirIndividuo(poblacionDescendiente.individuos[posicion+1], generacion, parentesco);
        Main.contenidoLog += "\n";
    }

    private void mutar() {
        for (int i = 0; i < poblacionDescendiente.getTam(); i++) {
            for (int j = 0; j < poblacionDescendiente.tamIndividuo; j++) {
                double probabilidad = Main.random.nextDouble();
                if (probabilidad < 0.05) {
                    seleccionarPosiciones(j);
                    ordenarPosiciones();
                    rotacion(i);
                }
            }
        }
    }

    private void seleccionarPosiciones(int primeraPosicion) {
        posIntercambio = new int[NUM_POSICIONES_INTERCAMBIADAS];
        posIntercambio[0] = primeraPosicion;
        int posAleatoria;
        for (int i = 1; i < NUM_POSICIONES_INTERCAMBIADAS; i++) {
            do {
                posAleatoria = Main.random.nextInt(poblacion.tamIndividuo);
            } while (estaPosicion(posAleatoria, i));
            posIntercambio[i] = posAleatoria;
        }
    }

    private boolean estaPosicion(int pos, int tam) {
        for (int i = 0; i < tam; i++)
            if (posIntercambio[i] == pos)
                return true;
        return false;
    }

    private void ordenarPosiciones() {
        for (int i = 0; i < NUM_POSICIONES_INTERCAMBIADAS - 1; i++) {
            for (int j = i + 1; j < NUM_POSICIONES_INTERCAMBIADAS; j++) {
                if (posIntercambio[i] > posIntercambio[j])
                    intercambiarPosIntercambio(i, j);
            }
        }
    }

    private void intercambiarPosIntercambio(int a, int b) {
        int aux = posIntercambio[a];
        posIntercambio[a] = posIntercambio[b];
        posIntercambio[b] = aux;
    }

    private void rotacion(int posIndividuo) {
        int valorPrimeraPosicion = 0;
        for (int i = 0; i < NUM_POSICIONES_INTERCAMBIADAS; i++) {
            if (i == 0)
                valorPrimeraPosicion = poblacionDescendiente.individuos[posIndividuo].solucion[posIntercambio[i]];
            else
                rotacionIzquierda(posIndividuo, i - 1);
        }
        rotacionPrimeroAUltimaPos(posIndividuo, valorPrimeraPosicion);
    }

    private void rotacionIzquierda(int posIndividuo, int posicionDestino) {
        int posFuente = posicionDestino + 1;
        poblacionDescendiente.individuos[posIndividuo].solucion[posIntercambio[posicionDestino]] =
                poblacionDescendiente.individuos[posIndividuo].solucion[posIntercambio[posFuente]];
    }

    /**
     * Copia el valor del individuo que se encuentra en la primera posición del vector posIntercambio
     * dentro del individuo en la última posición que indica el vector posIntercambio
     */
    private void rotacionPrimeroAUltimaPos(int posIndividuo, int valorPrimeraPosicion) {
        poblacionDescendiente.individuos[posIndividuo].solucion[posIntercambio[NUM_POSICIONES_INTERCAMBIADAS - 1]] =
                valorPrimeraPosicion;
    }

    private void reemplazar() {
        int[] posicionElites = poblacion.getElites();
        boolean[] estaElites = new boolean[posicionElites.length];
        int contadorElites = Main.getNumElites();

        for (int i = 0; i < poblacionDescendiente.individuos.length; i++)
            for (int j = 0; j < posicionElites.length; j++)
                if (poblacionDescendiente.individuos[i].sonIguales(poblacion.individuos[posicionElites[j]]) && !estaElites[j]) {
                    estaElites[j] = true;
                    contadorElites--;
                }
        if (contadorElites != 0) {
            poblacionDescendiente.calcularPeoresIndividuos(contadorElites);
            insertarElites(posicionElites, estaElites);
        }

        poblacion.copiarPoblacion(poblacionDescendiente);
    }

    private void insertarElites(int[] posicionElites, boolean[] estaElites) {
        int contador = 0;
        for (int i = 0; i < posicionElites.length; i++)
            if (!estaElites[i]) {
                int posPeorIndividuoDescendiente = poblacionDescendiente.posPeores[contador++];
                poblacionDescendiente.individuos[posPeorIndividuoDescendiente].copiar(poblacion.individuos[posicionElites[i]]);
            }
    }

    Solucion getSolucion() {
        return new Solucion(calcularMejorIndividuo());
    }

    private Solucion calcularMejorIndividuo() {
        int posMejorIndividuo = 0;
        int costeMejorIndividuo = Integer.MAX_VALUE;
        for (int i = 0; i < poblacion.getTam(); i++) {
            if (poblacion.individuos[i].coste < costeMejorIndividuo) {
                posMejorIndividuo = i;
                costeMejorIndividuo = poblacion.individuos[i].coste;
            }
        }
        return poblacion.individuos[posMejorIndividuo];
    }

    public static void incrementarIteraciones(){
        iteraciones++;
    }
}
