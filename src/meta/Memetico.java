package meta;

class Memetico {
    private Poblacion poblacion;
    private static int TAM_POBLACION = Main.getTamanioPoblacion();
    private static int NUM_POSICIONES_INTERCAMBIADAS = 3;
    private static int iteraciones;
    private static int generacion;
    private Reproduccion nuevaReproduccion;
    static int PADRES = 0;
    static int HIJOS = 1;
    private int[] posIntercambio;
    private Solucion[] descendientes;

    Memetico() {
    }

    void algoritmoMemetico() {
        generacion = 0;
        inicializarPoblacion();
        poblacion.evaluar();
        //Utils.escribirPoblacion(poblacion, generacion);
        int MAX_GENERACIONES = Main.getGeneracionesMaximas();
        TabuMemetico tabu = new TabuMemetico();
        while (generacion < MAX_GENERACIONES) {
            crearDescendientes();
            seleccionar();
            recombinar();
            mutar();
            if (generacion % Main.getRangoDeAplicacionDeTabu() == 0) {
                for(int i=0;i<descendientes.length;i++){
                    //Main.contenidoLog+="Búsqueda tabú:\n";
                   // Utils.escribirIndividuo(descendientes[i],generacion,PADRES);
                   // Main.contenidoLog+="Coste: "+descendientes[i].coste+"\n";
                    tabu.algoritmoTabu(descendientes[i]);
                    descendientes[i].copiar(tabu.getSolucion());
                    //Utils.escribirIndividuo(descendientes[i],generacion,HIJOS);
                   // Main.contenidoLog+="Coste: "+descendientes[i].coste+"\n";
                }
            }
            evaluarDescendientes();
            reemplazar();
            generacion++;
            //Utils.escribirPoblacion(poblacion, generacion);
        }
    }

    private void inicializarPoblacion() {
        poblacion = new Poblacion(TAM_POBLACION, Main.aeropuertoActual.numPuertas);
        poblacion.inicializar();
    }

    private void crearDescendientes() {
        descendientes = new Solucion[Main.getNumIndividuosDescendientes()];
        for(int i=0;i<descendientes.length;i++)
            descendientes[i] = new Solucion();
    }


    private void seleccionar() {
        int posIndividuoGanador;
        for (int i = 0; i < descendientes.length; i++) {
            posIndividuoGanador = torneoBinario();
            descendientes[i].copiar(poblacion.individuos[posIndividuoGanador]);
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
        double probabilidad = Main.random.nextDouble();
        if (probabilidad <= Main.getProbabilidadCruce()) {
           // Main.contenidoLog += "Cruce\n";
            //Main.contenidoLog += "------------\n";
           // escribirLogIndividuos(PADRES, generacion);//Individuos Antes de cruzar
            realizarCruce();
            copiarIndividuosCruzados();
            //Main.contenidoLog += "Cruce\n";
           // Main.contenidoLog += "------------\n";
            //escribirLogIndividuos(HIJOS, generacion + 1);//Individuos Despues de cruzar
            descendientes[0].marcarComoModificado();
            descendientes[1].marcarComoModificado();
        }
    }

    private void realizarCruce() {
        nuevaReproduccion = new Reproduccion(descendientes[0],
                descendientes[1]);
        if (Main.esCruceMOC()) nuevaReproduccion.cruceMOC();
        else nuevaReproduccion.cruceOX2();
    }

    private void copiarIndividuosCruzados() {
        descendientes[0].copiar(nuevaReproduccion.getPrimerProgenitor());
        descendientes[1].copiar(nuevaReproduccion.getSegundoProgenitor());
    }


    private void escribirLogIndividuos(int parentesco, int generacion) {
        Utils.escribirIndividuo(descendientes[0], generacion, parentesco);
        Utils.escribirIndividuo(descendientes[1], generacion, parentesco);
        Main.contenidoLog += "\n";
    }

    private void escribirLogIndividuo(int posicion, int parentesco, int generacion) {
        Utils.escribirIndividuo(descendientes[posicion], generacion, parentesco);
        Main.contenidoLog += "\n";
    }

    private void mutar() {
        for (int i = 0; i < descendientes.length; i++) {
            for (int j = 0; j < poblacion.tamIndividuo; j++) {
                double probabilidad = Main.random.nextDouble();
                if (probabilidad <= Main.getProbabilidadMutacion()) {
                    seleccionarPosiciones(j);
                    ordenarPosiciones();
                   // Main.contenidoLog += "Mutación\n";
                    //Main.contenidoLog += "------------\n";
                    //escribirLogIndividuo(i, PADRES, generacion + 1);//Individuos Antes de mutar
                    rotacion(i);
                    //Main.contenidoLog += "Mutación\n";
                   // Main.contenidoLog += "------------\n";
                   // escribirLogIndividuo(i, HIJOS, generacion + 1);//Individuos Antes de mutar
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
                valorPrimeraPosicion = descendientes[posIndividuo].solucion[posIntercambio[i]];
            else
                rotacionIzquierda(posIndividuo, i - 1);
        }
        rotacionPrimeroAUltimaPos(posIndividuo, valorPrimeraPosicion);
    }

    private void rotacionIzquierda(int posIndividuo, int posicionDestino) {
        int posFuente = posicionDestino + 1;
        descendientes[posIndividuo].solucion[posIntercambio[posicionDestino]] =
                descendientes[posIndividuo].solucion[posIntercambio[posFuente]];
    }

    /**
     * Copia el valor del individuo que se encuentra en la primera posición del vector posIntercambio
     * dentro del individuo en la última posición que indica el vector posIntercambio
     */
    private void rotacionPrimeroAUltimaPos(int posIndividuo, int valorPrimeraPosicion) {
        descendientes[posIndividuo].solucion[posIntercambio[NUM_POSICIONES_INTERCAMBIADAS - 1]] =
                valorPrimeraPosicion;
    }

    private void reemplazar() {
        poblacion.calcularPeoresIndividuos(2);
        for (int i = 0; i < descendientes.length; i++)
            poblacion.individuos[poblacion.posPeores[i]].copiar(descendientes[i]);
    }

    void evaluarDescendientes() {
        for (int i = 0; i < descendientes.length; i++) {
            descendientes[i].coste = Utils.calcularCoste(descendientes[i].solucion);
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
}

